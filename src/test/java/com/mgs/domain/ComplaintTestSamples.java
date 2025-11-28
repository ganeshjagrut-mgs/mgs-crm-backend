package com.mgs.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ComplaintTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Complaint getComplaintSample1() {
        return new Complaint().id(1L).complaintNumber("complaintNumber1").subject("subject1").description("description1");
    }

    public static Complaint getComplaintSample2() {
        return new Complaint().id(2L).complaintNumber("complaintNumber2").subject("subject2").description("description2");
    }

    public static Complaint getComplaintRandomSampleGenerator() {
        return new Complaint()
            .id(longCount.incrementAndGet())
            .complaintNumber(UUID.randomUUID().toString())
            .subject(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
