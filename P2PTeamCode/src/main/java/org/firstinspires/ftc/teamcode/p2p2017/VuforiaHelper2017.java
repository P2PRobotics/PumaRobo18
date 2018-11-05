package org.firstinspires.ftc.teamcode.p2p2017;

import com.qualcomm.robotcore.hardware.HardwareMap;

public class VuforiaHelper2017 {
    private int keyColumn;

    public VuforiaHelper2017(HardwareMap hardwareMap) {

    }

    public void start() {
        keyColumn = (int) (3 * Math.random() + 1);
    }

    public void loop() {
    }

    public int getKeyColumn() {
        return keyColumn;
    }
}