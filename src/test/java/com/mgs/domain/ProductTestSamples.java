package com.mgs.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProductTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Product getProductSample1() {
        return new Product()
            .id(1L)
            .name("name1")
            .nameSearch("nameSearch1")
            .sku("sku1")
            .description("description1")
            .category("category1")
            .unitLabel("unitLabel1")
            .currency("currency1");
    }

    public static Product getProductSample2() {
        return new Product()
            .id(2L)
            .name("name2")
            .nameSearch("nameSearch2")
            .sku("sku2")
            .description("description2")
            .category("category2")
            .unitLabel("unitLabel2")
            .currency("currency2");
    }

    public static Product getProductRandomSampleGenerator() {
        return new Product()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .nameSearch(UUID.randomUUID().toString())
            .sku(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .category(UUID.randomUUID().toString())
            .unitLabel(UUID.randomUUID().toString())
            .currency(UUID.randomUUID().toString());
    }
}
