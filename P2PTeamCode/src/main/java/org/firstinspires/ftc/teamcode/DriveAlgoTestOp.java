package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import static java.lang.Math.PI;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toDegrees;
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
        super.setupTelemetry();
    }

    //adjust to move and turn at same time
    @Override
    public void loop() {
        telemetry.update();

        if (gamepad1.a) bumpDriveMode();

        switch (getDriveMode()) {
            case "geometricDrive": geometricDrive(); break;
            case "legacyDrive": legacyDrive(); break;
            case "curveDrive": curveDrive(); break;
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

    void geometricDrive() {
        double x = gamepad1.right_stick_x;
        double y = -gamepad1.right_stick_y;

        double x2 = pow(x, 2.0);
        double y2 = pow(y, 2.0);
        double magnitude = sqrt(x2 + y2);
        double theta = atan2(y, x);
        double degrees = toDegrees(theta);
        double rad45 = toRadians(45.0);
        double angle = subtractRadians(theta, rad45); // shifted -45 for clever math #reasons

        double angleDegrees = toDegrees(angle);

        double cosAngle = cos(angle);
        double cos45 = cos(rad45);
        double left = magnitude * (cosAngle / cos45);
        double sinA = sin(angle);
        double sin45 = sin(rad45);
        double right = magnitude * (sinA / sin45);

        move(left, right);
    }

    void curveDrive() {
        //movement
        //move(y);
        double driveMagnitude = -gamepad1.right_stick_y;
        double driveCurve = gamepad1.right_stick_x;
        curveDrive(driveCurve, driveMagnitude);

        //left turn/pivot
        if (gamepad1.left_trigger > 0.005) {
            double trigger = gamepad1.left_trigger;
            //maybe use a constant number for turning--depends on driver comfort
            turn(trigger);
            return;
        }
        //right turn/pivot
        else if (gamepad1.right_trigger > 0.005) {
            double trigger = gamepad1.right_trigger;
            turn(-trigger);
            return;
        } else {
            turn(0);
        }
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
            move(-n);
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
