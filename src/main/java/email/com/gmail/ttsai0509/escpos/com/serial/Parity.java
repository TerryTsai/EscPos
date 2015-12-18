package email.com.gmail.ttsai0509.escpos.com.serial;

import gnu.io.SerialPort;

public enum Parity {

    PARITY_NONE(SerialPort.PARITY_NONE),
    PARITY_ODD(SerialPort.PARITY_ODD),
    PARITY_EVEN(SerialPort.PARITY_EVEN),
    PARITY_MARK(SerialPort.PARITY_MARK),
    PARITY_SPACE(SerialPort.PARITY_SPACE);
    
    public final int val;
    
    Parity(int val) {
        this.val = val;
    }
    
}
