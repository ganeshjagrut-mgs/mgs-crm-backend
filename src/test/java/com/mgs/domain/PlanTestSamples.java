package com.mgs.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PlanTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Plan getPlanSample1() {
        return new Plan()
            .id(1L)
            .code("code1")
            .name("name1")
            .description("description1")
            .maxUsers(1)
            .maxStorageMb(1)
            .maxCustomers(1)
            .maxContacts(1)
            .maxQuotations(1)
            .maxComplaints(1)
            .currency("currency1");
    }

    public static Plan getPlanSample2() {
        return new Plan()
            .id(2L)
            .code("code2")
            .name("name2")
            .description("description2")
            .maxUsers(2)
            .maxStorageMb(2)
            .maxCustomers(2)
            .maxContacts(2)
            .maxQuotations(2)
            .maxComplaints(2)
            .currency("currency2");
    }

    public static Plan getPlanRandomSampleGenerator() {
        return new Plan()
            .id(longCount.incrementAndGet())
            .code(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .maxUsers(intCount.incrementAndGet())
            .maxStorageMb(intCount.incrementAndGet())
            .maxCustomers(intCount.incrementAndGet())
            .maxContacts(intCount.incrementAndGet())
            .maxQuotations(intCount.incrementAndGet())
            .maxComplaints(intCount.incrementAndGet())
            .currency(UUID.randomUUID().toString());
    }
}
