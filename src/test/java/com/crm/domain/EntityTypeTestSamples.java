package com.crm.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EntityTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static EntityType getEntityTypeSample1() {
        return new EntityType().id(1L).name("name1").label("label1");
    }

    public static EntityType getEntityTypeSample2() {
        return new EntityType().id(2L).name("name2").label("label2");
    }

    public static EntityType getEntityTypeRandomSampleGenerator() {
        return new EntityType().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).label(UUID.randomUUID().toString());
    }
}
