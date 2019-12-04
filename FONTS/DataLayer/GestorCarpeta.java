package DataLayer;

import Enumeration.Algoritmo;

import java.io.File;
import java.io.IOException;

public abstract class GestorCarpeta {
    protected File carpeta;

    protected GestorCarpeta(String path) {
        carpeta = new File(path);
    }

    public abstract Algoritmo algoritmoProximoArchivo() throws IOException;

    public abstract byte[] leerProximoArchivo() throws IOException;

    public abstract void guardaProximoArchivo(byte[] data) throws IOException;

    public abstract void finalizarGestor() throws IOException;
}
