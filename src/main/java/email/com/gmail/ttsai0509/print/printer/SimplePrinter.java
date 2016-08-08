package email.com.gmail.ttsai0509.print.printer;

import email.com.gmail.ttsai0509.print.printer.exception.PrinterException;
import org.apache.commons.io.IOUtils;

import java.io.OutputStream;

public class SimplePrinter implements Printer {

    private final OutputStream output;

    protected SimplePrinter(OutputStream output) {
        if (output == null)
            throw new NullPointerException("OutputStream can not be null.");

        this.output = output;
    }

    @Override
    public void print(PrintJob job) throws PrinterException {
        if (job == null)
            throw new PrinterException(this, null);

        try {
            output.write(job.getData());
        } catch (Exception e) {
            throw new PrinterException(this, job);
        }
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
}
