package com.xobotun.durationprettyprinter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;

import static com.xobotun.durationprettyprinter.ChronoUnitTextRepresentation.getPlural;
import static com.xobotun.durationprettyprinter.ChronoUnitTextRepresentation.getSingular;
import static com.xobotun.durationprettyprinter.DropZeroMode.*;
import static com.xobotun.durationprettyprinter.DurationDivisor.modulo;


/**
 * {@link Duration} pretty printer. Supports three default modes:<br/>
 *  • P12Y4MT10H30.999000777S → 12 years 4 months 10 hours<br/>
 *  • P12Y4MT10H30.999000777S → 12 years 4 months 10 hours 1 second 999 milliseconds 777 nanoseconds<br/>
 *  • P12Y4MT10H1.999000777S → 12 years 4 months 0 days 10 hours 0 minutes 1 second 999 milliseconds 0 microseconds 777 nanoseconds<br/>
 * And a configurable you can choose the specifics yourself.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DurationPrettyPrinter {
    /**
     * Arbitrary chosen {@link ChronoUnit}s I thought are generally encountered
     * in the majority of software.<br/>
     * <br/>
     * In other words, they won't fit any task perfectly. Only "good enough".
     */
    static final ChronoUnit[] DEFAULT_TEMPORAL_BITS = {
        ChronoUnit.YEARS,
        ChronoUnit.MONTHS,
        ChronoUnit.DAYS,
        ChronoUnit.HOURS,
        ChronoUnit.MINUTES,
        ChronoUnit.SECONDS,
        ChronoUnit.MILLIS,
        ChronoUnit.MICROS,
        ChronoUnit.NANOS,
    };
    /**
     * I suppose three most significant chrono units are sufficient for a human
     * to understand the approximate duration.
     */
    static final int DEFAULT_SHORTENED_LENGTH = 3;
    /** In case you might want to use ", ". */
    static final String DEFAULT_JOINER = " ";

    ///
    // 1st variant overloads
    ///

    public static String shortenedToString(Duration duration) {
        return prettyPrint(duration, DEFAULT_TEMPORAL_BITS, DEFAULT_SHORTENED_LENGTH, DROP_ZEROS, DEFAULT_JOINER);
    }

    public static String shortenedToString(Duration duration, int shortenUpTo) {
        return prettyPrint(duration, DEFAULT_TEMPORAL_BITS, shortenUpTo, DROP_ZEROS, DEFAULT_JOINER);
    }

    public static String shortenedToString(Duration duration, TemporalUnit[] units) {
        return prettyPrint(duration, units, DEFAULT_SHORTENED_LENGTH, DROP_ZEROS, DEFAULT_JOINER);
    }

    public static String shortenedToString(Duration duration, TemporalUnit[] units, int shortenUpTo) {
        return prettyPrint(duration, units, shortenUpTo, DROP_ZEROS, DEFAULT_JOINER);
    }

    ///
    // 2nd variant overloads
    ///

    public static String nonzeroToString(Duration duration) {
        return prettyPrint(duration, DEFAULT_TEMPORAL_BITS, null, DROP_ZEROS, DEFAULT_JOINER);
    }

    public static String nonzeroToString(Duration duration, TemporalUnit[] units) {
        return prettyPrint(duration, units, null, DROP_ZEROS, DEFAULT_JOINER);
    }

    ///
    // 3rd variant overloads
    ///

    public static String fullToString(Duration duration) {
        return prettyPrint(duration, DEFAULT_TEMPORAL_BITS, null, DROP_HIGHEST, DEFAULT_JOINER);
    }

    public static String fullToString(Duration duration, TemporalUnit[] units) {
        return prettyPrint(duration, units, null, DROP_HIGHEST, DEFAULT_JOINER);
    }

    ///
    // Actual logic
    ///

    /**
     * The core function to form the human-readable duration string.<br/>
     * @param duration to be stringified
     * @param units to be printed out. Must be listed in a descending order, like years → seconds
     * @param shortenUpTo how many significant chrono units to print, starting from the largest one. Null to print all
     * @param dropZeros whether to include zero-valued
     * @param joiner which will be used to searate chrono units
     * @return "null" on null, human-readable string otherwise.
     */
    public static String prettyPrint(Duration duration, TemporalUnit[] units, Integer shortenUpTo, DropZeroMode dropZeros, String joiner) {
        if (duration == null) return "null";
        /* Keeps track of how many chrono units is there to print.                     *\
        \* When need to print all of the {@link units}, hackily use techical infinity. */
        int shortenCounter = (shortenUpTo != null) ? shortenUpTo : Integer.MAX_VALUE;

        long secondsRemaining = duration.getSeconds();
        int nanosRemaining = duration.getNano();
//        /**
//         * `999 milliseconds 999 microseconds 999 nanoseconds` is 49 chars long.
//         * Optimized for this kind of {@link #shortenedToString} calls, longer
//         * strings will cause arrays reallocations at {@link AbstractStringBuilder#newCapacity}
//         */
//        StringBuffer result = new StringBuffer(49);
        List<String> resultBits = new ArrayList<>(units.length);
        for (final TemporalUnit unit : units) {
            // Check it first in case someone passes shortenUpTo equal to 0. :D
            if (resultBits.size() >= shortenCounter) break;

            // Actually get the number to print
            long numberOfFits = modulo(secondsRemaining, nanosRemaining, unit);

            // Check if need to drop it
            if (numberOfFits == 0) {
                if (dropZeros == DROP_ZEROS) continue;
                if (dropZeros == DROP_HIGHEST && resultBits.isEmpty()) continue;
            }

            // Select plural or singular form.
            String unitName = (numberOfFits == 1) ? getSingular(unit) : getPlural(unit);
            resultBits.add("" + numberOfFits + " " + unitName);

            // Decrease seconds or nanos.
            secondsRemaining -= numberOfFits * unit.getDuration().getSeconds();
            nanosRemaining -= numberOfFits * unit.getDuration().getNano();
        }

        return String.join(joiner, resultBits);
    }

}
