package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import static java.lang.Math.PI;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toRadians;

@TeleOp(name = "DriveAlgoTestOp", group = "D")
public class DriveAlgoTestOp extends BaseOp {
    final String[] driveModes = new String[]{"geometricDrive", "curveDrive", "legacyDrive"};
    int currentDriveMode = 0;
    String getDriveMode() {
        return driveModes[currentDriveMode];
    }

    void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    int bumpDriveMode() {
        currentDriveMode = (currentDriveMode + 1) % driveModes.length;
        sleep(250);
        return currentDriveMode;
    }

    @Override
    public void init() {
        super.init();
        raiseContainer();
    }

    @Override
    public void init_loop() {
        super.init_loop();
        if (gamepad1.a) bumpDriveMode();
        telemetry.update();
    }

    @Override
    public void setupTelemetry() {
        telemetry
            .addLine("driver")
            .addData("right_stick_x", () -> gamepad1.right_stick_x)
            .addData("right_stick_y", () -> gamepad1.right_stick_y)
            .addData("driveMode", this::getDriveMode)
        ;
    }

    //adjust to move and turn at same time
    @Override
    public void loop() {
        telemetry.update();

        if (gamepad1.a) bumpDriveMode();

        switch (getDriveMode()) {
            case "geometricDrive": geometricDrive(); break;
            case "legacyDrive": legacyDrive(); break;
        }

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
     *  to the radius times the *cosine* of the angle,
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
    void geometricDrive() {
        // Right on the controller corresponds to positive.
        double x = gamepad1.right_stick_x;

        // Up on the controller corresponds to negative,
        // so negate the value to treat up as the positive
        // y-axis on a unit circle.
        double y = -gamepad1.right_stick_y;

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

    void legacyDrive() {
        double rawx = gamepad1.right_stick_x;
        double rawy = gamepad1.right_stick_y;
        double x = Math.pow(rawx, 7); //adjust sensitivity?
        double y = Math.pow(rawy, 7);
        //movement
        double n = (x + y);
        if (x != 0 || y != 0) {
            n = ((x + y) / Math.sqrt(2.0)); // n is the power of the motors in the +x +y direction
            //double m = ((x - y) / Math.sqrt(2.0)); // m is the power of the motors in the +x -y direction
            moveStraight(-n);
        }
        //left turn
        if (gamepad1.left_trigger > 0.005) {
            double trigger = gamepad1.left_trigger;
            //maybe use a constant number for turning--depends on driver comfort
            turn(trigger);
            return;
        }
        //turning right
        else if (gamepad1.right_trigger > 0.005) {
            double trigger = gamepad1.right_trigger;
            //move(-2);
            turn(-trigger);
            return;
        } else {
            turn(0);
        }
    }
}
