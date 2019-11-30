package DataLayer;

import Controllers.CtrlDatos;
import DomainLayer.Algoritmos.Algoritmo;
import Exceptions.FormatoErroneoException;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

public class GestorCarpetaComprimir extends GestorCarpeta {

    private Queue<File> archivosAComprimir;
    private Algoritmo algoritmoTexto;
    private File carpetaComprimida;

    private LinkedList<File> listarArchivosCarpeta(final File carpeta) {
        LinkedList<File> listaArchivos = new LinkedList<>();
        for (final File archivo : Objects.requireNonNull(carpeta.listFiles())) {
            if (archivo.isDirectory()) {
                listaArchivos.addAll(listarArchivosCarpeta(archivo));
            } else {
                listaArchivos.add(archivo);
            }
        }
        return listaArchivos;
    }

    public GestorCarpetaComprimir(String path, Algoritmo algoritmoTexto) {
        super(path);
        this.algoritmoTexto = algoritmoTexto;
        archivosAComprimir = new LinkedList<>(listarArchivosCarpeta(carpeta));
        carpetaComprimida = new File(CtrlDatos.actualizarPathSalida(carpeta.getPath(), Algoritmo.CARPETA, true));
    }

    @Override
    public Algoritmo algoritmoProximoArchivo() throws FormatoErroneoException {
        Algoritmo algoritmoArchivo = null;
        Algoritmo[] algoritmos;
        File proximoArchivo = archivosAComprimir.peek();
        if (proximoArchivo != null) {
            algoritmos = CtrlDatos.algoritmosPosibles(proximoArchivo.getPath());
            if (algoritmos.length > 1) algoritmoArchivo = algoritmoTexto;
            else algoritmoArchivo = Algoritmo.JPEG;
        }
        return algoritmoArchivo;
    }

    @Override
    public byte[] leerProximoArchivo() throws IOException {
        File proximoArchivo = archivosAComprimir.poll();
        byte[] datosArchivo;
        if (proximoArchivo != null) {
            datosArchivo = GestorArchivo.leeArchivo(proximoArchivo.getPath());
        }
        else datosArchivo = null;
        return datosArchivo;
    }

    @Override
    public void guardaProximoArchivo(byte[] data) {

    }
}
