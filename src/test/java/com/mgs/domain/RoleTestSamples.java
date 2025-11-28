package com.mgs.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class RoleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Role getRoleSample1() {
        return new Role().id(1L).name("name1").code("code1").roleLevel(1);
    }

    public static Role getRoleSample2() {
        return new Role().id(2L).name("name2").code("code2").roleLevel(2);
    }

    public static Role getRoleRandomSampleGenerator() {
        return new Role()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .code(UUID.randomUUID().toString())
            .roleLevel(intCount.incrementAndGet());
    }
}
