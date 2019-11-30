package DataLayer;

import DomainLayer.Algoritmos.Algoritmo;

public class GestorCarpetaComprimir extends GestorCarpeta {
    public GestorCarpetaComprimir(String path, Algoritmo algoritmoTexto) {
        super(path);
        this.algoritmoTexto = algoritmoTexto;
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
