package us.terrytsai.escpos.enums;

public enum Color {

    BLACK(0),
    RED(1),
    BLACK_ALT(48),
    RED_ALT(49);

    public final int code;

    Color(int code) {
        this.code = code;
    }
    
}
