package DomainLayer.Proceso;

import DomainLayer.Algoritmos.Algoritmo;
import DomainLayer.Algoritmos.OutputAlgoritmo;

/**
 *  Clase que recoge todos los datos necesarios para poder descomprimir con cualquier tipo de algoritmo.
 *  También se encarga de recoger todos los datos posteriores a la ejecución del proceso como medidas para estadística.
 */
public class ProcesoDescomprimir extends ProcesoFichero {

    /**
     * Constructora de proceso de descompresión de un fichero.
     * @param input Secuencia de bytes a procesar.
     * @param algoritmo Algoritmo de compresión a utilizar para procesar el {@link #input}.
     */
    public ProcesoDescomprimir(byte[] input, Algoritmo algoritmo) {
        super(input, algoritmo);
    }

    /**
     * Método que ejecuta el proceso de descompresión y genera la instancia para estadística.
     * @throws Exception El algoritmo no se ha ejecutado correctamente y no ha podido terminar.
     */
    @Override
    public void ejecutarProceso() throws Exception {
        if(!procesado) {
            OutputAlgoritmo outputAlgoritmo = compresorDecompresor.descomprimir(input);
            output = outputAlgoritmo.output;
            procesado = true;
            try {
                datos = new DatosProceso(outputAlgoritmo.tiempo, input.length, output.length, esComprimir());
            } catch (Exception e) {
                datos = null;
            }
        } else throw new Exception("El fichero ya ha sido descomprimido!");
    }

    /**
     * Indica si el proceso está creado para comprimir o no.
     * @return False.
     */
    @Override
    public boolean esComprimir() {
        return false;
    }
}
