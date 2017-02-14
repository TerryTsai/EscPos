package email.com.gmail.ttsai0509.print.printer;

import email.com.gmail.ttsai0509.print.printer.exception.PrinterException;

public interface Printer {

    void print(PrintJob job) throws PrinterException;

    void close();

}
