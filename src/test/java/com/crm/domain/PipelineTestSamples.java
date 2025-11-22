package com.crm.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PipelineTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Pipeline getPipelineSample1() {
        return new Pipeline()
            .id(1L)
            .pipelineName("pipelineName1")
            .noOfSamples(1)
            .correlationId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"));
    }

    public static Pipeline getPipelineSample2() {
        return new Pipeline()
            .id(2L)
            .pipelineName("pipelineName2")
            .noOfSamples(2)
            .correlationId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"));
    }

    public static Pipeline getPipelineRandomSampleGenerator() {
        return new Pipeline()
            .id(longCount.incrementAndGet())
            .pipelineName(UUID.randomUUID().toString())
            .noOfSamples(intCount.incrementAndGet())
            .correlationId(UUID.randomUUID());
    }
}
