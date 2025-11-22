package com.crm.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CustomerTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Customer getCustomerSample1() {
        return new Customer()
            .id(1L)
            .name("name1")
            .description("description1")
            .companyCity("companyCity1")
            .companyArea("companyArea1")
            .website("website1")
            .customerName("customerName1")
            .currencyType("currencyType1")
            .gstNo("gstNo1")
            .panNo("panNo1")
            .serviceTaxNo("serviceTaxNo1")
            .tanNo("tanNo1")
            .correlationId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .accountNo("accountNo1")
            .gstStateName("gstStateName1")
            .gstStateCode("gstStateCode1")
            .totalPipeline(1)
            .type("type1");
    }

    public static Customer getCustomerSample2() {
        return new Customer()
            .id(2L)
            .name("name2")
            .description("description2")
            .companyCity("companyCity2")
            .companyArea("companyArea2")
            .website("website2")
            .customerName("customerName2")
            .currencyType("currencyType2")
            .gstNo("gstNo2")
            .panNo("panNo2")
            .serviceTaxNo("serviceTaxNo2")
            .tanNo("tanNo2")
            .correlationId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .accountNo("accountNo2")
            .gstStateName("gstStateName2")
            .gstStateCode("gstStateCode2")
            .totalPipeline(2)
            .type("type2");
    }

    public static Customer getCustomerRandomSampleGenerator() {
        return new Customer()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .companyCity(UUID.randomUUID().toString())
            .companyArea(UUID.randomUUID().toString())
            .website(UUID.randomUUID().toString())
            .customerName(UUID.randomUUID().toString())
            .currencyType(UUID.randomUUID().toString())
            .gstNo(UUID.randomUUID().toString())
            .panNo(UUID.randomUUID().toString())
            .serviceTaxNo(UUID.randomUUID().toString())
            .tanNo(UUID.randomUUID().toString())
            .correlationId(UUID.randomUUID())
            .accountNo(UUID.randomUUID().toString())
            .gstStateName(UUID.randomUUID().toString())
            .gstStateCode(UUID.randomUUID().toString())
            .totalPipeline(intCount.incrementAndGet())
            .type(UUID.randomUUID().toString());
    }
}
