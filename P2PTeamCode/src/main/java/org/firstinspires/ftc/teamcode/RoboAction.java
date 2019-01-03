package org.firstinspires.ftc.teamcode;

import java.util.function.Predicate;
import java.util.function.Supplier;

class RoboAction {
    private final Supplier<Long> ticker;
    private final Predicate<RoboAction> predicate;
    private final Runnable run;
    private long initNanos;
    private RoboAction next;
    private RoboAction tail;
    private boolean initialized;
    private boolean completed;
    private long completedNanos;

    public static Predicate<RoboAction> ONCE = (unused) -> true;
    public static Predicate<RoboAction> FOREVER = (unused) -> false;
    public static Runnable DO_NOTHING = () -> {
    };

    private RoboAction(
        Supplier<Long> ticker,
        Predicate<RoboAction> predicate,
        Runnable run
    ) {
        this(ticker, predicate, run, null);
    }

    private RoboAction(
        Supplier<Long> ticker,
        Predicate<RoboAction> predicate,
        Runnable run,
        RoboAction next
    ) {
        this.ticker = ticker;
        this.predicate = predicate;
        this.run = run;
        this.next = next;
        this.tail = this;
    }

    public long getRuntimeNanos() {
        long now = ticker.get();
        long start = initialized ? initNanos : now;
        long end = completed ? completedNanos : now;
        return end - start;
    }

    public long getRuntimeMillis() {
        return getRuntimeNanos() / 1_000;
    }

    public long getRuntimeSecs() {
        return getRuntimeMillis() / 1_000;
    }

    public double runtime() {
        return getRuntimeSecs();
    }

    public RoboAction run() {
        if (completed) throw new IllegalStateException("run() called after completed");

        if (!initialized) {
            this.initialized = true;
            this.initNanos = ticker.get();
        }

        this.run.run();

        if (predicate.test(this)) {
            this.completed = true;
            this.completedNanos = ticker.get();
            return this.next == null ? nil(this.ticker) : this.next;
        } else {
            return this;
        }
    }

    public static RoboAction init() {
        return new RoboAction(System::nanoTime, ONCE, DO_NOTHING);
    }

    static RoboAction initWithTicker(Supplier<Long> ticker) {
        return new RoboAction(ticker, ONCE, DO_NOTHING);
    }

    public static RoboAction nil(Supplier<Long> ticker) {
        return new RoboAction(ticker, FOREVER, DO_NOTHING);
    }

    public RoboAction thenUntil(Predicate<RoboAction> until, Runnable run) {
        RoboAction next = new RoboAction(ticker, until, run);
        this.tail.next = next;
        this.tail = next;
        return this;
    }

    public RoboAction thenUntil(Supplier<Boolean> until, Runnable run) {
        return thenUntil((t) -> until.get(), run);
    }

    public RoboAction then(Runnable run) {
        return thenUntil((t) -> true, run);
    }

    public RoboAction thenForDuration(double seconds, Runnable run) {
        return thenUntil((t) -> t.runtime() >= seconds, run);
    }
}
