package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.pidcontrol.AngularPController;

import static org.firstinspires.ftc.teamcode.GameConstants.VUFORIA_KEY_P2PROBOTICSFTC;

/**
 * Acts as a base-class for all P2P Autonomous OpModes, also inherits from BaseOp for stuff that's
 * common to both Autonomous and Driver control.
 */
abstract class AutonomousBaseOp extends BaseOp {
    protected State state;

    //Timer
    protected ElapsedTime autoRuntime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

    //TensorFlow stuff
    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";

    private static final String VUFORIA_KEY = VUFORIA_KEY_P2PROBOTICSFTC;

    // The IMU sensor object
    BNO055IMU imu;
    AngularPController headingController;

    // add sensors here!!
    // private VuforiaHelper vuforia;

    @Override
    public void init() {
        super.init();
        autoRuntime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        imu = initIMU(this.hardwareMap);
        headingController = new AngularPController(
            () -> (double) imu.getAngularOrientation().firstAngle,
            2.0d,
            1.0d,
            0.2d
        );
        state = State.INIT_DROP;
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

    /**
     * This method is called repeatedly after the INIT button is pressed - while we're hanging
     * waiting for the round to start.
     */
    @Override
    public void init_loop() {
        super.init_loop();
        // TODO: Use Tensorflow here to passively attempt detection of the Gold element while we're waiting for the round to begin.
        // vuforia.start();

        // if (tfod != null) {
        //     tfod.activate();
        // }
        // check if ftc updated their VuMark resources!!
    }

    @Override
    public void loop() {
        super.loop();
        headingController.update();
    }

    @Override
    public void start() {
        headingController.calibrateTo(0.0f);
        headingController.setDesired(160.0f);
    }

    @Override
    public void stop() {
        super.stop();
    }

//    private void initVuforia() {
//        /*
//         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
//         */
//        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
//
//        parameters.vuforiaLicenseKey = VUFORIA_KEY;
//        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
//
//        //  Instantiate the Vuforia engine
//        vuforia = ClassFactory.getInstance().createVuforia(parameters);
//
//        // Loading trackables is not necessary for the Tensor Flow Object Detection engine.
//    }
//
//    private void initTfod() {
//        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
//                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
//        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
//        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
//        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
//    }
}
