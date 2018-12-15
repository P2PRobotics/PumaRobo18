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


        double rawx = gamepad1.right_stick_x;
        double rawy = -gamepad1.right_stick_y;
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



        //RB raises extender, LB lowers.
        if (gamepad2.right_bumper) {
            lift(1);
        } else if (gamepad2.left_bumper) {
            lift(-1);
        }
        //A to open grabber, x to close
        if (gamepad2.a) {
            latchBar(false);
        } else if (gamepad2.x) {
            latchBar(true);
        }
        //hold B for intake, hold Y for output
        if (gamepad2.b) {
            wheelIn();
        } else if (gamepad2.y) {
            wheelOut();
        } else {
            wheelStop();
        }
        //D-Pad to raise dish up and down
        if (gamepad2.dpad_up) {
            raiseContainer();
        } else if (gamepad2.dpad_down) {
            lowerContainer();
        }
        else{
            stopContainer();
        }
    }
}
