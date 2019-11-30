package DataLayer;

import DomainLayer.Algoritmos.Algoritmo;

import java.io.File;

public class GestorCarpetaDescomprimir extends GestorCarpeta {
    public GestorCarpetaDescomprimir(String path) {
        super(path);
    }

    @Override
    public Algoritmo algoritmoProximoArchivo() {
        return null;
    }

    @Override
    public byte[] leerProximoArchivo() {
        return new byte[0];
    }

    @Override
    public void guardaProximoArchivo(byte[] data) {

    }
}
