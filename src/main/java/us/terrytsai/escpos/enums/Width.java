package us.terrytsai.escpos.enums;

public enum Width {

    X1(0),
    X2(16),
    X3(32),
    X4(48),
    X5(64),
    X6(80),
    X7(96),
    X8(112);

    public final int code;

    Width(int code) {
        this.code = code;
    }
    
}
