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

        //RB raises extender, LB lowers.
        if (gamepad2.right_bumper) {
            lift(1);
        } else if (gamepad2.left_bumper) {
            lift(-1);
        }
        //A to open grabber, x to close
        if (gamepad2.a) {
            latchOpen();
        } else if (gamepad2.x) {
            latchClose();
        }
        //hold B for intake, hold Y for output
        if (gamepad2.b) {
            intakeIn();
        } else if (gamepad2.y) {
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
