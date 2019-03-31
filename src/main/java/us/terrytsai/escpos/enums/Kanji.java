package us.terrytsai.escpos.enums;

public enum Kanji {

    JIS_CODE(0),
    SHIFT_JIS_CODE(1),
    JIS_CODE_ALT(48),
    SHIFT_JIS_CODE_ALT(49);

    public final int code;

    Kanji(int code) {
        this.code = code;
    }
    
}
