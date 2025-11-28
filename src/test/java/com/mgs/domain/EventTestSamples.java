package com.mgs.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EventTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Event getEventSample1() {
        return new Event()
            .id(1L)
            .eventType("eventType1")
            .eventKey(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .description("description1");
    }

    public static Event getEventSample2() {
        return new Event()
            .id(2L)
            .eventType("eventType2")
            .eventKey(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .description("description2");
    }

    public static Event getEventRandomSampleGenerator() {
        return new Event()
            .id(longCount.incrementAndGet())
            .eventType(UUID.randomUUID().toString())
            .eventKey(UUID.randomUUID())
            .description(UUID.randomUUID().toString());
    }
}
