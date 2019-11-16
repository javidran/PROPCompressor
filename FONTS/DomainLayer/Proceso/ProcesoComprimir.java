package DomainLayer.Proceso;

import DomainLayer.Algoritmos.Algoritmo;
import DomainLayer.Algoritmos.OutputAlgoritmo;

public class ProcesoComprimir extends ProcesoFichero {

    public ProcesoComprimir(byte[] input, Algoritmo algoritmo) throws Exception {
        super(input, algoritmo);
    }

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

    @Override
    public boolean esComprimir() {
        return true;
    }
}
