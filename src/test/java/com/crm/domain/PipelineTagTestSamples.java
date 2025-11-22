package com.crm.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PipelineTagTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PipelineTag getPipelineTagSample1() {
        return new PipelineTag().id(1L).name("name1");
    }

    public static PipelineTag getPipelineTagSample2() {
        return new PipelineTag().id(2L).name("name2");
    }

    public static PipelineTag getPipelineTagRandomSampleGenerator() {
        return new PipelineTag().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
