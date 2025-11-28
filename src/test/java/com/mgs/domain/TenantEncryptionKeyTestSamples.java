package com.mgs.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TenantEncryptionKeyTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static TenantEncryptionKey getTenantEncryptionKeySample1() {
        return new TenantEncryptionKey().id(1L).keyVersion(1).encryptedDataKey("encryptedDataKey1").pinHash("pinHash1").pinSalt("pinSalt1");
    }

    public static TenantEncryptionKey getTenantEncryptionKeySample2() {
        return new TenantEncryptionKey().id(2L).keyVersion(2).encryptedDataKey("encryptedDataKey2").pinHash("pinHash2").pinSalt("pinSalt2");
    }

    public static TenantEncryptionKey getTenantEncryptionKeyRandomSampleGenerator() {
        return new TenantEncryptionKey()
            .id(longCount.incrementAndGet())
            .keyVersion(intCount.incrementAndGet())
            .encryptedDataKey(UUID.randomUUID().toString())
            .pinHash(UUID.randomUUID().toString())
            .pinSalt(UUID.randomUUID().toString());
    }
}
