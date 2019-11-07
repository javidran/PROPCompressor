// Creado por Javier Cabrera Rodriguez
package DomainLayer.Proceso;

import DomainLayer.Algoritmos.Algoritmos;
import DomainLayer.Algoritmos.AnalizadorArchivo;
import DomainLayer.Algoritmos.OutputAlgoritmo;
import Exceptions.FormatoErroneoException;

import java.io.File;

public class ProcesoDescomprimir extends ProcesoFichero {

    public ProcesoDescomprimir(File input) throws Exception {
        super(input);
        String path = ficheroIn.getAbsolutePath();
        if(AnalizadorArchivo.esComprimible(path)) throw new FormatoErroneoException("Este archivo no es descomprimible!");
        Algoritmos[] tipos = AnalizadorArchivo.algoritmosPosibles(path);
        tipoAlgoritmo = tipos[0];
        asignarAlgoritmo();
    }

    @Override
    public void ejecutarProceso() throws Exception {
        if(!procesado) {
            OutputAlgoritmo outputAlgoritmo = compresorDecompresor.descomprimir(ficheroIn);
            ficheroOut = outputAlgoritmo.outputFile;
            procesado = true;
            datos = new DatosProceso(outputAlgoritmo.tiempo,ficheroIn.length(),ficheroOut.length());
            //guardaDatos();
        } else throw new Exception("El fichero ya ha sido descomprimido!");
    }

    @Override
    public boolean esComprimir() {
        return false;
    }
}
