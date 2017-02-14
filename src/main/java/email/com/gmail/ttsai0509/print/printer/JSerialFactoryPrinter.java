package email.com.gmail.ttsai0509.print.printer;

import com.fazecast.jSerialComm.SerialPort;
import email.com.gmail.ttsai0509.print.printer.exception.PrinterException;
import email.com.gmail.ttsai0509.serial.SerialFactory;
import email.com.gmail.ttsai0509.serial.config.SerialConfig;

import java.io.OutputStream;

public class JSerialFactoryPrinter implements Printer {

    private static final int defaultTimeoutMode = SerialPort.TIMEOUT_READ_SEMI_BLOCKING | SerialPort.TIMEOUT_WRITE_SEMI_BLOCKING;
    private static final int defaultTimeoutRead = 5000;
    private static final int defaultTimeoutWrite = 5000;

    private final String portName;
    private final int timeoutMode;
    private final int timeoutRead;
    private final int timeoutWrite;
    private final SerialConfig serialConfig;

    public JSerialFactoryPrinter(String portName, SerialConfig serialConfig) {
        this(portName, serialConfig, defaultTimeoutMode, defaultTimeoutRead, defaultTimeoutWrite);
    }

    public JSerialFactoryPrinter(String portName, SerialConfig serialConfig, int timeoutMode, int timeoutRead, int timeoutWrite) {
        this.portName = portName;
        this.serialConfig = serialConfig;
        this.timeoutMode = timeoutMode;
        this.timeoutRead = timeoutRead;
        this.timeoutWrite = timeoutWrite;
    }

    @Override
    public void print(PrintJob job) throws PrinterException {
        if (job == null)
            throw new PrinterException(this, null);

        SerialPort serialPort = null;
        OutputStream outputStream = null;

        try {
            serialPort = SerialFactory.jSerialPort(portName, serialConfig);
            serialPort.openPort();
            serialPort.setComPortTimeouts(timeoutMode, timeoutRead, timeoutWrite);

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
