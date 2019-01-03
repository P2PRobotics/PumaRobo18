package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.pidcontrol.AngularPController;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

import static org.firstinspires.ftc.teamcode.RoboAction.initWithTicker;

public class RoboActionTest {
    void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void helloWorld() {
        AtomicBoolean fin = new AtomicBoolean(false);
        AtomicLong time = new AtomicLong();
        Supplier<Long> ticker = time::get;

        AtomicInteger absolute = new AtomicInteger(0);
        AtomicInteger turn = new AtomicInteger(0);
        AngularPController heading = new AngularPController(
            () -> (double) absolute.getAndAdd((int) Math.signum(turn.get())),
            2.0d,
            1.0d,
            0.2d
        );

        RoboAction script = initWithTicker(ticker)
            .then(
                () -> heading.setDesired(-15.0)
            )

            .thenUntil(
                () -> heading.getError() == 0.0d,
                () -> {
                    heading.update();
                    System.out.printf("turn(%.2f)\n", heading.getControlValue());
                    turn.set((int) (heading.getControlValue() * 1_000_000));
                }
            )

            .then(
                () -> System.out.println("moveStop()")
            )

            .thenForDuration(2.5,
                () -> System.out.println("move(0.5)"))

            .then(
                () -> {
                    System.out.println("moveStop()");
                    heading.setDesired(+15.0);
                })

            .thenUntil(
                () -> heading.getError() == 0.0d,
                () -> {
                    heading.update();
                    System.out.printf("turn(%.2f)\n", heading.getControlValue());
                    turn.set((int) (heading.getControlValue() * 1_000_000));
                }
            )

            .then(() -> System.out.println("moveStop()"));

        RoboAction current = script;

        do {
            current = current.run();
            sleep(100);
        } while (
            time.getAndAdd(200_000L) < 10_000_000
                &&
                !fin.get()
            );

    }
}