package com.xobotun.durationprettyprinter;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.xobotun.durationprettyprinter.DurationPrettyPrinter.DEFAULT_TEMPORAL_BITS;

/**
 * List of various duration types.<br/>
 * <br/>
 * Different lists have different types to prevent JIT optimisations to show worst-case performance in JMH.<br/>
 * Sorted by hashCode because it provides a random enough and determenistic order.<br/>
 */
public class DurationSamples {
    private static final Random RANDOM = new Random(0); //

    public static final List<Duration> DEFAULT_CHRONOUNITS = new ArrayList<>();
    public static final List<Duration> YMD_DURATIONS = new ArrayList<>(); // years + months + days
    public static final List<Duration> HMS_DURATIONS = new ArrayList<>(); // hours + minutes + seconds
    public static final List<Duration> SUBSECOND_DURATIONS = new ArrayList<>();
    public static final List<Duration> MIXED_DURATIONS = new ArrayList<>();
    public static final List<Duration> VARIOUS_DURATIONS = new ArrayList<>();

    static {
        // Populate lists
        DEFAULT_CHRONOUNITS.addAll(
            Arrays.stream(ChronoUnit.values())
                .map(ChronoUnit::getDuration)
                .collect(Collectors.toList())
        );
        int groupSize = DEFAULT_CHRONOUNITS.size() * 2;

        YMD_DURATIONS.addAll(
            IntStream.range(0, groupSize)
                .mapToObj(__ -> Duration.ZERO
                    .plus(RANDOM.nextInt(101) * ChronoUnit.YEARS.getDuration().getSeconds(), ChronoUnit.SECONDS)
                    .plus(RANDOM.nextInt(12) * ChronoUnit.MONTHS.getDuration().getSeconds(), ChronoUnit.SECONDS)
                    .plus(RANDOM.nextInt(30), ChronoUnit.DAYS)
                )
                .collect(Collectors.toList())
        );
        HMS_DURATIONS.addAll(
            IntStream.range(0, groupSize)
                .mapToObj(__ -> Duration.ZERO
                    .plus(RANDOM.nextInt(24), ChronoUnit.HOURS)
                    .plus(RANDOM.nextInt(60), ChronoUnit.MINUTES)
                    .plus(RANDOM.nextInt(60), ChronoUnit.SECONDS)
                )
                .collect(Collectors.toList())
        );
        SUBSECOND_DURATIONS.addAll(
            IntStream.range(0, groupSize)
                .mapToObj(__ -> Duration.ZERO
                    .plus(RANDOM.nextInt(1000), ChronoUnit.MILLIS)
                    .plus(RANDOM.nextInt(1000), ChronoUnit.MICROS)
                    .plus(RANDOM.nextInt(1000), ChronoUnit.NANOS)
                )
                .collect(Collectors.toList())
        );
        MIXED_DURATIONS.addAll(
            IntStream.range(0, groupSize)
                .mapToObj(__ -> {
                    Duration result = Duration.ZERO;
                    for (final ChronoUnit unit : DEFAULT_TEMPORAL_BITS) {
                        result = result.plus(RANDOM.nextInt(10) * unit.getDuration().getSeconds(), ChronoUnit.SECONDS);
                    }
                    return result;
                })
                .collect(Collectors.toList())
        );

        VARIOUS_DURATIONS.addAll(DEFAULT_CHRONOUNITS);
        VARIOUS_DURATIONS.addAll(YMD_DURATIONS);
        VARIOUS_DURATIONS.addAll(HMS_DURATIONS);
        VARIOUS_DURATIONS.addAll(SUBSECOND_DURATIONS);
        VARIOUS_DURATIONS.addAll(MIXED_DURATIONS);

        // Shuffle them to prevent JVM from optimizing consequental calls.
        DEFAULT_CHRONOUNITS.sort(Comparator.comparingInt(Duration::hashCode));
        YMD_DURATIONS      .sort(Comparator.comparingInt(Duration::hashCode));
        HMS_DURATIONS      .sort(Comparator.comparingInt(Duration::hashCode));
        SUBSECOND_DURATIONS.sort(Comparator.comparingInt(Duration::hashCode));
        MIXED_DURATIONS    .sort(Comparator.comparingInt(Duration::hashCode));
        VARIOUS_DURATIONS  .sort(Comparator.comparingInt(Duration::hashCode));
    }
}
