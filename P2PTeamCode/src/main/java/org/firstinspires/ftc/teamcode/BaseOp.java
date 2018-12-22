package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import static java.lang.Math.PI;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toRadians;

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

        raiseContainer();
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
        move(-speed, speed);
    }

    public void move(double speed) {
        double v = adjustedSpeed(speed);
        leftFrontMotor.setPower(v);
        leftBackMotor.setPower(v);
        rightFrontMotor.setPower(v);
        rightBackMotor.setPower(v);
    }
    public void movetimed(ElapsedTime runtime, double speed, double seconds) {

        if (runtime.time() < seconds) {
            move(speed);
        }
        else{
            move(0);
        }

    }

    public void move(double left, double right) {
        double l = adjustedSpeed(left);
        double r = adjustedSpeed(right);
        leftFrontMotor.setPower(l);
        leftBackMotor.setPower(l);
        rightFrontMotor.setPower(r);
        rightBackMotor.setPower(r);
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

    static double addRadians(double a, double b) {
        double tmp = (a + b + PI) % (2*PI);
        if (tmp < 0.0) tmp = (2*PI) + tmp;
        return tmp - PI;
    }

    static double subtractRadians(double a, double b) {
        return addRadians(a, -b);
    }

    /**
     *  Treats the joystick as a unit-circle, and determines
     *  the angle in radians and the radius of the current
     *  joystick position.
     *
     *  The power output of the *right* wheels is proportional
     *  to the radius times the *sine* of the angle, divided
     *  by the constant √2 / 2.
     *
     *  The power output of the *left* wheels is proportional
     *  to the radius times the *codesine* of the angle,
     *  divided by the constant √2 / 2.
     *
     *  When the joystick is full up or full down, we want
     *  the wheels to be moving at equal speed and direction,
     *  moving the robot forward or reverse.
     *
     *  On a unit circle, the sine and cosine values are
     *  equal at 45 degrees and 225 degrees. On the joystick
     *  this would correspond to upper-right and lower-left.
     *  By subtracting 45 degrees from the angle, we align
     *  these two points on the unit circle with full up
     *  and full down on the joystick.
     *
     *  Sensitivity is adjusted by applying an exponent to
     *  the radius to obtain a magnitude. Since the radius
     *  value is always between 0.0 and 1.0, applying an
     *  exponent has the effect of lowering the magnitude.
     *  For example, 0.5^3 is 0.125. In other words, this
     *  treats the linear action of the joystick along each
     *  as a curve.
     *
     *  #math
     */
    void geometricDrive(double x, double y) {
        double x2 = pow(x, 2.0);
        double y2 = pow(y, 2.0);
        double radius = sqrt(x2 + y2);

        // Larger sensitivity values correspond to lower
        // twitchiness. 1 is most twitchy. 3 to 5 feel
        // pretty good. 7 is kinda mushy.
        double sensitivity = 3;
        double magnitude = pow(radius, sensitivity);

        double theta = atan2(y, x);
        double rad45 = toRadians(45.0);
        double angle = subtractRadians(theta, rad45);

        double cosAngle = cos(angle);
        double cos45 = cos(rad45); // constant, same as √2 / 2
        double left = magnitude * (cosAngle / cos45);

        double sinAngle = sin(angle);
        double sin45 = sin(rad45); // constant, same as √2 / 2
        double right = magnitude * (sinAngle / sin45);

        move(left, right);
    }
}
