// Creado por Jan Escorza Fuertes
package DataLayer;

import Exceptions.ArchivoYaExisteException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * GestorArchivo es una clase utilizada por todos los algortimos tanto al comprimir como al descomir, se encarga de leer los ficheros.
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
        List<Byte> srclist = new ArrayList<>();// List to store bytes

        File fileIn = new File(path); // custom output format
        FileInputStream fis = new FileInputStream(fileIn);
        BufferedInputStream entrada = new BufferedInputStream(fis);
        int b;
        while ((b = entrada.read()) != -1) {
            byte d= (byte) b;
            srclist.add(d);
        }
        entrada.close();

        byte[] data = new byte[srclist.size()];
        int i = 0;
        for (byte d : srclist) {
            data[i] = d;
            ++i;
        }

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
     * @param sobrescribir Indica si se desearía sobrescribir en caso de que el fichero que se va a crear ya esté en el path
     * @throws IOException se ha producido un error en la escritura del fichero
     */
    public static void guardaArchivo(byte[] data, String path, boolean sobrescribir) throws IOException {
        //Miramos si existe el path
        File tempFile = new File(path);
        if (tempFile.exists() && !sobrescribir) throw new ArchivoYaExisteException("No se desea sobrescribir pero el fichero a escribir ya existe");

        File fileOut = new File(path); //custom output format
        FileOutputStream fout = null;
        fout = new FileOutputStream(fileOut);
        BufferedOutputStream bfout = new BufferedOutputStream(fout);
        for (byte b : data) {
            bfout.write(b);
        }
        bfout.close();
    }

}


