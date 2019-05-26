package com.github.terrytsai.escpos.serial;

import com.fazecast.jSerialComm.SerialPort;
import com.github.terrytsai.escpos.serial.config.SerialConfig;

public enum SerialFactory {

    ; // Static Factory

    public static SerialPort configure(SerialPort serialPort, SerialConfig config) {
        serialPort.setFlowControl(
                config.getFlowControl().val
        );
        serialPort.setComPortParameters(
                config.getBaud().val,
                config.getDataBits().val,
                config.getStopBits().val,
                config.getParity().val
        );
        serialPort.setComPortTimeouts(
                config.getTimeout().val,
                config.getReadTimeout(),
                config.getWriteTimeout()
        );
        return serialPort;
    }

    public static SerialPort port(String portDescriptor, SerialConfig config) {
        return configure(SerialPort.getCommPort(portDescriptor), config);
    }

    public static SerialPort com(int com, SerialConfig config) {
        if (com < 0) {
            throw new IllegalArgumentException("com must not be negative");
        }
        return port("COM" + com, config);
    }

    public static SerialPort tty(int tty, SerialConfig config) {
        if (tty < 0) {
            throw new IllegalArgumentException("tty must not be negative");
        }
        return port("/dev/tty" + tty, config);
    }

}
