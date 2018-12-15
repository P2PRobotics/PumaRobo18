package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.pidcontrol.AngularPController;

import java.util.List;
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
    //State stuff

    private State state;
    private Stack<State> nextStates;

    //Timer
    private ElapsedTime mRuntime;

    //TensorFlow stuff
    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";

    private static final String VUFORIA_KEY = VUFORIA_KEY_P2PROBOTICSFTC;

    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;

    public void init() {
        super.init();
        ElapsedTime mRuntime = new ElapsedTime();
        imu = initIMU(this.hardwareMap);
        headingController = new AngularPController(
                () -> imu.getAngularOrientation().firstAngle,
                2.0f,
                1.0f,
                0.2f
        );
        state = State.DROP;
        nextStates = new Stack<State>();

        initVuforia();
        initTfod();
    }

    public void start() {
        //vuforia.start();
        //check if ftc updated their VuMark resources!!
        headingController.calibrateTo(0.0f);
        headingController.setDesired(160.0f);
        lift(-1); //lower robot to the ground
        latchClose();
        latchCup(false);
        mRuntime.reset();
        if (tfod != null) {
            tfod.activate();
        }
    }

    public void loop() {
        headingController.update();
        float turnRate = headingController.getControlValue();
        if (turnRate == Float.NaN) turn(0.0);
        else turn(-turnRate);
        //IF ON CRATER SIDE: hit element near crater, curve to depot, place marker, park in crater if time permits
        //IF ON DEPOT SIDE: hit element near depot, place marker, drive to crater, park in crater if time permits

        //More state stuff
        switch (state) {
            case DROP:
                if (mRuntime.time() < 3) {
                    lift(0.5);
                } else {
                    lift(0);
                    latchOpen();
                    state = State.SCAN;
                }
                break;
            case SCAN:
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
                                    goldMineralX = (int) recognition.getLeft();
                                } else if (silverMineral1X == -1) {
                                    silverMineral1X = (int) recognition.getLeft();
                                } else {
                                    silverMineral2X = (int) recognition.getLeft();
                                }
                            }
                            if (goldMineralX != -1 && silverMineral1X != -1 && silverMineral2X != -1) {
                                if (goldMineralX < silverMineral1X && goldMineralX < silverMineral2X) {
                                    mRuntime.reset();
                                    state = State.GOLD_LEFT;
                                } else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X) {
                                    mRuntime.reset();
                                    state = State.GOLD_RIGHT;
                                } else {
                                    mRuntime.reset();
                                    state = State.GOLD_CENTER;
                                }
                            }
                        }
                    }
                }
                break;
            case GOLD_LEFT:
                if (mRuntime.time() < 3) {
                    curveDrive(1, 1);
                }
                //Placeholders: need to test ----------------------------------------------------------
                break;
            case GOLD_CENTER:
                if (mRuntime.time() < 3) {
                    move(0.5);
                }
                break;
            case GOLD_RIGHT:
                if (mRuntime.time() < 3) {
                    curveDrive(1, 1);
                }
                //Placeholders: need to test ----------------------------------------------------------
                break;
            case PLACE_MARKER:
                if (CRATER_SIDE) {

                } else {

                }
                break;
            case DRIVE_DEPOT:

                break;
            case DRIVE_CRATER:

                break;
            case PARK:
                if (CRATER_SIDE) {

                } else {

                }
                break;
        }
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

    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }
}
