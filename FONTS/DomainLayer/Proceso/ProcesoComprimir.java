package DomainLayer.Proceso;

import DomainLayer.Algoritmos.Algoritmo;
import DomainLayer.Algoritmos.OutputAlgoritmo;

/**
 *  Clase que recoge todos los datos necesarios para poder comprimir con cualquier tipo de algoritmo.
 *  También se encarga de recoger todos los datos posteriores a la ejecución del proceso como medidas para estadística.
 */
public class ProcesoComprimir extends ProcesoFichero {

    /**
     * Constructora de proceso de compresión de un fichero.
     * @param input Secuencia de bytes a procesar.
     * @param algoritmo Algoritmo de compresión a utilizar para procesar el {@link #input}.
     */
    public ProcesoComprimir(byte[] input, Algoritmo algoritmo) throws Exception {
        super(input, algoritmo);
    }


    /**
     * Método que ejecuta el proceso de compresión y genera la instancia para estadística.
     * @throws Exception El algoritmo no se ha ejecutado correctamente y no ha podido terminar.
     */
    @Override
    public void ejecutarProceso() throws Exception {
        if(!procesado) {
            OutputAlgoritmo outputAlgoritmo = compresorDecompresor.comprimir(input);
            output = outputAlgoritmo.output;
            procesado = true;
            try {
                datos = new DatosProceso(outputAlgoritmo.tiempo, input.length, output.length, esComprimir());
            } catch (Exception e) {
                datos = null;
            }
        } else throw new Exception("El fichero ya había sido comprimido!");
    }

    /**
     * Indica si el proceso está creado para comprimir o no el input.
     * @return True.
     */
    @Override
    public boolean esComprimir() {
        return true;
    }
}
