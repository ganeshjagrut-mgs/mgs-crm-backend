package com.crm.util;

import com.crm.domain.Encryption;
import com.crm.repository.EncryptionRepository;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class EncryptionUtil {

    private final EncryptionRepository encryptionRepository;

    public EncryptionUtil(EncryptionRepository encryptionRepository) {
        this.encryptionRepository = encryptionRepository;
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
        return encryptionRepository.findByTenantId(tenantId)
                .map(Encryption::getKey);
    }

    /**
     * Check if the provided pin matches the stored pin for the tenant.
     *
     * @param tenantId the id of the tenant.
     * @param pin      the pin to check.
     * @return true if the pin matches, false otherwise.
     */
    public boolean checkSecurityPin(Long tenantId, String pin) {
        return encryptionRepository.findByTenantId(tenantId)
                .map(encryption -> encryption.getPin().equals(pin))
                .orElse(false);
    }
}
