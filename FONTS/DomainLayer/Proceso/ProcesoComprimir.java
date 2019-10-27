// Creado por Javier Cabrera Rodriguez
package DomainLayer.Proceso;

import DomainLayer.Algoritmos.Algoritmos;
import DomainLayer.Algoritmos.OutputAlgoritmo;

import java.io.File;

public class ProcesoComprimir extends ProcesoFichero {

    public ProcesoComprimir(File input) throws Exception {
        super(input);
        Algoritmos[] tipos = null;
        if((tipos=tiposPosibles()) !=null) {
            tipoC = tipos[0];
            asignarAlgoritmo();
        } else throw new Exception("No hay ningun tipo de compresor compatible");
    }

    @Override
    public Algoritmos[] tiposPosibles() {
        if (ficheroIn.getAbsolutePath().endsWith(".txt") ) {
            return new Algoritmos[] {Algoritmos.LZSS, Algoritmos.LZW, Algoritmos.LZ78};
        }
        else if (ficheroIn.getAbsolutePath().endsWith(".ppm")) {
            return new Algoritmos[] {Algoritmos.JPEG};
        }
        return null;
    }

    @Override
    public void ejecutarProceso() throws Exception {
        if(!procesado) {
            OutputAlgoritmo outputAlgoritmo = compresorDecompresor.comprimir(ficheroIn);
            ficheroOut = outputAlgoritmo.outputFile;
            procesado = true;
            datos = new DatosProceso(outputAlgoritmo.tiempo,ficheroIn.length(),ficheroOut.length());
        } else throw new Exception("El fichero ya ha sido comprimido!");
    }

}
