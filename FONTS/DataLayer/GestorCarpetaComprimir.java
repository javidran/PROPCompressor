package DataLayer;

import java.io.*;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

/**
 * Clase encargada de gestionar la compresión de carpetas.
 * <p>
 *     Concretamente, crea una cola de archivos a comprimir, y permite para cada uno saber el algoritmo a usar,
 *     obtener sus datos, y guardarlo en una carpeta comprimida con extensión .comp.
 * </p>
 */
public class GestorCarpetaComprimir extends GestorCarpeta {
    private BufferedOutputStream bufferedOutputStream;
    private Queue<File> archivosAComprimir;

    /**
     * Función encargada de obtener todos los ficheros y carpetas existentes en la carpeta a comprimir.
     * @param carpeta Carpeta a explorar. Cuando se llama a la función, será la carpeta a comprimir, pero luego,
     *                la recursividad hará que este parámetro se use para todas las carpetas en su haber.
     * @return Una lista con todos los ficheros y carpetas contenidos en la carpeta a comprimir.
     */
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

    /**
     * Constructora de la clase. Inicializa todos sus atributos y el de la superclase.
     * <p>
     *     El path existe y tiene el formato de path propio de las carpetas o directorios.
     * </p>
     * @param pathOriginal Path de la carpeta a comprimir.
     * @param pathSalida Path destino de la carpeta comprimida.
     * @throws FileNotFoundException Si hay algún error con la creación del escritor de la carpeta comprimida, se activa una excepción de fichero no encontrado.
     */
    public GestorCarpetaComprimir(String pathOriginal, String pathSalida) throws FileNotFoundException {
        super(pathOriginal);
        archivosAComprimir = new LinkedList<>(listarArchivosCarpeta(carpeta));
        File carpetaComprimida = new File(pathSalida);
        FileOutputStream fileOutputStream = new FileOutputStream(carpetaComprimida);
        bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
    }

    /**
     * Obtiene el path del próximo archivo de la carpeta a comprimir.
     * @return El path de dicho fichero como un String.
     */
    @Override
    public String pathProximoArchivo() {
        File proximoArchivo = archivosAComprimir.peek();
        if (proximoArchivo == null) return null;
        return proximoArchivo.getPath();
    }

    /**
     * Lee y devuelve los datos en un byte array del archivo que está en la primera posición de la cola del proceso.
     * @return Un byte array con los datos de dicho archivo.
     * @throws IOException Si la cola está vacía o el fichero que se intenta leer no existe, se activa una excepción de IO.
     */
    @Override
    public byte[] leerProximoArchivo() throws IOException {
        File proximoArchivo = archivosAComprimir.poll();
        if (proximoArchivo == null) return null;
        return GestorArchivo.leeArchivo(proximoArchivo.getAbsolutePath());
    }

    /**
     * Guarda un archivo comprimido por el gestor.
     * @param data Los datos del archivo comprimido en un byte array.
     * @param path El path que tiene el fichero respecto a la carpeta procesada en la que se encuentra.
     * @throws IOException Si hay algún error de escritura del fichero, se activa una excepción de IO.
     */
    @Override
    public void guardaProximoArchivo(byte[] data, String path) throws IOException {
        String endOfLine = "\n";
        bufferedOutputStream.write(path.getBytes());
        bufferedOutputStream.write(endOfLine.getBytes());
        bufferedOutputStream.write(Integer.toString(data.length).getBytes());
        bufferedOutputStream.write(endOfLine.getBytes());
        bufferedOutputStream.write(data);
    }

    /**
     * Guarda una carpeta vacía en la carpeta comprimida. 
     * El formato .comp contempla que los archivos ya especifican el directorio donde se encuentran dentro de la carpeta para generarlas todas.
     * En el caso de una carpeta vacía, hay que guardarla también, pero usando esta función concreta.
     * @param path El path que tiene la carpeta respecto a la carpeta procesada en la que se encuentra.
     * @throws IOException Si hay algún error de escritura o lectura de la carpeta, se activa una excepción de IO.
     */
    @Override
    public void guardaCarpeta(String path) throws IOException {
        archivosAComprimir.remove();
        String endOfLine = "\n";
        bufferedOutputStream.write(path.getBytes());
        bufferedOutputStream.write(endOfLine.getBytes());
    }

    /**
     * Operación que finaliza el gestor de tareas. Esto es, cierra al escritor de este para un correcto comprimido de la carpeta en términos de IO.
     * @throws IOException Si hay algún error finalizando al escritor, se activa una excepción de IO.
     */
    @Override
    public void finalizarGestor() throws IOException {
        bufferedOutputStream.close();
    }
}
