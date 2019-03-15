package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.pidcontrol.AngularPController;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

import static java.lang.String.format;
import static org.firstinspires.ftc.teamcode.RoboAction.initWithTicker;

public class RoboActionTest {
    void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void turn(double power) {
        System.out.println(format("turn(%.2f)", power));
    }

    AtomicLong time = new AtomicLong();
    Supplier<Long> ticker = time::get;

    AtomicInteger absolute = new AtomicInteger(0);

    AngularPController heading = new AngularPController(
        () -> (double) absolute.getAndAdd((int) Math.signum(turn.get())),
        2.0d,
        1.0d,
        0.2d
    );


    static RoboAction turnBy(double delta, long timeout) {
        RoboAction
            .init(() -> {
                double current = heading.update();
                heading.setDesired(current + delta)
            }
            .loop(() -> {
                heading.update();
                turn(heading.getControlValue());
            })
            .duration(timeout)
            .until(() -> heading.getError() == 0.0f)
    }

    static RoboAction turnTo(double desired, long timeout) {
        RoboAction
            .startsWith(() -> heading.setDesired(desired))
            .loop(() -> {
                heading.update();
                turn(heading.getControlValue());
            })
            .duration(timeout)
            .until(() -> heading.getError() == 0.0f)
    }

    @Test
    public void helloWorld() {
        AtomicBoolean fin = new AtomicBoolean(false);

        RoboAction script = initWithTicker(ticker)
            .then(() -> heading.calibrateTo(45.0))
            .then(turnTo(-15.0))
            .then(drive(0.35, 2_500))
            .then(stop(500))

            .thenDoUntil(
                () -> {
                    heading.update();
                    System.out.printf("turn(%.2f)\n", heading.getControlValue());
                    turn.set((int) (heading.getControlValue() * 1_000));
                },
                () -> heading.getError() == 0.0d
            )

            .then(() -> System.out.println("moveStop()"))

            .thenDoForDuration(2.5,
                () -> System.out.println("move(0.5)")
            )

            .then(
                () -> {
                    System.out.println("moveStop()");
                    heading.setDesired(+15.0);
                })

            .thenDoUntil(
                () -> {
                    heading.update();
                    System.out.printf("turn(%.2f)\n", heading.getControlValue());
                    turn.set((int) (heading.getControlValue() * 1_000_000));
                },
                () -> heading.getError() == 0.0d
            )

            .then(() -> System.out.println("moveStop()"));

        RoboAction current = script;

        do {
            current = current.run(); // move current pointer into RoboAction
            sleep(100);
        } while (time.getAndAdd(200L) < 10_000);
    }
}