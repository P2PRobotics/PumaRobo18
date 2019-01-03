package org.firstinspires.ftc.teamcode.pidcontrol;

import java.util.function.Supplier;

import static java.lang.Double.NaN;
import static java.lang.Math.signum;

/**
 * Proportional Controller that reads angular input values between -180.0..180.0
 * degrees, and produces control values bounded by [-1.0..-clamp, 0.0, clamp..1.0, NaN].
 * <p>
 * This controller supports:
 * <ul>
 * <li>any supplier of an absolute angular input value, e.g. an IMU's orientation
 * sensor</li>
 * <li>runtime recalibration to a reference angle supplied by any external source,
 * e.g. Vuforia</li>
 * <li>error term tolerance, under which the control value produced is always 0.0f</li>
 * <li>control value gain</li>
 * <li>control value clamping [-1.0..-clamp, 0.0, clamp..1.0]</li>
 * </ul>
 * <p>
 * Example usage:
 * <pre>
 *  void init() {
 *    imu = initImu();
 *    heading = new AngularPController(
 *         // read the heading angle from the imu
 *         () -> imu.getAngularOrientation().firstAngle,
 *         1.0f, // tolerance
 *         3.0f, // gain
 *         0.1f  // clamp
 *    );
 *
 *    // known starting position of North-East at power-up
 *    heading.calibrateTo(-45.0f);
 *  }
 *
 *  void loop() {
 *    double h;
 *    // updated heading after reading from the supplier (may take time)
 *    h = heading.update();
 *
 *    // last known heading, does not read from the supplier (fast)
 *    h = heading.getCurrent();
 *
 *    // we want to turn until facing due West
 *    heading.setDesired(90.0);
 *
 *    // turning rate of either 0.0f or NaN means stop turning
 *    robot.setTurnRate(heading.getControlValue());
 *
 *    ...
 *
 *    // We don't have a desired heading right now so let
 *    // getControlValue() be NaN.
 *    heading.setDesired(Math.NaN);
 *  }
 * </pre>
 * <p>
 * Great care was taken to make this class have no dependencies aside from
 * the Java standard library. This makes the class easily testable, and
 * and easily embeddable into a simulation environment.
 */
public class AngularPController {
    private double zero;
    private double lastAbsolute;

    private double desired = NaN;

    private final Supplier<Double> absolute;
    private final double tolerance;
    private final double gain;
    private final double clamp;

    /**
     * Constructs a new [AngularPController] given a supplier of an absolute
     * angle, an error tolerance, a gain value, and a clamp value.
     *
     * @param absolute  Supplier of an absolute angle reading, e.g. from an IMU.
     *                  example: `() -> imu.getAngularOrientation().firstAngle`.
     *                  This argument is provided as a Supplier so as to keep
     *                  this class from depending on any specific hardware
     *                  device or simulation environment.
     * @param tolerance Minimum absolute value of a non-zero error term. Actual
     *                  errors with smaller absolute values are returned as an
     *                  error of 0.0f.
     * @param gain      Multiplier applied to the control value before clamping.
     *                  180.0/gain == minimum error value that produces maximum
     *                  control value.
     *                  example: gain == 3.0 produces 1.0 control value for all
     *                  error values > 60.0 degrees.
     * @param clamp     Minimum absolute value of the control value. Without a
     *                  clamp, a proportional controller may never reach its
     *                  target as the error value asymptotically approaches but
     *                  never reaches zero.
     *                  Example: when the control value is used as a turning
     *                  rate, clamp == 0.1f means the control value will never
     *                  command a turn slower than 10% max rate.
     */
    public AngularPController(
        Supplier<Double> absolute,
        double tolerance,
        double gain,
        double clamp
    ) {
        this.absolute = absolute;
        this.tolerance = tolerance;
        this.gain = gain;
        this.clamp = clamp;
    }

    /**
     * Adjusts the controller's current value based on a provided
     * reference angle.
     *
     * @param reference angle in degrees between [-1.0..1.0]
     */
    public void calibrateTo(double reference) {
        checkAngleArgument(reference);
        this.zero = subtractAngle(readAbsolute(), reference);
    }

    /**
     * Sets the desired angle. When set to an angle, calls to
     * [AngularPController#getControlValue()] will provide a
     * value. When set to NaN, [AngularPController#getControlValue()]
     * will also provide NaN.
     *
     * @param desired angle in degrees within [-180.0..180.0] or NaN.
     */
    public void setDesired(double desired) {
        checkAngleArgumentOrNaN(desired);
        this.desired = desired;
    }

