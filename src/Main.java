import DomainLayer.Algoritmos.*;
import DomainLayer.Proceso.ProcesoComprimir;
import DomainLayer.Proceso.ProcesoDescomprimir;

import java.io.File;


public class Main {
    public static void main(String[] args) throws Exception {
        File fileIn = new File(args[0]);
        File fileComp = null, fileOut = null;
        ProcesoComprimir comp;
        ProcesoDescomprimir desc;

        Algoritmos tipoCompresor = Algoritmos.JPEG;

        comp = new ProcesoComprimir(fileIn);
        comp.setTipoC(tipoCompresor);
        comp.ejecutarProceso();
        desc = new ProcesoDescomprimir(comp.getFicheroOut());
        desc.setTipoC(tipoCompresor);
        desc.ejecutarProceso();
        if(desc.isProcesado()) System.out.println("El archivo se ha comprimido y descomprimido correctamente");

    }
}

