package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.pidcontrol.AngularPController;

import java.util.List;

import static org.firstinspires.ftc.teamcode.GameConstants.VUFORIA_KEY_P2PROBOTICSFTC;

/**
 * Acts as a base-class for all P2P Autonomous OpModes, also inherits from BaseOp for stuff that's
 * common to both Autonomous and Driver control.
 */
abstract class AutonomousBaseOp extends BaseOp {
    protected int state;

    //Timer
    protected ElapsedTime autoRuntime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

    //TensorFlow stuff
    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";
    int goldElement = 2;

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
                0.6d //maybe adjust??
        );
        initVuforia();

        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }

        if (tfod != null) {
            tfod.activate();
        }

        state = 0;
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
        if (tfod != null) {
            // getUpdatedRecognitions() will return null if no new information is available since
            // the last time that call was made.
            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
            if (updatedRecognitions != null) {
                telemetry.addData("# Object Detected", updatedRecognitions.size());
                if (updatedRecognitions.size() == 3) {
                    int goldMineralX = -1;
                    int silverMineral1X = -1;
                    int silverMineral2X = -1;
                    for (Recognition recognition : updatedRecognitions) {
                        if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                            goldMineralX = (int) recognition.getTop();
                        } else if (silverMineral1X == -1) {
                            silverMineral1X = (int) recognition.getTop();
                        } else {
                            silverMineral2X = (int) recognition.getTop();
                        }
                    }
                    if (goldMineralX != -1 && silverMineral1X != -1 && silverMineral2X != -1) {
                        if (goldMineralX < silverMineral1X && goldMineralX < silverMineral2X) {
                            goldElement = 1;
                            telemetry.addData("Gold Mineral Position", "Left");
                        } else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X) {
                            goldElement = 3;
                            telemetry.addData("Gold Mineral Position", "Right");
                        } else {
                            goldElement = 2;
                            telemetry.addData("Gold Mineral Position", "Center");
                        }
                    }
                }
                telemetry.update();
            }
        }
    }

    @Override
    public void loop() {
        super.loop();
        headingController.update();
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
    }

    //index starting from zero
    public static int addUp(int[] arr, int index) {
//        if (index != 0) {
        int total = 0;
        for (int i = 0; i < index; i++) {
            total += arr[i];
        }
        return total;
    }

    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    private VuforiaLocalizer vuforia;

    /**
     * {@link #tfod} is the variable we will use to store our instance of the Tensor Flow Object
     * Detection engine.
     */
    private TFObjectDetector tfod;

    /**
     * Initialize the Vuforia localization engine.
     */
    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the Tensor Flow Object Detection engine.
    }

    /**
     * Initialize the Tensor Flow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }

    void sleep(long millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
        }
    }
}
