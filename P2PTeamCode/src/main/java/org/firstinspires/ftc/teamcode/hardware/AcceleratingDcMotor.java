package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;

import static java.lang.Math.abs;
import static java.lang.Math.signum;

public class AcceleratingDcMotor extends DelegatingDcMotor {
    public AcceleratingDcMotor(DcMotor delegate) {
        super(delegate);
        lastNanos = System.nanoTime();
        desiredPower = super.getPower();

    }

    private double desiredPower;
    final double LIMIT = 0.000003;
    double lastNanos;

    @Override
    public void setPower(double power) {
        desiredPower = power;
        update();
    }

    @Override
    public double getPower() {
        return desiredPower;
    }

    public void update() {
        double currentPower = super.getPower();
        double deltaPower = desiredPower - currentPower;
        double currentTime = System.nanoTime();
        double deltaTime = (currentTime - lastNanos) / 1000.0D;
        double maxIncrement = deltaTime * LIMIT * signum(deltaPower);
        double actualIncrement = minabs(deltaPower, maxIncrement);
        lastNanos = currentTime;
        super.setPower(currentPower + actualIncrement);
    }

    public static double minabs(double a, double b) {
        return (abs(a) < abs(b)) ? a : b;
    }
}
