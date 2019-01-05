package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * ON CRATER SIDE:
 * hit element near crater, turn left to depot, place marker, park in crater if time permits
 */
@Autonomous(name = "AutonomousCraterSideOp", group = "Competition")
public class AutonomousCraterSideOp extends AutonomousBaseOp implements GameConstants {

    int[] time = {1000, 500, 400, 600, 500, 200,1000,700,1100,200,1900,1300};
    @Override
    public void loop() {
        super.loop();

        switch (state) {
            case INIT_DROP:
                autoRuntime.reset();
                state = State.DROPPING;
                break;
            case DROPPING:
                if (autoRuntime.time() < 2_500) {
                    lift(0.5);
                } else {
                    lift(0);
                    state = State.INIT_UNLATCH;
                }
                break;

            case INIT_UNLATCH:
                autoRuntime.reset();
                state = State.UNLATCHING;
                break;
            case UNLATCHING:
                if (autoRuntime.time() < 2_500) {
                    latchOpen();
                } else {
                    latchStop();
                    state = State.INIT_DRIVE;
                }
                break;

            case INIT_DRIVE:
                autoRuntime.reset();
                state = State.DRIVE;
                break;
            case DRIVE:
                //TODO: make time variable (more difficult than you might think)
                //crater side code
                if (autoRuntime.time() < addUp(time, 0)) {
                    moveStraight(0.5);
                } else if (autoRuntime.time() < addUp(time, 1)) {
                    moveStop();
                } else if (autoRuntime.time() < addUp(time, 2)) {
                    moveStraight(-0.5);
                } else if (autoRuntime.time() < addUp(time, 3)) {
                    moveStop();
                } else if (autoRuntime.time() < addUp(time, 4)) {
                    turn(0.5);
                } else if (autoRuntime.time() < addUp(time, 5)) {
                    turn(0);
                } else if (autoRuntime.time() < addUp(time, 6)) {
                    moveStraight(0.45);
                } else if (autoRuntime.time() < addUp(time, 7)) {
                    moveStop();
                } else if (autoRuntime.time() < addUp(time, 8)) {
                    turn(0.4);
                } else if (autoRuntime.time() < addUp(time, 9)) {
                    turn(0);
                } else if (autoRuntime.time() < addUp(time, 10)) {
                    moveStraight(0.5);
                } else if (autoRuntime.time() < addUp(time, 11)) {
                    lowerContainer();
                    moveStop();
                    state = State.INIT_DEPOSIT;
                }
                break;

            // INIT_DEPOSIT and DEPOSITING:
            //   Need to open the container while moving, then brake so that
            //   the element in the container is forced into the outtake wheel.
            case INIT_DEPOSIT:
                autoRuntime.reset();
                lowerContainer();
                state = State.DEPOSITING;
                break;
            case DEPOSITING:
                if (autoRuntime.time() < 1000) {
                    intakeOut();
                } else {
                    intakeStop();
                    state = State.STOP;
                }
                break;

            case STOP:
                stop();
                break;


//            case SCAN:
//                if (tfod != null) {
//                    // getUpdatedRecognitions() will return null if no new information is available since
//                    // the last time that call was made.
//                    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
//                    if (updatedRecognitions != null) {
//                        telemetry.addData("# Object Detected", updatedRecognitions.size());
//                        if (updatedRecognitions.size() == 3) {
//                            int goldMineralX = -1;
//                            int silverMineral1X = -1;
//                            int silverMineral2X = -1;
//                            for (Recognition recognition : updatedRecognitions) {
//                                if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
//                                    goldMineralX = (int) recognition.getLeft();
//                                } else if (silverMineral1X == -1) {
//                                    silverMineral1X = (int) recognition.getLeft();
//                                } else {
//                                    silverMineral2X = (int) recognition.getLeft();
//                                }
//                            }
//                            if (goldMineralX != -1 && silverMineral1X != -1 && silverMineral2X != -1) {
//                                if (goldMineralX < silverMineral1X && goldMineralX < silverMineral2X) {
//                                    autoRuntime.reset();
//                                    state = State.GOLD_LEFT;
//                                } else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X) {
//                                    autoRuntime.reset();
//                                    state = State.GOLD_RIGHT;
//                                } else {
//                                    autoRuntime.reset();
//                                    state = State.GOLD_CENTER;
//                                }
//                            }
//                        }
//                    }
//                }
//                break;
//            case GOLD_LEFT:
//                if (autoRuntime.time() < 3) {
//                    curveDrive(1, 1);
//                }
//                //Placeholders: need to test ----------------------------------------------------------
//                break;
//            case GOLD_CENTER:
//                if (autoRuntime.time() < 3) {
//                    moveStraight(0.5);
//                }
//                break;
//            case GOLD_RIGHT:
//                if (autoRuntime.time() < 3) {
//                    curveDrive(1, 1);
//                }
//                //Placeholders: need to test ----------------------------------------------------------
//                break;
//            case PLACE_MARKER:
//                if (CRATER_SIDE) {
//
//                } else {
//
//                }
//                break;
//            case DRIVE_DEPOT:
//
//                break;
//            case DRIVE_CRATER:
//
//                break;
//            case PARK:
//                if (CRATER_SIDE) {
//
//                } else {
//
//                }
//                break;
        }
    }
}
