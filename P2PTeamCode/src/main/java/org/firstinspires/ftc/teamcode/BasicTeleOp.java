package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "BasicTeleOp", group = "D")
public class BasicTeleOp extends BaseOp {


    @Override
    public void init() {
        super.init();
    }

    public void start() {

    }

    //adjust to move and turn at same time
    @Override
    public void loop() {
        telemetry.update();
        //sets initial power to 0 so motors don't move upon initialization

        double y = -gamepad1.left_stick_y;
        //movement
        move(y);

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
    }
}
