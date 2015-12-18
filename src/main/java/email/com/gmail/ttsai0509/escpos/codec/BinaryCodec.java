package email.com.gmail.ttsai0509.escpos.codec;

import email.com.gmail.ttsai0509.escpos.codec.exception.DecodeException;
import email.com.gmail.ttsai0509.escpos.codec.exception.EncodeException;

public class BinaryCodec implements Codec {

    @Override
    public String encode(byte[] dat) throws EncodeException {
        try {
            return org.apache.commons.codec.binary.BinaryCodec.toAsciiString(dat);
        } catch (Exception e) {
            throw new EncodeException(this, dat);
        }
    }

    @Override
    public byte[] decode(String str) throws DecodeException {
        try {
            return org.apache.commons.codec.binary.BinaryCodec.fromAscii(str.getBytes());
        } catch (Exception e) {
            throw new DecodeException(this, str);
        }
    }

    @Override
    public String toString() {
        return "CODEC_BINARY";
    }

}
