package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "BasicTeleOp", group = "Competition")
public class BasicTeleOp extends BaseOp {


    @Override
    public void init() {
        super.init();
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void setupTelemetry() {
        telemetry
                .addLine("driver")
                .addData("right_stick_x", () -> gamepad1.right_stick_x)
                .addData("right_stick_y", () -> gamepad1.right_stick_y);
    }

    //adjust to move and turn at same time
    @Override
    public void loop() {
        super.loop();

        geometricDrive(gamepad1.right_stick_x, -gamepad1.right_stick_y);

        // B raises extender, A lowers.
        if (gamepad2.b) {
            lift(0.5);
        } else if (gamepad2.a) {
            lift(-0.5);
        } else {
            lift(0);
        }
        // X to open latch, Y to close
        if (gamepad2.y) {
            latchClose();
        } else if (gamepad2.x) {
            latchOpen();
        } else {
            latchStop();
        }
        //hold RB for intake, hold LB for output
        if (gamepad2.right_bumper) {
            intakeIn();
        } else if (gamepad2.left_bumper) {
            intakeOut();
        } else {
            intakeStop();
        }
        //D-Pad to raise dish up and down
        if (gamepad2.dpad_up) {
            raiseContainer();
        } else if (gamepad2.dpad_down) {
            lowerContainer();
        }

        if (gamepad2.dpad_right) {
            landerRotateUp();
        } else if (gamepad2.dpad_left) {
            landerRotateDown();
        } else {
            landerRotateStop();
        }

    }
}
