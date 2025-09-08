package Simulator;

/**
 * Questa classe contiene le informazioni di base del Buffer
 */
public abstract class BufferProperties {

    //Dimensione della finestra di misurazioni
    public static final int WINDOW_SIZE = 8;

    // Avanzamento della finestra (50% overlap → 4 elementi)
    public static final int STEP = WINDOW_SIZE / 2;

}
