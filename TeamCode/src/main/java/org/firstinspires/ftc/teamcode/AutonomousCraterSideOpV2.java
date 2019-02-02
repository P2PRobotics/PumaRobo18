package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * ON DEPOT SIDE:
 * hit element near depot, place marker, turn left to opposite crater, park in crater if time permits
 */
@Autonomous(name = "AutonomousCraterSideOpV2", group = "Competition")
public class AutonomousCraterSideOpV2 extends AutonomousBaseOp implements GameConstants {
    int goldDriveTime = 570;
    double lastOrientation = 45.0;

    @Override
    public void loop() {
        super.loop();

        telemetry.addData("State: ", state);
        telemetry.addData("Gold: ", goldElement);
        telemetry.addData("GoldDriveTime: ", goldDriveTime);

        switch (state) {
            case 0: //ANNOUNCE MAKENNA AS CODEGOD
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

            case 2: // UNLATCH AND INCH FORWARD
                if (autoRuntime.time() < 2_500) {
                    latchOpen();
                } else if (autoRuntime.time() < 3_400) {
                    latchStop();
                    moveStraight(0.25);
                } else if (autoRuntime.time() < 4_700) {
                    moveStop();
                } else {
                    headingController.calibrateTo(42.5d);
                    // goldElement = 2; // HARD CODE Gold Element here for testing
                    switch (goldElement) {
                        case 1: //HIT LEFT ELEMENT WITH RIGHT WHEEL
                            headingController.setDesired(-100.0d);
                            goldDriveTime = 1250;
                            break;

                        case 2: //HIT MIDDLE ELEMENT WITH RIGHT WHEEL BY SPINNING
                            headingController.setDesired(-135.0d);
                            goldDriveTime = 1250;
                            break;

                        case 3: //HIT RIGHT ELEMENT WITH LEFT WHEEL
                            headingController.setDesired(180.0d);
                            goldDriveTime = 1200;
                            break;
                    }
                    autoRuntime.reset();
                    state+=2;
                }
                break;


//            case 3: // RAISE LIFT
//                if (autoRuntime.time() < 2_500) {
//                    lift(-0.5);
//                } else if (autoRuntime.time() < 3_000) {
//                    lift(0);
//                } else {
//                    autoRuntime.reset();
//                    state++;
//                }
//                break;

            case 4: //TURN TOWARD GOLD ELEMENT
                if (headingController.getError() != 0.0d) {
                    turn(headingController.getControlValue());
                } else {
                    moveStop();
                    autoRuntime.reset();
                    state++;
                }
                break;

            case 5: // DRIVE BKWD TO GOLD ELEMENT
                if (autoRuntime.time() < goldDriveTime) {
                    moveStraight(-0.35);
                } else if (autoRuntime.time() < goldDriveTime) {
                    moveStop();
                } else {
                    autoRuntime.reset();
                    state++;

                }
                break;


            case 6: // DRIVE FWD FROM GOLD ELEMENT
                if (autoRuntime.time() < goldDriveTime) {
                    moveStraight(0.35);
                } else if (autoRuntime.time() < goldDriveTime + 1_000) {
                    moveStop();
                } else {
                    headingController.setDesired(-75.0d);
                    autoRuntime.reset();
                    state++;
                }
                break;

            case 7: // TURN RIGHT
                if (headingController.getError() != 0.0d) {
                    turn(headingController.getControlValue());
                } else {
                    moveStop();
                    autoRuntime.reset();
                    state++;
                }
                break;

            case 8: // DRIVE BKWD TO SIDE WALL
                if (autoRuntime.time() < 1_750) {
                    moveStraight(-0.45d);
                } else if (autoRuntime.time() < 2_200) {
                    moveStop();
                } else {
                    headingController.setDesired(177.0d);
                    autoRuntime.reset();
                    state++;
                }
                break;

            case 9: // TURN TOWARDS DEPOT
                if (headingController.getError() != 0.0d && autoRuntime.time() < 4_000) {
                    turn(headingController.getControlValue());
                } else {
                    moveStop();
                    autoRuntime.reset();
                    state++;
                }
                break;

            case 10: // DRIVE FWD TO DEPOT & EJECT
                if (autoRuntime.time() < 1_350) {
                    moveStraight(0.5);
                    if (autoRuntime.time() > 1_250) {
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

            case 11: // MOVE BKWD WHILE EJECTING
                if (autoRuntime.time() < 500) {
                    moveStraight(-0.5d);
                } else if (autoRuntime.time() < 1_000) {
                    moveStop();
                } else {
                    autoRuntime.reset();
                    state++;
                }
                break;

            case 12: // STOP & EJECT
                if (autoRuntime.time() < 750) {
                    intakeOut();
                } else {
                    intakeStop();
                    raiseContainer();
                    headingController.setDesired(177.0d);
                    autoRuntime.reset();
                    state=17; //skips turn toward crater
                }
                break;

            case 13: // TURN TOWARDS CRATER
                if (headingController.getError() != 0.0d && autoRuntime.time() < 3_000) {
                    turn(headingController.getControlValue());
                } else {
                    moveStop();
                    autoRuntime.reset();
                    state++;
                }
                break;

            case 17: // DRIVE BKWD TO CRATER
                if (autoRuntime.time() < 2_000) {
                    moveStraight(-0.60d);
                    lift(-0.5);
                } else if (autoRuntime.time() < 2_100) {
                    moveStop();
                    lift(0);
                } else {
                    autoRuntime.reset();
                    state++;
                }
                break;

            case 99:
                stop();
                break;

            default:
                state++;
                break;
        }

    }

}
