package org.firstinspires.ftc.teamcode;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

public class RoboFutureTest {
    AtomicInteger heading = new AtomicInteger(0);
    Map<Predicate, CompletableFuture> futures = new ConcurrentHashMap<>();

    <T> CompletableFuture<T> when(Predicate<T> predicate) {
        return futures.computeIfAbsent(predicate, (s) -> new CompletableFuture<T>());
    }


    public void update() {
        Integer current = heading.incrementAndGet();
        futures.entrySet().stream()
            .filter(e -> e.getKey().test(current))
            .map(e -> { e.getValue().complete(current); return e;} )
            .forEach(e -> futures.remove(e.getKey()));
    }

    @Before
    public void setup() {
        this.heading = new AtomicInteger(0);
        this.futures = new ConcurrentHashMap<>();
    }

    @Test
    public void test() {
        AtomicInteger imu = new AtomicInteger(1);

        CompletableFuture f =
            when((Predicate<Integer>) hdg -> hdg > 0)
                .thenCompose(hh -> {
                        System.out.println("blort:");
                        return when((Predicate<Integer>) hdg -> hdg > 10);
                    }
                )
                .thenCompose(hh -> {
                        System.out.println("blat");
                        return when((Predicate<Integer>) hdg -> hdg > 20);
                    }
                )
            .thenRun(() -> System.out.println("done"));

        for (int i = 0; i < 10; i++) {
            update();
        }

        System.out.println("done? : " + f.isDone());
    }
}
