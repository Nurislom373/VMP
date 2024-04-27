package org.khasanof.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class NotificationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Notification getNotificationSample1() {
        return new Notification().id(1L).sender("sender1").recipient("recipient1");
    }

    public static Notification getNotificationSample2() {
        return new Notification().id(2L).sender("sender2").recipient("recipient2");
    }

    public static Notification getNotificationRandomSampleGenerator() {
        return new Notification()
            .id(longCount.incrementAndGet())
            .sender(UUID.randomUUID().toString())
            .recipient(UUID.randomUUID().toString());
    }
}
