package com.mgs.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PipelineTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Pipeline getPipelineSample1() {
        return new Pipeline().id(1L).name("name1").nameSearch("nameSearch1");
    }

    public static Pipeline getPipelineSample2() {
        return new Pipeline().id(2L).name("name2").nameSearch("nameSearch2");
    }

    public static Pipeline getPipelineRandomSampleGenerator() {
        return new Pipeline().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).nameSearch(UUID.randomUUID().toString());
    }
}
