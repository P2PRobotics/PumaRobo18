package org.firstinspires.ftc.teamcode.p2p2017;

public enum Movements2017 {
    FORWARD(1), BACKWARD(-1);

    private final int value;

    Movements2017(final int newValue) {
        value = newValue;
    }

    public int getValue() {
        return value;
    }
}

