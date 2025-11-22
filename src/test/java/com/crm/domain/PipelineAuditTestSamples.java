package com.crm.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PipelineAuditTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PipelineAudit getPipelineAuditSample1() {
        return new PipelineAudit()
            .id(1L)
            .action("action1")
            .rowId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .correlationId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"));
    }

    public static PipelineAudit getPipelineAuditSample2() {
        return new PipelineAudit()
            .id(2L)
            .action("action2")
            .rowId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .correlationId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"));
    }

    public static PipelineAudit getPipelineAuditRandomSampleGenerator() {
        return new PipelineAudit()
            .id(longCount.incrementAndGet())
            .action(UUID.randomUUID().toString())
            .rowId(UUID.randomUUID())
            .correlationId(UUID.randomUUID());
    }
}
