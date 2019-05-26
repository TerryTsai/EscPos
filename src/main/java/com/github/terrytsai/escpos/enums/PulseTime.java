package com.github.terrytsai.escpos.enums;

public enum PulseTime {

    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8);

    public final int code;

    PulseTime(int code) {
        this.code = code;
    }

}
