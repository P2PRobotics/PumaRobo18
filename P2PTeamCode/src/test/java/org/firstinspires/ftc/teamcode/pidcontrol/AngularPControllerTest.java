package org.firstinspires.ftc.teamcode.pidcontrol;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.Test;
import org.junit.runner.RunWith;

import static java.lang.Math.signum;
import static org.firstinspires.ftc.teamcode.pidcontrol.AngularPController.addAngle;
import static org.firstinspires.ftc.teamcode.pidcontrol.AngularPController.subtractAngle;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(JUnitQuickcheck.class)
public class AngularPControllerTest {
    public static float EXACT = 0.0f;
    public static float CLOSE = 0.01f;

    @Property
    public void testWithDefaults(
        @InRange(min = "-180.0f", max = "180.0f") float absolute,
        @InRange(min = "-180.0f", max = "180.0f") float reference,
        @InRange(min = "-180.0f", max = "180.0f") float desired
    ) {
        AngularPController ctrl = new AngularPController(() -> absolute, 0.0f, 1.0f, 0.0f);
        ctrl.calibrateTo(reference);
        ctrl.setDesired(desired);
        ctrl.update();

        assertEquals(reference, ctrl.update(), CLOSE);
        assertEquals(desired, addAngle(ctrl.update(), ctrl.getError()), CLOSE);
        assertEquals(signum(ctrl.getError()), signum(ctrl.getControlValue()), EXACT);
        assertThat(ctrl.getError(), is(both(greaterThanOrEqualTo(-180.0f)).and(lessThanOrEqualTo(180.0f))));
    }

    @Property
    public void testArithmatic(
        @InRange(min = "-180.0f", max = "180.0f") float a,
        @InRange(min = "-180.0f", max = "180.0f") float b
    ) {
        assertThat(subtractAngle(a, b), is(both(greaterThanOrEqualTo(-180.0f)).and(lessThanOrEqualTo(180.0f))));
        assertThat(addAngle(a, b), is(both(greaterThanOrEqualTo(-180.0f)).and(lessThanOrEqualTo(180.0f))));
    }

    @Test
    public void cornerCases() {
        assertThat(subtractAngle(180.0f, 180.0f), is(0.0f));
        assertThat(subtractAngle(-180.0f, 180.0f), is(0.0f));
        assertThat(subtractAngle(180.0f, -180.0f), is(0.0f));
        assertThat(subtractAngle(-180.0f, -180.0f), is(0.0f));
        assertThat(addAngle(180.0f, 180.0f), is(0.0f));
        assertThat(addAngle(-180.0f, 180.0f), is(0.0f));
        assertThat(addAngle(180.0f, -180.0f), is(0.0f));
        assertThat(addAngle(-180.0f, -180.0f), is(0.0f));
    }
}

