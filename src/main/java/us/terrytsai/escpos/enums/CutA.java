package us.terrytsai.escpos.enums;

public enum CutA {

    FULL(0),
    PARTIAL(1),
    FULL_ALT(48),
    PARTIAL_ALT(49);

    public final int code;

    CutA(int code) {
        this.code = code;
    }

}
