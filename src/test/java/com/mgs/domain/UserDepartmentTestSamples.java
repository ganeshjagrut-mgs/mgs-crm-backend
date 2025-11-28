package com.mgs.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class UserDepartmentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UserDepartment getUserDepartmentSample1() {
        return new UserDepartment().id(1L);
    }

    public static UserDepartment getUserDepartmentSample2() {
        return new UserDepartment().id(2L);
    }

    public static UserDepartment getUserDepartmentRandomSampleGenerator() {
        return new UserDepartment().id(longCount.incrementAndGet());
    }
}
