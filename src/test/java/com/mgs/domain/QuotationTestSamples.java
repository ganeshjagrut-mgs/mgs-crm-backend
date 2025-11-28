package com.mgs.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class QuotationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Quotation getQuotationSample1() {
        return new Quotation()
            .id(1L)
            .quoteNumber("quoteNumber1")
            .revisionNumber(1)
            .currency("currency1")
            .title("title1")
            .headerNotes("headerNotes1")
            .footerNotes("footerNotes1")
            .termsAndConditions("termsAndConditions1")
            .referenceNumber("referenceNumber1")
            .pdfTemplateCode("pdfTemplateCode1");
    }

    public static Quotation getQuotationSample2() {
        return new Quotation()
            .id(2L)
            .quoteNumber("quoteNumber2")
            .revisionNumber(2)
            .currency("currency2")
            .title("title2")
            .headerNotes("headerNotes2")
            .footerNotes("footerNotes2")
            .termsAndConditions("termsAndConditions2")
            .referenceNumber("referenceNumber2")
            .pdfTemplateCode("pdfTemplateCode2");
    }

    public static Quotation getQuotationRandomSampleGenerator() {
        return new Quotation()
            .id(longCount.incrementAndGet())
            .quoteNumber(UUID.randomUUID().toString())
            .revisionNumber(intCount.incrementAndGet())
            .currency(UUID.randomUUID().toString())
            .title(UUID.randomUUID().toString())
            .headerNotes(UUID.randomUUID().toString())
            .footerNotes(UUID.randomUUID().toString())
            .termsAndConditions(UUID.randomUUID().toString())
            .referenceNumber(UUID.randomUUID().toString())
            .pdfTemplateCode(UUID.randomUUID().toString());
    }
}
