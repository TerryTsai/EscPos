package email.com.gmail.ttsai0509.serial.config;

import gnu.io.SerialPort;

public enum DataBits {

    DATABITS_5(SerialPort.DATABITS_5, 5),
    DATABITS_6(SerialPort.DATABITS_6, 6),
    DATABITS_7(SerialPort.DATABITS_7, 7),
    DATABITS_8(SerialPort.DATABITS_8, 8);

    public final int rxtxVal;
    public final int jserialVal;

    DataBits(int rxtxVal, int jserialVal) {
        this.rxtxVal = rxtxVal;
        this.jserialVal = jserialVal;
    }

}
