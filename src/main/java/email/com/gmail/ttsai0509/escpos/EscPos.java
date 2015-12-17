package email.com.gmail.ttsai0509.escpos;


import gnu.io.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
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

public final class EscPos {

    private static final Logger log = LoggerFactory.getLogger(EscPos.class);

    private EscPos() {}

    public static void main(String[] args) {}

    /*=======================================================================*
     =                                                                       =
     = Port Discovery                                                        =
     =                                                                       =
     *=======================================================================*/

    public static String listPorts() {
        String ports = "";
        Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier portIdentifier = portEnum.nextElement();
            ports += portIdentifier.getName() + " - " + getPortTypeName(portIdentifier.getPortType()) + "\n";
        }
        return ports;
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

        log.info("Connecting to Serial Port " + portName);

        CommPortIdentifier commPortId = CommPortIdentifier.getPortIdentifier(portName);
        log.info("    Name      : " + commPortId.getName());
        log.info("    Type      : " + getPortTypeName(commPortId.getPortType()));
        log.info("    Owner     : " + commPortId.getCurrentOwner());

        CommPort commPort = commPortId.open(EscPos.class.getSimpleName(), 2000);
        log.info("    Opened    : " + true);

        SerialPort serialPort = (SerialPort) commPort;
        serialPort.setSerialPortParams(config.baud, config.dataBits, config.stopBits, config.parity);
        log.info("    Baud      : " + config.baud);
        log.info("    Data Bits : " + config.dataBits);
        log.info("    Stop Bits : " + config.stopBits);
        log.info("    Parity    : " + config.parity);

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

        log.info("Connecting to Comm Port " + portName);

        CommPortIdentifier commPortId = CommPortIdentifier.getPortIdentifier(portName);
        log.info("    Name      : " + commPortId.getName());
        log.info("    Type      : " + getPortTypeName(commPortId.getPortType()));
        log.info("    Owner     : " + commPortId.getCurrentOwner());

        CommPort commPort = commPortId.open(EscPos.class.getSimpleName(), 2000);
        log.info("    Opened    : " + true);

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

        private static final SimpleDateFormat datetime = new SimpleDateFormat("MM/dd/yy hh:mm a");
        private static final SimpleDateFormat date = new SimpleDateFormat("MM/dd/yyyy");
        private static final SimpleDateFormat time = new SimpleDateFormat("hh:mm:ss a");

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

        public Commander print(byte... cmd) throws IOException {
            out.write(cmd);
            return this;
        }

        public Commander datetime() throws IOException {
            out.write(datetime.format(new Date()).getBytes());
            return this;
        }

        public Commander date() throws IOException {
            out.write(date.format(new Date()).getBytes());
            return this;
        }

        public Commander time() throws IOException {
            out.write(time.format(new Date()).getBytes());
            return this;
        }

    }

    /*=======================================================================*
     =                                                                       =
     = ESCPos Commands
     =                                                                       =
     *=======================================================================*/

    public static boolean isCommand(byte[] command, byte... bytes) {
        if (command.length != bytes.length)
            return false;

        for (int i = 0; i < command.length; i++)
            if (command[i] != bytes[i])
                return false;

        return true;
    }

    public static final byte[] INITIALIZE = new byte[]{0x1B, 0x40};

    public static final byte[] FEED = new byte[]{0x0A};
    public static final byte[] FEED_N = new byte[]{0x1B, 0x64};

    public static final byte[] FONT_REG = new byte[]{0x1B, 0x21, 0b00000000};
    public static final byte[] FONT_EMPH = new byte[]{0x1B, 0x21, 0b00001000};
    public static final byte[] FONT_DWDH = new byte[]{0x1B, 0X21, 0x30};
    public static final byte[] FONT_DWDH_EMPH = new byte[]{0x1B, 0x21, 0x38};
    public static final byte[] FONT_DH = new byte[]{0x1B, 0x21, 0b00010000};
    public static final byte[] FONT_DH_EMPH = new byte[]{0x1B, 0x21, 0b00011000};

    public static final byte[] ALIGN_LEFT = new byte[]{0x1B, 0x61, 0x00};
    public static final byte[] ALIGN_RIGHT = new byte[]{0x1B, 0x61, 0x02};
    public static final byte[] ALIGN_CENTER = new byte[]{0x1B, 0x61, 0x01};

    public static final byte[] FULL_CUT = new byte[]{0x1D, 0x56, 0x00, 0x65};
    public static final byte[] PART_CUT = new byte[]{0x1D, 0x56, 0x01, 0x66};

    /*=======================================================================*
     =                                                                       =
     = Constants
     =                                                                       =
     *=======================================================================*/

    public static final int ROW_SIZE_REG = 42;
    public static final int ROW_SIZE_DW = 21;

}