package email.com.gmail.ttsai0509.escpos.codec;

import email.com.gmail.ttsai0509.escpos.codec.exception.DecodeException;
import email.com.gmail.ttsai0509.escpos.codec.exception.EncodeException;
import org.apache.commons.codec.binary.Hex;

public class HexCodec implements Codec {

    @Override
    public String encode(byte[] dat) throws EncodeException {
        try {
            return new String(Hex.encodeHex(dat));
        } catch (Exception e) {
            throw new EncodeException(this, dat);
        }
    }

    @Override
    public byte[] decode(String str) throws DecodeException {
        try {
            return Hex.decodeHex(str.toCharArray());
        } catch (Exception e) {
            throw new DecodeException(this, str);
        }
    }

    @Override
    public String toString() {
        return "CODEC_HEX";
    }
}
