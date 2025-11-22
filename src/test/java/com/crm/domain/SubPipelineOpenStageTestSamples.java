package com.crm.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SubPipelineOpenStageTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static SubPipelineOpenStage getSubPipelineOpenStageSample1() {
        return new SubPipelineOpenStage().id(1L).index(1);
    }

    public static SubPipelineOpenStage getSubPipelineOpenStageSample2() {
        return new SubPipelineOpenStage().id(2L).index(2);
    }

    public static SubPipelineOpenStage getSubPipelineOpenStageRandomSampleGenerator() {
        return new SubPipelineOpenStage().id(longCount.incrementAndGet()).index(intCount.incrementAndGet());
    }
}
