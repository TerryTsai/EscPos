package email.com.gmail.ttsai0509.serial.config;

public enum DataBits {

    DATABITS_5(5),
    DATABITS_6(6),
    DATABITS_7(7),
    DATABITS_8(8);

    public final int val;

    DataBits(int val) {
        this.val = val;
    }

}
