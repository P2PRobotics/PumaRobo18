package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.pidcontrol.AngularPController;

import java.util.ArrayList;
import java.util.List;

import static org.firstinspires.ftc.teamcode.GameConstants.VUFORIA_KEY_P2PROBOTICSFTC;

@Autonomous (name = "AutonomousTensorFlowTest", group = "A")
public class AutonomousTensorFlowTest extends BaseOp {

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
        headingController = new AngularPController(
                () -> (double) imu.getAngularOrientation().firstAngle,
                2.0d,
                1.0d,
                0.2d
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

    @Override
    public void init_loop() {
        super.init_loop();
        if (tfod != null) {
            // getUpdatedRecognitions() will return null if no new information is available since
            // the last time that call was made.
            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
            if (updatedRecognitions != null) {
                telemetry.addData("# Object Detected", updatedRecognitions.size());
                //if (updatedRecognitions.size() == 3) {
                    int goldMineralX = -1;
                    ArrayList<Integer> silverLocations = new ArrayList<Integer>();
                    boolean centerSilver = false;
                    boolean leftSilver = false;
                    boolean rightSilver = false;
                    for (Recognition recognition : updatedRecognitions) {
                        if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                            goldMineralX = (int) recognition.getTop();
                        } else if (recognition.getLabel().equals(LABEL_SILVER_MINERAL)) {
                            silverLocations.add((int) recognition.getTop());
                        }
                    }
                    for (Integer xLoc: silverLocations) {
                        if (xLoc < 400) {
                            leftSilver = true;
                        } else if (xLoc < 800) {
                            centerSilver = true;
                        } else if (xLoc < 1200){
                            rightSilver = true;
                        }
                    }
                    int foundSilvers = 0;
                    if (leftSilver)
                        foundSilvers++;
                    if (centerSilver)
                        foundSilvers++;
                    if (rightSilver)
                        foundSilvers++;
                    if (goldMineralX != -1) {
                        if (goldMineralX < 400) {
                            goldElement = 1;
                            telemetry.addData("Gold Mineral Position", "Left");
                        } else if (goldMineralX < 800) {
                            goldElement = 2;
                            telemetry.addData("Gold Mineral Position", "Center");
                        } else {
                            goldElement = 3;
                            telemetry.addData("Gold Mineral Position", "Right");
                        }
                    } else {
                        if (foundSilvers  > 1) {
                            if (rightSilver && centerSilver) {
                                goldElement = 1;
                                telemetry.addData("Gold Mineral Position", "Left");
                            } else  if (leftSilver && centerSilver) {
                                goldElement = 3;
                                telemetry.addData("Gold Mineral Position", "Right");
                            } else if (rightSilver && leftSilver) {
                                goldElement = 2;
                                telemetry.addData("Gold Mineral Position", "Center");
                            }
                        } else if (foundSilvers == 1) {
                            if (leftSilver) {
                                double rand = Math.random();
                                if (rand < 0.5) {
                                    goldElement = 3;
                                    telemetry.addData("Gold Mineral Position", "Right");
                                } else {
                                    goldElement = 2;
                                    telemetry.addData("Gold Mineral Position", "Center");
                                }
                            } else if (centerSilver) {
                                double rand = Math.random();
                                if (rand < 0.5) {
                                    goldElement = 3;
                                    telemetry.addData("Gold Mineral Position", "Right");
                                } else {
                                    goldElement = 1;
                                    telemetry.addData("Gold Mineral Position", "Left");
                                }
                            } else if (rightSilver) {
                                double rand = Math.random();
                                if (rand < 0.5) {
                                    goldElement = 1;
                                    telemetry.addData("Gold Mineral Position", "Left");
                                } else {
                                    goldElement = 2;
                                    telemetry.addData("Gold Mineral Position", "Center");
                                }
                            }
                        } else {

                        }
                    }
                //}

                telemetry.addData("Gold LocationX", goldMineralX);
                telemetry.update();
            }
        }
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
