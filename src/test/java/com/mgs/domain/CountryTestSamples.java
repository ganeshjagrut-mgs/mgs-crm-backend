package com.mgs.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CountryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Country getCountrySample1() {
        return new Country().id(1L).isoCode2("isoCode21").isoCode3("isoCode31").name("name1");
    }

    public static Country getCountrySample2() {
        return new Country().id(2L).isoCode2("isoCode22").isoCode3("isoCode32").name("name2");
    }

    public static Country getCountryRandomSampleGenerator() {
        return new Country()
            .id(longCount.incrementAndGet())
            .isoCode2(UUID.randomUUID().toString())
            .isoCode3(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString());
    }
}
