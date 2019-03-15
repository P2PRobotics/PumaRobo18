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
        return initWithTicker(System::currentTimeMillis);
    }

    static RoboAction initWithTicker(Supplier<Long> ticker) {
        return new RoboAction(ticker, ONCE, DO_NOTHING);
    }

    public static RoboAction nil(Supplier<Long> ticker) {
        return new RoboAction(ticker, FOREVER, DO_NOTHING);
    }

    public RoboAction thenDoUntil(Runnable run, Predicate<RoboAction> until) {
        RoboAction next = new RoboAction(ticker, until, run);
        this.tail.next = next;
        this.tail = next;
        return this;
    }

    public RoboAction thenDoUntil(Runnable run, Supplier<Boolean> until) {
        return thenDoUntil(run, (t) -> until.get());
    }

    public RoboAction then(Runnable run) {
        return thenDoUntil(run, (t) -> true);
    }

    public RoboAction thenDoForDuration(double seconds, Runnable run) {
        return thenDoUntil(run, (t) -> t.runtime() >= seconds);
    }
}
