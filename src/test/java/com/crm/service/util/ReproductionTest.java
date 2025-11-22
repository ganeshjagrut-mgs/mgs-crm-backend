package com.crm.service.util;

import static org.assertj.core.api.Assertions.assertThat;

import com.crm.util.EncryptedField;
import com.crm.util.EncryptionUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ReproductionTest {

    private EncryptionUtil encryptionUtil;
    private ObjectMapper objectMapper;
    private String key = "mySecretKey";

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        encryptionUtil = new EncryptionUtil(objectMapper);
    }

    @Test
    void testEncryptDecryptStringDirectly() {
        String original = "Hello World";
        String encrypted = encryptionUtil.encryptObject(original, key);
        System.out.println("Encrypted String: " + encrypted);

        String decrypted = encryptionUtil.decryptObject(encrypted, key);
        assertThat(decrypted).isEqualTo(original);
    }

    @Test
    void testEncryptDecryptObjectWithAnnotation() {
        AnnotatedObject original = new AnnotatedObject("secret");
        encryptionUtil.encryptObject(original, key);
        System.out.println("Encrypted Field: " + original.secretField);

        encryptionUtil.decryptObject(original, key);
        assertThat(original.secretField).isEqualTo("secret");
    }

    @Test
    void testEncryptDecryptList() {
        List<String> list = new ArrayList<>();
        list.add("Item 1");
        list.add("Item 2");

        List<String> encryptedList = encryptionUtil.encryptObject(list, key);
        System.out.println("Encrypted List: " + encryptedList);

        List<String> decryptedList = encryptionUtil.decryptObject(encryptedList, key);
        assertThat(decryptedList).containsExactly("Item 1", "Item 2");
    }

    @Test
    void testDecryptOptional() {
        AnnotatedObject original = new AnnotatedObject("secret");
        encryptionUtil.encryptObject(original, key);

        java.util.Optional<AnnotatedObject> optional = java.util.Optional.of(original);
        encryptionUtil.decryptObject(optional, key);

        assertThat(optional.get().secretField).isEqualTo("secret");
    }

    static class AnnotatedObject {
        @EncryptedField
        public String secretField;

        public AnnotatedObject(String secretField) {
            this.secretField = secretField;
        }
    }
}
