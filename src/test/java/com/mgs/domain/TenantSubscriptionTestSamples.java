package com.mgs.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class TenantSubscriptionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TenantSubscription getTenantSubscriptionSample1() {
        return new TenantSubscription().id(1L);
    }

    public static TenantSubscription getTenantSubscriptionSample2() {
        return new TenantSubscription().id(2L);
    }

    public static TenantSubscription getTenantSubscriptionRandomSampleGenerator() {
        return new TenantSubscription().id(longCount.incrementAndGet());
    }
}
