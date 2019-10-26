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

        switch (tipoCompresor) {
            case JPEG:
                comp = new ProcesoComprimir(fileIn);
                comp.setTipoC(Algoritmos.JPEG);
                comp.ejecutarProceso();
                desc = new ProcesoDescomprimir(comp.getFicheroOut());
                if(desc.isProcesado()) System.out.println("El archivo se ha comprimido y descomprimido correctamente");
                break;
            case LZSS:
                comp = new ProcesoComprimir(fileIn);
                comp.setTipoC(Algoritmos.LZSS);
                comp.ejecutarProceso();
                desc = new ProcesoDescomprimir(comp.getFicheroOut());
                if(desc.isProcesado()) System.out.println("El archivo se ha comprimido y descomprimido correctamente");
                break;
        }
    }
}

