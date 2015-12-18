package email.com.gmail.ttsai0509.escpos;


import email.com.gmail.ttsai0509.escpos.com.ComUtils;
import email.com.gmail.ttsai0509.escpos.com.serial.Baud;
import email.com.gmail.ttsai0509.escpos.com.serial.SerialConfig;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class EscPosBuilder {

    @SuppressWarnings({"ConstantConditions", "EmptyCatchBlock"})
    public static void main(String[] args) {

        SerialPort port = null;
        OutputStream out = null;
        EscPosBuilder escPos = new EscPosBuilder();

        try {
            port = ComUtils.connectSerialPort("COM18", 2000, SerialConfig.CONFIG_8N1(Baud.BAUD_19200));
            out = port.getOutputStream();
            out.write(escPos.initialize()
                            .text("HELLO WORLD")
                            .feed(5)
                            .cut(Cut.PART)
                            .getBytes()
            );

        } catch (NoSuchPortException | PortInUseException | UnsupportedCommOperationException | IOException e) {
            e.printStackTrace();

        } finally {
            { // @formatter:off
            try {   out.close(); } catch (Exception e) {}
            try {  port.close(); } catch (Exception e) {}
            try {escPos.close(); } catch (Exception e) {}
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

    public static enum Font {
        REGULAR, EMPHASIZED,
        DWDH, DWDH_EMPHASIZED,
        DH, DH_EMPHASIZED,
        DW, DW_EMPHASIZED
    }

    public static enum Align {
        LEFT, CENTER, RIGHT
    }

    public static enum Cut {
        PART, FULL
    }

}