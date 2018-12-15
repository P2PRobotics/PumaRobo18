package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.pidcontrol.AngularPController;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

@Autonomous(name = "AutonomousOp", group = "AAAAAARP")
public class AutonomousOp extends BaseOp implements GameConstants {

    // The IMU sensor object
    BNO055IMU imu;
    AngularPController headingController;

    //add sensors here!!
    //private OrientationSensor orientationSensor;
    //private VuforiaHelper vuforia;

    //if you do states, don't forget to make a state.java file!!


    public void init() {
        super.init();
        imu = initIMU(this.hardwareMap);
        headingController = new AngularPController(
                () -> imu.getAngularOrientation().firstAngle,
                2.0f,
                1.0f,
                0.2f
        );

    }

    public void start() {
        //vuforia.start();
        //check if ftc updated their VuMark resources!!
        headingController.calibrateTo(0.0f);
        headingController.setDesired(160.0f);
        lift(-1); //lower robot to the ground
        latchBar(false);
        latchCup(false);
    }

    public void loop() {
        headingController.update();
        float turnRate = headingController.getControlValue();
        if (turnRate == Float.NaN) turn(0.0);
        else turn(-turnRate);
        //IF ON CRATER SIDE: hit element near crater, curve to depot, place marker, park in crater if time permits
        //IF ON DEPOT SIDE: hit element near depot, place marker, drive to crater, park in crater if time permits


    }

    public static BNO055IMU initIMU(HardwareMap hardwareMap) {
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
