package email.com.gmail.ttsai0509.escpos.codec;

import email.com.gmail.ttsai0509.escpos.codec.exception.DecodeException;
import email.com.gmail.ttsai0509.escpos.codec.exception.EncodeException;

public class DefaultCodec implements Codec {

    @Override
    public String encode(byte[] dat) throws EncodeException {
        try {
            return new String(dat);
        } catch (Exception e) {
            throw new EncodeException(this, dat);
        }
    }

    @Override
    public byte[] decode(String str) throws DecodeException {
        try {
            return str.getBytes();
        } catch (Exception e) {
            throw new DecodeException(this, str);
        }
    }

    @Override
    public String toString() {
        return "CODEC_DEFAULT";
    }
}