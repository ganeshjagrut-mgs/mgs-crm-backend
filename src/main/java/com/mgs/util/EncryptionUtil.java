package com.mgs.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgs.domain.TenantEncryptionKey;
import com.mgs.repository.TenantEncryptionKeyRepository;
import com.mgs.web.rest.errors.EncryptionKeyNotFoundException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Collection;
import java.util.Optional;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.stereotype.Component;

@Component
public class EncryptionUtil {

    private final TenantEncryptionKeyRepository tenantEncryptionKeyRepository;
    private final ObjectMapper objectMapper;
    private static final String ALGORITHM = "AES";

    public EncryptionUtil(TenantEncryptionKeyRepository tenantEncryptionKeyRepository, ObjectMapper objectMapper) {
        this.tenantEncryptionKeyRepository = tenantEncryptionKeyRepository;
        this.objectMapper = objectMapper;
    }

    public static String generateKey() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32]; // 256-bit key
        random.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static String generatePin() {
        SecureRandom random = new SecureRandom();
        int pin = 100000 + random.nextInt(900000); // 6 digit PIN
        return String.valueOf(pin);
    }

    /**
     * Get the encryption key for a tenant.
     *
     * @param tenantId the id of the tenant.
     * @return the encryption key.
     */
    public Optional<String> getEncryptionKey(Long tenantId) {
        return tenantEncryptionKeyRepository
            .findByTenantIdAndIsActiveTrue(tenantId)
            .map(TenantEncryptionKey::getEncryptedDataKey);
    }

    /**
     * Get the encryption key for a tenant or throw exception if not found.
     *
     * @param tenantId the id of the tenant.
     * @return the encryption key.
     * @throws EncryptionKeyNotFoundException if no active encryption key is found for the tenant.
     */
    public String getEncryptionKeyOrThrow(Long tenantId) {
        return tenantEncryptionKeyRepository
            .findByTenantIdAndIsActiveTrue(tenantId)
            .map(TenantEncryptionKey::getEncryptedDataKey)
            .orElseThrow(() -> new EncryptionKeyNotFoundException(tenantId));
    }

    public String encrypt(Object data, String encryptionId) {
        if (encryptionId == null) {
            throw new IllegalArgumentException("Encryption ID cannot be null");
        }
        try {
            String json = objectMapper.writeValueAsString(data);
            SecretKeySpec keySpec = getKey(encryptionId);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encrypted = cipher.doFinal(json.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("Error while encrypting data", e);
        }
    }

    public <T> T encryptObject(T object, String encryptionId) {
        if (object == null) return null;
        if (object instanceof String text) {
            return (T) encrypt(text, encryptionId);
        }
        if (object instanceof Collection<?> collection) {
            collection.forEach(item -> encryptObject(item, encryptionId));
            return object;
        }
        if (object instanceof java.util.Optional<?> optional) {
            optional.ifPresent(item -> encryptObject(item, encryptionId));
            return object;
        }
        for (Field field : object.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(EncryptedField.class)) {
                field.setAccessible(true);
                try {
                    Object value = field.get(object);
                    if (value != null) {
                        String encryptedValue = encrypt(value.toString(), encryptionId);
                        field.set(object, encryptedValue);
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Could not access field " + field.getName(), e);
                }
            }
        }
        return object;
    }

    @SuppressWarnings("unchecked")
    public <T> T decryptObject(T object, String encryptionId) {
        if (object == null) return null;

        if (object instanceof String encryptedStr) {
            if (isEncrypted(encryptedStr)) {
                String plain = decrypt(encryptedStr, encryptionId, String.class);
                return (T) plain;
            }
            return object;
        }
        if (object instanceof Collection<?> collection) {
            collection.forEach(item -> decryptObject(item, encryptionId));
            return object;
        }
        if (object instanceof java.util.Optional<?> optional) {
            optional.ifPresent(item -> decryptObject(item, encryptionId));
            return object;
        }
        Class<?> clazz = object.getClass();
        // Skip primitive wrappers and other immutable types
        if (clazz.isPrimitive() || clazz.getPackage() != null && clazz.getPackage().getName().startsWith("java.")) {
            return object;
        }
        // Walk class hierarchy to include superclass fields
        while (clazz != null && !clazz.equals(Object.class)) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                // Only process fields annotated with @EncryptedField
                if (!field.isAnnotationPresent(EncryptedField.class)) {
                    continue;
                }
                boolean accessible = field.canAccess(object);
                try {
                    if (!accessible) {
                        field.setAccessible(true);
                    }
                    Object value = field.get(object);
                    if (value == null) {
                        continue;
                    }
                    // If the annotated field is a String, decrypt it
                    if (value instanceof String s) {
                        if (isEncrypted(s)) {
                            String decrypted = decrypt(s, encryptionId, String.class);
                            field.set(object, decrypted);
                        }
                        continue;
                    }
                    // If field is a nested object/collection → recurse
                    // Important: The recursion modifies the object in place for mutable objects
                    decryptObject(value, encryptionId);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Could not access field " + field.getName(), e);
                } finally {
                    if (!accessible) {
                        field.setAccessible(false);
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }

        return object;
    }

    /**
     * Check if a string appears to be encrypted (base64 encoded)
     */
    private boolean isEncrypted(String value) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        try {
            Base64.getDecoder().decode(value);
            return value.length() >= 16 && value.matches("^[A-Za-z0-9+/]+=*$");
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public <T> T decrypt(String encryptedData, String encryptionId, Class<T> targetClass) {
        if (encryptionId == null) {
            throw new IllegalArgumentException("Encryption ID cannot be null");
        }

        if (encryptedData == null || encryptedData.isEmpty()) {
            return null;
        }

        try {
            SecretKeySpec keySpec = getKey(encryptionId);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);

            byte[] decoded = Base64.getDecoder().decode(encryptedData);
            byte[] decrypted = cipher.doFinal(decoded);
            String decryptedText = new String(decrypted, StandardCharsets.UTF_8);

            return objectMapper.readValue(decryptedText, targetClass);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid encrypted data format (not valid Base64)", e);
        } catch (BadPaddingException e) {
            throw new RuntimeException("Decryption failed - wrong key or corrupted data", e);
        } catch (Exception e) {
            throw new RuntimeException("Error while decrypting data: " + e.getMessage(), e);
        }
    }

    private SecretKeySpec getKey(String myKey) throws Exception {
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] key = sha.digest(myKey.getBytes(StandardCharsets.UTF_8));
        return new SecretKeySpec(key, ALGORITHM);
    }
}
