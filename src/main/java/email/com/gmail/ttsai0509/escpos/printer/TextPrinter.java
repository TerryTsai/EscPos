package email.com.gmail.ttsai0509.escpos.printer;

import email.com.gmail.ttsai0509.escpos.EscPosBuilder;
import email.com.gmail.ttsai0509.escpos.printer.exception.PrinterException;

import java.io.OutputStream;
import java.io.PrintStream;

public class TextPrinter extends BaseOutputStreamPrinter {

    private final PrintStream printStream;

    protected TextPrinter(OutputStream output) {
        super(output);
        this.printStream = new PrintStream(output);
    }

    @Override
    public void print(PrintJob job) throws PrinterException {

        try {

            byte[] data = job.getData();

            // TODO : Temporary
            printStream.write(data);

            /*
            int i = 0;
            while (i < data.length) {

                if (i + 3 <= data.length && data[i] == 0x1B && data[i + 1] == 0x21) {
                    i += 3;

                } else if (i + 3 <= data.length && data[i] == 0x1B && data[i + 1] == 0x61) {
                    i += 3;

                } else if (i + 2 <= data.length && EscPosBuilder.isCommand(EscPosBuilder.INITIALIZE, data[i], data[i
                        + 1])) {
                    printStream.println("===INITIALIZED===");
                    i += 2;

                } else if (i + 1 <= data.length && EscPosBuilder.isCommand(EscPosBuilder.FEED, data[i])) {
                    printStream.println();
                    i += 1;

                } else if (i + 3 <= data.length && EscPosBuilder.isCommand(EscPosBuilder.FEED_N, data[i], data[i + 1])) {
                    for (int n = 0; n < data[i + 2]; n++)
                        printStream.println();
                    i += 3;

                } else if (i + 4 <= data.length && EscPosBuilder.isCommand(EscPosBuilder.PART_CUT, data[i], data[i +
                                1], data[i + 2],
                        data[i + 3])) {
                    printStream.println("=======CUT=======");
                    i += 4;

                } else if (i + 4 <= data.length && EscPosBuilder.isCommand(EscPosBuilder.FULL_CUT, data[i], data[i + 1], data[i + 2],
                        data[i + 3])) {
                    printStream.println("=======CUT=======");
                    i += 4;

                } else {
                    printStream.write(data[i]);
                    i += 1;

                }

            }
            */

            // PrintStream does not flush by default.
            printStream.flush();

        } catch (Exception e) {

            throw new PrinterException(this, job);

        }

    }

}
