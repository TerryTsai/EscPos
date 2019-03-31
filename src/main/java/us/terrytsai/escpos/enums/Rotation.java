package us.terrytsai.escpos.enums;

public enum Rotation {

    OFF(0),
    ON_1(1),
    ON_1_5(2),
    OFF_ALT(48),
    ON_1_ALT(49),
    ON_1_5_ALT(50);

    public final int code;

    Rotation(int code) {
        this.code = code;
    }

}
