package com.mgs.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DepartmentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Department getDepartmentSample1() {
        return new Department().id(1L).name("name1").nameSearch("nameSearch1").type("type1");
    }

    public static Department getDepartmentSample2() {
        return new Department().id(2L).name("name2").nameSearch("nameSearch2").type("type2");
    }

    public static Department getDepartmentRandomSampleGenerator() {
        return new Department()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .nameSearch(UUID.randomUUID().toString())
            .type(UUID.randomUUID().toString());
    }
}
