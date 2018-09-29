package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class Autonomous extends OpMode implements GameConstants {
    //don't forget to double check that this is IDENTICAL to teleop
    private DcMotor motor1; //motors 1-4 are drivetrain
    private DcMotor motor2;
    private DcMotor motor3;
    private DcMotor motor4;

    private DcMotor motor5; //?? confirm w mechanical on motors 5-8, maybe rename?
    private DcMotor motor6;
    private DcMotor motor7;
    private DcMotor motor8;

    //don't forget to initialize other mechanical aspects!!
    //add sensors here!!
    //consider adding states and stacks here
    //if you do states, don't forget to make a state.java file!!
    //don't forget your important variables here
    public void init(){
        //initialization and stuff. see BasicTeleOp
    }
    public void start(){
        //?? set up vuforia here??
    }
    public void loop(){
        //actual code here. reference last years code
        //cases and states and stacks might work here??
    }
}
