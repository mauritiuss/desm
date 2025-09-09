package common.log;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Log {
    private static final DateTimeFormatter F = DateTimeFormatter.ofPattern("HH:mm:ss");

    private Log() {}

    // Log informativo (normale flusso di esecuzione, eventi attesi)
    public static void i(String tag, String msg) {
        System.out.println(ts() + " [" + tag + "] " + msg);
    }

    // Log informativo del client
    public static void ic(String msg){
        System.out.println("" + msg + "");
    }

    // Log di warning (qualcosa di anomalo ma non bloccante)
    public static void w(String tag, String msg) {
        System.out.println(ts() + " [WARN][" + tag + "] " + msg);
    }

    // Log di errore senza eccezione (problema grave da segnalare, ma senza stacktrace)
    public static void e(String tag, String msg) {
        System.err.println(ts() + " [ERR ][" + tag + "] " + msg);
    }

    // Log di errore con eccezione (problema grave e voglio anche stampare stacktrace)
    public static void e(String tag, String msg, Throwable t) {
        System.err.println(ts() + " [ERR ][" + tag + "] " + msg + " — " + t.getMessage());
        if (t != null) t.printStackTrace(System.err);
    }

    private static String ts() {
        return LocalTime.now().format(F);
    }
}
