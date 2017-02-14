package email.com.gmail.ttsai0509.print.printer;

import email.com.gmail.ttsai0509.common.IOUtils;
import email.com.gmail.ttsai0509.print.printer.exception.PrinterException;

import java.io.OutputStream;

public class OutputStreamPrinter implements Printer {

    private final OutputStream output;

    public OutputStreamPrinter(OutputStream output) {
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
