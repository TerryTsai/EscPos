package email.com.gmail.ttsai0509.escpos.codec;

import email.com.gmail.ttsai0509.escpos.codec.exception.DecodeException;
import email.com.gmail.ttsai0509.escpos.codec.exception.EncodeException;

/******************************************************************
 *                                                                *
 * The MixedCodec combines the DefaultCodec, BinaryCodec, and
 * HexCodec. Substrings between the binPrefix & binSuffix are
 * handled by the BinaryCodec. Substrings between the hexPrefix
 * & hexSuffix are handled by the HexCodec. All other substrings
 * are handled by the DefaultCodec.
 *
 * Examples with MixedCodec("0b", " ", "0x", " ") :
 *
 *      "A0b01000001 0x41 "     == "AAA"
 *      "A 0b01000010  0x43  D" == "A B C D"
 *                                                                *
 ******************************************************************/

public class MixedCodec implements Codec {

    private final static DefaultCodec defCodec = new DefaultCodec();
    private final static BinaryCodec binCodec = new BinaryCodec();
    private final static HexCodec hexCodec = new HexCodec();

    private final String binPrefix;
    private final String binSuffix;
    private final String hexPrefix;
    private final String hexSuffix;

    public MixedCodec() {
        this("0b", " ", "0x", " ");
    }

    public MixedCodec(String binPrefix, String binSuffix, String hexPrefix, String hexSuffix) {
        this.binPrefix = binPrefix;
        this.hexPrefix = hexPrefix;
        this.binSuffix = binSuffix;
        this.hexSuffix = hexSuffix;

        // TODO : Implement It
        throw new RuntimeException("Has not been implemented yet.");
    }

    @Override
    public String encode(byte[] dat) throws EncodeException {
        return null;
    }

    @Override
    public byte[] decode(String str) throws DecodeException {
        return new byte[0];
    }
}
