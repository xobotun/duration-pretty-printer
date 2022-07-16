package com.xobotun.durationprettyprinter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WeirdTemporalUnit implements TemporalUnit {
    private static final Duration WEIRD_DURATION = Duration.ofSeconds(1, 999_000_000);
    public static final WeirdTemporalUnit WEIRD = new WeirdTemporalUnit();

    @Override
    public Duration getDuration() {
        return WEIRD_DURATION;
    }

    @Override
    public boolean isDurationEstimated() {
        return false;
    }

    @Override
    public boolean isDateBased() {
        return false;
    }

    @Override
    public boolean isTimeBased() {
        return true;
    }

    @Override
    public <R extends Temporal> R addTo(final R temporal, final long amount) {
        throw new RuntimeException("Too lazy to implement for tests");
    }

    @Override
    public long between(final Temporal temporal1Inclusive, final Temporal temporal2Exclusive) {
        return temporal1Inclusive.until(temporal2Exclusive, this);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
