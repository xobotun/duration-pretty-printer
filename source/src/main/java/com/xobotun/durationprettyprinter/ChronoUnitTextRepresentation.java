package com.xobotun.durationprettyprinter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.temporal.TemporalUnit;
import java.util.HashMap;
import java.util.Map;

import static java.time.temporal.ChronoUnit.*;

/**
 * A dictionary for {@link java.time.temporal.ChronoUnit} human readable names.<br/>
 * Mutable to allow expansion with custom {@link TemporalUnit}s,
 * though I have never seen one.<br/>
 * <br/>
 * Also, this project was never intended to tackle different locales,
 * just to print good enough string for API messages or logs.<br/>
 * <br/>
 * Also strings are hardcoded for a little bit faster startup time.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChronoUnitTextRepresentation {
    private static final Map<TemporalUnit, String> PLURAL = new HashMap<>(Map.ofEntries(
        Map.entry(NANOS,     "nanoseconds"),
        Map.entry(MICROS,    "microseconds"),
        Map.entry(MILLIS,    "milliseconds"),
        Map.entry(SECONDS,   "seconds"),
        Map.entry(MINUTES,   "minutes"),
        Map.entry(HOURS,     "hours"),
        Map.entry(HALF_DAYS, "half-days"),
        Map.entry(DAYS,      "days"),
        Map.entry(WEEKS,     "weeks"),
        Map.entry(MONTHS,    "months"),
        Map.entry(YEARS,     "years"),
        Map.entry(DECADES,   "decades"),
        Map.entry(CENTURIES, "centuries"),
        Map.entry(MILLENNIA, "millenia"),
        Map.entry(ERAS,      "eras"),
        Map.entry(FOREVER,   "forevers")
    ));

    private static final Map<TemporalUnit, String> SINGULAR = new HashMap<>(Map.ofEntries(
        Map.entry(NANOS,     "nanosecond"),
        Map.entry(MICROS,    "microsecond"),
        Map.entry(MILLIS,    "millisecond"),
        Map.entry(SECONDS,   "second"),
        Map.entry(MINUTES,   "minute"),
        Map.entry(HOURS,     "hour"),
        Map.entry(HALF_DAYS, "half-day"),
        Map.entry(DAYS,      "day"),
        Map.entry(WEEKS,     "week"),
        Map.entry(MONTHS,    "month"),
        Map.entry(YEARS,     "year"),
        Map.entry(DECADES,   "decade"),
        Map.entry(CENTURIES, "century"),
        Map.entry(MILLENNIA, "millenium"),
        Map.entry(ERAS,      "era"),
        Map.entry(FOREVER,   "forever")
    ));

    public static String getPlural(TemporalUnit unit) {
        return PLURAL.get(unit);
    }

    public static String getSingular(TemporalUnit unit) {
        return SINGULAR.get(unit);
    }

    /**
     * Register a custom {@link TemporalUnit}.<br/>
     * Also, send me a message on GitHub why did you need to do it, I'm geniunely curious.
     * @param unit to be registered
     * @param singular representation of said unit
     * @param plural representation of said unit
     */
    public static void registerCustom(TemporalUnit unit, String singular, String plural) {
        if (unit.getDuration().getSeconds() != 0L && unit.getDuration().getNano() != 0) {
            throw new UnsupportedTemporalUnitException(unit);
        }

        PLURAL.put(unit, plural);
        SINGULAR.put(unit, singular);
    }
}
