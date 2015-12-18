package email.com.gmail.ttsai0509.escpos.codec.exception;

import email.com.gmail.ttsai0509.escpos.codec.Codec;

public class DecodeException extends Exception {

    public DecodeException(Codec codec, String str) {

        super(codec.getClass().getSimpleName() + " could not decode " + str);

    }

}
