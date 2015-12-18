package email.com.gmail.ttsai0509.escpos.codec;


import email.com.gmail.ttsai0509.escpos.codec.exception.DecodeException;
import email.com.gmail.ttsai0509.escpos.codec.exception.EncodeException;

/******************************************************************
 *                                                                *
 * An encoding/decoding scheme between String and byte[] data.
 *
 * Exceptions are NOT propagated. Instead, the codec should
 * return NO_DAT and NO_STR for any exceptions.
 *                                                                *
 ******************************************************************/

public interface Codec {

    String encode(byte[] dat) throws EncodeException;

    byte[] decode(String str) throws DecodeException;

}
