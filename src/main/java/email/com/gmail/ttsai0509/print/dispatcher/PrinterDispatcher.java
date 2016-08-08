package email.com.gmail.ttsai0509.print.dispatcher;

import email.com.gmail.ttsai0509.print.dispatcher.exception.PrinterDispatcherException;
import email.com.gmail.ttsai0509.print.printer.PrintJob;
import email.com.gmail.ttsai0509.print.printer.Printer;

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
