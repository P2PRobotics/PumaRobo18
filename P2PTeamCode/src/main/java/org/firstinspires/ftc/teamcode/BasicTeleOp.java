package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="BasicTeleOp",group="D")
public class BasicTeleOp extends OpMode {
    //drivetrain
    private DcMotor lmotor1;
    private DcMotor lmotor2;
    private DcMotor rmotor1;
    private DcMotor rmotor2;

    //lifter and lander (servo latches on to lander, motor extends/retracts arm)
    private DcMotor liftmotor;
    private Servo liftgrab;

    //wheeled intake system
    private DcMotor intake1;


    @Override
    public void init(){
        //initializes motors, retrieve configs
        lmotor1 = hardwareMap.dcMotor.get("m12");
        lmotor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

       lmotor2 = hardwareMap.dcMotor.get("m13");
       lmotor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
       lmotor2.setDirection(DcMotorSimple.Direction.REVERSE);

        rmotor1 = hardwareMap.dcMotor.get("m11");
        rmotor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        rmotor2 = hardwareMap.dcMotor.get("m10");
        rmotor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rmotor2.setDirection(DcMotorSimple.Direction.REVERSE);

        liftmotor= hardwareMap.dcMotor.get("liftmo");
        liftmotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        liftgrab= hardwareMap.servo.get("liftserv");

        intake1=hardwareMap.dcMotor.get("intake1");
        intake1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    }
    public void start(){

    }
    @Override
    public void loop(){
        //sets initial power to 0 so motors don't move upon initialization
       lmotor1.setPower(0);
        lmotor2.setPower(0);
        rmotor1.setPower(0);
        rmotor2.setPower(0);
        //pivoting and turning, uses left and right triggers
        if (gamepad1.left_trigger > 0.05) {
            double trigger = gamepad1.left_trigger;
            //move forward w left trigger
            lmotor1.setPower(-Math.pow(trigger, 5));
            lmotor2.setPower(-Math.pow(trigger, 5));
            rmotor1.setPower(Math.pow(trigger, 5));
            rmotor2.setPower(Math.pow(trigger, 5));
            return;
        }
        if (gamepad1.right_trigger > 0.05) {
            double trigger = gamepad1.right_trigger;
            //move backward w right trigger
            lmotor1.setPower(Math.pow(trigger, 5));
            lmotor2.setPower(Math.pow(trigger, 5));
            rmotor1.setPower(-Math.pow(trigger, 5));
           rmotor2.setPower(-Math.pow(trigger, 5));
            return;
        }
        double rawx = gamepad1.right_stick_x;
        double rawy = -gamepad1.right_stick_y;
        double x= Math.pow(rawx, 7); //adjust sensitivity?
        double y = Math.pow(rawy, 7);
        //pivot w right joystick
        if (x != 0 || y != 0) {
            double n = ((x + y) / Math.sqrt(2.0)); // n is the power of the motors in the +x +y direction
            //double m = ((x - y) / Math.sqrt(2.0)); // m is the power of the motors in the +x -y direction

            lmotor1.setPower(n);
            lmotor2.setPower(n);
            rmotor1.setPower(n);
            rmotor2.setPower(n);
        }
        //RB raises extender, LB lowers. maybe adjust power?
        if(gamepad1.right_bumper){
            liftmotor.setPower(5);
        }
        if(gamepad1.left_bumper){
            liftmotor.setPower(-5);
        }
        //A to open grabber, x to close
        if(gamepad1.a){
            liftgrab.setPosition(0.5);
        }
        if(gamepad1.x){
            liftgrab.setPosition(-0.5);
        }
        //B for intake, Y for output
        if(gamepad1.b){
            intake1.setPower(5);

        }
        if(gamepad1.y){
            intake1.setPower(-5);

        }
    }
}
