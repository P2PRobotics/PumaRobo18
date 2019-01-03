package org.firstinspires.ftc.teamcode.pidcontrol;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;

import org.junit.Test;
import org.junit.runner.RunWith;

import static java.lang.Math.signum;
import static org.firstinspires.ftc.teamcode.pidcontrol.AngularPController.addAngle;
import static org.firstinspires.ftc.teamcode.pidcontrol.AngularPController.subtractAngle;
import static org.hamcrest.Matchers.both;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(JUnitQuickcheck.class)
public class AngularPControllerTest {
    public static double EXACT = 0.0d;
    public static double CLOSE = 0.01f;

    @Property
    public void testWithDefaults(
        @InRange(min = "-180.0d", max = "180.0d") double absolute,
        @InRange(min = "-180.0d", max = "180.0d") double reference,
        @InRange(min = "-180.0d", max = "180.0d") double desired
    ) {
        AngularPController ctrl = new AngularPController(() -> absolute, 0.0d, 1.0d, 0.0d);
        ctrl.calibrateTo(reference);
        ctrl.setDesired(desired);
        ctrl.update();

        assertEquals(reference, ctrl.update(), CLOSE);
        assertEquals(desired, addAngle(ctrl.update(), ctrl.getError()), CLOSE);
        assertEquals(signum(ctrl.getError()), signum(ctrl.getControlValue()), EXACT);
        assertThat(ctrl.getError(), is(both(greaterThanOrEqualTo(-180.0d)).and(lessThanOrEqualTo(180.0d))));
    }

    @Property
    public void testArithmatic(
        @InRange(min = "-180.0d", max = "180.0d") double a,
        @InRange(min = "-180.0d", max = "180.0d") double b
    ) {
        assertThat(subtractAngle(a, b), is(both(greaterThanOrEqualTo(-180.0d)).and(lessThanOrEqualTo(180.0d))));
        assertThat(addAngle(a, b), is(both(greaterThanOrEqualTo(-180.0d)).and(lessThanOrEqualTo(180.0d))));
    }

    @Test
    public void cornerCases() {
        assertThat(subtractAngle(180.0d, 180.0d), is(0.0d));
        assertThat(subtractAngle(-180.0d, 180.0d), is(0.0d));
        assertThat(subtractAngle(180.0d, -180.0d), is(0.0d));
        assertThat(subtractAngle(-180.0d, -180.0d), is(0.0d));
        assertThat(addAngle(180.0d, 180.0d), is(0.0d));
        assertThat(addAngle(-180.0d, 180.0d), is(0.0d));
        assertThat(addAngle(180.0d, -180.0d), is(0.0d));
        assertThat(addAngle(-180.0d, -180.0d), is(0.0d));
    }
}

