package com.mgs.domain;

import static com.mgs.domain.NotificationTestSamples.*;
import static com.mgs.domain.TenantTestSamples.*;
import static com.mgs.domain.UserTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mgs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NotificationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Notification.class);
        Notification notification1 = getNotificationSample1();
        Notification notification2 = new Notification();
        assertThat(notification1).isNotEqualTo(notification2);

        notification2.setId(notification1.getId());
        assertThat(notification1).isEqualTo(notification2);

        notification2 = getNotificationSample2();
        assertThat(notification1).isNotEqualTo(notification2);
    }

    @Test
    void tenantTest() {
        Notification notification = getNotificationRandomSampleGenerator();
        Tenant tenantBack = getTenantRandomSampleGenerator();

        notification.setTenant(tenantBack);
        assertThat(notification.getTenant()).isEqualTo(tenantBack);

        notification.tenant(null);
        assertThat(notification.getTenant()).isNull();
    }

    @Test
    void recipientTest() {
        Notification notification = getNotificationRandomSampleGenerator();
        User userBack = getUserRandomSampleGenerator();

        notification.setRecipient(userBack);
        assertThat(notification.getRecipient()).isEqualTo(userBack);

        notification.recipient(null);
        assertThat(notification.getRecipient()).isNull();
    }
}
