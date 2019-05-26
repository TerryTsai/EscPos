package com.github.terrytsai.escpos.enums;

public enum DataAction {

    CANCEL_DATA(0),
    PRINT_DATA(1),
    CANCEL_DATA_ALT(48),
    PRINT_DATA_ALT(49);

    public final int code;

    DataAction(int code) {
        this.code = code;
    }
    
}
