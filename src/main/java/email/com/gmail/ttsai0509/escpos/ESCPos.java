package email.com.gmail.ttsai0509.escpos;

import gnu.io.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;

/*=======================================================================*
 =
 =  Sample Usage :
 =
 =      SerialPort printer = ESCPos.connectSerialPort("COM1");
 =      OutputStream out = printer.getOutputStream();
 =
 =      ESCPos.Commander.command(out)
 =              .print(ESCPos.Command.INITIALIZE)
 =              .print(ESCPos.Command.ALIGN_CENTER)
 =              .print("Hello World")
 =              .print(ESCPos.Command.FEED_N)
 =              .print(10)
 =              .print(ESCPos.Command.FULL_CUT);
 =
 =      out.close();
 =      printer.close();
 =
 *=======================================================================*/

public final class ESCPos {

    /*=======================================================================*
     =                                                                       =
     = Port Discovery                                                        =
     =                                                                       =
     *=======================================================================*/

    public static void listPorts() {
        Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier portIdentifier = portEnum.nextElement();
            System.out.println(portIdentifier.getName() + " - " + getPortTypeName(portIdentifier.getPortType()));
        }
    }

    private static String getPortTypeName(int portType) {
        switch (portType) {
            case CommPortIdentifier.PORT_I2C:
                return "I2C";
            case CommPortIdentifier.PORT_PARALLEL:
                return "Parallel";
            case CommPortIdentifier.PORT_RAW:
                return "Raw";
            case CommPortIdentifier.PORT_RS485:
                return "RS485";
            case CommPortIdentifier.PORT_SERIAL:
                return "Serial";
            default:
                return "Unknown";
        }
    }

    /*=======================================================================*
     =                                                                       =
     = Port Connection                                                       =
     =                                                                       =
     *=======================================================================*/

    public static class SerialConfig {

        public static final SerialConfig CONFIG_9600_8_N_1 =
                new SerialConfig(9000, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

        private final int baud;
        private final int dataBits;
        private final int stopBits;
        private final int parity;

        public SerialConfig(int baud, int dataBits, int stopBits, int parity) {
            this.baud = baud;
            this.dataBits = dataBits;
            this.stopBits = stopBits;
            this.parity = parity;
        }
    }

    public static SerialPort connectSerialPort(String portName, SerialConfig config) throws
            NoSuchPortException,
            PortInUseException,
            UnsupportedCommOperationException {

        CommPortIdentifier commPortId = CommPortIdentifier.getPortIdentifier(portName);
        System.out.println(portName + " found.");

        CommPort commPort = commPortId.open(ESCPos.class.getSimpleName(), 2000);
        System.out.println(portName + " opened.");

        SerialPort serialPort = (SerialPort) commPort;
        serialPort.setSerialPortParams(config.baud, config.dataBits, config.stopBits, config.parity);
        System.out.println(portName + " configured.");

        return serialPort;
    }

    public static SerialPort connectSerialPort(String portName) throws
            NoSuchPortException,
            PortInUseException,
            UnsupportedCommOperationException {
        return connectSerialPort(portName, SerialConfig.CONFIG_9600_8_N_1);
    }

    public static CommPort connectCommPort(String portName) throws
            IOException,
            NoSuchPortException,
            PortInUseException,
            UnsupportedCommOperationException {

        CommPortIdentifier commPortId = CommPortIdentifier.getPortIdentifier(portName);
        System.out.println(portName + " found.");

        CommPort commPort = commPortId.open(ESCPos.class.getSimpleName(), 2000);
        System.out.println(portName + " opened.");

        return commPort;
    }

    /*=======================================================================*
     =                                                                       =
     = Fluent Commander
     =                                                                       =
     *=======================================================================*/

    public static Commander command(OutputStream out) {
        return new Commander(out);
    }

    public static class Commander {

        protected final OutputStream out;

        public Commander(OutputStream out) {
            this.out = out;
        }

        public Commander print(int val) throws IOException {
            out.write(val);
            return this;
        }

        public Commander print(String text) throws IOException {
            out.write(text.getBytes());
            return this;
        }

        public Commander print(byte ... cmd) throws IOException {
            out.write(cmd);
            return this;
        }

    }

    /*=======================================================================*
     =                                                                       =
     = ESCPos Commands
     =                                                                       =
     *=======================================================================*/

    public static final byte[] INITIALIZE = new byte[]{0x1B, 0x40};

    public static final byte[] FEED = new byte[]{0x0A};
    public static final byte[] FEED_N = new byte[]{0x1B, 0x64};

    public static final byte[] FONT_REG = new byte[]{0x1B, 0x21, 0b00000000};
    public static final byte[] FONT_EMPH = new byte[]{0x1B, 0x21, 0b00001000};
    public static final byte[] FONT_DWDH = new byte[]{0x1B, 0X21, 0b00110000};
    public static final byte[] FONT_DWDH_EMPH = new byte[]{0x1B, 0x21, 0b00111000};
    public static final byte[] FONT_DH = new byte[]{0x1B, 0x21, 0b00010000};
    public static final byte[] FONT_DH_EMPH = new byte[]{0x1B, 0x21, 0b00011000};

    public static final byte[] ALIGN_LEFT = new byte[]{0x1B, 0x61, 0x00};
    public static final byte[] ALIGN_RIGHT = new byte[]{0x1B, 0x61, 0x02};
    public static final byte[] ALIGN_CENTER = new byte[]{0x1B, 0x61, 0x01};

    public static final byte[] FULL_CUT = new byte[]{0x1D, 0x56, 0x00, 0x65};
    public static final byte[] PART_CUT = new byte[]{0x1D, 0x56, 0x01, 0x66};

    /*=======================================================================*
     =                                                                       =
     = Byte Utils
     =                                                                       =
     *=======================================================================*/

}
