### Reasoning
This project exists to address the fact [`java.lang.Duration#toString`](https://docs.oracle.com/javase/8/docs/api/java/time/Duration.html#toString)
outputs duration in a not very human-readable way like `PT48H20.345S`, making it difficult to calculate how many years are actually within `PT87660H`. SI microseconds are actually more or less fine.

### Features
There are three main knobs you can tune:
 - Which `ChronoUnit`s to output. You can stick to default
```java
    static final ChronoUnit[] DEFAULT_TEMPORAL_BITS = {
        YEARS, MONTHS, DAYS, HOURS, MINUTES, SECONDS, MILLIS, MICROS, NANOS 
    };
```
&nbsp;&nbsp;or provide custom set of `TemporalUnit`s
```java
    val units = new TemporalUnit[]{
        CustomTemporalUnit.STANDARD_HOURS,
        CustomTemporalUnit.STANDARD_MINUTES,
        SECONDS,
        MILLIS,
        MICROS,
        NANOS
    };
    return DurationPrettyPrinter.nonzeroToString(duration, units);
```

 - How many significant portions to output. Sometimes you need to have an general understanding of a Duration, 
not a down-to-nanosecond precision. This way `PT87661H30.111222333S` will be rendered as `10 years 1 hour 30 seconds`. 
Default value is 3 most significant entries, but it is also tweakable.

 - What to do with zero-valued entries. You can preserve them `0 years 1 month 0 days`, drop completely `1 month`, 
 or choose to drop only first ones `1 month 0 days`.

### Performance
This is not a very performant library, but it is the price for custom `TemporalUnit`s support and variable output.

```
Benchmark                                                    Mode  Cnt       Score      Error  Units
SpeedComparison.measure_Apache                               avgt   25  172766.453 ± 1829.317  ns/op
SpeedComparison.measure_PrettyTime                           avgt   25  179679.857 ± 3543.858  ns/op
SpeedComparison.measure_stackOverflow01                      avgt   25   28054.521 ±  506.785  ns/op
SpeedComparison.measure_stackOverflow02                      avgt   25  113781.989 ± 2446.993  ns/op
SpeedComparison.measure_thisPrettyPrinter_fullToString       avgt   25   76670.984 ±  845.790  ns/op
SpeedComparison.measure_thisPrettyPrinter_nonzeroToString    avgt   25   40193.310 ±  876.509  ns/op
SpeedComparison.measure_thisPrettyPrinter_shortenedToString  avgt   25   32807.863 ±  595.229  ns/op
SpeedComparison.measure_toString                             avgt   25    8315.955 ±  177.915  ns/op
```

\* Apache Commons and PrettyTime versions are flawed, because some test samples have exceptionally failed with overflows.

### TODOs
 - Fill this list
 - Make better benchmarks
 - Write "won't do"s
 - Better tests
 - Full Duration seconds+nanos support (issue #1)
