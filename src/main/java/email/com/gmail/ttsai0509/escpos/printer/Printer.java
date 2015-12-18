package email.com.gmail.ttsai0509.escpos.printer;

import email.com.gmail.ttsai0509.escpos.printer.exception.PrinterException;

public interface Printer {

    public void print(PrintJob job) throws PrinterException;

    public void close();

}
