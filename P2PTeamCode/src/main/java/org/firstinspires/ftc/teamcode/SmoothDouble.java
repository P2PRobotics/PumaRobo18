package org.firstinspires.ftc.teamcode;

import java.util.function.Supplier;

import static java.lang.Math.abs;
import static java.lang.Math.min;
import static java.lang.Math.signum;

class SmoothDouble {
    private final Supplier<Long> timeSupplier;
    private final double maxDeltaPerSecond;
    private long lastUpdatedTime;

    private double currentValue;
    private double desiredValue;

    SmoothDouble(double currentValue, double maxDeltaPerSecond) {
        this(currentValue, maxDeltaPerSecond, System::nanoTime);
    }

    SmoothDouble(double currentValue, double maxDeltaPerSecond, Supplier<Long> timeSupplier) {
        this.currentValue = currentValue;
        this.maxDeltaPerSecond = maxDeltaPerSecond;
        this.timeSupplier = timeSupplier;
        this.lastUpdatedTime = timeSupplier.get();
    }

    public double getCurrentValue() {
        return currentValue;
    }

    public double getDesiredValue() {
        return desiredValue;
    }

    public void setDesiredValue(double desiredValue) {
        this.desiredValue = desiredValue;
    }

    public double update() {
        long currentTime = timeSupplier.get();
        this.currentValue = calculateCurrentValue(
            currentValue,
            desiredValue,
            currentTime,
            this.lastUpdatedTime,
            maxDeltaPerSecond
        );
        this.lastUpdatedTime = currentTime;
        return getCurrentValue();
    }

    static double calculateCurrentValue(
        double currentValue,
        double desiredValue,
        long currentTime,
        long lastUpdateTime,
        double maxDeltaPerSecond
    ) {
        double deltaTime = (currentTime - lastUpdateTime) / 1.0E9D;
        double deltaValue = desiredValue - currentValue;
        double scaledMaxDeltaValue = signum(deltaValue) * abs(maxDeltaPerSecond) * deltaTime;
        return currentValue + minabs(deltaValue, scaledMaxDeltaValue);
    }

    static double minabs(double a, double b) {
        return abs(a) < abs(b) ? a : b;
    }
}