    /**
     * Retrieves the controller's desired angle, or NaN if
     * no desired angle is requested.
     *
     * @return angle in degrees within [-180.0..180.0] or NaN
     */
    public double getDesired() {
        return desired;
    }

    /**
     * Reads from the absolute angle provider, and returns the
     * calibration-adjusted current angle. This method must be
     * called in order for the controller to update its state.
     * e.g. call from within a main loop().
     *
     * @return a value in degrees within [-180.0..180.0].
     * @see AngularPController#getCurrent() ()
     * @see AngularPController#calibrateTo(double)
     */
    public double update() {
        readAbsolute();
        return getCurrent();
    }

    /**
     * The controller's main output, value proportional to the error term,
     * multiplied by the controller's gain and then clampped, or NaN if the
     * desired angle is also NaN.
     *
     * @return a value within [-1.0..-clamp, 0.0, clamp..1.0, NaN]
     */
    public double getControlValue() {
        return calcControlValue(getProportion(), gain, clamp, 1.0f);
    }

    /**
     * The last known angle, as adjusted by calibration. Does
     * not read from the absolute angle provider.
     *
     * @return last known angle in degrees between
     * [-180.0..180.0], as adjusted by calibration.
     * @see AngularPController#update()
     * @see AngularPController#calibrateTo(double)
     */
    public double getCurrent() {
        return subtractAngle(getAbsolute(), getZero());
    }

    /**
     * Returns the controller's error term - the difference between the
     * desired and current angles, or 0.0f if the difference is less than
     * the controller's error tolerance. Does not read from the absolute
     * angle provider.
     *
     * @return A value in degrees [-180.0..-tolerance, 0.0, tolerance..180.0]
     * @see AngularPController#update()
     */
    public double getError() {
        return calcAngularError(getDesired(), getCurrent(), tolerance);
    }

    /**
     * The pure proportion of the error term to 180.0.
     *
     * @return A value between [-1.0..1.0].
     */
    public double getProportion() {
        return calcAngularProportion(getError());
    }

    /**
     * The offset in degrees from the absolute provider's zero angle to
     * the controller's calibrated zero angle.
     *
     * @return a value within[-180.0..180.0]
     */
    public double getZero() {
        return zero;
    }

    /**
     * The last known absolute angle as read from the absolute provider.
     *
     * @return a value within[-180.0..180.0]
     */
    public double getAbsolute() {
        return lastAbsolute;
    }

    private double readAbsolute() {
        this.lastAbsolute = absolute.get();
        checkAngleArgument(this.lastAbsolute);
        return this.lastAbsolute;
    }

    private static void checkAngleArgumentOrNaN(double given) {
        if (NaN == given) return;
        checkAngleArgument(given);
    }

    private static void checkAngleArgument(double given) {
        if (given < -180.0f || given > 180.0f)
            throw new IllegalArgumentException(
                "Angle value must be between -180.0..180.0 degrees!"
            );
    }

    // ----------------------------------------------------------------------
    // Below here are the main algorithmic functions used by the controller.
    //   - package-visible to allow for unit-testing the algorithms
    //   - static since they are stateless logic only

    public static double addAngle(double a, double b) {
        double tmp = (a + b + 180.0f) % 360.0f;
        if (tmp < 0.0f) tmp = 360.0f + tmp;
        return tmp - 180.0f;
    }

    public static double subtractAngle(double a, double b) {
        return addAngle(a, -b);
    }

    static double calcAngularError(double desired, double current, double tolerance) {
        if (NaN == desired) return NaN;

        double err = subtractAngle(desired, current);
        return (Math.abs(err) < Math.abs(tolerance)) ? 0.0f : err;
    }

    static double calcAngularProportion(double err) {
        return (NaN == err) ? NaN : err / 180.0f;
    }

    static double calcControlValue(double proportion, double gain, double min, double max) {
        if (NaN == proportion) return NaN;
        if (0.0f == proportion) return 0.0f;

        final double propGain = proportion * gain;

        if (propGain < -max) return -max;
        else if (propGain > max) return max;
        else if (propGain > -min && propGain < min) return min * signum(propGain);
        else return propGain;
    }

}
