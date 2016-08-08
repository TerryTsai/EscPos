package email.com.gmail.ttsai0509.print.printer;

import email.com.gmail.ttsai0509.print.printer.exception.PrinterException;

import java.io.OutputStream;

public class FactoryPrinter implements Printer {

    public interface OutputStreamFactory {
        OutputStream getOutputStream();
    }

    private final OutputStreamFactory factory;

    protected FactoryPrinter(OutputStreamFactory factory) {
        if (factory == null)
            throw new NullPointerException("Factory can not be null.");

        this.factory = factory;
    }

    @Override
    public void print(PrintJob job) throws PrinterException {
        if (job == null)
            throw new PrinterException(this, null);

        try (OutputStream output = factory.getOutputStream()) {
            output.write(job.getData());
        } catch (Exception e) {
            throw new PrinterException(this, job);
        }
    }

    @Override
    public void close() {
        // OutputStream is closed after each print
    }

}
