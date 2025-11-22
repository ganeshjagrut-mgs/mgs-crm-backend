package com.crm.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class QuotationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Quotation getQuotationSample1() {
        return new Quotation()
            .id(1L)
            .quotationNumber("quotationNumber1")
            .referenceNumber("referenceNumber1")
            .subject("subject1")
            .currency("currency1")
            .statusReason("statusReason1")
            .emailFailureReason("emailFailureReason1")
            .correlationId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"));
    }

    public static Quotation getQuotationSample2() {
        return new Quotation()
            .id(2L)
            .quotationNumber("quotationNumber2")
            .referenceNumber("referenceNumber2")
            .subject("subject2")
            .currency("currency2")
            .statusReason("statusReason2")
            .emailFailureReason("emailFailureReason2")
            .correlationId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"));
    }

    public static Quotation getQuotationRandomSampleGenerator() {
        return new Quotation()
            .id(longCount.incrementAndGet())
            .quotationNumber(UUID.randomUUID().toString())
            .referenceNumber(UUID.randomUUID().toString())
            .subject(UUID.randomUUID().toString())
            .currency(UUID.randomUUID().toString())
            .statusReason(UUID.randomUUID().toString())
            .emailFailureReason(UUID.randomUUID().toString())
            .correlationId(UUID.randomUUID());
    }
}
