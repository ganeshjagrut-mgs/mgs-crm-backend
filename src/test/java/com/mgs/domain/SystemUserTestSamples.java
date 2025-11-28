package com.mgs.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SystemUserTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SystemUser getSystemUserSample1() {
        return new SystemUser().id(1L).email("email1").passwordHash("passwordHash1");
    }

    public static SystemUser getSystemUserSample2() {
        return new SystemUser().id(2L).email("email2").passwordHash("passwordHash2");
    }

    public static SystemUser getSystemUserRandomSampleGenerator() {
        return new SystemUser()
            .id(longCount.incrementAndGet())
            .email(UUID.randomUUID().toString())
            .passwordHash(UUID.randomUUID().toString());
    }
}
