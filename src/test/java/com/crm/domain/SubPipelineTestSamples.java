package com.crm.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SubPipelineTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static SubPipeline getSubPipelineSample1() {
        return new SubPipeline().id(1L).name("name1").index(1);
    }

    public static SubPipeline getSubPipelineSample2() {
        return new SubPipeline().id(2L).name("name2").index(2);
    }

    public static SubPipeline getSubPipelineRandomSampleGenerator() {
        return new SubPipeline().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).index(intCount.incrementAndGet());
    }
}
