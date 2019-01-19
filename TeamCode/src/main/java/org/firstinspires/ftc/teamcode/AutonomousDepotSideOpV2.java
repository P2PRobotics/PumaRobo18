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
    double goldSpin = 35.0;

    @Override
    public void loop() {
        super.loop();

        telemetry.addData("State: ", state);
        telemetry.addData("Gold: ", goldElement);
        telemetry.addData("GoldDriveTime: ", goldDriveTime);
        telemetry.addData("GoldSpin: ", goldSpin);

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
                } else if (autoRuntime.time() < 3_500) {
                    latchStop();
                    moveStraight(0.25);
                } else if (autoRuntime.time() < 4_200) {
                    moveStop();
                    headingController.setDesired(180d);
                    autoRuntime.reset();
                    state++;
                }
                break;

            case 3: //TURN AROUND
                if (headingController.getError() != 0.0d) {
                    turn(headingController.getControlValue());
                } else {
                    moveStop();
                    autoRuntime.reset();
                    state++;
                }
                break;

            case 4: // CALLIBRATE ANGLE
                headingController.calibrateTo(42.5d);
                switch (goldElement) {
                    case 1: //HIT LEFT ELEMENT WITH RIGHT WHEEL
                        headingController.setDesired(-2.0d);
                        goldDriveTime = 850;
                        goldSpin = 0.0;
                        break;

                    case 2: //HIT MIDDLE ELEMENT WITH RIGHT WHEEL BY SPINNING
                        headingController.setDesired(headingController.update());
                        goldDriveTime = 450;
                        goldSpin = 40.0;
                        break;

                    case 3: //HIT RIGHT ELEMENT WITH LEFT WHEEL
                        headingController.setDesired(80.0d);
                        goldDriveTime = 850;
                        goldSpin = -0.0;
                        break;
                }


            case 5: // DRIVE FWD TO GOLD ELEMENT
                if (autoRuntime.time() < goldDriveTime) {
                    moveStraight(-0.35);
                } else if (autoRuntime.time() < goldDriveTime + 500) {
                    moveStop();
                } else {
                    autoRuntime.reset();
                    state++;
                }
                break;

            case 6: // STORE CURRENT HEADING
                lastOrientation = headingController.update();
                headingController.setDesired(lastOrientation + goldSpin);
                autoRuntime.reset();
                state++;
                break;

            case 7: // SPIN OVER THE GOLD ELEMENT
                if (headingController.getError() != 0.0d) {
                    turn(headingController.getControlValue());
                } else {
                    moveStop();
                    autoRuntime.reset();
                    state++;
                }
                break;

            case 8: // SPIN BACK
                headingController.setDesired(lastOrientation);
                autoRuntime.reset();
                state++;
                break;

            case 9: // SPIN BACK
                if (headingController.getError() != 0.0d) {
                    turn(headingController.getControlValue());
                } else {
                    moveStop();
                    autoRuntime.reset();
                    state++;
                }
                break;

            case 10: // DRIVE BKWD FROM GOLD ELEMENT
                if (autoRuntime.time() < goldDriveTime) {
                    moveStraight(-0.35);
                } else if (autoRuntime.time() < goldDriveTime + 500) {
                    moveStop();
                } else {
                    headingController.setDesired(135.0d); //?
                    autoRuntime.reset();
                    state++;
                }
                break;

            case 11: // TURN RIGHT
                if (headingController.getError() != 0.0d) {
                    turn(headingController.getControlValue());
                } else {
                    moveStop();
                    autoRuntime.reset();
                    state++;
                }
                break;

            case 12: // DRIVE BKWD TO SIDE WALL
                if (autoRuntime.time() < 1_600) {
                    moveStraight(-0.45d);
                } else if (autoRuntime.time() < 2_100) {
                    moveStop();
                } else {
                    headingController.setDesired(-5.0d);
                    autoRuntime.reset();
                    state++;
                }
                break;

            case 13: // TURN TOWARDS DEPOT
                if (headingController.getError() != 0.0d) {
                    turn(headingController.getControlValue());
                } else {
                    moveStop();
                    autoRuntime.reset();
                    state++;
                }
                break;

            case 14: // DRIVE FWD TO DEPOT & EJECT
                if (autoRuntime.time() < 400) {
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

            case 15: // MOVE BKWD & EJECT
                if (autoRuntime.time() < 500) {
                    moveStraight(-0.5d);
                } else if (autoRuntime.time() < 1_000) {
                    moveStop();
                } else {
                    autoRuntime.reset();
                    state++;
                }
                break;

            case 16: // STOP & EJECT
                if (autoRuntime.time() < 1_500) {
                    intakeOut();
                } else {
                    intakeStop();
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
