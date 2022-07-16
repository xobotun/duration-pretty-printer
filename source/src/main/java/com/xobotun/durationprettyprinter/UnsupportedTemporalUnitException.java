package com.xobotun.durationprettyprinter;

import java.time.temporal.TemporalUnit;

public class UnsupportedTemporalUnitException extends UnsupportedOperationException {
    UnsupportedTemporalUnitException(final TemporalUnit unit) {
        // Wow, long useless string in this jar!
        super("This library does not support temporal units with both seconds and nanos specified. See more on https://github.com/xobotun/duration-pretty-printer/issues/1. Culprit: " + unit);
    }
}
