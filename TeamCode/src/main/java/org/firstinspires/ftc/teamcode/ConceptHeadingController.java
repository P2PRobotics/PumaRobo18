package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.pidcontrol.AngularPController;

import java.util.Locale;
import java.util.Random;

@Autonomous(name = "ConceptHeadingController", group = "Concept")
public class ConceptHeadingController extends OpMode {

    private BNO055IMU imu;
    private AngularPController heading;
    Random random = new Random(0xDEADBEEF);

    @Override
    public void init() {
        imu = initImu();
        heading = new AngularPController(
            () -> imu.getAngularOrientation().firstAngle,
            1.0f,
            3.0f,
            0.1f
        );
        heading.calibrateTo(0.0f);
        initHeadingTelemetry(heading, telemetry);
    }

    @Override
    public void init_loop() {
        super.init_loop();
        heading.update();
        telemetry.update();
    }

    @Override
    public void start() {
        super.start();
        heading.calibrateTo(-45.0f);
        heading.setDesired((random.nextFloat() * 360.0f) - 180.0f);
    }

    @Override
    public void loop() {
        heading.update();
        telemetry.update();
    }

    @Override
    public void stop() {
        super.stop();
    }


    private BNO055IMU initImu() {
        // Set up the parameters with which we will use our IMU. Note that integration
        // algorithm here just reports accelerations to the logcat log; it doesn't actually
        // provide positional information.
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        BNO055IMU imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
        return imu;
    }

    private static void initHeadingTelemetry(final AngularPController heading, final Telemetry telemetry) {
        telemetry.addLine("hdg ")
            .addData("z", () -> formatDegrees(heading.getZero()))
            .addData("a", () -> formatDegrees(heading.getAbsolute()))
            .addData("d", () -> formatDegrees(heading.getDesired()))
            .addData("c", () -> formatDegrees(heading.getCurrent()))
            .addData("e", () -> formatDegrees(heading.getError()))
            .addData("p", () -> format2f(heading.getProportion()))
            .addData("v", () -> format2f(heading.getControlValue()))
        ;
    }

    public static String formatDegrees(double degrees) {
        return String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(degrees));
    }

    public static String format2f(float f) {
        return String.format(Locale.getDefault(), "%.2f", f);
    }
}

