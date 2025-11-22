package com.crm.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AddressTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Address getAddressSample1() {
        return new Address().id(1L).addressLine1("addressLine11").addressLine2("addressLine21").pincode(1);
    }

    public static Address getAddressSample2() {
        return new Address().id(2L).addressLine1("addressLine12").addressLine2("addressLine22").pincode(2);
    }

    public static Address getAddressRandomSampleGenerator() {
        return new Address()
            .id(longCount.incrementAndGet())
            .addressLine1(UUID.randomUUID().toString())
            .addressLine2(UUID.randomUUID().toString())
            .pincode(intCount.incrementAndGet());
    }
}
