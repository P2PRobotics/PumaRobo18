package org.firstinspires.ftc.teamcode.p2p2017;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Disabled
public class MotorTestOp2017 extends OpMode {

    private DcMotor motor1;
    private DcMotor motor2;
    private DcMotor motor3;
    private DcMotor motor4;
    private DcMotor motor5;
    private DcMotor motor6;

    private OrientationSensor2017 orientationSensor;

    private double pos = 0.5;


    @Override
    public void init() {
        // TODO Auto-generated method stub
        motor1 = hardwareMap.dcMotor.get("w1");
        motor2 = hardwareMap.dcMotor.get("w2");
        motor3 = hardwareMap.dcMotor.get("w3");
        motor4 = hardwareMap.dcMotor.get("w4");
        motor5 = hardwareMap.dcMotor.get("w5");
        motor6 = hardwareMap.dcMotor.get("w6");

        // motor2.setDirection(DcMotorSimple.Direction.REVERSE);
        // motor3.setDirection(DcMotorSimple.Direction.REVERSE);

        orientationSensor = new OrientationSensor2017(hardwareMap);
    }

    @Override
    public void loop() {
        // TODO Auto-generated method stub
        motor1.setPower(0);
        motor2.setPower(0);
        motor3.setPower(0);
        motor4.setPower(0);
        if (gamepad1.a) {
            motor1.setPower(1);
        }
        if (gamepad1.b) {
            motor2.setPower(1);
        }
        if (gamepad1.x) {
            motor3.setPower(1);
        }
        if (gamepad1.y) {
            motor4.setPower(1);
        }

    }

}
