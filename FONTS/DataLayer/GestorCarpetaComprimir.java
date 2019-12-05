package DataLayer;

import Controllers.CtrlDatos;
import Enumeration.Algoritmo;

import java.io.*;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.regex.Pattern;

public class GestorCarpetaComprimir extends GestorCarpeta {
    private BufferedOutputStream bufferedOutputStream;
    private Queue<File> archivosAComprimir;
    private Algoritmo algoritmoTexto;

    private LinkedList<File> listarArchivosCarpeta(final File carpeta) {
        LinkedList<File> listaArchivos = new LinkedList<>();
        for (final File archivo : Objects.requireNonNull(carpeta.listFiles())) {
            if (archivo.isDirectory()) {
                boolean hayMas = listaArchivos.addAll(listarArchivosCarpeta(archivo));
                if (!hayMas) listaArchivos.add(archivo);
            } else {
                listaArchivos.add(archivo);
            }
        }
        return listaArchivos;
    }

    public GestorCarpetaComprimir(String pathOriginal, String pathSalida, Algoritmo algoritmoTexto) throws FileNotFoundException {
        super(pathOriginal);
        this.algoritmoTexto = algoritmoTexto;
        archivosAComprimir = new LinkedList<>(listarArchivosCarpeta(carpeta));
        File carpetaComprimida = new File(pathSalida);
        FileOutputStream fileOutputStream = new FileOutputStream(carpetaComprimida);
        bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
    }

    @Override
    public String pathProximoArchivo() {
        File proximoArchivo = archivosAComprimir.peek();
        if (proximoArchivo != null)  return null;
        return proximoArchivo.getPath();
    }

    @Override
    public byte[] leerProximoArchivo() throws IOException {
        File proximoArchivo = archivosAComprimir.poll();
        byte[] bytesProximoArchivo = new byte[0];
        if (proximoArchivo != null) {
            pathArchivoActual = proximoArchivo.getAbsolutePath();
            if (!proximoArchivo.isDirectory()) bytesProximoArchivo = GestorArchivo.leeArchivo(pathArchivoActual);
        }
        return bytesProximoArchivo;
    }

    @Override
    public void guardaProximoArchivo(byte[] data, String path) throws IOException {
        String pathComprimido = formatearPath(path);
        String endOfLine = "\n";
        bufferedOutputStream.write(pathComprimido.getBytes());
        bufferedOutputStream.write(endOfLine.getBytes());
        bufferedOutputStream.write(Integer.toString(data.length).getBytes());
        bufferedOutputStream.write(endOfLine.getBytes());
        bufferedOutputStream.write(data);
    }

    @Override
    public void guardaCarpeta(String path) throws IOException {
        String pathComprimido = formatearPath(path);
        String endOfLine = "\n";
        bufferedOutputStream.write(pathComprimido.getBytes());
        bufferedOutputStream.write(endOfLine.getBytes());
    }

    private String formatearPath(String path) {
        String nombreCarpeta = carpeta.getName();
        String[] pathRelativo = path.split(nombreCarpeta);
        StringBuilder pathRootCarpeta = new StringBuilder(path.replace(pathRelativo[0], ""));
        String[] dirs = pathRootCarpeta.toString().split(Pattern.quote(System.getProperty("file.separator")));
        pathRootCarpeta = new StringBuilder(File.separator);
        for (int i = 1; i < dirs.length; ++i) {
            pathRootCarpeta.append(dirs[i]);
            if (i < dirs.length - 1) pathRootCarpeta.append(File.separator);
        }
        return pathRootCarpeta.toString();
    }

    @Override
    public void finalizarGestor() throws IOException {
        bufferedOutputStream.close();
    }
}
