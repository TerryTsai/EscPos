package email.com.gmail.ttsai0509.serial.config;

import com.fazecast.jSerialComm.SerialPort;

public enum Parity {

    PARITY_NONE(SerialPort.NO_PARITY),
    PARITY_ODD(SerialPort.ODD_PARITY),
    PARITY_EVEN(SerialPort.EVEN_PARITY),
    PARITY_MARK(SerialPort.MARK_PARITY),
    PARITY_SPACE(SerialPort.SPACE_PARITY);

    public final int val;
    
    Parity(int val) {
        this.val = val;
    }
    
}
