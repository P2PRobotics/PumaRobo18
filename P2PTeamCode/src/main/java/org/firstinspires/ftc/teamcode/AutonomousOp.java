package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.sun.xml.internal.rngom.parse.host.Base;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous(name = "AutonomousOp" , group = "AAAAAARP")
public class AutonomousOp extends BaseOp implements GameConstants {
    //drivetrain
    private DcMotor lmotor1;
    private DcMotor lmotor2;
    private DcMotor rmotor1;
    private DcMotor rmotor2;

    private DcMotor liftmotor;
    private Servo liftgrab;


    private DcMotor intake;

    private Servo placemarker; //?? double check if we need this

    //add sensors here!!
    //private OrientationSensor orientationSensor;
    //private VuforiaHelper vuforia;

    //consider adding states and stacks here
    //if you do states, don't forget to make a state.java file!!
    //don't forget your important variables here
    public void init(){
        super.init();
    }
    public void start(){
        //vuforia.start();
        //check if ftc updated their VuMark resources!!
    }
    public void loop(){
        //detatch from lander
        liftmotor.setPower(-3);
        liftgrab.setPosition(-0.5);
        //orient self
        //use vuforia to locate depot
            // if(RED_TEAM){
            // if(LEFT_FIELD){
            // (location 1 --> depot)
            // }
            // (locaton 2 --> depot)
            // }
            // else{
            // if(LEFT_FIELD){
            // (location 3 --> depot)
            // }
            // (location 4 --> depot)
            // }
        //place marker
        //if(RED_TEAM){
        //depot--> crater1
        //set power on all motors to 0
        //}
        //else{
        //depot--> crater2
        //set power on all motors to 0
        //}
        //use vuforia to locate crater
        //park in crater

    }
}
