package us.terrytsai.escpos.enums;

public enum RealTimeRequest {

    FEED(0),
    RECOVER_RESTART_PRINT(1),
    RECOVER_CLEAR_BUFFER(2);

    public final int code;

    RealTimeRequest(int code) {
        this.code = code;
    }

}
