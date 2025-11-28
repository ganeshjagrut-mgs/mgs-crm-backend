package com.mgs.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ReportTemplateTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ReportTemplate getReportTemplateSample1() {
        return new ReportTemplate().id(1L).code("code1").name("name1").description("description1").configJson("configJson1");
    }

    public static ReportTemplate getReportTemplateSample2() {
        return new ReportTemplate().id(2L).code("code2").name("name2").description("description2").configJson("configJson2");
    }

    public static ReportTemplate getReportTemplateRandomSampleGenerator() {
        return new ReportTemplate()
            .id(longCount.incrementAndGet())
            .code(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .configJson(UUID.randomUUID().toString());
    }
}
