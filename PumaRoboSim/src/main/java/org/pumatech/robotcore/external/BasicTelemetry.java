package org.pumatech.robotcore.external;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class BasicTelemetry extends SimTelemetry {

    public Telemetry.Item addData(String caption, Object value) {
        System.out.println(caption + ": " + value);
        return null;
    }

    public void clearAll() {
    }

    public boolean update() {
        return false;
    }
}
