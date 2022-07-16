package com.xobotun.durationprettyprinter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.temporal.TemporalUnit;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DurationDivisor {
    public static long modulo(long secondsRemaining, int nanosRemaining, TemporalUnit unit) {
        Duration unitDuration = unit.getDuration();
        long secondsDuration = unitDuration.getSeconds();
        int nanosDuration = unitDuration.getNano();

        if (secondsDuration != 0L && nanosDuration != 0) {
            throw new UnsupportedTemporalUnitException(unit);
        }

        if (nanosDuration == 0) {
            return secondsRemaining / secondsDuration;
        } else {
            return nanosRemaining / nanosDuration;
        }
    }
}
