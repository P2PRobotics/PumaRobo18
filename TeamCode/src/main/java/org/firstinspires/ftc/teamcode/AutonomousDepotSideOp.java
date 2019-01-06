package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * ON DEPOT SIDE:
 * hit element near depot, place marker, turn left to opposite crater, park in crater if time permits
 */
@Autonomous(name = "AutonomousDepotSideOp", group = "Competition")
public class AutonomousDepotSideOp extends AutonomousBaseOp implements GameConstants {
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

            case 3: // DRIVE FWD TO DEPOT & EJECT
                if (autoRuntime.time() < 1_000) {
                    moveStraight(0.5);
                    if (autoRuntime.time() > 600) {
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

            case 4: // MOVE BKWD & EJECT
                if (autoRuntime.time() < 500) {
                    moveStraight(-0.5d);
                } else if (autoRuntime.time() < 1_000) {
                    moveStop();
                } else {
                    autoRuntime.reset();
                    state++;
                }
                break;

            case 5: // STOP & EJECT
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
