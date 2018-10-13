package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Autonomous(name="MotorTest",group="A")
public final class MotorTest extends OpMode {
    private DcMotor lmotor1;
    private DcMotor lmotor2;
    private DcMotor rmotor1;
    private DcMotor rmotor2;

    public void init(){
        lmotor1 = hardwareMap.dcMotor.get("lm1");
        lmotor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        lmotor2 = hardwareMap.dcMotor.get("lm2");
        lmotor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lmotor2.setDirection(DcMotorSimple.Direction.REVERSE);


        rmotor1 = hardwareMap.dcMotor.get("rm1");
        rmotor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        rmotor2 = hardwareMap.dcMotor.get("rm2");
        rmotor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rmotor2.setDirection(DcMotorSimple.Direction.REVERSE);

    }
    public void start(){
    }
    public void loop(){
        lmotor1.setPower(5);
        lmotor2.setPower(5);
        rmotor1.setPower(5);
        rmotor2.setPower(5);
    }
}
