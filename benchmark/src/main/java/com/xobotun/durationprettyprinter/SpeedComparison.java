package com.xobotun.durationprettyprinter;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.ocpsoft.prettytime.PrettyTime;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static com.xobotun.durationprettyprinter.DurationSamples.VARIOUS_DURATIONS;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork() // Runtime.getRuntime().availableProcessors(), maybe?
@State(Scope.Benchmark)
public class SpeedComparison {
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
            .include(SpeedComparison.class.getSimpleName())
            .build();

        new Runner(opt).run();
    }

    @Benchmark
    public void measure_toString(Blackhole blackhole) {
        forAllDurations(duration -> blackhole.consume(duration.toString()));
    }

    @Benchmark
    public void measure_thisPrettyPrinter_shortenedToString(Blackhole blackhole) {
        forAllDurations(duration -> blackhole.consume(DurationPrettyPrinter.shortenedToString(duration)));
    }

    @Benchmark
    public void measure_thisPrettyPrinter_nonzeroToString(Blackhole blackhole) {
        forAllDurations(duration -> blackhole.consume(DurationPrettyPrinter.nonzeroToString(duration)));
    }

    @Benchmark
    public void measure_thisPrettyPrinter_fullToString(Blackhole blackhole) {
        forAllDurations(duration -> blackhole.consume(DurationPrettyPrinter.fullToString(duration)));
    }

    @Benchmark
    public void measure_stackOverflow01(Blackhole blackhole) {
        forAllDurations(duration -> blackhole.consume(StackOverflow01.formatDuration(duration)));
    }

    @Benchmark
    public void measure_stackOverflow02(Blackhole blackhole) {
        forAllDurations(duration -> blackhole.consume(StackOverflow02.humanReadableFormat(duration)));
    }

    @Benchmark
    public void measure_PrettyTime(Blackhole blackhole) {
        forAllDurations(duration -> blackhole.consume(PrettyTimeWrapper.humanReadableFormat(duration)));
    }

    @Benchmark
    public void measure_Apache(Blackhole blackhole) {
        forAllDurations(duration -> blackhole.consume(ApacheLangCommons.humanReadableFormat(duration)));
    }

    // This surely breaks the benchmarking.
    // Both loop and some INVOKEINTERFACE will surely make some weird differences
    private static void forAllDurations(Consumer<Duration> function) {
        for (final Duration duration : VARIOUS_DURATIONS) {
            function.accept(duration);
        }
    }
}

// Same approach, less configurable
// https://stackoverflow.com/a/66537069/7643283
class StackOverflow01 {
    static String formatDuration(Duration duration) {
        List<String> parts = new ArrayList<>();
        long days = duration.toDaysPart();
        if (days > 0) {
            parts.add(plural(days, "day"));
        }
        int hours = duration.toHoursPart();
        if (hours > 0 || !parts.isEmpty()) {
            parts.add(plural(hours, "hour"));
        }
        int minutes = duration.toMinutesPart();
        if (minutes > 0 || !parts.isEmpty()) {
            parts.add(plural(minutes, "minute"));
        }
        int seconds = duration.toSecondsPart();
        parts.add(plural(seconds, "second"));
        return String.join(", ", parts);
    }

    private static String plural(long num, String unit) {
        return num + " " + unit + (num == 1 ? "" : "s");
    }
}

// Duration#toString-based, regex-based
// https://stackoverflow.com/a/40487511/7643283
class StackOverflow02 {
    public static String humanReadableFormat(Duration duration) {
        return duration.toString()
            .substring(2)
            .replaceAll("(\\d[HMS])(?!$)", "$1 ")
            .toLowerCase();
    }
}

// Another library, much more rich in features.
// Turned out they don't have a way to format java.lang.Duration, and
// have their own Duration.
// Decided to circumvent it by formatting an Instant.
// https://stackoverflow.com/a/16323842/7643283
class PrettyTimeWrapper {
    private static final PrettyTime PRETTY_TIME = new PrettyTime();
    private static final LocalDateTime ZERO = LocalDateTime.of(0, 1, 1, 0, 0);

    public static String humanReadableFormat(Duration duration) {
        try {
            return PRETTY_TIME.format(ZERO.plus(duration));
        } catch (DateTimeException e) {
            return "overflown :/";
        }
    }
}

// Large Apache library just to format Duration
// (TBH, it will also be used for a lot of things in a real project)
// https://stackoverflow.com/a/18633466/7643283
class ApacheLangCommons {
    public static String humanReadableFormat(Duration duration) {
        try {
            return DurationFormatUtils.formatDurationWords(duration.toMillis(), true, false);
        } catch (ArithmeticException e) {
            return "overflown :/";
        }
    }
}
