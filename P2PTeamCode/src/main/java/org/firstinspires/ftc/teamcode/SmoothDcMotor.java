package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.function.Supplier;

public class SmoothDcMotor extends DcMotorDelegate {
    private final SmoothDouble smoothSpeed;

    public SmoothDcMotor(DcMotor motor, double maxDeltaPerSecond) {
        this(motor, maxDeltaPerSecond, System::nanoTime);
    }

    protected SmoothDcMotor(
        DcMotor motor,
        double maxDeltaPerSecond,
        Supplier<Long> timeSupplier
    ) {
        super(motor);
        this.smoothSpeed = new SmoothDouble(motor.getPower(), maxDeltaPerSecond, timeSupplier);
    }

    @Override
    public double getPower() {
        return smoothSpeed.getDesiredValue();
    }

    @Override
    public void setPower(double power) {
        smoothSpeed.setDesiredValue(power);
        super.setPower(smoothSpeed.update());
    }

}

