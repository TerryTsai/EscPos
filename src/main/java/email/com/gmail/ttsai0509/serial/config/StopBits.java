package email.com.gmail.ttsai0509.serial.config;

import gnu.io.SerialPort;

public enum StopBits {

    STOPBITS_1(SerialPort.STOPBITS_1, com.fazecast.jSerialComm.SerialPort.ONE_STOP_BIT),
    STOPBITS_2(SerialPort.STOPBITS_2, com.fazecast.jSerialComm.SerialPort.TWO_STOP_BITS),
    STOPBITS_1_5(SerialPort.STOPBITS_1_5, com.fazecast.jSerialComm.SerialPort.ONE_POINT_FIVE_STOP_BITS);

    public final int rxtxVal;
    public final int jserialVal;

    StopBits(int rxtxVal, int jserialVal) {
        this.rxtxVal = rxtxVal;
        this.jserialVal = jserialVal;
    }

}
