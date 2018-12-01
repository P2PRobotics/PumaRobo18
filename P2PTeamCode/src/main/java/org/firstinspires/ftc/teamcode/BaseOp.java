package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

public class BaseOp extends OpMode {

    //drivetrain
    public DcMotor lmotor1;
    public DcMotor lmotor2;
    public DcMotor rmotor1;
    public DcMotor rmotor2;
    private DcMotor[] motors = new DcMotor[]{lmotor1,lmotor2,rmotor1,rmotor2};

    //lifter and lander (servo latches on to lander, motor extends/retracts arm)
    public DcMotor liftmotor;
    public Servo liftgrab;

    //wheeled intake system
    public DcMotor intake1;
    public Servo intake2;


    public void init() {
        //initializes motors, retrieve configs
        lmotor1 = hardwareMap.dcMotor.get("m12");
        lmotor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        lmotor2 = hardwareMap.dcMotor.get("m13");
        lmotor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lmotor2.setDirection(DcMotorSimple.Direction.REVERSE);

        rmotor1 = hardwareMap.dcMotor.get("m11");
        rmotor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        rmotor2 = hardwareMap.dcMotor.get("m10");
        rmotor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rmotor2.setDirection(DcMotorSimple.Direction.REVERSE);

        //make sure these match what's in the config

        liftmotor= hardwareMap.dcMotor.get("liftmo");
        liftmotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        liftgrab= hardwareMap.servo.get("liftserv");

        intake1=hardwareMap.dcMotor.get("intake1");
        intake1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        intake2=hardwareMap.servo.get("intake2");

    }
    public void start() {}
    public void loop() {}

//    public void wheelIn(boolean in){
//        if(in){
//            intake1.setPower(0.75);
//        } else {
//            intake1.setPower(0);
//        }
//    }

    public void turn(double speed){
        double v = adjustedSpeed(speed);
        rmotor1.setPower(v);
        rmotor2.setPower(-v);
        lmotor1.setPower(v);
        lmotor2.setPower(-v);
    }

//    public void move(double speed){
//        double v = adjustedSpeed(speed);
//        for (DcMotor motor : motors) {
//            motor.setPower(v);
//        }
//    }
    
    static double adjustedSpeed(double speed) {
        if (speed < -1.0) return -1.0;
        else if (speed > 1.0) return 1.0;
        else if (speed < 0.2 && speed > 0) return 0.2;
        else if (speed > -0.2 && speed < 0) return -0.2;
        else return speed;
        //Adjust speed so it doesn't slow down too much!!!!!!!
    }
//hi guys ;) <3 mak
}
