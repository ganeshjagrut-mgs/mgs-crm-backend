package com.mgs.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PermissionModuleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PermissionModule getPermissionModuleSample1() {
        return new PermissionModule().id(1L).name("name1").description("description1");
    }

    public static PermissionModule getPermissionModuleSample2() {
        return new PermissionModule().id(2L).name("name2").description("description2");
    }

    public static PermissionModule getPermissionModuleRandomSampleGenerator() {
        return new PermissionModule()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
