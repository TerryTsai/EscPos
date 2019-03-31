package us.terrytsai.escpos.enums;

public enum Pin {

    TWO(0),
    FIVE(1),
    TWO_ALT(48),
    FIVE_ALT(49);

    public final int code;

    Pin(int code) {
        this.code = code;
    }

}
