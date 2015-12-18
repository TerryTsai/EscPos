package email.com.gmail.ttsai0509.escpos.printer;

import email.com.gmail.ttsai0509.escpos.printer.exception.PrinterException;

import java.io.OutputStream;

@SuppressWarnings("EmptyCatchBlock")
public class RawPrinter extends BaseOutputStreamPrinter {

    public RawPrinter(OutputStream output) {
        super(output);
    }

    @Override
    public void print(PrintJob job) throws PrinterException {
        try {
            output.write(job.getData());
        } catch (Exception e) {
            throw new PrinterException(this, job);
        }
    }

}
