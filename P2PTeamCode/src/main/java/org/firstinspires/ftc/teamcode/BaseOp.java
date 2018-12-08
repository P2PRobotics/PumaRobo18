package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

public class BaseOp extends OpMode {

    //drivetrain
    public DcMotor lmotor1;
    public DcMotor lmotor2;
    public DcMotor rmotor1;
    public DcMotor rmotor2;

    //lifter and lander (servo latches on to lander, motor extends/retracts arm)
    public DcMotor liftMotor;
    public Servo latchBarM;
    public Servo latchCupM;

    //wheeled intake system
    public DcMotor intake1;
    public CRServo intake2;

    //motor power reg
    public static final double deltamax = 0.2;

    public void init() {
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

        //make sure these match what's in the config

        liftMotor= hardwareMap.dcMotor.get("liftmo"); //revHub 2, motor port 0
        liftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        latchBarM = hardwareMap.servo.get("latchBarM"); //revHub 1, servo port 0
        latchCupM = hardwareMap.servo.get("latchCupM"); //revHub 1, servo port 1

        intake1=hardwareMap.dcMotor.get("intake1"); //revHub 2, motor port 1
        intake1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        intake2=hardwareMap.crservo.get("intake2"); //revHub 1, servo port 2


        lmotor1.setPower(0);
        lmotor2.setPower(0);
        rmotor1.setPower(0);
        rmotor2.setPower(0);

        liftMotor.setPower(0);
        intake1.setPower(0);
    }
    public void start() {}
    public void loop() {}

    public void wheelIn(boolean in){
        if(in){
            intake1.setPower(0.75);
        } else {
            intake1.setPower(0);
        }
    }

    public void wheelOut(boolean out) {
        if(out){
            intake1.setPower(-0.75);
        } else {
            intake1.setPower(0);
        }
    }

    public void raiseContainer(boolean raised){
        if(raised) {
          //  intake2.setDirection(1);
        } else {
           // intake2.setDirection(-1);
        }
    }


    public void lift(double speed){
        liftMotor.setPower(speed);
    }

    public void latchBar(boolean latchedBar) {
        if(latchedBar) {
            latchBarM.setPosition(0.5);
        } else {
            latchBarM.setPosition(0);
        }
    }
    public void latchCup(boolean latchedCup){
        if(latchedCup){
            latchCupM.setPosition(0.5);
        } else {
            latchCupM.setPosition(0);
        }
    }



    public void turn(double speed){
        double v = adjustedSpeed(speed);
        double curPowRm1 = rmotor1.getPower();
        if ((Math.abs(curPowRm1)+deltamax) > 1.0)
        rmotor1.setPower(v);
        rmotor2.setPower(v);
        lmotor1.setPower(v);
        lmotor2.setPower(v);
    }

    public void move(double speed){
        double v = adjustedSpeed(speed);
        lmotor1.setPower(-v);
        lmotor2.setPower(-v);
        rmotor1.setPower(v);
        rmotor2.setPower(v);

    }

    static double adjustedSpeed(double speed) {
        if (speed < -1.0) return -1.0;
        else if (speed > 1.0) return 1.0;
        else if (speed < 0.2 && speed > 0) return 0.2;
        else if (speed > -0.2 && speed < 0) return -0.2;
        else return speed;
        //Adjust speed so it doesn't slow down too much!!!!!!!
    }

}
