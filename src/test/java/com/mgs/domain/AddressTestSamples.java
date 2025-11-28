package com.mgs.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class AddressTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Address getAddressSample1() {
        return new Address().id(1L);
    }

    public static Address getAddressSample2() {
        return new Address().id(2L);
    }

    public static Address getAddressRandomSampleGenerator() {
        return new Address().id(longCount.incrementAndGet());
    }
}
