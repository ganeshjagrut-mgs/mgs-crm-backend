package com.crm.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MasterCategoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MasterCategory getMasterCategorySample1() {
        return new MasterCategory().id(1L).name("name1").description("description1").code("code1");
    }

    public static MasterCategory getMasterCategorySample2() {
        return new MasterCategory().id(2L).name("name2").description("description2").code("code2");
    }

    public static MasterCategory getMasterCategoryRandomSampleGenerator() {
        return new MasterCategory()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .code(UUID.randomUUID().toString());
    }
}
