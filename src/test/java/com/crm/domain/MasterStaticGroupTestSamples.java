package com.crm.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MasterStaticGroupTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MasterStaticGroup getMasterStaticGroupSample1() {
        return new MasterStaticGroup().id(1L).name("name1").description("description1").uiLabel("uiLabel1");
    }

    public static MasterStaticGroup getMasterStaticGroupSample2() {
        return new MasterStaticGroup().id(2L).name("name2").description("description2").uiLabel("uiLabel2");
    }

    public static MasterStaticGroup getMasterStaticGroupRandomSampleGenerator() {
        return new MasterStaticGroup()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .uiLabel(UUID.randomUUID().toString());
    }
}
