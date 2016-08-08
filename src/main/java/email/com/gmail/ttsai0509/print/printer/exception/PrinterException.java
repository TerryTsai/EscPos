package email.com.gmail.ttsai0509.print.printer.exception;

import email.com.gmail.ttsai0509.print.printer.PrintJob;
import email.com.gmail.ttsai0509.print.printer.Printer;

public class PrinterException extends Exception {

    public PrinterException(Printer printer, PrintJob job) {

        super(printer + " could not print " + job);

    }

}
