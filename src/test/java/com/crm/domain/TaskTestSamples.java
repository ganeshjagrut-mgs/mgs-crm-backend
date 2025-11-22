package com.crm.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TaskTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Task getTaskSample1() {
        return new Task().id(1L).taskName("taskName1").correlationId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"));
    }

    public static Task getTaskSample2() {
        return new Task().id(2L).taskName("taskName2").correlationId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"));
    }

    public static Task getTaskRandomSampleGenerator() {
        return new Task().id(longCount.incrementAndGet()).taskName(UUID.randomUUID().toString()).correlationId(UUID.randomUUID());
    }
}
