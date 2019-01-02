package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * ON DEPOT SIDE:
 * hit element near depot, place marker, turn left to opposite crater, park in crater if time permits
 */
@Autonomous(name = "AutonomousDepotSideOp", group = "AAAAAARP")
public class AutonomousDepotSideOp extends AutonomousBaseOp implements GameConstants {

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
                state = State.STOP;
                break;

            case STOP:
                stop();
                break;

        }
    }
}
