package com.mgs.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EventNotificationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static EventNotification getEventNotificationSample1() {
        return new EventNotification().id(1L).notificationType("notificationType1").message("message1");
    }

    public static EventNotification getEventNotificationSample2() {
        return new EventNotification().id(2L).notificationType("notificationType2").message("message2");
    }

    public static EventNotification getEventNotificationRandomSampleGenerator() {
        return new EventNotification()
            .id(longCount.incrementAndGet())
            .notificationType(UUID.randomUUID().toString())
            .message(UUID.randomUUID().toString());
    }
}
