package DataLayer;

import Enumeration.Algoritmo;

import java.io.File;
import java.io.IOException;

public abstract class GestorCarpeta {
    protected File carpeta;
    protected String pathSalida;
    protected String pathArchivoActual;

    protected GestorCarpeta(String pathOriginal, String pathSalida) {
        carpeta = new File(pathOriginal);
        this.pathSalida = pathSalida;
    }

    public abstract Algoritmo algoritmoProximoArchivo() throws IOException;

    public abstract byte[] leerProximoArchivo() throws IOException;

    public abstract void guardaProximoArchivo(byte[] data) throws IOException;

    public abstract void finalizarGestor() throws IOException;
}
