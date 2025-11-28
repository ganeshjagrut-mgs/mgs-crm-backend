package com.mgs.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UserHierarchyTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UserHierarchy getUserHierarchySample1() {
        return new UserHierarchy().id(1L).relationshipType("relationshipType1");
    }

    public static UserHierarchy getUserHierarchySample2() {
        return new UserHierarchy().id(2L).relationshipType("relationshipType2");
    }

    public static UserHierarchy getUserHierarchyRandomSampleGenerator() {
        return new UserHierarchy().id(longCount.incrementAndGet()).relationshipType(UUID.randomUUID().toString());
    }
}
