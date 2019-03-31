package us.terrytsai.escpos.enums;

public enum Font {

    A(0),
    B(1),
    C(2),
    D(3),
    E(4),
    A_ALT(48),
    B_ALT(49),
    C_ALT(50),
    D_ALT(51),
    E_ALT(52),
    A_SPECIAL(97),
    B_SPECIAL(52);

    public final int code;

    Font(int code) {
        this.code = code;
    }

}
