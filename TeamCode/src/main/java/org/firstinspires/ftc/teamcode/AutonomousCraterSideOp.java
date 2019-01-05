package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * ON CRATER SIDE:
 * hit element near crater, turn left to depot, place marker, park in crater if time permits
 */
@Autonomous(name = "AutonomousCraterSideOp", group = "Competition")
public class AutonomousCraterSideOp extends AutonomousBaseOp implements GameConstants {

    @Override
    public void loop() {
        super.loop();

        switch (state) {
            case 0:
                autoRuntime.reset();
                state++;
                break;

            case 1: // LOWER
                if (autoRuntime.time() < 2_500) {
                    lift(0.5);
                } else if (autoRuntime.time() < 3_000) {
                    lift(0);
                } else {
                    autoRuntime.reset();
                    state++;
                }
                break;

            case 2: // UNLATCH
                if (autoRuntime.time() < 2_500) {
                    latchOpen();
                } else if (autoRuntime.time() < 3_000) {
                    latchStop();
                } else {
                    headingController.calibrateTo(42.5d);
                    autoRuntime.reset();
                    state++;
                }
                break;

            case 3: // DRIVE FWD TO GAME ELEMENT
                //crater side code
                if (autoRuntime.time() < 1_000) {
                    moveStraight(0.5);
                } else if (autoRuntime.time() < 1_500) {
                    moveStop();
                } else {
                    autoRuntime.reset();
                    state++;
                }
                break;

            case 4: // DRIVE BKWD FROM GAME ELEMENT
                if (autoRuntime.time() < 400) {
                    moveStraight(-0.5);
                } else if (autoRuntime.time() < 900) {
                    moveStop();
                } else {
                    headingController.setDesired(100.0d);
                    autoRuntime.reset();
                    state++;
                }
                break;

            case 5: // TURN LEFT
                if (headingController.getError() != 0.0d) {
                    turn(headingController.getControlValue());
                } else {
                    moveStop();
                    autoRuntime.reset();
                    state++;
                }
                break;

            case 6: // DRIVE FWD TO SIDE WALL
                if (autoRuntime.time() < 1_200) {
                    moveStraight(0.45d);
                } else if (autoRuntime.time() < 1_700) {
                    moveStop();
                } else {
                    headingController.setDesired(167.0d);
                    autoRuntime.reset();
                    state++;
                }
                break;

            case 7: // TURN TOWARDS DEPOT
                if (headingController.getError() != 0.0d) {
                    turn(headingController.getControlValue());
                } else {
                    moveStop();
                    autoRuntime.reset();
                    state++;
                }
                break;

            case 8: // DRIVE FWD TO DEPOT & EJECT
                if (autoRuntime.time() < 500) {
                    moveStraight(0.5);
                    if (autoRuntime.time() > 300) {
                        lowerContainer();
                        intakeOut();
                    }
                } else if (autoRuntime.time() < 1_000) {
                    moveStop();
                } else {
                    autoRuntime.reset();
                    state++;
                }
                break;

            case 9: // MOVE BKWD & EJECT
                if (autoRuntime.time() < 500) {
                    moveStraight(-0.5d);
                } else if (autoRuntime.time() < 1_000) {
                    moveStop();
                } else {
                    autoRuntime.reset();
                    state++;
                }
                break;

            case 10: // STOP & EJECT
                if (autoRuntime.time() < 1_500) {
                    intakeOut();
                } else {
                    intakeStop();
                    autoRuntime.reset();
                    state++;
                }
                break;

//            case 11:
//                autoRuntime.reset();
//                lowerContainer();
//                state++;
//                break;
//
//            case 12:
//                if (autoRuntime.time() < 200) {
//
//                } else  if(autoRuntime.time() < 700){
//
//                } else {
//                    state++;
//                }
//                break;

            case 99:
                stop();
                break;

            default:
                state++;
                break;
        }

        // INIT_DEPOSIT and DEPOSITING:
        //   Need to open the container while moving, then brake so that
        //   the element in the container is forced into the outtake wheel.


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
//                                    state = GOLD_LEFT;
//                                } else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X) {
//                                    autoRuntime.reset();
//                                    state = GOLD_RIGHT;
//                                } else {
//                                    autoRuntime.reset();
//                                    state = GOLD_CENTER;
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

