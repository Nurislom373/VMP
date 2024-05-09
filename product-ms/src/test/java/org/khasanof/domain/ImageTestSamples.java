package org.khasanof.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ImageTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Image getImageSample1() {
        return new Image().id(1L).imageKey("imageKey1");
    }

    public static Image getImageSample2() {
        return new Image().id(2L).imageKey("imageKey2");
    }

    public static Image getImageRandomSampleGenerator() {
        return new Image().id(longCount.incrementAndGet()).imageKey(UUID.randomUUID().toString());
    }
}
