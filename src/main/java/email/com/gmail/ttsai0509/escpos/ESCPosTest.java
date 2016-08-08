package email.com.gmail.ttsai0509.escpos;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ESCPosTest {

    public static void main(String[] args) throws
            IOException,
            PortInUseException,
            UnsupportedCommOperationException,
            NoSuchPortException, InterruptedException {

        test2();

    }

    public static void test1() throws
            IOException,
            PortInUseException,
            UnsupportedCommOperationException,
            NoSuchPortException {

        SerialPort printer = ESCPos.connectSerialPort("COM1");

        OutputStream out = printer.getOutputStream();

        ESCPos.Commander commander = ESCPos.command(out).print(ESCPos.INITIALIZE);

        header(commander);
        date(commander);

        commander
                .print(ESCPos.ALIGN_LEFT)
                .print("C5 TSC")
                .print(ESCPos.FEED)
                .print(ESCPos.ALIGN_RIGHT)
                .print("$9.65".getBytes())
                .print(ESCPos.FEED)
                .print(ESCPos.ALIGN_CENTER)
                .print("something".getBytes());

        footer(commander);
        done(commander);

        out.close();

        printer.close();

    }

    public static void test2() throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException,
            IOException, InterruptedException {

        final PrinterDispatcher pd = new PrinterDispatcher(System.out);
        new Thread(pd).start();


        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ESCPos.Commander jobBuilder = new ESCPos.Commander(stream);

        for (int i = 0; i < 10; i++) {
            header(jobBuilder);
            pd.print(stream.toByteArray());
            stream.reset();
            pd.printStatus();
        }

        Thread.sleep(TimeUnit.SECONDS.toMillis(30));

        for (int i = 0; i < 10; i++) {
            final int j = i;
            Thread.sleep(j * 100);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    ESCPos.Commander jobBuilder = new ESCPos.Commander(stream);
                    try {

                        footer(jobBuilder);
                        pd.print(stream.toByteArray());
                        stream.reset();
                        pd.printStatus();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        Thread.sleep(TimeUnit.SECONDS.toMillis(30));
        pd.requestClose();

        pd.printStatus();

    }

    public static ESCPos.Commander header(ESCPos.Commander commander) throws IOException {

        return commander
                .print(ESCPos.ALIGN_CENTER)
                .print(ESCPos.FONT_DH_EMPH)
                .print("ORIENTAL WOK")
                .print(ESCPos.FEED_N)
                .print(2)
                .print(ESCPos.FONT_REG)
                .print("6 NORTH BOLTON AVE.")
                .print(ESCPos.FEED)
                .print("ALEXANDRIA, LA 71301")
                .print(ESCPos.FEED)
                .print("(318) 448-8247")
                .print(ESCPos.FEED_N)
                .print(2);

    }

    public static ESCPos.Commander date(ESCPos.Commander commander) throws IOException {

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyy hh:mm:ss a");

        return commander
                .print(ESCPos.ALIGN_LEFT)
                .print(ESCPos.FONT_REG)
                .print(sdf.format(new Date()))
                .print(ESCPos.FEED_N)
                .print(2);

    }

    public static ESCPos.Commander footer(ESCPos.Commander commander) throws IOException {

        return commander
                .print(ESCPos.ALIGN_CENTER)
                .print(ESCPos.FONT_REG)
                .print(ESCPos.FEED_N)
                .print(2)
                .print(Long.toString(new Date().getTime()))
                .print(ESCPos.FEED_N)
                .print(2);

    }

    public static void done(ESCPos.Commander commander) throws IOException {

        commander.print(ESCPos.FEED_N)
                .print(6)
                .print(ESCPos.FULL_CUT);

    }
}