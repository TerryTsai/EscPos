package email.com.gmail.ttsai0509.escpos.com.serial;

import gnu.io.SerialPort;

public enum StopBits {

    STOPBITS_1(SerialPort.STOPBITS_1),
    STOPBITS_2(SerialPort.STOPBITS_2),
    STOPBITS_1_5(SerialPort.STOPBITS_1_5);
    
    public final int val;
    
    StopBits(int val) {
        this.val = val;
    }

}
