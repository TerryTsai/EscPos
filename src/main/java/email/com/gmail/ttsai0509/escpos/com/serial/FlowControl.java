package email.com.gmail.ttsai0509.escpos.com.serial;

import gnu.io.SerialPort;

public enum FlowControl {

    FLOWCONTROL_NONE(SerialPort.FLOWCONTROL_NONE),
    FLOWCONTROL_RTSCTS_IN(SerialPort.FLOWCONTROL_RTSCTS_IN),
    FLOWCONTROL_RTSCTS_OUT(SerialPort.FLOWCONTROL_RTSCTS_OUT),
    FLOWCONTROL_XONXOFF_IN(SerialPort.FLOWCONTROL_XONXOFF_IN),
    FLOWCONTROL_XONXOFF_OUT(SerialPort.FLOWCONTROL_XONXOFF_OUT);

    public final int val;

    FlowControl(int val) {
        this.val = val;
    }

}
