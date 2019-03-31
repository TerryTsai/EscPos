package us.terrytsai.escpos.serial.config;

import com.fazecast.jSerialComm.SerialPort;

public enum Timeout {

    TIMEOUT_SCANNER(SerialPort.TIMEOUT_SCANNER),
    TIMEOUT_NONBLOCKING(SerialPort.TIMEOUT_NONBLOCKING),
    TIMEOUT_READ_SEMI_BLOCKING(SerialPort.TIMEOUT_READ_SEMI_BLOCKING),
    TIMEOUT_READ_FULL_BLOCKING(SerialPort.TIMEOUT_READ_BLOCKING),
    TIMEOUT_WRITE_BLOCKING(SerialPort.TIMEOUT_WRITE_BLOCKING);

    public final int val;

    Timeout(int val) {
        this.val = val;
    }

}
