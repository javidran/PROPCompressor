package DataLayer;

import java.io.File;
import java.io.IOException;

public abstract class GestorCarpeta {
    protected File carpeta;

    protected GestorCarpeta(String pathOriginal) {
        carpeta = new File(pathOriginal);
    }

    public abstract String pathProximoArchivo() throws IOException;

    public abstract byte[] leerProximoArchivo() throws IOException;

    public abstract void guardaProximoArchivo(byte[] data, String path) throws IOException;

    public abstract void guardaCarpeta(String path) throws IOException;

    public abstract void finalizarGestor() throws IOException;
}
