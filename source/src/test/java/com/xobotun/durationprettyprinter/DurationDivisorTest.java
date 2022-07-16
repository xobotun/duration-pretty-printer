package com.xobotun.durationprettyprinter;

import org.junit.jupiter.api.Test;

import java.time.temporal.ChronoUnit;

import static com.xobotun.durationprettyprinter.WeirdTemporalUnit.WEIRD;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Just a sanity test, everything should behave as I expect.
 *
 * And it worked, as I originally wrote %. >_<
 */
class DurationDivisorTest {
    private static final int SECONDS = 59 + 60;
    private static final int NANOS = 123_000_456;

    @Test
    void modulo_unsupportedUnit() {
        assertThrowsExactly(UnsupportedTemporalUnitException.class, () -> { DurationDivisor.modulo(SECONDS, NANOS, WEIRD); });
    }

    @Test
    void modulo_hoursDoNotFit0() {
        long numOfFits = DurationDivisor.modulo(SECONDS, NANOS, ChronoUnit.HOURS);

        assertEquals(0, numOfFits);
    }

    @Test
    void modulo_minutesFitOnce() {
        long numOfFits = DurationDivisor.modulo(SECONDS, NANOS, ChronoUnit.MINUTES);

        assertEquals(1, numOfFits);
    }

    @Test
    void modulo_secondsFit() {
        long numOfFits = DurationDivisor.modulo(SECONDS, NANOS, ChronoUnit.SECONDS);

        assertEquals(SECONDS, numOfFits);
    }

    @Test
    void modulo_millisFit123Times() {
        long numOfFits = DurationDivisor.modulo(SECONDS, NANOS, ChronoUnit.MILLIS);

        assertEquals(123, numOfFits);
    }

    @Test
    void modulo_nanosFit() {
        long numOfFits = DurationDivisor.modulo(SECONDS, NANOS, ChronoUnit.NANOS);

        assertEquals(NANOS, numOfFits);
    }
}
