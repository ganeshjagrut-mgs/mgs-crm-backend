package com.mgs.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TenantBrandingTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TenantBranding getTenantBrandingSample1() {
        return new TenantBranding()
            .id(1L)
            .logoPath("logoPath1")
            .logoDarkPath("logoDarkPath1")
            .faviconPath("faviconPath1")
            .primaryColor("primaryColor1")
            .secondaryColor("secondaryColor1")
            .accentColor("accentColor1")
            .pdfHeaderLogoPath("pdfHeaderLogoPath1")
            .pdfFooterText("pdfFooterText1")
            .pdfPrimaryColor("pdfPrimaryColor1");
    }

    public static TenantBranding getTenantBrandingSample2() {
        return new TenantBranding()
            .id(2L)
            .logoPath("logoPath2")
            .logoDarkPath("logoDarkPath2")
            .faviconPath("faviconPath2")
            .primaryColor("primaryColor2")
            .secondaryColor("secondaryColor2")
            .accentColor("accentColor2")
            .pdfHeaderLogoPath("pdfHeaderLogoPath2")
            .pdfFooterText("pdfFooterText2")
            .pdfPrimaryColor("pdfPrimaryColor2");
    }

    public static TenantBranding getTenantBrandingRandomSampleGenerator() {
        return new TenantBranding()
            .id(longCount.incrementAndGet())
            .logoPath(UUID.randomUUID().toString())
            .logoDarkPath(UUID.randomUUID().toString())
            .faviconPath(UUID.randomUUID().toString())
            .primaryColor(UUID.randomUUID().toString())
            .secondaryColor(UUID.randomUUID().toString())
            .accentColor(UUID.randomUUID().toString())
            .pdfHeaderLogoPath(UUID.randomUUID().toString())
            .pdfFooterText(UUID.randomUUID().toString())
            .pdfPrimaryColor(UUID.randomUUID().toString());
    }
}
