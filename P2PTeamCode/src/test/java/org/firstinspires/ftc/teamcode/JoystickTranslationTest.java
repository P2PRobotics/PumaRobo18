package org.firstinspires.ftc.teamcode;

import org.junit.Test;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.min;
import static java.lang.Math.pow;
import static java.lang.Math.signum;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toDegrees;
import static java.lang.Math.toRadians;
import static org.junit.Assert.assertEquals;

public class JoystickTranslationTest {

    public double clamp(double val) {
        return min(abs(val), 1.0) * signum(val);
    }

    public double clamp(double val, double absVal) {
        return min(abs(val), absVal) * signum(val);
    }

    static double addRadians(double a, double b) {
        double tmp = (a + b + PI) % (2*PI);
        if (tmp < 0.0) tmp = (2*PI) + tmp;
        return tmp - PI;
    }

    static double subtractRadians(double a, double b) {
        return addRadians(a, -b);
    }

    public Pair<Double> translate(double x, double y) {
        double x2 = pow(x, 2.0);
        double y2 = pow(y, 2.0);
        double magnitude = sqrt(x2 + y2);
        double theta = atan2(y, x);
        double degrees = toDegrees(theta);
        double rad45 = toRadians(45.0);
        double angle = subtractRadians(theta, rad45); // shifted -45 for clever math #reasons

        double angleDegrees = toDegrees(angle);

        double cosAngle = cos(angle);
        double cos45 = cos(rad45);
        double left = magnitude * (cosAngle / cos45);
        double sinA = sin(angle);
        double sin45 = sin(rad45);
        double right = magnitude * (sinA / sin45);

        return new Pair<>(clamp(left), clamp(right));
    }

    @Test
    public void east() {
        Pair<Double> motorOutput = translate(1.0, 0.0);
        assertEquals(1.0, motorOutput.left, 0.1);
        assertEquals(-1.0, motorOutput.right, 0.1);
    }

    @Test
    public void north() {
        Pair<Double> motorOutput = translate(0.0, 1.0);
        assertEquals(1.0, motorOutput.left, 0.1);
        assertEquals(1.0, motorOutput.right, 0.1);
    }

    @Test
    public void west() {
        Pair<Double> motorOutput = translate(-1.0, 0.0);
        assertEquals(-1.0, motorOutput.left, 0.1);
        assertEquals(1.0, motorOutput.right, 0.1);
    }

    @Test
    public void south() {
        Pair<Double> motorOutput = translate(0.0, -1.0);
        assertEquals(-1.0, motorOutput.left, 0.1);
        assertEquals(-1.0, motorOutput.right, 0.1);
    }

    @Test
    public void northEast() {
        Pair<Double> motorOutput = translate(cos(toRadians(45.0)), sin(toRadians(45.0)));
        assertEquals(1.0, motorOutput.left, 0.1);
        assertEquals(0.0, motorOutput.right, 0.1);
    }


    @Test
    public void northWest() {
        Pair<Double> motorOutput = translate(cos(toRadians(135.0)), sin(toRadians(135.0)));
        assertEquals(0.0, motorOutput.left, 0.1);
        assertEquals(1.0, motorOutput.right, 0.1);
    }

    @Test
    public void southEast() {
        Pair<Double> motorOutput = translate(cos(toRadians(315.0)), sin(toRadians(315.0)));
        assertEquals(0.0, motorOutput.left, 0.1);
        assertEquals(-1.0, motorOutput.right, 0.1);
    }

    @Test
    public void southWest() {
        Pair<Double> motorOutput = translate(cos(toRadians(225.0)), sin(toRadians(225.0)));
        assertEquals(-1.0, motorOutput.left, 0.1);
        assertEquals(0.0, motorOutput.right, 0.1);
    }

}
