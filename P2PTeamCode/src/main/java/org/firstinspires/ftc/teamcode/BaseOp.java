package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

public class BaseOp extends OpMode {

    //drivetrain
    public DcMotor leftFrontMotor;
    public DcMotor leftBackMotor;
    public DcMotor rightFrontMotor;
    public DcMotor rightBackMotor;

    //lifter and lander (servo latches on to lander, motor extends/retracts arm)
    public DcMotor liftMotor;
    public Servo latchBarM;
    public Servo latchCupM;

    //wheeled intake system
    public DcMotor intake1;
    public CRServo intake2;

    //motor power reg
    public static final double deltamax = 0.2;

    public void init() {
        //initializes motors, retrieve configs
        leftFrontMotor = hardwareMap.dcMotor.get("m12");
//        leftFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        leftBackMotor = hardwareMap.dcMotor.get("m13");
        leftBackMotor.setDirection(DcMotorSimple.Direction.REVERSE);

//        leftBackMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        rightFrontMotor = hardwareMap.dcMotor.get("m11");
        rightFrontMotor.setDirection(DcMotorSimple.Direction.REVERSE);

//        rightFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        rightBackMotor = hardwareMap.dcMotor.get("m10");

//        rightBackMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        //make sure these match what's in the config

        liftMotor = hardwareMap.dcMotor.get("liftmo"); //revHub 2, motor port 0
        liftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        latchBarM = hardwareMap.servo.get("latchBarM"); //revHub 1, servo port 0
        latchCupM = hardwareMap.servo.get("latchCupM"); //revHub 1, servo port 1

        intake1 = hardwareMap.dcMotor.get("intake1"); //revHub 2, motor port 1
        intake1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        intake2 = hardwareMap.crservo.get("intake2"); //revHub 1, servo port 2


        leftFrontMotor.setPower(0);
        leftBackMotor.setPower(0);
        rightFrontMotor.setPower(0);
        rightBackMotor.setPower(0);

        liftMotor.setPower(0);
        intake1.setPower(0);
    }

    public void start() {
    }

    public void loop() {
    }

    public void wheelIn() {
        intake1.setPower(0.75);
    }

    public void wheelOut() {
        intake1.setPower(-0.75);
    }

    public void wheelStop() {
        intake1.setPower(0);
    }

    public void raiseContainer() {
        intake2.setPower(0.25);
    }

    public void lowerContainer() {
        intake2.setPower(-0.25);
    }

    public void stopContainer() {
        intake2.setPower(0);
    }


    public void lift(double speed) {
        liftMotor.setPower(speed);
    }

    public void latchBar(boolean latchedBar) {
        if (latchedBar) {
            latchBarM.setPosition(0.5);
        } else {
            latchBarM.setPosition(-0.5);
        }
    }

    public void latchCup(boolean latchedCup) {
        if (latchedCup) {
            latchCupM.setPosition(0.5);
        } else {
            latchCupM.setPosition(-0.5);
        }
    }


    public void turn(double speed) {
        double v = adjustedSpeed(speed);
        //double curPowRm1 = rightFrontMotor.getPower();
        //if ((Math.abs(curPowRm1) + deltamax) > 1.0)
        rightFrontMotor.setPower(v);
        rightBackMotor.setPower(v);
        leftFrontMotor.setPower(-v);
        leftBackMotor.setPower(-v);
    }

    public void move(double speed) {
        double v = adjustedSpeed(speed);
        leftFrontMotor.setPower(v);
        leftBackMotor.setPower(v);
        rightFrontMotor.setPower(v);
        rightBackMotor.setPower(v);
    }

    static double adjustedSpeed(double speed) {
        if (speed < -1.0) return -1.0;
        else if (speed > 1.0) return 1.0;
        else if (speed < 0.2 && speed > 0) return 0.2;
        else if (speed > -0.2 && speed < 0) return -0.2;
        else return speed;
        //Adjust speed so it doesn't slow down too much!!!!!!!
    }

    public void curveDrive(double curve, double magnitude) {
        double leftOutput;
        double rightOutput;
        double sensitivity = 0.5; //check to see if this is a good number
        if (curve < 0) {
            double value = Math.log(-curve);
            double ratio = (value - sensitivity) / (value + sensitivity);
            if (ratio == 0) {
                ratio = .0000000001;
            }
            leftOutput = magnitude / ratio;
            rightOutput = magnitude;
        } else if (curve > 0) {
            double value = Math.log(curve);
            double ratio = (value - sensitivity) / (value + sensitivity);
            if (ratio == 0) {
                ratio = .0000000001;
            }
            leftOutput = magnitude;
            rightOutput = magnitude / ratio;
        } else {
            leftOutput = magnitude;
            rightOutput = magnitude;
        }
        leftFrontMotor.setPower(-leftOutput);
        leftBackMotor.setPower(-leftOutput);
        rightFrontMotor.setPower(rightOutput);
        rightBackMotor.setPower(rightOutput);
    }

}
