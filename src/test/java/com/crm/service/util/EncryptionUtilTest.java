package com.crm.service.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.crm.util.EncryptedField;
import com.crm.util.EncryptionUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EncryptionUtilTest {

    private EncryptionUtil encryptionUtil;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        encryptionUtil = new EncryptionUtil(objectMapper);
    }

    @Test
    void testEncryptDecryptString() {
        String original = "Hello World";
        String key = "mySecretKey";

        String encrypted = encryptionUtil.encrypt(original, key);
        assertThat(encrypted).isNotNull();
        assertThat(encrypted).isNotEqualTo(original);

        String decrypted = encryptionUtil.decrypt(encrypted, key, String.class);
        assertThat(decrypted).isEqualTo(original);
    }

    @Test
    void testEncryptDecryptObject() {
        TestObject original = new TestObject("test", 123);
        String key = "anotherKey";

        String encrypted = encryptionUtil.encrypt(original, key);
        assertThat(encrypted).isNotNull();

        TestObject decrypted = encryptionUtil.decrypt(encrypted, key, TestObject.class);
        assertThat(decrypted).isEqualTo(original);
    }

    @Test
    void testDecryptWithWrongKey() {
        String original = "Secret Data";
        String key = "correctKey";
        String wrongKey = "wrongKey";

        String encrypted = encryptionUtil.encrypt(original, key);

        assertThrows(RuntimeException.class, () -> {
            encryptionUtil.decrypt(encrypted, wrongKey, String.class);
        });
    }

    @Test
    void testEncryptWithNullKey() {
        assertThrows(IllegalArgumentException.class, () -> {
            encryptionUtil.encrypt("data", null);
        });
    }

    @Test
    void testDecryptWithNullKey() {
        assertThrows(IllegalArgumentException.class, () -> {
            encryptionUtil.decrypt("data", null, String.class);
        });
    }

    @Test
    void testEncryptDecryptObjectWithAnnotation() {
        AnnotatedTestObject original = new AnnotatedTestObject("secret", "public", 123);
        String key = "myKey";

        encryptionUtil.encryptObject(original, key);

        assertThat(original.secretField).isNotEqualTo("secret");
        assertThat(original.publicField).isEqualTo("public");
        assertThat(original.numberField).isEqualTo(123);

        encryptionUtil.decryptObject(original, key);

        assertThat(original.secretField).isEqualTo("secret");
        assertThat(original.publicField).isEqualTo("public");
        assertThat(original.numberField).isEqualTo(123);
    }

    static class AnnotatedTestObject {
        @EncryptedField
        private String secretField;
        private String publicField;
        private int numberField;

        public AnnotatedTestObject(String secretField, String publicField, int numberField) {
            this.secretField = secretField;
            this.publicField = publicField;
            this.numberField = numberField;
        }
    }

    static class TestObject {
        public String name;
        public int value;

        public TestObject() {
        }

        public TestObject(String name, int value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            TestObject that = (TestObject) o;
            return value == that.value && name.equals(that.name);
        }
    }
}
