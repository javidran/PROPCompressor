package DomainLayer.Proceso;

import DomainLayer.Algoritmos.*;
import DomainLayer.Algoritmos.LZ78;


/**
 *  Clase abstracta que recoge todos los datos necesarios para poder comprimir y descomprimir con cualquier tipo de algoritmo.
 *  También se encarga de recoger todos los datos posteriores a la ejecución del proceso como medidas para estadística.
 */
public abstract class ProcesoFichero {

    /**
     * Secuencia de bytes a procesar.
     */
    protected byte[] input;
    /**
     * Secuencia de bytes procesada.
     * Declarada como null hasta que no se ejecute el proceso.
     */
    protected byte[] output = null;
    /**
     * Enumeration.Algoritmo que utilizará el proceso a comprimir.
     */
    protected Algoritmo tipoAlgoritmo;
    /**
     * Instancia del algoritmo que utilizará el proceso.
     */
    protected CompresorDecompresor compresorDecompresor;
    /**
     * Indica si el proceso ya ha sido ejecutado o no.
     */
    protected boolean procesado;
    /**
     * Instancia de los datos estadísiticos sobre el proceso ejecutado.
     * Declarada como null hasta que no se ejecute el proceso.
     */
    protected DatosProceso datos = null;


    /**
     * Constructora de proceso de un fichero.
     * <p>
     * Dado que es una clase abstracta no se puede llamar directamente, solo a través de las clases que extienden de ProcesoFichero.
     * </p>
     * @param input Secuencia de bytes a procesar.
     * @param algoritmo Enumeration.Algoritmo de compresión a utilizar para procesar el {@link #input}.
     */
    protected ProcesoFichero(byte[] input, Algoritmo algoritmo) {
        procesado = false;
        this.input = input;
        output = null;
        tipoAlgoritmo = algoritmo;
        asignarAlgoritmo();
    }


    /**
     * Devuelve la instancia de DatosProceso del proceso ejecutado. Si este aún no se ha ejecutado la instancia es null.
     * @return Instancia de DatosProceso.
     */
    public DatosProceso getDatosProceso() {
        return datos;
    }


    /**
     * Asigna la instancia del algoritmo adecuado según el valor de {@link #tipoAlgoritmo}.
     */
    protected void asignarAlgoritmo() {
        switch (tipoAlgoritmo) {
            case JPEG:
                compresorDecompresor = JPEG.getInstance();
                break;
            case LZW:
                compresorDecompresor = LZW.getInstance();
                break;
            case LZSS:
                compresorDecompresor = LZSS.getInstance();
                break;
            case LZ78:
                compresorDecompresor = LZ78.getInstance();
                break;
            default:
                throw new EnumConstantNotPresentException(Algoritmo.class, "El tipo de compresor" + tipoAlgoritmo + "no existe");
        }
    }


    /**
     * Método abstracto que ejecuta el proceso de compresión o descompresión del input y genera la instancia para estadística.
     * @throws Exception El algoritmo no se ha ejecutado correctamente y no ha podido terminar.
     */
    public abstract void ejecutarProceso() throws Exception;


    /**
     * Indica si el proceso está creado para comprimir o no.
     * @return Si el proceso está creado con intención de comprimir.
     */
    public abstract boolean esComprimir();


    /**
     * Indica si el proceso ya ha sido ejecutado.
     * @return Si el proceso ya ha sido ejecutado.
     */
    public boolean isProcesado() {
        return procesado;
    }

    /**
     * Devuelve la secuencia de bytes a procesar.
     * @return Secuencia de bytes a procesar.
     */
    public byte[] getInput() {
        return input;
    }

    /**
     * Devuelve la secuencia de bytes procesada.
     * @return Secuencia de bytes procesada. Null si aún no se ha ejecutado el proceso.
     */
    public byte[] getOutput() {
        return output;
    }


    /**
     * Devuelve el algoritmo que se ha asignado al proceso en el momento de su creación.
     * @return El algoritmo asignado al proceso.
     */
    public Algoritmo getTipoCompresor() {
        return tipoAlgoritmo;
    }

}
