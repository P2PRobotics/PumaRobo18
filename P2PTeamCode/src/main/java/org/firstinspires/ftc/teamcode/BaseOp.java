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
    public CRServo latchBarServo;
    public Servo latchCupServo;

    //wheeled intake system
    public DcMotor intakeMotor;
    public Servo hopperServo;

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
        liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        latchBarServo = hardwareMap.crservo.get("latchBarM"); //revHub 1, servo port 0
        latchCupServo = hardwareMap.servo.get("latchCupM"); //revHub 1, servo port 1

        intakeMotor = hardwareMap.dcMotor.get("intakemo"); //revHub 2, motor port 1
        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        hopperServo = hardwareMap.servo.get("hopperServo"); //revHub 1, servo port 2

        leftFrontMotor.setPower(0);
        leftBackMotor.setPower(0);
        rightFrontMotor.setPower(0);
        rightBackMotor.setPower(0);

        liftMotor.setPower(0);
        intakeMotor.setPower(0);

        setupTelemetry();
    }

    public void start() {
    }

    public void loop() {
    }

    public void intakeIn() {
        intakeMotor.setPower(0.75);
    }

    public void intakeOut() {
        intakeMotor.setPower(-0.75);
    }

    public void intakeStop() {
        intakeMotor.setPower(0);
    }

    public void raiseContainer() {
        hopperServo.setPosition(0.75);
    }

    public void lowerContainer() {
        hopperServo.setPosition(0.57);
    }


    public void lift(double speed) {
        liftMotor.setPower(speed);
    }

    public void latchClose() {
        latchBarServo.setPower(0.2);
    }

    public void latchOpen() {
        latchBarServo.setPower(-0.2);
    }

    public void latchStop() {
        latchBarServo.setPower(-0);
    }

    public void latchCup(boolean latchedCup) {
        if (latchedCup) {
            latchCupServo.setPosition(0.5);
        } else {
            latchCupServo.setPosition(-0.5);
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
        double sensitivity = 1.0; //check to see if this is a good number

        if (curve == 0.0) {
            leftOutput = magnitude;
            rightOutput = magnitude;
        } else {
            double value = Math.log(Math.abs(curve));
            double ratio = (value - sensitivity) / (value + sensitivity);
            if (ratio == 0) {
                ratio = .0000000001;
            }

            if (curve < 0) {
                leftOutput = magnitude / ratio;
                rightOutput = magnitude;
            } else {
                leftOutput = magnitude;
                rightOutput = magnitude / ratio;
            }
        }

        leftFrontMotor.setPower(adjustedSpeed(leftOutput));
        leftBackMotor.setPower(adjustedSpeed(leftOutput));
        rightFrontMotor.setPower(adjustedSpeed(rightOutput));
        rightBackMotor.setPower(adjustedSpeed(rightOutput));
    }

    public void setupTelemetry() {
        telemetry
                .addLine("hardwareMap")
                .addData("latchServo", () -> latchBarServo.getPower())
                .addData("hopperServo", () -> hopperServo.getPosition());
    }
}
