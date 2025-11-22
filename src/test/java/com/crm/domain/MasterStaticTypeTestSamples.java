package com.crm.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MasterStaticTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static MasterStaticType getMasterStaticTypeSample1() {
        return new MasterStaticType().id(1L).name("name1").description("description1").displayOrder(1);
    }

    public static MasterStaticType getMasterStaticTypeSample2() {
        return new MasterStaticType().id(2L).name("name2").description("description2").displayOrder(2);
    }

    public static MasterStaticType getMasterStaticTypeRandomSampleGenerator() {
        return new MasterStaticType()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .displayOrder(intCount.incrementAndGet());
    }
}
