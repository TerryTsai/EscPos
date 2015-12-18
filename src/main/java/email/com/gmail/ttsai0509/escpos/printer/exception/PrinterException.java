package email.com.gmail.ttsai0509.escpos.printer.exception;

import email.com.gmail.ttsai0509.escpos.printer.Printer;
import email.com.gmail.ttsai0509.escpos.printer.PrintJob;

public class PrinterException extends Exception {

    public PrinterException(Printer printer, PrintJob job) {

        super(printer + " could not print " + job);

    }

}
