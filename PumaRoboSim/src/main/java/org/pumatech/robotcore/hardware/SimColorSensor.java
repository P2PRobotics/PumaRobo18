package org.pumatech.robotcore.hardware;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.I2cAddr;

import static com.qualcomm.robotcore.hardware.HardwareDevice.Manufacturer.Other;

public class SimColorSensor implements ColorSensor {
    @Override
    public int red() {
        return 0;
    }

    @Override
    public int green() {
        return 0;
    }

    @Override
    public int blue() {
        return 0;
    }

    @Override
    public int alpha() {
        return 0;
    }

    @Override
    public int argb() {
        return 0;
    }

    @Override
    public void enableLed(boolean enable) {

    }

    @Override
    public void setI2cAddress(I2cAddr newAddress) {

    }

    @Override
    public I2cAddr getI2cAddress() {
        return null;
    }

    @Override
    public Manufacturer getManufacturer() {
        return Other;
    }

    @Override
    public String getDeviceName() {
        return this.getClass().getName();
    }

    @Override
    public String getConnectionInfo() {
        return this.getClass().getName();
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public void resetDeviceConfigurationForOpMode() {

    }

    @Override
    public void close() {

    }
}
