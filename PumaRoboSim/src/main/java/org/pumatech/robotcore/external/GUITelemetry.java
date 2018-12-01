package org.pumatech.robotcore.external;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

public class GUITelemetry extends SimTelemetry {
    private Map<String, Object> mappy = new ConcurrentHashMap<>();

    @Override
    public Item addData(String caption, String format, Object... args) {
        for (Object arg : args) {
            mappy.put(caption, arg);
        }
        return null;
    }

    @Override
    public Telemetry.Item addData(String caption, Object value) {
        mappy.put(caption, value);
        return null;
    }

    @Override
    public void clearAll() {
        mappy.clear();
    }

    @Override
    public boolean update() {
        for (Map.Entry<String, Object> e : mappy.entrySet()) {
            System.out.println(String.format("%s: %s", e.getKey(), e.getValue()));
        }
        return true;
    }

    public void draw(Graphics2D d2) {
        d2.setColor(Color.YELLOW);
        for (Map.Entry<String, Object> entry : mappy.entrySet()) {
            d2.drawString(entry.getKey() + ": " + entry.getValue(), 10, 15);
        }
    }
}
