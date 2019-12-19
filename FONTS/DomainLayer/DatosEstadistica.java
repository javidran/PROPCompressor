package DomainLayer;

public class DatosEstadistica {
    /**
     * Numero de archivos comprimidos por un algoritmo.
     */
    private int archivosComprimidos;
    /**
     * Numero de archivos descomprimidos por un algoritmo.
     */
    private int archivosDescomprimidos;
    /**
     * Tiempo medio de ejecución de las compresiones de un algoritmo.
     */
    private long tiempoCompresion;
    /**
     * Tiempo medio de ejecución de las descompresiones de un algoritmo.
     */
    private long tiempoDescompresion;
    /**
     *  Porcentaje medio de los archivos comprimidos de un algoritmo
     */
    private double RatioCompresion;
    /**
     *  Porcentaje medio de los archivos descomprimidos de un algoritmo
     */
    private double RatioDescompresion;

    private double velocidadCompresion;

    private double velocidadDescompresion;



    /**
     * Constructora de la clase, la cual crea una instancia de DatosEstadísctica con los 6 atributos asignados
     * @param comprimidos Los archivos que han sido comprimidos por un algoritmo
     * @param tiempoC  El tiempo medio de compresión de un algoritmo
     * @param ratioC El porcentaje medio de compresión de un algoritmo
     * @param descomprimidos Los archivos que han sido descomprimidos por un algoritmo
     * @param tiempoD  El tiempo medio de descompresión de un algoritmo
     * @param ratioD El porcentaje medio de descompresión de un algoritmo
     * @param velocidadC Velocidad media de compresión de un algoritmo
     * @param velocidadD Velocidad media de descompresión de un algoritmo
     */
    public DatosEstadistica(int comprimidos, long tiempoC, double ratioC, double velocidadC, int descomprimidos, long tiempoD, double ratioD, double velocidadD) {
        archivosComprimidos = comprimidos;
        tiempoCompresion = tiempoC;
        RatioCompresion = ratioC;
        archivosDescomprimidos = descomprimidos;
        tiempoDescompresion = tiempoD;
        RatioDescompresion = ratioD;
        velocidadCompresion = velocidadC;
        velocidadDescompresion = velocidadD;
    }

    /**
     * @return El numero de archivos comprimidos
     */
    public int getArchivosComprimidos() {
        return archivosComprimidos;
    }

    /**
     * @return El numero de archivos descomprimidos
     */
    public int getArchivosDescomprimidos() {
        return archivosDescomprimidos;
    }

    /**
     * @return El tiempo medio de compresión
     */
    public long getTiempoCompresion() {
        return tiempoCompresion;
    }

    /**
     * @return El tiempo medio de descompresión
     */
    public long getTiempoDescompresion() {
        return tiempoDescompresion;
    }

    /**
     * @return El porcentaje medio de compresión
     */
    public double getRatioCompresion() {
        return RatioCompresion;
    }

    /**
     * @return El porcentaje medio de descompresión
     */
    public double getRatioDescompresion() {
        return RatioDescompresion;
    }

    public double getVelocidadCompresion() { return velocidadCompresion; }

    public double getVelocidadDescompresion() {
        return velocidadDescompresion;
    }
}
