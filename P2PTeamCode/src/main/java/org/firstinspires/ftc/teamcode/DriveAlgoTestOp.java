package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import static java.lang.Math.abs;
import static java.lang.Math.atan;
import static java.lang.Math.cos;
import static java.lang.Math.min;
import static java.lang.Math.pow;
import static java.lang.Math.signum;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

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

    void geometricDrive() {
        double x = gamepad1.right_stick_x;
        double y = -gamepad1.right_stick_y;

        double x2 = pow(gamepad1.right_stick_x, 2.0);
        double y2 = pow(gamepad1.right_stick_y, 2.0);
        double magnitude = sqrt(x2 + y2);
        double degrees = atan(y / x) - 45.0; // shifted -45 for clever math #reasons

        double left = magnitude * (cos(degrees) / cos(45.0));
        double right = magnitude * (sin(degrees) / sin(45.0));

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
