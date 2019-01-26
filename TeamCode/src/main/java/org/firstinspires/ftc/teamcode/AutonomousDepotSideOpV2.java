package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * ON DEPOT SIDE:
 * hit element near depot, place marker, turn left to opposite crater, park in crater if time permits
 */
@Autonomous(name = "AutonomousDepotSideOpV2", group = "Competition")
public class AutonomousDepotSideOpV2 extends AutonomousBaseOp implements GameConstants {
    int goldDriveTime = 550;
    double lastOrientation = 45.0;

    @Override
    public void loop() {
        super.loop();

        telemetry.addData("State: ", state);
        telemetry.addData("Gold: ", goldElement);
        telemetry.addData("GoldDriveTime: ", goldDriveTime);

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
                            headingController.setDesired(-80.0d);
                            goldDriveTime = 1700;
                            break;

                        case 2: //HIT MIDDLE ELEMENT WITH RIGHT WHEEL BY SPINNING
                            headingController.setDesired(-135.0d);
                            goldDriveTime = 1900;
                            break;

                        case 3: //HIT RIGHT ELEMENT WITH LEFT WHEEL
                            headingController.setDesired(-170.0d);
                            goldDriveTime = 1700;
                            break;
                    }
                    autoRuntime.reset();
                    state++;
                }
                break;

            case 3: //TURN TOWARD GOLD ELEMENT
                if (headingController.getError() != 0.0d) {
                    turn(headingController.getControlValue());
                } else {
                    moveStop();
                    autoRuntime.reset();
                    state++;
                }
                break;

            case 4: // DRIVE BKWD TO GOLD ELEMENT
                if (autoRuntime.time() < goldDriveTime) {
                    moveStraight(-0.35);
                } else if (autoRuntime.time() < goldDriveTime) {
                    moveStop();
                } else {
                    autoRuntime.reset();
                    state=9;

                }
                break;


            case 9: // DRIVE FWD FROM GOLD ELEMENT
                if (autoRuntime.time() < goldDriveTime) {
                    moveStraight(0.35);
                } else if (autoRuntime.time() < goldDriveTime + 1_000) {
                    moveStop();
                } else {
                    headingController.setDesired(-57.0d);
                    autoRuntime.reset();
                    state++;
                }
                break;

            case 10: // TURN RIGHT
                if (headingController.getError() != 0.0d) {
                    turn(headingController.getControlValue());
                } else {
                    moveStop();
                    autoRuntime.reset();
                    state++;
                }
                break;

            case 11: // DRIVE BKWD TO SIDE WALL
                if (autoRuntime.time() < 1_750) {
                    moveStraight(-0.45d);
                } else if (autoRuntime.time() < 2_100) {
                    moveStop();
                } else {
                    headingController.setDesired(7.0d);
                    autoRuntime.reset();
                    state++;
                }
                break;

            case 12: // TURN TOWARDS DEPOT
                if (headingController.getError() != 0.0d) {
                    turn(headingController.getControlValue());
                } else {
                    moveStop();
                    autoRuntime.reset();
                    state++;
                }
                break;

            case 13: // DRIVE FWD TO DEPOT & EJECT
                if (autoRuntime.time() < 1_650) {
                    moveStraight(0.5);
                    if (autoRuntime.time() > 1_550) {
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

            case 14: // MOVE BKWD & EJECT
                if (autoRuntime.time() < 500) {
                    moveStraight(-0.5d);
                } else if (autoRuntime.time() < 1_000) {
                    moveStop();
                } else {
                    autoRuntime.reset();
                    state++;
                }
                break;

            case 15: // STOP & EJECT
                if (autoRuntime.time() < 1_500) {
                    intakeOut();
                } else {
                    intakeStop();
                    raiseContainer();
                    headingController.setDesired(-5.0d);
                    autoRuntime.reset();
                    state++;
                }
                break;

            case 16: // TURN TOWARDS CRATER
                if (headingController.getError() != 0.0d) {
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
                } else if (autoRuntime.time() < 2_100) {
                    moveStop();
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
