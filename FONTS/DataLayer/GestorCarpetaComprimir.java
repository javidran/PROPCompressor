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
        String nombreCarpeta = carpeta.getName();
        String[] pathRelativo = pathArchivoActual.split(nombreCarpeta);
        Algoritmo algoritmoArchivo;
        Algoritmo[] algoritmos;
        StringBuilder pathRootCarpeta = new StringBuilder(pathArchivoActual.replace(pathRelativo[0], ""));
        String[] dirs = pathRootCarpeta.toString().split(Pattern.quote(System.getProperty("file.separator")));
        pathRootCarpeta = new StringBuilder(File.separator);
        for (int i = 1; i < dirs.length; ++i) {
            pathRootCarpeta.append(dirs[i]);
            if (i < dirs.length - 1) pathRootCarpeta.append(File.separator);
        }
        algoritmos = CtrlDatos.algoritmosPosibles(pathRootCarpeta.toString());
        if (algoritmos.length > 1) algoritmoArchivo = algoritmoTexto;
        else algoritmoArchivo = Algoritmo.JPEG;
        String pathComprimido = CtrlDatos.actualizarPathSalida(pathRootCarpeta.toString(), algoritmoArchivo, true);
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
