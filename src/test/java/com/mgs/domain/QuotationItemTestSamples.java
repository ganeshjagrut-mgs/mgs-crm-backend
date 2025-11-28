package com.mgs.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class QuotationItemTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static QuotationItem getQuotationItemSample1() {
        return new QuotationItem()
            .id(1L)
            .productName("productName1")
            .productSku("productSku1")
            .productDescription("productDescription1")
            .unitLabel("unitLabel1")
            .sortOrder(1);
    }

    public static QuotationItem getQuotationItemSample2() {
        return new QuotationItem()
            .id(2L)
            .productName("productName2")
            .productSku("productSku2")
            .productDescription("productDescription2")
            .unitLabel("unitLabel2")
            .sortOrder(2);
    }

    public static QuotationItem getQuotationItemRandomSampleGenerator() {
        return new QuotationItem()
            .id(longCount.incrementAndGet())
            .productName(UUID.randomUUID().toString())
            .productSku(UUID.randomUUID().toString())
            .productDescription(UUID.randomUUID().toString())
            .unitLabel(UUID.randomUUID().toString())
            .sortOrder(intCount.incrementAndGet());
    }
}
