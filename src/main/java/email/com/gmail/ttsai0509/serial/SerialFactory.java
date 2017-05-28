package email.com.gmail.ttsai0509.serial;

import com.fazecast.jSerialComm.SerialPort;
import email.com.gmail.ttsai0509.serial.config.SerialConfig;

public enum SerialFactory {

    ; // Static Factory

    public static SerialPort jSerialPort(String portName, SerialConfig config) {

        SerialPort serialPort = SerialPort.getCommPort(portName);

        serialPort.setFlowControl(config.getFlowControl().val);
        serialPort.setComPortParameters(
                config.getBaud().val,
                config.getDataBits().val,
                config.getStopBits().val,
                config.getParity().val
        );

        return serialPort;
    }

}
