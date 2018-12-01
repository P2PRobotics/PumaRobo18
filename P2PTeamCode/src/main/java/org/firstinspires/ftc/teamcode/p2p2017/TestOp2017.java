package org.firstinspires.ftc.teamcode.p2p2017;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Disabled
public class TestOp2017 extends OpMode {

    DcMotor motor1;
    DcMotor motor2;

    public void init() {
        telemetry.addData("Initialized", true);
        motor1 = hardwareMap.dcMotor.get("w1");
        motor2 = hardwareMap.dcMotor.get("w2");
    }

    public void start() {
        telemetry.addData("Starting", true);
        motor1.setPower(1);
        //motor2.setPower(-1);
    }

    public void loop() {

    }
}
