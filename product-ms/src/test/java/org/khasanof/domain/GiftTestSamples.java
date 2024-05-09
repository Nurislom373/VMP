package org.khasanof.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class GiftTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Gift getGiftSample1() {
        return new Gift().id(1L).name("name1").stock(1L);
    }

    public static Gift getGiftSample2() {
        return new Gift().id(2L).name("name2").stock(2L);
    }

    public static Gift getGiftRandomSampleGenerator() {
        return new Gift().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).stock(longCount.incrementAndGet());
    }
}
