// Creado por Javier Cabrera Rodriguez
package DomainLayer.Proceso;

import DomainLayer.Algoritmos.Algoritmo;
import DomainLayer.Algoritmos.OutputAlgoritmo;

public class ProcesoDescomprimir extends ProcesoFichero {

    public ProcesoDescomprimir(byte[] input, Algoritmo algoritmo) throws Exception {
        super(input, algoritmo);
    }

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

    @Override
    public boolean esComprimir() {
        return false;
    }
}
