package email.com.gmail.ttsai0509.dispatcher;

import email.com.gmail.ttsai0509.escpos.dispatcher.exception.PrinterDispatcherException;
import email.com.gmail.ttsai0509.escpos.printer.PrintJob;
import email.com.gmail.ttsai0509.escpos.printer.Printer;
import email.com.gmail.ttsai0509.escpos.printer.exception.PrinterException;

public class PrinterDispatcherSync implements PrinterDispatcher {

    private final Map<String, Printer> printers = new HashMap<>();

    @Override
    public Map<String, Printer> getPrinters() {
        return Collections.unmodifiableMap(printers);
    }

    @Override
    public void requestPrint(String name, PrintJob job) throws PrinterDispatcherException {
        Printer printer = printers.get(name);
        if (printer == null)
            throw new PrinterDispatcherException(name + " not registered");
        if (job == null)
            throw new PrinterDispatcherException("Can not print null job");

        try {
            printer.print(job);
        } catch (PrinterException e) {
            throw new PrinterDispatcherException("Printer encountered a problem : " + e.getMessage());
        }
    }

    @Override
    public void registerPrinter(String name, Printer printer) throws PrinterDispatcherException {
        if (printers.containsKey(name))
            throw new PrinterDispatcherException(printer + " already registered to " + name);
        if (printer == null)
            throw new PrinterDispatcherException("Can not register null printer");

        printers.put(name, printer);
    }

    @Override
    public void unregisterPrinter(Printer printer) {
        for (Map.Entry<String, Printer> entries : printers.entrySet()) {
            if (entries.getValue() == printer) {
                printers.remove(entries.getKey());
                return;
            }
        }
    }

    @Override
    public void unregisterPrinter(String name) {
        printers.remove(name);
    }

    @Override
    public void requestClose(boolean closePrinters) {
        log.info("Close requested");
    }

    @Override
    public String getStatus() {
        return  PrinterDispatcherSync.class.getSimpleName() + " has " + printers.size() + " registered printers.";
    }

    @Override
    public void run() {

    }
}
