package com.github.terrytsai.escpos.enums;

public enum Direction {

    LEFT_TO_RIGHT(0),
    BOTTOM_TO_TOP(1),
    RIGHT_TO_LEFT(2),
    TOP_TO_BOTTOM(3),
    LEFT_TO_RIGHT_ALT(48),
    BOTTOM_TO_TOP_ALT(49),
    RIGHT_TO_LEFT_ALT(50),
    TOP_TO_BOTTOM_ALT(51);

    public final int code;

    Direction(int code) {
        this.code = code;
    }
    
}
