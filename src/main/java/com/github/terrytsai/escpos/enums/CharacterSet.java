package com.github.terrytsai.escpos.enums;

public enum CharacterSet {

    USA(0),
    FRANCE(1),
    GERMANY(2),
    UK(3),
    DENMARK_1(4),
    SWEDEN(5),
    ITALY(6),
    SPAIN_1(7),
    JAPAN(8),
    NORWAY(9),
    DENMARK_2(10),
    SPAIN_2(11),
    LATIN_AMERICA(12),
    KOREA(13),
    SLOVENIA(14),
    CROATIA(14),
    CHINA(15),
    VIETNAM(16),
    ARABIA(17),
    INDIA_DEVANAGARI(66),
    INDIA_BENGALI(67),
    INDIA_TAMIL(68),
    INDIA_TELUGU(69),
    INDIA_ASSAMESE(70),
    INDIA_ORIYA(71),
    INDIA_KANNADA(72),
    INDIA_MALAYALAM(73),
    INDIA_GUJARATI(74),
    INDIA_PUNJABI(75),
    INDIA_MARATHI(82);

    public final int code;

    CharacterSet(int code) {
        this.code = code;
    }

}
