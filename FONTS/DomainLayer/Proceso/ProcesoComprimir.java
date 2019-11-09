// Creado por Javier Cabrera Rodriguez
package DomainLayer.Proceso;

import DomainLayer.Algoritmos.Algoritmos;
import DomainLayer.Algoritmos.OutputAlgoritmo;

public class ProcesoComprimir extends ProcesoFichero {

    public ProcesoComprimir(byte[] input, Algoritmos algoritmo) throws Exception {
        super(input, algoritmo);
    }

    @Override
    public void ejecutarProceso() throws Exception {
        if(!procesado) {
            OutputAlgoritmo outputAlgoritmo = compresorDecompresor.comprimir(input);
            output = outputAlgoritmo.output;
            procesado = true;
            datos = new DatosProceso(outputAlgoritmo.tiempo,input.length,output.length);
        } else throw new Exception("El fichero ya había sido comprimido!");
    }

    @Override
    public boolean esComprimir() {
        return true;
    }
}
