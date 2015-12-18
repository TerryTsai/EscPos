package email.com.gmail.ttsai0509.escpos.printer;

import email.com.gmail.ttsai0509.escpos.printer.exception.PrinterException;
import org.apache.commons.io.IOUtils;

import java.io.OutputStream;

/******************************************************************
 *                                                                *
 * BaseOutputStreamPrinter is a Printer based on an OutputStream.
 * It will perform null checks, properly close the streams, and
 * implements a basic toString method.
 *
 * NOTE - When extending this class, the print method should
 * ideally make a call to super.
 *                                                                *
 ******************************************************************/

public abstract class BaseOutputStreamPrinter implements Printer {

    protected final OutputStream output;

    protected BaseOutputStreamPrinter(OutputStream output) {
        if (output == null)
            throw new NullPointerException("OutputStream can not be null.");

        this.output = output;
    }

    @Override
    public void print(PrintJob job) throws PrinterException {
        if (job == null)
            throw new PrinterException(this, null);
    }

    @Override
    public void close() {
        try {
            output.flush();
            output.close();
        } catch (Exception e) {
            // Suppress
        } finally {
            IOUtils.closeQuietly(output);
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " using output " + output.getClass();
    }
}
