package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import static org.firstinspires.ftc.teamcode.State.EXAMPLE_TURN;

/**
 * ON CRATER SIDE:
 * hit element near crater, turn left to depot, place marker, park in crater if time permits
 */
@Autonomous(name = "AutonomousCraterSideOp", group = "Concept")
@Disabled
public class AutonomousTurnTestOp extends AutonomousBaseOp implements GameConstants {
    RoboAction script;

    public RoboAction buildScript() {
        return RoboAction.init()
            .then(
                () -> headingController.setDesired(-45.0f)

            ).thenDoUntil(
                () -> {
                    headingController.update();
                    turn(headingController.getControlValue());
                }, () -> headingController.getError() == 0.0f

            ).thenDoForDuration(2.5, () -> moveStraight(-0.4)

            ).then(
                () -> headingController.setDesired(45.0f)

            ).thenDoUntil(
                () -> {
                    headingController.update();
                    turn(headingController.getControlValue());
                }, () -> headingController.getError() == 0.0f

            );
    }

    @Override
    public void init() {
        super.init();
        state = EXAMPLE_TURN;
    }

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
                if (autoRuntime.time() < 1000) {
                    moveStraight(0.5);
                } else {
                    moveStop();
                }
                break;

            case EXAMPLE_TURN:
                headingController.setDesired(-45.0);
                turn(headingController.getControlValue());
                if (headingController.getError() == 0.0) {
                    state = State.STOP;
                }
                break;

            case STOP:
                stop();
                break;

        }
    }
}
