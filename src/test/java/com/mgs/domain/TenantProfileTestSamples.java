package com.mgs.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TenantProfileTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TenantProfile getTenantProfileSample1() {
        return new TenantProfile()
            .id(1L)
            .subdomain("subdomain1")
            .customDomain("customDomain1")
            .defaultLocale("defaultLocale1")
            .timezone("timezone1");
    }

    public static TenantProfile getTenantProfileSample2() {
        return new TenantProfile()
            .id(2L)
            .subdomain("subdomain2")
            .customDomain("customDomain2")
            .defaultLocale("defaultLocale2")
            .timezone("timezone2");
    }

    public static TenantProfile getTenantProfileRandomSampleGenerator() {
        return new TenantProfile()
            .id(longCount.incrementAndGet())
            .subdomain(UUID.randomUUID().toString())
            .customDomain(UUID.randomUUID().toString())
            .defaultLocale(UUID.randomUUID().toString())
            .timezone(UUID.randomUUID().toString());
    }
}
