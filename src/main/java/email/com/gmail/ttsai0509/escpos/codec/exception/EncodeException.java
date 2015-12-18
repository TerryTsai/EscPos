package email.com.gmail.ttsai0509.escpos.codec.exception;

import email.com.gmail.ttsai0509.escpos.codec.Codec;

import java.util.Arrays;

public class EncodeException extends Exception {

    public EncodeException(Codec codec, byte[] dat) {

        super(codec.getClass().getSimpleName() + " could not encode " + Arrays.toString(dat));

    }

}
