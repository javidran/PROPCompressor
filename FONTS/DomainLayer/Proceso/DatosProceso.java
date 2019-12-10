package DomainLayer.Proceso;

/**
 * DatosProceso es una clase que contiene los datos relevantes para la generación de estadísticas de un proceso fichero.
 * <p>
 *     Esta clase permite generar estadísticas a partir de los datos que almacena, tanto a nivel de proceso individual como al poder ser usado por el generador de estadísticas globales.
 * </p>
 */
public class DatosProceso {
    /**
     * Tiempo de ejecución del proceso de compresión o descompresión de un fichero.
     */
    private long tiempo;
    /**
     * Tamaño del fichero tratado antes del proceso de compresión o descompresión.
     */
    private long oldSize;
    /**
     * Tamaño del fichero tratado tras la ejecución del proceso de compresión o descompresión.
     */
    private long newSize;
    /**
     * Indicación para saber si es un proceso de compresión o descompresión.
     */
    private boolean esCompresion;

    private double velocidad;
    /**
     * Indicación para saber si el proceso ha resultado satisfactorio o no según los datos del proceso.
     */
    private boolean satisfactorio;

    /**
     * Constructora de la clase, la cual crea una instancia de DatosProceso con un tiempo, oldSize, newSize y esCompresion asignados
     * @param time El tiempo de ejecución del proceso de compresión o descompresión de un fichero
     * @param oldSize Tamaño del fichero tratado antes del proceso de compresión o descompresión
     * @param newSize Tamaño del fichero tratado tras la ejecución del proceso de compresión o descompresión
     * @param esCompresion Indicación para saber si es un proceso de compresión o descompresión
     */
    public DatosProceso(long time, long oldSize, long newSize, boolean esCompresion) {
        tiempo = time;
        this.oldSize = oldSize;
        this.newSize = newSize;
        this.esCompresion = esCompresion;
        this.velocidad = Math.floor((double)oldSize/tiempo * 1000000000.0);

        satisfactorio = getDiffSize() > 0;
    }

    /**
     * Devuelve la diferencia entre el original size y el new size.
     * @return La diferencia entre el antiguo tamaño y el nuevo.
     */
    public long getDiffSize() {
        if(esCompresion) return oldSize - newSize;
        else return newSize - oldSize;
    }
    /**
     * Devuelve el porcentaje de tamaño que representad el nuevo archivo respecto al original.
     * @return  El porcentaje de tamaño que representad el nuevo archivo respecto al original.
     */
    public double getDiffSizePercentage() {
        if(newSize==oldSize) return 100;
        else return Math.floor((newSize /(double) oldSize)*100);
    }

    /**
     * Obtiene el nuevo tamaño del fichero sobre el que trabaja el proceso
     * @return El tamaño que tiene el archivo una vez se ha realizado el proceso
     */
    public long getTiempo() { return tiempo; }

    /**
     * Obtiene el nuevo tamaño del fichero sobre el que trabaja el proceso
     * @return El tamaño que tenía el archivo después de que se realizara el proceso
     */
    public long getNewSize() { return newSize; }

    /**
     * Obtiene el antiguo tamaño del fichero sobre el que trabaja el proceso
     * @return El tamaño que tenía el archivo antes de que se realizara el proceso
     */
    public long getOldSize() { return oldSize; }

    /**
     * Obtiene una indicación de si el proceso ha resultado satisfactorio o no.
     * @return Si el proceso ha resultado satisfactorio o no.
     */
    public boolean isSatisfactorio() { return satisfactorio; }

    public double getVelocidad() {
        return velocidad; }
}
