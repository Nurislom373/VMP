package org.khasanof.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class OptionVariantTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static OptionVariant getOptionVariantSample1() {
        return new OptionVariant().id(1L).name("name1").stock(1L);
    }

    public static OptionVariant getOptionVariantSample2() {
        return new OptionVariant().id(2L).name("name2").stock(2L);
    }

    public static OptionVariant getOptionVariantRandomSampleGenerator() {
        return new OptionVariant().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).stock(longCount.incrementAndGet());
    }
}
