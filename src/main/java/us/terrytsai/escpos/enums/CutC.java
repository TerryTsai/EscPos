package us.terrytsai.escpos.enums;

public enum CutC {

    FULL(97),
    PARTIAL(98);

    public final int code;

    CutC(int code) {
        this.code = code;
    }

}
