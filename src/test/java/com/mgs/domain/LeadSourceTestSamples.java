package com.mgs.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class LeadSourceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static LeadSource getLeadSourceSample1() {
        return new LeadSource().id(1L).name("name1").nameSearch("nameSearch1");
    }

    public static LeadSource getLeadSourceSample2() {
        return new LeadSource().id(2L).name("name2").nameSearch("nameSearch2");
    }

    public static LeadSource getLeadSourceRandomSampleGenerator() {
        return new LeadSource().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).nameSearch(UUID.randomUUID().toString());
    }
}
