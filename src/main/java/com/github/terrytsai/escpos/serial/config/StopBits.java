package com.github.terrytsai.escpos.serial.config;

import com.fazecast.jSerialComm.SerialPort;

public enum StopBits {

    STOPBITS_1(SerialPort.ONE_STOP_BIT),
    STOPBITS_2(SerialPort.TWO_STOP_BITS),
    STOPBITS_1_5(SerialPort.ONE_POINT_FIVE_STOP_BITS);

    public final int val;

    StopBits(int val) {
        this.val = val;
    }

}
