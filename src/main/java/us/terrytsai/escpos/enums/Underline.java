package us.terrytsai.escpos.enums;

public enum Underline {

    CANCEL(0),
    ONE_DOT(1),
    TWO_DOT(2),
    CANCEL_ALT(48),
    ONE_DOT_ALT(49),
    TWO_DOT_ALT(50);

    public final int code;

    Underline(int code) {
        this.code = code;
    }

}
