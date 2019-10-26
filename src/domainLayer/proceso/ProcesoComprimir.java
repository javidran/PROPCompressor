package domainLayer.proceso;

import domainLayer.TipoCompresor;
import domainLayer.algoritmos.OutputAlgoritmo;

import java.io.File;

public class ProcesoComprimir extends ProcesoFichero {

    public ProcesoComprimir(File input) throws Exception {
        super(input);
        TipoCompresor[] tipos = null;
        if((tipos=tiposPosibles()) !=null) {
            tipoC = tipos[0];
            asignarAlgoritmo();
        } else throw new Exception("No hay ningun tipo de compresor compatible");
    }

    @Override
    public TipoCompresor[] tiposPosibles() {
        if (ficheroIn.getAbsolutePath().endsWith(".txt") ) {
            return new TipoCompresor[] {TipoCompresor.LZ78, TipoCompresor.LZW, TipoCompresor.LZSS};
        }
        else if (ficheroIn.getAbsolutePath().endsWith(".ppm")) {
            return new TipoCompresor[] {TipoCompresor.JPEG};
        }
        return null;
    }

    public void ejecutarProceso() throws Exception {
        if(!procesado) {
            OutputAlgoritmo outputAlgoritmo = compresorDecompresor.comprimir(ficheroIn);
            ficheroOut = outputAlgoritmo.outputFile;
            procesado = true;
        } else throw new Exception("El fichero ya ha sido comprimido!");
    }

}
