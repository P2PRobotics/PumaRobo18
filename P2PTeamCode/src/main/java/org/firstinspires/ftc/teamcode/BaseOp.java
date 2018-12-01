package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

public class BaseOp extends OpMode {
    private boolean latchedBar = true;
    private boolean latchedCup = true;
    private boolean in = false;
    private boolean out = false;

    //drivetrain
    public DcMotor lmotor1;
    public DcMotor lmotor2;
    public DcMotor rmotor1;
    public DcMotor rmotor2;
    private DcMotor[] wheelMotors = new DcMotor[]{lmotor1,lmotor2,rmotor1,rmotor2};

    //lifter and lander (servo latches on to lander, motor extends/retracts arm)
    public DcMotor liftmotor;
    public Servo latchBarM;
    public Servo latchCupM;

    //wheeled intake system
    public DcMotor intake1;
    public Servo intake2;


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

        liftmotor= hardwareMap.dcMotor.get("liftmo");
        liftmotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        latchBarM = hardwareMap.servo.get("latchBarM");
        latchCupM = hardwareMap.servo.get("latchCupM");

        intake1=hardwareMap.dcMotor.get("intake1");
        intake1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        intake2=hardwareMap.servo.get("intake2");

    }
    public void start() {}
    public void loop() {}


    public boolean getWheelIn() {
        return in;
    }
    public boolean getWheelOut() {
        return out;
    }
    public boolean getLatchedCup() {
        return latchedCup;
    }
    public boolean getLatchedBar() {
        return latchedBar;
    }
    public void setWheelIn(boolean in) {
        this.in = in;
    }
    public void setWheelOut(boolean out) {
        this.out = out;
    }
    public void setLatchedCup(boolean latchedCup) {
        this.latchedCup = latchedCup;
    }
    public void setLatchedBar(boolean latchedBar) {
        this.latchedBar = latchedBar;
    }

    public void wheelCheck(){
        if(in){
            intake1.setPower(0.75);
        } else if (out) {
            intake1.setPower(-0.75);
        } else {
            intake1.setPower(0);
        }
    }

    public void latchBar() {
        if(latchedBar) {
            latchBarM.setPosition(0.5);
        } else {
            latchBarM.setPosition(0);
        }
    }
    public void latchCup(){
        if(latchedCup){
            latchCupM.setPosition(0.5);
        } else {
            latchCupM.setPosition(0);
        }
    }



    public void turn(double speed){
        double v = adjustedSpeed(speed);
        rmotor1.setPower(v);
        rmotor2.setPower(-v);
        lmotor1.setPower(v);
        lmotor2.setPower(-v);
    }

    public void move(double speed){
        double v = adjustedSpeed(speed);
        for (DcMotor motor : wheelMotors) {
            motor.setPower(v);
        }
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
