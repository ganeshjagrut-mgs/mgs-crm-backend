package com.crm.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EncryptionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Encryption getEncryptionSample1() {
        return new Encryption().id(1L).key("key1").pin("pin1");
    }

    public static Encryption getEncryptionSample2() {
        return new Encryption().id(2L).key("key2").pin("pin2");
    }

    public static Encryption getEncryptionRandomSampleGenerator() {
        return new Encryption().id(longCount.incrementAndGet()).key(UUID.randomUUID().toString()).pin(UUID.randomUUID().toString());
    }
}
