package org.pumatech.robotcore.external;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BasicTelemetry extends SimTelemetry {
    private Map<String, Object> mappy = new ConcurrentHashMap<>();

    public Telemetry.Item addData(String caption, Object value) {
        mappy.put(caption, value);
        System.out.println(caption + ": " + value);
        return null;
    }

    public void clearAll() {
        mappy.clear();
    }

    public boolean update() {
        for (Map.Entry<String, Object> e : mappy.entrySet()) {
            System.out.println(String.format("%s: %s", e.getKey(), e.getValue()));
        }
        return true;
    }
}
