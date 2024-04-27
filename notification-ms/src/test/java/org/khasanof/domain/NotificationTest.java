package org.khasanof.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.khasanof.domain.NotificationTestSamples.*;

import org.junit.jupiter.api.Test;
import org.khasanof.web.rest.TestUtil;

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
}
