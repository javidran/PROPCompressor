package DataLayer;

import Controllers.CtrlDatos;
import DomainLayer.Algoritmos.Algoritmo;
import Exceptions.FormatoErroneoException;

import java.io.*;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.regex.Pattern;

public class GestorCarpetaComprimir extends GestorCarpeta {
    private BufferedOutputStream bufferedOutputStream;
    private Queue<File> archivosAComprimir;
    private Algoritmo algoritmoTexto;
    private File carpetaComprimida;
    private String pathArchivoActual;

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

    public GestorCarpetaComprimir(String path, Algoritmo algoritmoTexto) throws FileNotFoundException {
        super(path);
        this.algoritmoTexto = algoritmoTexto;
        archivosAComprimir = new LinkedList<>(listarArchivosCarpeta(carpeta));
        carpetaComprimida = new File(CtrlDatos.actualizarPathSalida(carpeta.getAbsolutePath(), Algoritmo.CARPETA, true));
        FileOutputStream fileOutputStream = new FileOutputStream(carpetaComprimida);
        bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
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
        if (proximoArchivo != null) pathArchivoActual = proximoArchivo.getAbsolutePath();
        return GestorArchivo.leeArchivo(pathArchivoActual);
    }

    @Override
    public void guardaProximoArchivo(byte[] data) throws IOException {
        String[] directoriosPathCarpeta = carpeta.getAbsolutePath().split(Pattern.quote(System.getProperty("file.separator")));
        String nombreCarpeta = directoriosPathCarpeta[directoriosPathCarpeta.length - 1];
        int ocurrenciasNombreCarpeta = 0;
        for (String s : directoriosPathCarpeta) {
            if (s.equals(nombreCarpeta)) ++ocurrenciasNombreCarpeta;
        }
        String[] pathRelativo = pathArchivoActual.split(nombreCarpeta);
        Algoritmo algoritmoArchivo = null;
        Algoritmo[] algoritmos;
        String pathRootCarpeta = pathRelativo[ocurrenciasNombreCarpeta];
        algoritmos = CtrlDatos.algoritmosPosibles(pathRootCarpeta);
        if (algoritmos.length > 1) algoritmoArchivo = algoritmoTexto;
        else algoritmoArchivo = Algoritmo.JPEG;
        String pathComprimido = CtrlDatos.actualizarPathSalida(pathRootCarpeta, algoritmoArchivo, true);
        String endOfLine = "\n";
        bufferedOutputStream.write(pathComprimido.getBytes());
        bufferedOutputStream.write(endOfLine.getBytes());
        bufferedOutputStream.write(Integer.toString(data.length).getBytes());
        bufferedOutputStream.write(endOfLine.getBytes());
        bufferedOutputStream.write(data);
        bufferedOutputStream.write(endOfLine.getBytes());
    }

    @Override
    public void finalizarGestor() throws IOException {
        bufferedOutputStream.close();
    }
}
