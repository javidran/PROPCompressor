package DataLayer;

import java.io.File;
import java.io.IOException;

/**
 * Clase abstracta encargada de gestionar la compresión y descompresión de carpetas desde la capa de datos.
 * <p>
 *     Concretamente, permite para los archivos de una carpeta saber el algoritmo a usar para procesarlo,
 *     obtener sus datos, y guardarlos en una carpeta procesada.
 * </p>
 */
public abstract class GestorCarpeta {
    /**
     * Carpeta sujeto del proceso. Los archivos y carpetas que contiene se tratan teniendo en cuenta a esta como la carpeta raíz.
     */
    protected File carpeta;

    /**
     * Constructora de la clase. Inicializa el valor de la carpeta, mientras que la constructora de la subclase correspondiente lo hace para las demás variables.
     * Sólo se puede llamar a esta desde la subclase correspondiente.
     * <p>
     *     El path existe y tiene el formato de path propio de las carpetas o directorios.
     * </p>
     * @param pathOriginal Path de la carpeta a procesar.
     */
    protected GestorCarpeta(String pathOriginal) {
        carpeta = new File(pathOriginal);
    }

    /**
     * Obtiene el path del próximo archivo de la carpeta a tratar en el proceso.
     * @return El path de dicho fichero como un String.
     * @throws IOException Si hay un error debido a la no existencia de tal archivo en el sistema, se activa una excepción de IO.
     */
    public abstract String pathProximoArchivo() throws IOException;

    /**
     * Lee y devuelve los datos en un byte array del archivo que está en la primera posición de la cola del proceso.
     * @return Un byte array con los datos de dicho archivo.
     * @throws IOException Si la cola está vacía o el fichero que se intenta leer no existe, se activa una excepción de IO.
     */
    public abstract byte[] leerProximoArchivo() throws IOException;

    /**
     * Guarda un archivo procesado por el gestor.
     * @param data Los datos del archivo procesado en un byte array.
     * @param path El path que tiene el fichero respecto a la carpeta procesada en la que se encuentra.
     * @throws IOException Si hay algún error de escritura de la carpeta, se activa una excepción de IO.
     */
    public abstract void guardaProximoArchivo(byte[] data, String path) throws IOException;

    /**
     * Guarda una carpeta vacía en la carpeta comprimida.
     * El formato .comp contempla que los archivos ya especifican el directorio donde se encuentran dentro de la carpeta para generarlas todas.
     * En el caso de una carpeta vacía, hay que guardarla también, pero usando esta función concreta.
     * @param path El path que tiene la carpeta respecto a la carpeta procesada en la que se encuentra.
     * @throws IOException Si hay algún error de escritura o lectura en la carpeta, se activa una excepción de IO.
     */
    public abstract void guardaCarpeta(String path) throws IOException;

    /**
     * Operación que finaliza el gestor de tareas. Esto es, cierra al escritor o lector de este para un correcto procesado de la carpeta en términos de IO.
     * @throws IOException Si hay algún error finalizando al escritor o lector, se activa una excepción de IO.
     */
    public abstract void finalizarGestor() throws IOException;
}
