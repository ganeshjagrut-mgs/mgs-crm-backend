package com.mgs.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class EventTaskAssignmentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static EventTaskAssignment getEventTaskAssignmentSample1() {
        return new EventTaskAssignment().id(1L);
    }

    public static EventTaskAssignment getEventTaskAssignmentSample2() {
        return new EventTaskAssignment().id(2L);
    }

    public static EventTaskAssignment getEventTaskAssignmentRandomSampleGenerator() {
        return new EventTaskAssignment().id(longCount.incrementAndGet());
    }
}
