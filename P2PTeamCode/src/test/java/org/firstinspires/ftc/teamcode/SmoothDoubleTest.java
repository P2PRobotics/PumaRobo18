package org.firstinspires.ftc.teamcode;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicLong;

import static org.firstinspires.ftc.teamcode.SmoothDouble.calculateCurrentValue;
import static org.hamcrest.Matchers.both;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class SmoothDoubleTest {
    long MSEC = 1_000_000L;
    long SEC = 1_000L * MSEC;

    @Test
    public void zeros() {
        assertEquals(0.0D, calculateCurrentValue(0.0D, 0.0D, 0L, 0L, 1.8D), 0.00001);
    }

    @Test
    public void zeroToOne() {
        assertEquals(0.0D, calculateCurrentValue(0.0D, 1.0D, 0L, 0L, 1.8D), 0.00001);
        assertEquals(0.45D, calculateCurrentValue(0.0D, 1.0D, 250L * MSEC, 0L, 1.8D), 0.00001);
        assertEquals(0.9D, calculateCurrentValue(0.0D, 1.0D, 500L * MSEC, 0L, 1.8D), 0.00001);
        assertEquals(0.9D, calculateCurrentValue(0.45D, 1.0D, 500L * MSEC, 250L * MSEC, 1.8D), 0.00001);
    }

    @Test
    public void zeroToNegOne() {
        assertEquals(0.0D, calculateCurrentValue(0.0D, -1.0D, 0L, 0L, 1.8D), 0.00001);
        assertEquals(-0.45D, calculateCurrentValue(0.0D, -1.0D, 250L * MSEC, 0L, 1.8D), 0.00001);
        assertEquals(-0.9D, calculateCurrentValue(0.0D, -1.0D, 500L * MSEC, 0L, 1.8D), 0.00001);
        assertEquals(-0.9D, calculateCurrentValue(-0.45D, -1.0D, 500L * MSEC, 250L * MSEC, 1.8D), 0.00001);
    }

    @Test
    public void spinup() {
        AtomicLong millis = new AtomicLong(0L);
        SmoothDouble d = new SmoothDouble(0.0D, 1.8D, () -> millis.get() * 1_000L * 1_000L);
        d.setDesiredValue(1.0D);
        while (millis.getAndIncrement() < 500L) {
            assertThat(d.update(), is(lessThan(0.91D)));
        }
        while (millis.getAndIncrement() < 1_000L) {
            assertThat(d.update(), is(both(lessThan(1.01D)).and(greaterThan(0.9))));
        }
        assertThat(d.getCurrentValue(), is(closeTo(1.0D, 0.01D)));
    }

    @Test
    public void revToFwd() {
        AtomicLong millis = new AtomicLong(0L);
        SmoothDouble d = new SmoothDouble(-1.0D, 1.8D, () -> millis.get() * 1_000L * 1_000L);
        d.setDesiredValue(1.0D);
        while (millis.getAndIncrement() < 500L) {
            assertThat(d.update(), is(lessThan(-0.09D)));
        }
        while (millis.getAndIncrement() < 1_000L) {
            assertThat(d.update(), is(both(lessThan(0.81D)).and(greaterThan(-0.1))));
        }
        assertThat(d.getCurrentValue(), is(closeTo(0.8D, 0.01D)));
    }

}