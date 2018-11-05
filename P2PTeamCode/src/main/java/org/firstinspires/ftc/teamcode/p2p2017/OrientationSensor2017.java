package org.firstinspires.ftc.teamcode.p2p2017;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class OrientationSensor2017 {
    private BNO055IMU imu;
    private double startAngle;
    private boolean hasStarted;

    public OrientationSensor2017(HardwareMap map) {
        imu = map.get(BNO055IMU.class, "imu");
    }

    public double getOrientation() {
        double angle = imu.getAngularOrientation().firstAngle;
        if (!hasStarted) {
            startAngle = angle;
            hasStarted = true;
        }
        angle = -Math.toDegrees(startAngle - angle) % 360;
        if (angle > 180) {
            angle = angle - 360;
        } else if (angle < -180) {
            angle = 360 + angle;
        }
        return -angle;
    }
}
