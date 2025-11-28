package com.mgs.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DealTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Deal getDealSample1() {
        return new Deal().id(1L).dealNumber("dealNumber1").currency("currency1").notes("notes1");
    }

    public static Deal getDealSample2() {
        return new Deal().id(2L).dealNumber("dealNumber2").currency("currency2").notes("notes2");
    }

    public static Deal getDealRandomSampleGenerator() {
        return new Deal()
            .id(longCount.incrementAndGet())
            .dealNumber(UUID.randomUUID().toString())
            .currency(UUID.randomUUID().toString())
            .notes(UUID.randomUUID().toString());
    }
}
