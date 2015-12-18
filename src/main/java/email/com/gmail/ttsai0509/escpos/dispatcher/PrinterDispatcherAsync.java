package email.com.gmail.ttsai0509.escpos.dispatcher;

import email.com.gmail.ttsai0509.escpos.dispatcher.exception.PrinterDispatcherException;
import email.com.gmail.ttsai0509.escpos.printer.Printer;
import email.com.gmail.ttsai0509.escpos.printer.PrintJob;
import email.com.gmail.ttsai0509.escpos.printer.exception.PrinterException;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class PrinterDispatcherAsync implements PrinterDispatcher {

    // Dispatcher Statistics
    private final AtomicBoolean closeRequest, closeRequestPrinters;
    private final AtomicInteger jobRejected, jobTakeInterrupt,
            jobRequest, jobRequestPass, jobRequestFail,
            jobProcess, jobProcessPass, jobProcessFail;

    private Thread currentThread;
    private final ConcurrentMap<String, Printer> printerMap;
    private final BlockingQueue<PrintRequest> printRequests;

    public PrinterDispatcherAsync() {

        closeRequest = new AtomicBoolean(false);
        closeRequestPrinters = new AtomicBoolean(false);
        jobRequest = new AtomicInteger(0);
        jobRequestPass = new AtomicInteger(0);
        jobRequestFail = new AtomicInteger(0);
        jobProcess = new AtomicInteger(0);
        jobProcessPass = new AtomicInteger(0);
        jobProcessFail = new AtomicInteger(0);
        jobRejected = new AtomicInteger(0);
        jobTakeInterrupt = new AtomicInteger(0);

        printerMap = new ConcurrentHashMap<>();
        printRequests = new LinkedBlockingQueue<>();

    }

    @Override
    public Map<String, Printer> getPrinters() {
        return Collections.unmodifiableMap(printerMap);
    }

    @Override
    public void requestPrint(String name, PrintJob job) throws PrinterDispatcherException {

        if (closeRequest.get()) {

            jobRejected.incrementAndGet();
            throw new PrinterDispatcherException("Dispatcher already closed.");

        } else {

            jobRequest.incrementAndGet();

            if (!printerMap.containsKey(name)) {
                jobRequestFail.incrementAndGet();
                throw new PrinterDispatcherException(name + " was not registered.");
            }

            try {
                printRequests.put(new PrintRequest(name, job));
                jobRequestPass.incrementAndGet();

            } catch (InterruptedException e) {
                jobRequestFail.incrementAndGet();
                throw new PrinterDispatcherException("Request was interrupted.");
            }

        }
    }

    @Override
    public void registerPrinter(String name, Printer printer) throws PrinterDispatcherException {
        if (printerMap.containsKey(name))
            throw new PrinterDispatcherException(printer + " already registered to " + name);
        if (printer == null)
            throw new PrinterDispatcherException("Can not register null printer");

        printerMap.put(name, printer);
    }

    @Override
    public void unregisterPrinter(Printer printer) {
        for (Map.Entry<String, Printer> entries : printerMap.entrySet()) {
            if (entries.getValue() == printer) {
                printerMap.remove(entries.getKey());
                return;
            }
        }
    }

    @Override
    public void unregisterPrinter(String name) {
        printerMap.remove(name);
    }

    @Override
    public void requestClose(boolean closePrinters) {
        closeRequest.set(true);
        closeRequestPrinters.set(closePrinters);
        currentThread.interrupt();
    }

    @Override
    public void run() {

        currentThread = Thread.currentThread();

        while (!closeRequest.get() || !printRequests.isEmpty()) {

            try {

                // Take blocks until a request is available.
                PrintRequest request = printRequests.take();
                Printer printer = printerMap.get(request.name);

                jobProcess.incrementAndGet();

                if (printer == null) {

                    jobProcessFail.incrementAndGet();

                } else {

                    try {
                        printer.print(request.job);
                        jobProcessPass.incrementAndGet();
                    } catch (PrinterException e) {
                        jobProcessFail.incrementAndGet();
                    }

                }

            } catch (InterruptedException e) {
                jobTakeInterrupt.incrementAndGet();
            }

        }

        if (closeRequestPrinters.get())
            printerMap.values().forEach(Printer::close);

    }

    @Override
    public String getStatus() {
        return "Printer Dispatcher Status" +
                "\n=====================" +
                "\nJobs Requested : " + jobRequest.get() +
                "\n   Passed      : " + jobRequestPass.get() +
                "\n   Failed      : " + jobRequestFail.get() +
                "\nJobs Processed : " + jobProcess.get() +
                "\n   Passed      : " + jobProcessPass.get() +
                "\n   Failed      : " + jobProcessFail.get() +
                "\nJobs Rejected  : " + jobRejected.get() +
                "\nTake Interrupt : " + jobTakeInterrupt.get() +
                "\n=====================";
    }

    private static class PrintRequest {
        public final String name;
        public final PrintJob job;

        public PrintRequest(String name, PrintJob job) {
            this.name = name;
            this.job = job;
        }
    }
}
