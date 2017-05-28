package email.com.gmail.ttsai0509.escpos.command;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;

public interface Command {

    void write(OutputStream out) throws IOException;

    default void uncheckedWrite(OutputStream out) {
        try {
            write(out);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
