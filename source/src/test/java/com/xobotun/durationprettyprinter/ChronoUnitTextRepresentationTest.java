package com.xobotun.durationprettyprinter;

import org.junit.jupiter.api.Test;

import static com.xobotun.durationprettyprinter.WeirdTemporalUnit.WEIRD;
import static org.junit.jupiter.api.Assertions.*;

class ChronoUnitTextRepresentationTest {

    @Test
    void registerCustom_mustFail() {
        assertThrowsExactly(UnsupportedTemporalUnitException.class, () -> ChronoUnitTextRepresentation.registerCustom(WEIRD, "weird", "weirds"));
    }
}
