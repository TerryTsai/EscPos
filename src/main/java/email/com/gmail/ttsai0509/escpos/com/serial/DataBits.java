package email.com.gmail.ttsai0509.escpos.com.serial;

import gnu.io.SerialPort;

public enum DataBits {

    DATABITS_5(SerialPort.DATABITS_5),
    DATABITS_6(SerialPort.DATABITS_6),
    DATABITS_7(SerialPort.DATABITS_7),
    DATABITS_8(SerialPort.DATABITS_8);

    public final int val;

    DataBits(int val) {
        this.val = val;
    }

}
