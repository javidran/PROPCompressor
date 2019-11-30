package DataLayer;

import DomainLayer.Algoritmos.Algoritmo;

import java.io.File;

public abstract class GestorCarpeta {
    protected File carpeta;
    protected Algoritmo algoritmoTexto = null;

    protected GestorCarpeta(String path) {
        carpeta = new File(path);
    }

    public abstract Algoritmo algoritmoProximoArchivo();

    public abstract byte[] leerProximoArchivo();

    public abstract void guardaProximoArchivo(byte[] data);
}
