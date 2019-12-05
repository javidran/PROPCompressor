package DataLayer;

import java.io.*;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

public class GestorCarpetaComprimir extends GestorCarpeta {
    private BufferedOutputStream bufferedOutputStream;
    private Queue<File> archivosAComprimir;

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

    public GestorCarpetaComprimir(String pathOriginal, String pathSalida) throws FileNotFoundException {
        super(pathOriginal);
        archivosAComprimir = new LinkedList<>(listarArchivosCarpeta(carpeta));
        File carpetaComprimida = new File(pathSalida);
        FileOutputStream fileOutputStream = new FileOutputStream(carpetaComprimida);
        bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
    }

    @Override
    public String pathProximoArchivo() {
        File proximoArchivo = archivosAComprimir.peek();
        if (proximoArchivo == null) return null;
        return proximoArchivo.getPath();
    }

    @Override
    public byte[] leerProximoArchivo() throws IOException {
        File proximoArchivo = archivosAComprimir.poll();
        if (proximoArchivo == null) return null;
        return GestorArchivo.leeArchivo(proximoArchivo.getAbsolutePath());
    }

    @Override
    public void guardaProximoArchivo(byte[] data, String path) throws IOException {
        String endOfLine = "\n";
        bufferedOutputStream.write(path.getBytes());
        bufferedOutputStream.write(endOfLine.getBytes());
        bufferedOutputStream.write(Integer.toString(data.length).getBytes());
        bufferedOutputStream.write(endOfLine.getBytes());
        bufferedOutputStream.write(data);
    }

    @Override
    public void guardaCarpeta(String path) throws IOException {
        archivosAComprimir.remove();
        String endOfLine = "\n";
        bufferedOutputStream.write(path.getBytes());
        bufferedOutputStream.write(endOfLine.getBytes());
    }

    @Override
    public void finalizarGestor() throws IOException {
        bufferedOutputStream.close();
    }
}
