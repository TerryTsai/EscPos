package email.com.gmail.ttsai0509.print.printer;

import com.fazecast.jSerialComm.SerialPort;
import email.com.gmail.ttsai0509.print.printer.exception.PrinterException;
import email.com.gmail.ttsai0509.serial.config.SerialConfig;
import ow.micropos.server.common.PrinterConfig;

import java.io.OutputStream;

public class JSerialFactoryPrinter implements Printer {

    private final SerialConfig serialConfig;

    public JSerialFactoryPrinter(PrinterConfig printerConfig) {
        this.printerConfig = printerConfig;
    }

    @Override
    public void print(PrintJob job) throws PrinterException {
        if (job == null)
            throw new PrinterException(this, null);

        SerialPort serialPort = null;
        OutputStream outputStream = null;

        try {
            serialPort = SerialPort.getCommPort(portDescriptor);
            serialPort.setComPortTimeouts(
                    SerialPort.TIMEOUT_READ_SEMI_BLOCKING | SerialPort.TIMEOUT_WRITE_SEMI_BLOCKING,
                    5000,
                    5000
            );
            outputStream = serialPort.getOutputStream();
            outputStream.write(job.getData());
        } catch (Exception e) {
            throw new PrinterException(this, job);
        } finally {
            try { outputStream.close(); } catch (Exception e) { /* Suppress */ }
            try { serialPort.closePort(); } catch (Exception e) { /* Suppress */ }
        }
    }

    @Override
    public void close() {
        // OutputStream is closed after each print
    }

}
