package com.mgs.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AuditLogTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AuditLog getAuditLogSample1() {
        return new AuditLog()
            .id(1L)
            .actionType("actionType1")
            .entityType("entityType1")
            .entityId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .oldValue("oldValue1")
            .newValue("newValue1");
    }

    public static AuditLog getAuditLogSample2() {
        return new AuditLog()
            .id(2L)
            .actionType("actionType2")
            .entityType("entityType2")
            .entityId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .oldValue("oldValue2")
            .newValue("newValue2");
    }

    public static AuditLog getAuditLogRandomSampleGenerator() {
        return new AuditLog()
            .id(longCount.incrementAndGet())
            .actionType(UUID.randomUUID().toString())
            .entityType(UUID.randomUUID().toString())
            .entityId(UUID.randomUUID())
            .oldValue(UUID.randomUUID().toString())
            .newValue(UUID.randomUUID().toString());
    }
}
