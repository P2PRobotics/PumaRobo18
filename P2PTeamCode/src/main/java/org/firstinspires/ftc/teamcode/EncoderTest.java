package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Autonomous(name = "EncoderTest", group = "T")
public class EncoderTest extends OpMode {

    public DcMotor motor;

    @Override
    public void init() {
        motor = hardwareMap.dcMotor.get("m");
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        motor.setPower(0);
    }

    @Override
    public void loop() {
        if(motor.getCurrentPosition() < 1120)
        {
            motor.setPower(.1);
        }
        else
        {
            motor.setPower(0);
        }
        telemetry.addData("Encoder", (360/1120) * motor.getCurrentPosition();
        telemetry.update();
    }
}
