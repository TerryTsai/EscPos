package email.com.gmail.ttsai0509.escpos.com;

import email.com.gmail.ttsai0509.escpos.com.serial.SerialConfig;
import gnu.io.*;

import java.io.IOException;
import java.util.Enumeration;

public final class ComUtils {

    private ComUtils() {}

    @SuppressWarnings("unchecked")
    public static String listPorts() {
        String ports = "";
        Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier portIdentifier = portEnum.nextElement();
            ports += portIdentifier.getName() + " - " + getPortType(portIdentifier) + "\n";
        }
        return ports;
    }

    public static String getPortType(CommPortIdentifier portIdentifier) {
        switch (portIdentifier.getPortType()) {
            case CommPortIdentifier.PORT_I2C:
                return "I2C";
            case CommPortIdentifier.PORT_PARALLEL:
                return "Parallel";
            case CommPortIdentifier.PORT_RAW:
                return "Raw";
            case CommPortIdentifier.PORT_RS485:
                return "RS485";
            case CommPortIdentifier.PORT_SERIAL:
                return "Serial";
            default:
                return "Unknown";
        }

    }

    public static CommPort connectCommPort(String portName, int timeout) throws
            IOException,
            NoSuchPortException,
            PortInUseException,
            UnsupportedCommOperationException {

        CommPortIdentifier commPortId = CommPortIdentifier.getPortIdentifier(portName);

        return commPortId.open(ComUtils.class.getSimpleName(), timeout);

    }

    public static SerialPort connectSerialPort(String portName, int timeout, SerialConfig config) throws
            NoSuchPortException,
            PortInUseException,
            UnsupportedCommOperationException,
            IOException {

        SerialPort serialPort = (SerialPort) connectCommPort(portName, timeout);

        serialPort.setSerialPortParams(
                config.getBaud().val,
                config.getDataBits().val,
                config.getStopBits().val,
                config.getParity().val
        );

        return serialPort;
    }

    public static SerialPort connectSerialPort(String portName) throws
            NoSuchPortException,
            PortInUseException,
            UnsupportedCommOperationException,
            IOException {

        return connectSerialPort(portName, 2000, SerialConfig.CONFIG_9600_8N1());

    }

}
