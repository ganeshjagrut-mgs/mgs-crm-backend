package com.crm.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TaskAuditTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TaskAudit getTaskAuditSample1() {
        return new TaskAudit()
            .id(1L)
            .action("action1")
            .rowId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .correlationId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"));
    }

    public static TaskAudit getTaskAuditSample2() {
        return new TaskAudit()
            .id(2L)
            .action("action2")
            .rowId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .correlationId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"));
    }

    public static TaskAudit getTaskAuditRandomSampleGenerator() {
        return new TaskAudit()
            .id(longCount.incrementAndGet())
            .action(UUID.randomUUID().toString())
            .rowId(UUID.randomUUID())
            .correlationId(UUID.randomUUID());
    }
}
