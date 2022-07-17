package com.xobotun.durationprettyprinter;

/**
 * Changes behaviour on how to handle zero-valued chrono units.
 */
public enum DropZeroMode {
    /**
     * Removes all entries.<br>
     * P12Y4MT10H30.999000777S will yield only "12 years", "4 months" and so on.
     */
    DROP_ZEROS,
    /**
     * Keeps all entries.<br>
     * P12Y4MT10H30.999000777S will yield "0 centuries", "12 years", "4 months", "0 weeks", "0 days" and so on.
     */
    PRESERVE_ALL,
    /**
     * Removes all zero entries only after a first non-zero.<br>
     * P12Y4MT10H30.999000777S will yield only "12 years", "4 months", "0 weeks", "0 days" and so on.
     */
    DROP_HIGHEST,
    ;
}
