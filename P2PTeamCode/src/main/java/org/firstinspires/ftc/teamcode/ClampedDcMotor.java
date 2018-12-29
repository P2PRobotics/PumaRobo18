package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import static java.lang.Math.abs;
import static java.lang.Math.signum;

public class ClampedDcMotor extends DcMotorDelegate {
    private final double minPower;
    private final double maxPower;
    private double desiredPower;

    protected ClampedDcMotor(DcMotor motor, double minPower, double maxPower) {
        super(motor);
        this.minPower = minPower;
        this.maxPower = maxPower;
        this.desiredPower = motor.getPower();
    }

    @Override
    public void setPower(double power) {
        this.desiredPower = power;
        super.setPower(clamped(power, minPower, maxPower));
    }

    @Override
    public double getPower() {
        return this.desiredPower;
    }

    static double clamped(double power, double min, double max) {
        if (0.0 == power) return power;
        else if (abs(power) < abs(min)) return signum(power) * abs(min);
        else if (abs(power) > abs(max)) return signum(power) * abs(max);
        else return power;
    }
}
