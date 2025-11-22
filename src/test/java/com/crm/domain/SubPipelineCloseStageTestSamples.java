package com.crm.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SubPipelineCloseStageTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static SubPipelineCloseStage getSubPipelineCloseStageSample1() {
        return new SubPipelineCloseStage().id(1L).index(1);
    }

    public static SubPipelineCloseStage getSubPipelineCloseStageSample2() {
        return new SubPipelineCloseStage().id(2L).index(2);
    }

    public static SubPipelineCloseStage getSubPipelineCloseStageRandomSampleGenerator() {
        return new SubPipelineCloseStage().id(longCount.incrementAndGet()).index(intCount.incrementAndGet());
    }
}
