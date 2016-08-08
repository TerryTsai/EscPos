package email.com.gmail.ttsai0509.print.printer;

import java.util.Arrays;
import java.util.Date;

/******************************************************************
 *                                                                *
 * A PrintJob is a byte array of data to be printed.
 *                                                                *
 ******************************************************************/

public class PrintJob {

    private static final byte[] NO_DATA = new byte[0];

    private final Date date;
    private final byte[] data;

    public PrintJob(byte[] data, boolean copy) {
        this.date = new Date();

        if (copy)
            this.data = (data == null) ? NO_DATA : Arrays.copyOf(data, data.length);
        else
            this.data = (data == null) ? NO_DATA : data;
    }

    // REMINDER - Data should not be modified.
    byte[] getData() {
        return data;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Print Job : " + data.length + " bytes @ " + date;
    }
}
