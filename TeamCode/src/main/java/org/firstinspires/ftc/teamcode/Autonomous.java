package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

public class Autonomous extends OpMode implements GameConstants {
    //drivetrain
    private DcMotor lmotor1;
    private DcMotor lmotor2;
    private DcMotor rmotor1;
    private DcMotor rmotor2;

    private DcMotor liftmotor;
    private Servo liftservo;

    private DcMotor intake;

    private Servo placemarker; //?? double check if we need this

    //add sensors here!!

    //consider adding states and stacks here
    //if you do states, don't forget to make a state.java file!!
    //don't forget your important variables here
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
        //?? set up vuforia here??
    }
    public void loop(){
        //actual code here. reference last years code
        //cases and states and stacks might work here??
    }
}
