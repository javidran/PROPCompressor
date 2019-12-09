package DataLayer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase encargada de gestionar la descompresión de carpetas.
 * <p>
 *     Concretamente, permite para cada archivo saber el algoritmo a usar para descomprimirlo,
 *     obtener sus datos, y guardarlos en una carpeta descomprimida.
 * </p>
 */
public class GestorCarpetaDescomprimir extends GestorCarpeta {
    private BufferedInputStream lector;

    /**
     * Constructora de la clase. Inicializa todos sus atributos y el de la superclase.
     * <p>
     *     El path existe y tiene el formato de path propio de las carpetas o directorios comprimidos con extensión .comp.
     * </p>
     * @param pathOriginal Path de la carpeta a descomprimir.
     * @throws FileNotFoundException Si hay algún error con la creación del lector de la carpeta comprimida, se activa una excepción de fichero no encontrado.
     */
    public GestorCarpetaDescomprimir(String pathOriginal) throws FileNotFoundException {
        super(pathOriginal);
        lector = new BufferedInputStream(new FileInputStream(carpeta));
    }

    /**
     * Lee y devuelve los datos en un byte array del archivo que está en la primera posición de la cola del proceso.
     * @return Un byte array con los datos de dicho archivo.
     * @throws IOException Si la cola está vacía o el fichero que se intenta leer no existe, se activa una excepción de IO.
     */
    @Override
    public String pathProximoArchivo() throws IOException {
        byte endOfLine = '\n';
        byte b;
        List<Byte> byteList = new ArrayList<>();
        while((b=(byte)lector.read())!= endOfLine) {
            if(b == -1)
                return null;
            byteList.add(b);
        }
        byte[] byteArray = new byte[byteList.size()];
        for(int i=0; i<byteArray.length; ++i) {
            byteArray[i] = byteList.get(i);
        }
        return new String(byteArray);
    }

    /**
     * Obtiene los datos del siguiente archivo comprimido en la carpeta con extensión .comp.
     * @return Los datos de dicho archivo.
     * @throws IOException Si el lector tiene algún problema leyendo la carpeta, se activa una excepción de IO.
     */
    @Override
    public byte[] leerProximoArchivo() throws IOException {
        byte endOfLine = '\n';
        byte b;
        List<Byte> byteList = new ArrayList<>();
        while((b=(byte)lector.read())!= endOfLine) {
            if(b == -1)
                return null;
            byteList.add(b);
        }
        byte[] byteArray = new byte[byteList.size()];
        for(int i=0; i<byteArray.length; ++i) {
            byteArray[i] = byteList.get(i);
        }

        byte[] data = new byte[Integer.parseInt(new String(byteArray))];
        lector.read(data);
        return data;
    }

    /**
     * Guarda un archivo descomprimido por el gestor.
     * @param data Los datos del archivo descomprimido en un byte array.
     * @param path El path que tiene el fichero respecto a la carpeta procesada en la que se encuentra.
     * @throws IOException Si se produce un error en la escritura del fichero, se activa una excepción de IO.
     */
    @Override
    public void guardaProximoArchivo(byte[] data, String path) throws IOException {
        GestorArchivo.guardaArchivo(data, path);
    }

    /**
     * Guarda una carpeta vacía en la carpeta descomprimida.
     * El formato .comp contempla que los archivos ya especifican el directorio donde se encuentran dentro de la carpeta para generarlas todas.
     * En el caso de una carpeta vacía, hay que guardarla también, pero usando esta función concreta.
     * @param path El path que tiene la carpeta respecto a la carpeta procesada en la que se encuentra.
     */
    @Override
    public void guardaCarpeta(String path) {
        new File(path).mkdirs();
    }

    /**
     * Operación que finaliza el gestor de tareas. Esto es, cierra al lector de este para un correcto descomprimido de la carpeta en términos de IO.
     * @throws IOException Si hay algún error finalizando al lector, se activa una excepción de IO.
     */
    @Override
    public void finalizarGestor() throws IOException {
        lector.close();
    }
}
