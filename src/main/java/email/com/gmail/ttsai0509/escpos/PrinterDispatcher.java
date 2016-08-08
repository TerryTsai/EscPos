package email.com.gmail.ttsai0509.escpos;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class PrinterDispatcher implements Runnable {

    private AtomicBoolean closeRequest;
    private AtomicInteger jobRejected, jobTakeInterrupt,
            jobRequest, jobRequestPass, jobRequestFail,
            jobProcess, jobProcessPass, jobProcessFail;

    private Thread currentThread;

    private final LinkedBlockingQueue<byte[]> printJobs;
    private final OutputStream printer;

    public PrinterDispatcher(OutputStream printer) throws
            NoSuchPortException,
            PortInUseException,
            UnsupportedCommOperationException {

        closeRequest = new AtomicBoolean(false);
        jobRequest = new AtomicInteger(0);
        jobRequestPass = new AtomicInteger(0);
        jobRequestFail = new AtomicInteger(0);
        jobProcess = new AtomicInteger(0);
        jobProcessPass = new AtomicInteger(0);
        jobProcessFail = new AtomicInteger(0);
        jobRejected = new AtomicInteger(0);
        jobTakeInterrupt = new AtomicInteger(0);

        printJobs = new LinkedBlockingQueue<>();
        this.printer = printer;
    }

    public boolean print(byte[] job) {

        if (closeRequest.get()) {

            jobRejected.incrementAndGet();
            return false;

        } else {

            jobRequest.incrementAndGet();

            try {
                printJobs.put(job);
                jobRequestPass.incrementAndGet();
                return true;

            } catch (InterruptedException e) {
                jobRequestFail.incrementAndGet();
                return false;
            }

        }
    }

    public void requestClose() {
        closeRequest.set(true);
        currentThread.interrupt();
    }

    @Override
    public void run() {

        currentThread = Thread.currentThread();

        while (!closeRequest.get() || !printJobs.isEmpty()) {

            try {
                byte[] job = printJobs.take();

                jobProcess.incrementAndGet();

                try {
                    printer.write(job);
                    jobProcessPass.incrementAndGet();
                } catch (IOException e) {
                    jobProcessFail.incrementAndGet();
                }

            } catch (InterruptedException e) {
                jobTakeInterrupt.incrementAndGet();
            }

        }
    }

    public void printStatus() {
        System.out.println("\n=====================" +
                "\nJobs Requested : " + jobRequest.get() +
                "\n   Passed      : " + jobRequestPass.get() +
                "\n   Failed      : " + jobRequestFail.get() +
                "\nJobs Processed : " + jobProcess.get() +
                "\n   Passed      : " + jobProcessPass.get() +
                "\n   Failed      : " + jobProcessFail.get() +
                "\nJobs Rejected  : " + jobRejected.get() +
                "\nTake Interrupt : " + jobTakeInterrupt.get() +
                "\n=====================");
    }

}
