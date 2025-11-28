package com.mgs.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ReportRunTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ReportRun getReportRunSample1() {
        return new ReportRun().id(1L).name("name1").filterJson("filterJson1").format("format1").filePath("filePath1");
    }

    public static ReportRun getReportRunSample2() {
        return new ReportRun().id(2L).name("name2").filterJson("filterJson2").format("format2").filePath("filePath2");
    }

    public static ReportRun getReportRunRandomSampleGenerator() {
        return new ReportRun()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .filterJson(UUID.randomUUID().toString())
            .format(UUID.randomUUID().toString())
            .filePath(UUID.randomUUID().toString());
    }
}
