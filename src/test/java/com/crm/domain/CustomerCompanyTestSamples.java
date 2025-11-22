package com.crm.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CustomerCompanyTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CustomerCompany getCustomerCompanySample1() {
        return new CustomerCompany()
            .id(1L)
            .name("name1")
            .code("code1")
            .description("description1")
            .website("website1")
            .registrationNumber("registrationNumber1");
    }

    public static CustomerCompany getCustomerCompanySample2() {
        return new CustomerCompany()
            .id(2L)
            .name("name2")
            .code("code2")
            .description("description2")
            .website("website2")
            .registrationNumber("registrationNumber2");
    }

    public static CustomerCompany getCustomerCompanyRandomSampleGenerator() {
        return new CustomerCompany()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .code(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .website(UUID.randomUUID().toString())
            .registrationNumber(UUID.randomUUID().toString());
    }
}
