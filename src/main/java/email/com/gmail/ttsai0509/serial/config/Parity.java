package email.com.gmail.ttsai0509.serial.config;

import gnu.io.SerialPort;

public enum Parity {

    PARITY_NONE(SerialPort.PARITY_NONE, com.fazecast.jSerialComm.SerialPort.NO_PARITY),
    PARITY_ODD(SerialPort.PARITY_ODD, com.fazecast.jSerialComm.SerialPort.ODD_PARITY),
    PARITY_EVEN(SerialPort.PARITY_EVEN, com.fazecast.jSerialComm.SerialPort.EVEN_PARITY),
    PARITY_MARK(SerialPort.PARITY_MARK, com.fazecast.jSerialComm.SerialPort.MARK_PARITY),
    PARITY_SPACE(SerialPort.PARITY_SPACE, com.fazecast.jSerialComm.SerialPort.SPACE_PARITY);

    public final int rxtxVal;
    public final int jserialVal;
    
    Parity(int rxtxVal, int jserialVal) {
        this.rxtxVal = rxtxVal;
        this.jserialVal = jserialVal;
    }
    
}
