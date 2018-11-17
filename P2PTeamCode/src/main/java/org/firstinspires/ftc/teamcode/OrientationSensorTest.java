package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

@Autonomous(name = "OrientationSensorTest" , group = "A")
public class OrientationSensorTest extends OpMode {
    BNO055IMU imu;

    @Override
    public void init() {
        imu = hardwareMap.get(BNO055IMU.class, "imu");
    }

    @Override
    public void loop() {
        telemetry.addData("Orientation",imu.getAngularOrientation().firstAngle);
        telemetry.update();
    }
}
