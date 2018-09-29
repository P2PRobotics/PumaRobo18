package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="BasicTeleOp",group="D")
public class BasicTeleOp extends OpMode {
    //motors 1-4 are drivetrain
    private DcMotor leftMotor1;
    private DcMotor leftMotor2;
    private DcMotor rightMotor1;
    private DcMotor rightMotor2;

    private DcMotor motor5; //?? confirm w mechanical on motors 5-8, maybe rename?
    private DcMotor motor6;
    private DcMotor motor7;
    private DcMotor motor8;

    //don't forget to initialize other mechanical aspects!!

    @Override
    public void init(){
        //initializes motors, retrieve configs from RevHub and Controllers
        leftMotor1 = hardwareMap.dcMotor.get("l1"); //front wheels (dirMotor1) go backwards
        leftMotor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftMotor1.setDirection(DcMotorSimple.Direction.REVERSE);
        //hey software-- don't forget to configure motors w these exact names!!
       leftMotor2 = hardwareMap.dcMotor.get("l2");
        leftMotor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        rightMotor1 = hardwareMap.dcMotor.get("r1");
        rightMotor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightMotor1.setDirection(DcMotorSimple.Direction.REVERSE);

        rightMotor2 = hardwareMap.dcMotor.get("r2");
        rightMotor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        //retrieve configs of any other mechanical aspects
    }
    public void start(){
    //??
    }
    @Override
    public void loop(){
        //sets initial power to 0 so motors don't move upon initialization
        leftMotor1.setPower(0);
        leftMotor2.setPower(0);
        rightMotor1.setPower(0);
        rightMotor2.setPower(0);
        //pivoting and turning, uses left and right triggers
        if (gamepad1.left_trigger > 0.05) {
            double trigger = gamepad1.left_trigger;
            leftMotor1.setPower(-Math.pow(trigger, 5));
            leftMotor2.setPower(-Math.pow(trigger, 5));
            rightMotor1.setPower(Math.pow(trigger, 5));
            rightMotor2.setPower(Math.pow(trigger, 5));
            return;
        }
        if (gamepad1.right_trigger > 0.05) {
            double trigger = gamepad1.right_trigger;
            leftMotor1.setPower(Math.pow(trigger, 5));
            leftMotor2.setPower(Math.pow(trigger, 5));
            rightMotor1.setPower(-Math.pow(trigger, 5));
            rightMotor2.setPower(-Math.pow(trigger, 5));
            return;
        }
        double rawx = gamepad1.right_stick_x;
        double rawy = -gamepad1.right_stick_y;
        double x= Math.pow(rawx, 7); //adjust sensitivity?
        double y = Math.pow(rawy, 7);
        if (x != 0 || y != 0) {
            double n = ((x + y) / Math.sqrt(2.0)); // n is the power of the motors in the +x +y direction
            double m = ((x - y) / Math.sqrt(2.0)); // m is the power of the motors in the +x -y direction

            leftMotor1.setPower(m);
            leftMotor2.setPower(m);
            rightMotor1.setPower(n);
            rightMotor2.setPower(n);
        }
        //LB lowers extender, RB raises extender
        //A opens grabber, X closes
        //Right Joystick for movement
        //actual controls go here
        //add pivot mechanicism (?)
        //motion
        //any extraneous motions

    }
}
