package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="BasicTeleOp",group="D")
public class BasicTeleOp extends BaseOp {


    @Override
    public void init(){
        super.init();
    }
    public void start(){

    }
    @Override
    public void loop() {
        //sets initial power to 0 so motors don't move upon initialization
        lmotor1.setPower(0);
        lmotor2.setPower(0);
        rmotor1.setPower(0);
        rmotor2.setPower(0);
        //Forwards
        if (gamepad1.left_trigger > 0.05) {
            double trigger = gamepad1.left_trigger;
            move(Math.pow(trigger, 5));
            return;
        }
        //Backwards
        if (gamepad1.right_trigger > 0.05) {
            double trigger = gamepad1.right_trigger;
            move(-Math.pow(trigger, 5));
            return;
        }

        double rawx = gamepad1.right_stick_x;
        double rawy = -gamepad1.right_stick_y;
        double x = Math.pow(rawx, 7); //adjust sensitivity?
        double y = Math.pow(rawy, 7);
        //Turning WIP might work
        if (x != 0 || y != 0) {
            double n = ((x + y) / Math.sqrt(2.0)); // n is the power of the motors in the +x +y direction
            //double m = ((x - y) / Math.sqrt(2.0)); // m is the power of the motors in the +x -y direction
            turn(n);
        }
        //RB raises extender, LB lowers. maybe adjust power?
        if (gamepad2.right_bumper) {
            lift(5);
        }
        if (gamepad2.left_bumper) {
            lift(-5);
        }
        //A to open grabber, x to close
        if (gamepad2.a) {
            latchBar(false);
        }
        if (gamepad2.x) {
            latchBar(true);
        }
        //B for intake, Y for output
        if (gamepad2.b) {
            wheelOut(false);
            wheelIn(true);
        }
        if (gamepad2.y) {
            wheelIn(false);
            wheelOut(true);
        }
        //D-Pad to raise dish up and down
        if (gamepad2.dpad_up) {
            raiseContainer(true);
        }
        if (gamepad2.dpad_down) {
            raiseContainer(false);
        }
    }
}
