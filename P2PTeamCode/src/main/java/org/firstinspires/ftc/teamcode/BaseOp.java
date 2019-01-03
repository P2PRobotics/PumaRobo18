package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.hardware.AcceleratingDcMotor;
import org.firstinspires.ftc.teamcode.hardware.ClampingDcMotor;

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

    @Override
    public void init() {
        //initializes motors, retrieve configs

        // All drive motors are now wrapped in "decorator" classes that dampen acceleration and
        // ensure the requested power range makes sense for the motors.
        // See: https://www.journaldev.com/1540/decorator-design-pattern-in-java-example
        leftFrontMotor = new AcceleratingDcMotor(new ClampingDcMotor(hardwareMap.dcMotor.get("m12")));

        leftBackMotor = new AcceleratingDcMotor(new ClampingDcMotor(hardwareMap.dcMotor.get("m13")));
        leftBackMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        rightFrontMotor = new AcceleratingDcMotor(new ClampingDcMotor(hardwareMap.dcMotor.get("m11")));
        rightFrontMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        rightBackMotor = new AcceleratingDcMotor(new ClampingDcMotor(hardwareMap.dcMotor.get("m10")));

        liftMotor = new AcceleratingDcMotor(new ClampingDcMotor(hardwareMap.dcMotor.get("liftmo"))); //revHub 2, motor port 0
        liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        latchBarServo = hardwareMap.crservo.get("latchBarM"); //revHub 1, servo port 0
        latchCupServo = hardwareMap.servo.get("latchCupM"); //revHub 1, servo port 1

        intakeMotor = hardwareMap.dcMotor.get("intakemo"); //revHub 2, motor port 1
        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        hopperServo = hardwareMap.servo.get("hopperServo"); //revHub 1, servo port 2

        liftMotor.setPower(0);
        intakeMotor.setPower(0);

        moveStop();
        raiseContainer();
        setupTelemetry();
    }

    @Override
    public void loop() {
        telemetry.update();

        //Because we are using an accelerating motor, we let it update each time through the loop.
        leftFrontMotor.setPower(leftFrontMotor.getPower());
        leftBackMotor.setPower(leftBackMotor.getPower());
        rightFrontMotor.setPower(rightFrontMotor.getPower());
        rightBackMotor.setPower(rightBackMotor.getPower());
    }

    @Override
    public void stop() {
        moveStop();
        super.stop();
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

    public void lift(double power) {
        liftMotor.setPower(power);
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

    public void turn(double power) {
        moveLR(-power, power);
    }

    public void moveStop() {
        move4(0, 0, 0, 0);
    }

    public void moveStraight(double power) {
        moveLR(power, power);
    }

    private void moveLR(double left, double right) {
        move4(left, left, right, right);
    }

    private void move4(double leftFront, double leftBack, double rightFront, double rightBack) {
        leftFrontMotor.setPower(leftFront);
        leftBackMotor.setPower(leftBack);
        rightFrontMotor.setPower(rightFront);
        rightBackMotor.setPower(rightBack);
    }

    public void setupTelemetry() {
        telemetry
            .addLine("hardwareMap")
            .addData("latchServo", () -> latchBarServo.getPower())
            .addData("hopperServo", () -> hopperServo.getPosition());
    }

    static double addRadians(double a, double b) {
        double tmp = (a + b + PI) % (2 * PI);
        if (tmp < 0.0) tmp = (2 * PI) + tmp;
        return tmp - PI;
    }

    static double subtractRadians(double a, double b) {
        return addRadians(a, -b);
    }

    /**
     * Treats the joystick as a unit-circle, and determines
     * the angle in radians and the radius of the current
     * joystick position.
     * <p>
     * The power output of the *right* wheels is proportional
     * to the radius times the *sine* of the angle, divided
     * by the constant √2 / 2.
     * <p>
     * The power output of the *left* wheels is proportional
     * to the radius times the *cosine* of the angle,
     * divided by the constant √2 / 2.
     * <p>
     * When the joystick is full up or full down, we want
     * the wheels to be moving at equal power and direction,
     * moving the robot forward or reverse.
     * <p>
     * On a unit circle, the sine and cosine values are
     * equal at 45 degrees and 225 degrees. On the joystick
     * this would correspond to upper-right and lower-left.
     * By subtracting 45 degrees from the angle, we align
     * these two points on the unit circle with full up
     * and full down on the joystick.
     * <p>
     * Sensitivity is adjusted by applying an exponent to
     * the radius to obtain a magnitude. Since the radius
     * value is always between 0.0 and 1.0, applying an
     * exponent has the effect of lowering the magnitude.
     * For example, 0.5^3 is 0.125. In other words, this
     * treats the linear action of the joystick along each
     * as a curve.
     * <p>
     * #math
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

        moveLR(left, right);
    }
}


///**
// * <pre>{@code
// *                                      current           last
// *                                      system ───┐ ┌──► update
// *                                       time     │ │     time
// *                                                │ │
// *          ┌─► geometric ─► move4 ─► desired ──┐ │ │ ┌───────┐
// *          │     drive               power[4]  │ │ │ │       │
// *          │                                   ▼ ▼ ▼ ▼       │
// * loop() ──┴──────────────────────────────► acceleration ─► commanded ─► clamp ─► motor
// *                                            dampener       power[4]             .setPower[4]
// *
// * }</pre>
// */
