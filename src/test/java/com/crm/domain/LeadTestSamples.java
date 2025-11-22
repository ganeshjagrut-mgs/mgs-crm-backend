package com.crm.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class LeadTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Lead getLeadSample1() {
        return new Lead().id(1L).name("name1").leadNumber("leadNumber1").annualRevenue(1);
    }

    public static Lead getLeadSample2() {
        return new Lead().id(2L).name("name2").leadNumber("leadNumber2").annualRevenue(2);
    }

    public static Lead getLeadRandomSampleGenerator() {
        return new Lead()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .leadNumber(UUID.randomUUID().toString())
            .annualRevenue(intCount.incrementAndGet());
    }
}
