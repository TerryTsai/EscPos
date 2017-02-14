package email.com.gmail.ttsai0509.serial.config;

import gnu.io.SerialPort;

public enum FlowControl {

    FLOWCONTROL_NONE(SerialPort.FLOWCONTROL_NONE, com.fazecast.jSerialComm.SerialPort.FLOW_CONTROL_DISABLED),
    FLOWCONTROL_RTSCTS_IN(SerialPort.FLOWCONTROL_RTSCTS_IN, com.fazecast.jSerialComm.SerialPort.FLOW_CONTROL_RTS_ENABLED | com.fazecast.jSerialComm.SerialPort.FLOW_CONTROL_CTS_ENABLED),
    FLOWCONTROL_RTSCTS_OUT(SerialPort.FLOWCONTROL_RTSCTS_OUT, com.fazecast.jSerialComm.SerialPort.FLOW_CONTROL_DSR_ENABLED | com.fazecast.jSerialComm.SerialPort.FLOW_CONTROL_DTR_ENABLED),
    FLOWCONTROL_XONXOFF_IN(SerialPort.FLOWCONTROL_XONXOFF_IN, com.fazecast.jSerialComm.SerialPort.FLOW_CONTROL_XONXOFF_IN_ENABLED),
    FLOWCONTROL_XONXOFF_OUT(SerialPort.FLOWCONTROL_XONXOFF_OUT, com.fazecast.jSerialComm.SerialPort.FLOW_CONTROL_XONXOFF_OUT_ENABLED);

    public final int rxtxVal;
    public final int jserialVal;

    FlowControl(int rxtxVal, int jserialVal) {
        this.rxtxVal = rxtxVal;
        this.jserialVal = jserialVal;
    }

}
