package com.crm.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TenantTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Tenant getTenantSample1() {
        return new Tenant()
            .id(1L)
            .companyName("companyName1")
            .contactPerson("contactPerson1")
            .logo("logo1")
            .website("website1")
            .registrationNumber("registrationNumber1")
            .subId(1);
    }

    public static Tenant getTenantSample2() {
        return new Tenant()
            .id(2L)
            .companyName("companyName2")
            .contactPerson("contactPerson2")
            .logo("logo2")
            .website("website2")
            .registrationNumber("registrationNumber2")
            .subId(2);
    }

    public static Tenant getTenantRandomSampleGenerator() {
        return new Tenant()
            .id(longCount.incrementAndGet())
            .companyName(UUID.randomUUID().toString())
            .contactPerson(UUID.randomUUID().toString())
            .logo(UUID.randomUUID().toString())
            .website(UUID.randomUUID().toString())
            .registrationNumber(UUID.randomUUID().toString())
            .subId(intCount.incrementAndGet());
    }
}
