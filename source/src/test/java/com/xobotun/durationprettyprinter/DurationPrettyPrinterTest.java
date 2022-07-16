package com.xobotun.durationprettyprinter;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static com.xobotun.durationprettyprinter.DurationPrettyPrinter.*;
import static org.junit.jupiter.api.Assertions.*;

class DurationPrettyPrinterTest {
    // P12Y4MT10H30.999000777S
    private static final Duration TEST_DURATION = Duration.ZERO
        .plus(12 * ChronoUnit.YEARS.getDuration().getSeconds(), ChronoUnit.SECONDS)
        .plus(4 * ChronoUnit.MONTHS.getDuration().getSeconds(), ChronoUnit.SECONDS)
        .plus(10, ChronoUnit.HOURS)
        .plus(30, ChronoUnit.SECONDS)
        .plus(999, ChronoUnit.MILLIS)
        .plus(777, ChronoUnit.NANOS);

    @Test
    void shortenedTest() {
        String actual = shortenedToString(TEST_DURATION);

        assertEquals("12 years 4 months 10 hours", actual);
    }

    @Test
    void nonzeroTest() {
        String actual = nonzeroToString(TEST_DURATION);

        assertEquals("12 years 4 months 10 hours 30 seconds 999 milliseconds 777 nanoseconds", actual);
    }

    @Test
    void fullTest() {
        String actual = fullToString(TEST_DURATION);

        assertEquals("12 years 4 months 0 days 10 hours 0 minutes 30 seconds 999 milliseconds 0 microseconds 777 nanoseconds", actual);
    }

    @Test
    void zeroPlacesMeansEmptyString() {
        String actual = prettyPrint(TEST_DURATION, DEFAULT_TEMPORAL_BITS, 0, DropZeroMode.DROP_ZEROS, DEFAULT_JOINER);

        assertEquals("", actual);
    }

}
