package DataLayer;

import java.io.*;

/**
 * GestorArchivo es una clase utilizada por todos los algoritmos tanto al comprimir como al descomir, se encarga de leer los ficheros.
 * <p>
 *     Esta clase permite que los algoritmos de compresión y descompresión puedan leer arrays de bytes evitando que tengan acceso a la capa de datos. La salida de los algoritmos tambien es en este formato por la misma razón.
 * </p>
 */
public class GestorArchivo {

    /**
     * Crea un array de bytes que contiene toda la información de un fichero, este se encuentra indicado por el path que se pasa por parametro.
     * <p>
     *     El path debe seguir el formato general de cualquier tipo de path de archivo, y puede ser absoluto o relativo
     * </p>
     * @param path El path donde se encuentra el fichero a leer y transformar en Array de bytes
     * @return retorna el array de bytes con toda la información que contiene el fichero
     * @throws IOException se ha producido un error en la lectura del fichero
     */
    public static byte[] leeArchivo(String path) throws IOException {
        File fileIn = new File(path);
        int length = (int) fileIn.length();
        FileInputStream fis = new FileInputStream(fileIn);
        BufferedInputStream entrada = new BufferedInputStream(fis);

        byte[] data = new byte[length];
        if(entrada.read(data) < 0) throw new IOException("El tamaño máximo operable del fichero ha sido alcanzado");

        return data;
    }

    /**
     * Crea un fichero que contiene toda la información contenida en el array de bytes data, el fichero se va a encontarra indicaso por el path que se pasa por parametro. El usuario también escoge si se desea sobrescribir
     * en el caso que el fichero a descomprimir tenga el mismo nombre uq eotro en el mismo paz.
     * <p>
     *     El path debe seguir el formato general de cualquier tipo de path de archivo, y puede ser absoluto o relativo
     * </p>
     * @param data contiene el archivo codificado o descodificado que se va a guardar en un archivo nuevo
     * @param path El path donde se escribirá el fichero que contiene el Array de bytes
     * @throws IOException se ha producido un error en la escritura del fichero
     */
    public static void guardaArchivo(byte[] data, String path) throws IOException {
        File fileOut = new File(path);
        fileOut.getParentFile().mkdirs();
        fileOut.createNewFile();
        FileOutputStream fout = new FileOutputStream(fileOut);
        BufferedOutputStream bfout = new BufferedOutputStream(fout);
        bfout.write(data);
        bfout.close();
    }

    /**
     * Comprueba si existe un archivo con el path provisto.
     * @param path El path del supuesto archivo.
     * @return Un booleano indicando si existe o no.
     */
    public static boolean existeArchivo(String path) {
        boolean existe;
        File archivoAExaminar = new File(path);
        if (archivoAExaminar.exists()) existe = true;
        else existe = false;
        return existe;
    }

    /**
     * Elimina el archivo que se indica en el path pasado por parámetro
     * @param path indica el archivo que se desea eliminar
     */
    public static void eliminaArchivo(String path) {
        new File(path).delete();
    }
}


