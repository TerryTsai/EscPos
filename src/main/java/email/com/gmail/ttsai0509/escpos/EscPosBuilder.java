package email.com.gmail.ttsai0509.escpos;


import com.fazecast.jSerialComm.SerialPort;
import email.com.gmail.ttsai0509.common.IOUtils;
import email.com.gmail.ttsai0509.serial.SerialFactory;
import email.com.gmail.ttsai0509.serial.config.SerialConfig;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class EscPosBuilder {

    @SuppressWarnings({"ConstantConditions", "EmptyCatchBlock"})
    public static void main(String[] args) {

        SerialPort port = null;
        EscPosBuilder escPos = new EscPosBuilder();

        try {

            byte[] data = escPos.initialize()
                    .text("HELLO WORLD")
                    .feed(5)
                    .cut(Cut.PART)
                    .getBytes();

            port = SerialFactory.jSerialPort("COM8", SerialConfig.CONFIG_9600_8N1());

            port.openPort();
            port.getOutputStream().write(data);
            port.closePort();

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            { // @formatter:off
            try { port.closePort(); } catch (Exception e) {}
            try {   escPos.close(); } catch (Exception e) {}
            } // @formatter:on
        }

    }

    /******************************************************************
     *                                                                *
     * Implementation
     *                                                                *
     ******************************************************************/

    private final ByteArrayOutputStream out;

    // TODO : Add parameter - private final int cols;

    public EscPosBuilder() {
        this.out = new ByteArrayOutputStream();
    }

    public EscPosBuilder reset() {
        out.reset();
        return this;
    }

    public EscPosBuilder raw(int val) {
        out.write(val);
        return this;
    }

    public EscPosBuilder raw(byte val) {
        out.write(val);
        return this;
    }

    public EscPosBuilder raw(byte... vals) {
        if (vals != null)
            out.write(vals, 0, vals.length);

        return this;
    }

    public EscPosBuilder text(String text) {
        if (text != null)
            raw(text.getBytes());

        return this;
    }

    public EscPosBuilder initialize() {
        out.write(0x1B);
        out.write(0x40);
        return this;
    }

    public EscPosBuilder feed(int lines) {
        if (lines <= 1) {
            out.write(0xA);
        } else {
            out.write(0x1B);
            out.write(0x64);
            out.write(lines);
        }
        return this;
    }

    public EscPosBuilder font(Font font) {
        if (font != null) {
            out.write(0x1B);
            out.write(0x21);
            switch (font) {
                // @formatter:off
                case REGULAR:           out.write(0x00); break;
                case DH:                out.write(0x10); break;
                case DW:                out.write(0x20); break;
                case DWDH:              out.write(0x30); break;
                case EMPHASIZED:        out.write(0x08); break;
                case DH_EMPHASIZED:     out.write(0x18); break;
                case DW_EMPHASIZED:     out.write(0x28); break;
                case DWDH_EMPHASIZED:   out.write(0x38); break;
                default:                out.write(0x00); break;
                // @formatter:on
            }
        }
        return this;
    }

    public EscPosBuilder align(Align align) {
        if (align != null) {
            out.write(0x1B);
            out.write(0x61);
            switch (align) {
                // @formatter:off
                case LEFT:              out.write(0x00); break;
                case CENTER:            out.write(0x01); break;
                case RIGHT:             out.write(0x02); break;
                default:                out.write(0x00); break;
                // @formatter:on
            }
        }
        return this;
    }

    public EscPosBuilder cut(Cut cut) {
        if (cut != null) {
            out.write(0x1D);
            out.write(0x56);
            switch (cut) {
                // @formatter:off
                case FULL:              out.write(0x00); out.write(0x65); break;
                case PART:              out.write(0x01); out.write(0x66); break;
                default:                out.write(0x00); out.write(0x65); break;
                // @formatter:on
            }
        }
        return this;
    }

    public byte[] getBytes() {
        return out.toByteArray();
    }

    @Override
    public String toString() {
        return out.toString();
    }

    public void close() {
        IOUtils.closeQuietly(out);
    }

    /*=======================================================================*
     =                                                                       =
     = Enums
     =                                                                       =
     *=======================================================================*/

    public enum Font {
        REGULAR, EMPHASIZED,
        DWDH, DWDH_EMPHASIZED,
        DH, DH_EMPHASIZED,
        DW, DW_EMPHASIZED
    }

    public enum Align {
        LEFT, CENTER, RIGHT
    }

    public enum Cut {
        PART, FULL
    }

}