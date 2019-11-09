// Creado por Javier Cabrera Rodriguez
package DomainLayer.Proceso;

import DomainLayer.Algoritmos.Algoritmos;
import DomainLayer.Algoritmos.AnalizadorArchivo;
import DomainLayer.Algoritmos.OutputAlgoritmo;
import Exceptions.FormatoErroneoException;

import java.io.File;

public class ProcesoDescomprimir extends ProcesoFichero {

    public ProcesoDescomprimir(byte[] input, Algoritmos algoritmo) throws Exception {
        super(input, algoritmo);
    }

    @Override
    public void ejecutarProceso() throws Exception {
        if(!procesado) {
            OutputAlgoritmo outputAlgoritmo = compresorDecompresor.descomprimir(ficheroIn);
            ficheroOut = outputAlgoritmo.output;
            procesado = true;
            datos = new DatosProceso(outputAlgoritmo.tiempo,ficheroIn.length,ficheroOut.length);
            //guardaDatos();
        } else throw new Exception("El fichero ya ha sido descomprimido!");
    }

    @Override
    public boolean esComprimir() {
        return false;
    }
}
