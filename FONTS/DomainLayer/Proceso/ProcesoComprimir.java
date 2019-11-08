// Creado por Javier Cabrera Rodriguez
package DomainLayer.Proceso;

import Controllers.CtrlProcesos;
import DomainLayer.Algoritmos.Algoritmos;
import DomainLayer.Algoritmos.AnalizadorArchivo;
import DomainLayer.Algoritmos.OutputAlgoritmo;
import Exceptions.FormatoErroneoException;

import java.io.File;

public class ProcesoComprimir extends ProcesoFichero {

    public ProcesoComprimir(File input) throws Exception {
        super(input);
        String path = ficheroIn.getAbsolutePath();
        if(!AnalizadorArchivo.esComprimible(path)) throw new FormatoErroneoException("Este archivo no es comprimible!");
        Algoritmos[] tipos = AnalizadorArchivo.algoritmosPosibles(path);
        if(tipos[0]==Algoritmos.JPEG) tipoAlgoritmo = Algoritmos.JPEG;
        else tipoAlgoritmo = CtrlProcesos.getAlgoritmoPredeterminadoTexto();
        asignarAlgoritmo();
    }

    @Override
    public void ejecutarProceso() throws Exception {
        if(!procesado) {
            OutputAlgoritmo outputAlgoritmo = compresorDecompresor.comprimir(ficheroIn);
            ficheroOut = outputAlgoritmo.outputFile;
            procesado = true;
            datos = new DatosProceso(outputAlgoritmo.tiempo,ficheroIn.length(),ficheroOut.length());
            //guardaDatos();
        } else throw new Exception("El fichero ya ha sido comprimido!");
    }

    @Override
    public boolean esComprimir() {
        return true;
    }

}
