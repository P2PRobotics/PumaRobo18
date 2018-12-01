package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.pidcontrol.AngularPController;

@Autonomous(name = "AutonomousOp" , group = "AAAAAARP")
public class AutonomousOp extends BaseOp implements GameConstants {

    // The IMU sensor object
    BNO055IMU imu;
    AngularPController headingController;

    //add sensors here!!
    //private OrientationSensor orientationSensor;
    //private VuforiaHelper vuforia;

    //consider adding states and stacks here
    //if you do states, don't forget to make a state.java file!!
    //don't forget your important variables here
    public void init(){
        super.init();
        imu = initIMU(this.hardwareMap);
        headingController = new AngularPController(
                () -> imu.getAngularOrientation().firstAngle,
                1.0f,
                1.0f,
                0.2f
        );
    }
    public void start(){
        //vuforia.start();
        //check if ftc updated their VuMark resources!!
        headingController.calibrateTo(0.0f);
        headingController.setDesired(45.0f);
    }
    public void loop(){
        headingController.update();
        float turnRate = headingController.getControlValue();
        if (turnRate == Float.NaN) turn(0.0);
        else turn(turnRate);
        //detatch from lander
        //liftmotor.setPower(-3);
        //liftgrab.setPosition(-0.5);
        //orient self
        //use vuforia to locate depot
            // if(RED_TEAM){
            // if(LEFT_FIELD){
            // (location 1 --> depot)
            // }
            // (locaton 2 --> depot)
            // }
            // else{
            // if(LEFT_FIELD){
            // (location 3 --> depot)
            // }
            // (location 4 --> depot)
            // }
        //place marker
        //if(RED_TEAM){
        //depot--> crater1
        //set power on all motors to 0
        //}
        //else{
        //depot--> crater2
        //set power on all motors to 0
        //}
        //use vuforia to locate crater
        //park in crater

    }
    public static BNO055IMU initIMU (HardwareMap hardwareMap) {
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        // The IMU sensor object
        BNO055IMU imu;
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
        return imu;
    }
}
