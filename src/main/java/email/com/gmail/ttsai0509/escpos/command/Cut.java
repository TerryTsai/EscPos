package email.com.gmail.ttsai0509.escpos.command;

import java.io.IOException;
import java.io.OutputStream;

public enum Cut implements Command {

    FULL(65),
    PART(66);

    private final int code;

    Cut(int code) {
        this.code = code;
    }

    @Override
    public void write(OutputStream out) throws IOException {
        write(out, 0);
    }

    public void write(OutputStream out, int feed) throws IOException {
        out.write(0x1D);
        out.write(0x56);
        out.write(code);
        out.write(feed);
    }

}
