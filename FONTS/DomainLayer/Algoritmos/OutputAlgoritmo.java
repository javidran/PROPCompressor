// Creado por Joan Gamez Rodriguez
package DomainLayer.Algoritmos;

/**
 * OutputAlgoritmo es una clase que pretende emular un struct de los lenguajes de la familia de C.
 * <p>
 *     Esta clase permite que los algoritmos de compresión y descompresión puedan devolver la tupla de datos resultante de haber llevado a cabo la ejecución del proceso
 * </p>
 */
public class OutputAlgoritmo {
    /**
     * Tiempo de ejecución de la compresión o descompresión de un fichero
     */
    public long tiempo;
    /**
     * Byte array conteniente de la información comprimida o descomprimida tras haber sido sometida a la ejecución del proceso
     */
    public byte[] output;

    /**
     * Constructora de la clase, la cual crea una instancia de OutputAlgoritmo con un tiempo y output asignados
     * @param tiempo El tiempo de ejecución de la compresión o descompresión de un fichero
     * @param output El byte array conteniente de la información comprimida o descomprimida tras haber sido sometida a la ejecución del proceso
     */
    public OutputAlgoritmo(long tiempo, byte[] output) {
        this.tiempo = tiempo;
        this.output = output;
    }
}
