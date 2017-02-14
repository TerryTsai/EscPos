package email.com.gmail.ttsai0509.common;

import java.io.Closeable;
import java.io.IOException;

public enum IOUtils {

    ;

    public static void closeQuietly(Closeable c) {
        try {
            c.close();
        } catch (IOException e) {
            // Shh...
        }
    }

}
