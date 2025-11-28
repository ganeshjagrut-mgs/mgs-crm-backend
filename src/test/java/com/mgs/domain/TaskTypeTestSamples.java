package com.mgs.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TaskTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TaskType getTaskTypeSample1() {
        return new TaskType().id(1L).name("name1");
    }

    public static TaskType getTaskTypeSample2() {
        return new TaskType().id(2L).name("name2");
    }

    public static TaskType getTaskTypeRandomSampleGenerator() {
        return new TaskType().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
