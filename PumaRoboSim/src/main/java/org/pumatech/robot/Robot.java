package org.pumatech.robot;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.pumatech.physics.Body;

import java.awt.*;
import java.util.List;

public interface Robot {
    void draw(Graphics2D g);

    void update(double dt);

    List<Body> getBodies();

    HardwareMap getHardwareMap();
}
