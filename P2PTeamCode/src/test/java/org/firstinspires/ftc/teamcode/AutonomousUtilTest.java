package org.firstinspires.ftc.teamcode;

import org.junit.Test;

import static org.firstinspires.ftc.teamcode.AutonomousUtil.adjustedSpeed;
import static org.firstinspires.ftc.teamcode.AutonomousUtil.angularDifference;
import static org.firstinspires.ftc.teamcode.AutonomousUtil.closeEnough;
import static org.firstinspires.ftc.teamcode.AutonomousUtil.rotatedBy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AutonomousUtilTest {

    @Test
    public void adjustedSpeedTest() {
        assertEquals(1.0, adjustedSpeed(1.0),0.0);
        assertEquals(-1.0, adjustedSpeed(-1.0),0.0);
        assertEquals(0.0, adjustedSpeed(0.0),0.0);
        assertEquals(0.2, adjustedSpeed(0.1),0.0);
        assertEquals(-0.2, adjustedSpeed(-0.1),0.0);
        assertEquals(1.0, adjustedSpeed(2.0),0.0);
        assertEquals(-1.0, adjustedSpeed(-2.0),0.0);
    }

    @Test
    public void angularDifferenceTest() {
        assertEquals(178.0, angularDifference(1.0, 179.0),0.0);
        assertEquals(-178.0, angularDifference(179.0, 1.0),0.0);
        assertEquals(0.0, angularDifference(0.0, 0.0),0.0);
        assertEquals(179.9, angularDifference(0.0, 179.9),0.01);
        assertEquals(-180, angularDifference(-90, 90),0);
        assertEquals(-135, angularDifference(-45, -180.0),0);
        //assertEquals(1, angularDifference(2.0, 3.0),0.0);
    }

    @Test(expected = RuntimeException.class)
    public void angularDifferenceExceptionTest() {
        angularDifference(0.0, 730.0);
    }

    @Test(expected = RuntimeException.class)
    public void angularDifferenceException2Test() {
        angularDifference(730.0, 0.0);
    }

    @Test
    public void closeEnoughTest() {
        assertTrue(closeEnough(34,35,2));
        assertFalse(closeEnough(56,67,0));
    }
    @Test(expected = RuntimeException.class)
    public void closeEnoughExceptionTest() {
        closeEnough(56,970,1000);
    }

    @Test
    public void rotatedByTest() {
        assertEquals(45.0, rotatedBy(0.0,45.0),0.0);
        assertEquals(179.9, rotatedBy(0.0,179.9),0.01);
        assertEquals(-141, rotatedBy(179.0,40.0),0.0);
        assertEquals(141, rotatedBy(-179.0,-40.0),0.0);

    }
}