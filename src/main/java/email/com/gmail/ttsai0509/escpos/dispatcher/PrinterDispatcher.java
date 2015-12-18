package email.com.gmail.ttsai0509.escpos.dispatcher;

import email.com.gmail.ttsai0509.escpos.dispatcher.exception.PrinterDispatcherException;
import email.com.gmail.ttsai0509.escpos.printer.Printer;
import email.com.gmail.ttsai0509.escpos.printer.PrintJob;

import java.util.Map;

/******************************************************************
 *                                                                *
 * PrintDispatcher dispatches PrintJobs to registered Printers.
 *                                                                *
 ******************************************************************/

public interface PrinterDispatcher extends Runnable {

    Map<String, Printer> getPrinters();

    void requestPrint(String name, PrintJob job) throws PrinterDispatcherException;

    void registerPrinter(String name, Printer printer) throws PrinterDispatcherException;

    void unregisterPrinter(Printer printer);

    void unregisterPrinter(String name);

    void requestClose(boolean closePrinters);

    String getStatus();

}
