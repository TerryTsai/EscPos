package com.github.terrytsai.escpos.serial.config;

import com.fazecast.jSerialComm.SerialPort;

public enum FlowControl {

    FLOWCONTROL_NONE(SerialPort.FLOW_CONTROL_DISABLED),
    FLOWCONTROL_RTSCTS_IN(SerialPort.FLOW_CONTROL_RTS_ENABLED | SerialPort.FLOW_CONTROL_CTS_ENABLED),
    FLOWCONTROL_RTSCTS_OUT(SerialPort.FLOW_CONTROL_DSR_ENABLED | SerialPort.FLOW_CONTROL_DTR_ENABLED),
    FLOWCONTROL_XONXOFF_IN(SerialPort.FLOW_CONTROL_XONXOFF_IN_ENABLED),
    FLOWCONTROL_XONXOFF_OUT(SerialPort.FLOW_CONTROL_XONXOFF_OUT_ENABLED);

    public final int val;

    FlowControl(int val) {
        this.val = val;
    }

}
