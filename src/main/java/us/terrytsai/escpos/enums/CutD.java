package us.terrytsai.escpos.enums;

public enum CutD {

    FULL(103),
    PARTIAL(104);

    public final int code;

    CutD(int code) {
        this.code = code;
    }

}
