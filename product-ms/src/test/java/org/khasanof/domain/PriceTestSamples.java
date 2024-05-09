package org.khasanof.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PriceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Price getPriceSample1() {
        return new Price().id(1L).sku("sku1");
    }

    public static Price getPriceSample2() {
        return new Price().id(2L).sku("sku2");
    }

    public static Price getPriceRandomSampleGenerator() {
        return new Price().id(longCount.incrementAndGet()).sku(UUID.randomUUID().toString());
    }
}
