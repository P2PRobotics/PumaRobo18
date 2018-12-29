package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

public class ClampingDcMotor extends DelegatingDcMotor {
    public ClampingDcMotor(DcMotor delegate) {
        super(delegate);
    }

    private double desiredPower;

    @Override
    public void setPower(double power) {
        desiredPower = power;
        super.setPower(clamp(power));
    }

    @Override
    public double getPower() {
        return desiredPower;
    }

    /**
     * Clamps a requested power to the operating range of our motors.
     * <pre>{@code
     *   out of range |-1.0        -0.2| dead zone |0.2          1.0| out of range
     * <--------------|----------------|-----|-----|----------------|-------------->
     * }</pre>
     *
     * @param power any desired power value
     * @return a number in the range [-1.0..-0.2, 0.0, 0.2..1.0]
     */
    static double clamp(double power) {
        if (power < -1.0) return -1.0;
        else if (power > 1.0) return 1.0;
        else if (power < 0.2 && power > 0) return 0.2;
        else if (power > -0.2 && power < 0) return -0.2;
        else return power;
    }
}
