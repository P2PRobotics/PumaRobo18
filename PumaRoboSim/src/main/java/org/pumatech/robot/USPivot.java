package org.pumatech.robot;

import org.pumatech.robotcore.hardware.modernrobotics.SimModernRoboticsI2cRangeSensor;
import org.pumatech.robotcore.hardware.SimServo;

public class USPivot extends SimServo {

    private SimModernRoboticsI2cRangeSensor rangeSensor;

    public USPivot(SimModernRoboticsI2cRangeSensor rangeSensor) {
        this.rangeSensor = rangeSensor;
    }

    @Override
    public void setPosition(double position) {
        rangeSensor.setDirection(Math.asin((.5 - position) * 2));
    }
}
