// Creado por Joan Gamez Rodriguez
import DomainLayer.Algoritmos.*;
import DomainLayer.Proceso.DatosProceso;
import DomainLayer.Proceso.ProcesoComprimir;
import DomainLayer.Proceso.ProcesoDescomprimir;
import DomainLayer.Proceso.ProcesoFichero;

import java.io.File;


public class Main {
    public static void main(String[] args) throws Exception {
        File fileIn = new File(args[0]);
        File fileComp = null, fileOut = null;
        ProcesoFichero comp;
        ProcesoFichero desc;

        Algoritmos tipoCompresor = Algoritmos.LZSS;

        comp = new ProcesoComprimir(fileIn);
        comp.setTipoC(tipoCompresor);
        comp.ejecutarProceso();

        //Testing Datos proceso
        DatosProceso test = comp.getDatosProceso();
        long time = test.getTiempo();
        long newSize = test.getNuevoTamaño();
        long oldSize = test.getAntiguoTamaño();
        long ladiferencia = test.diffTam();
        double elpercent = test.diffTamPercent();

        System.out.println("tiempo: "+time+" nuevoTamaño: "+newSize+" antiguotamaño: "+ oldSize+" la diferencia es: "+ladiferencia+" que es un:"+elpercent+"%");

        //End Testing Datos proceso

        if(comp.isProcesado()) System.out.println("El archivo se ha comprimido correctamente");
        desc = new ProcesoDescomprimir(comp.getFicheroOut());
        desc.setTipoC(tipoCompresor);
        desc.ejecutarProceso();

        //Testing Datos proceso
        test = desc.getDatosProceso();
         time = test.getTiempo();
         newSize = test.getNuevoTamaño();
         oldSize = test.getAntiguoTamaño();
         ladiferencia = test.diffTam();
         elpercent = test.diffTamPercent();

        System.out.println("tiempo: "+time+" nuevoTamaño: "+newSize+" antiguotamaño: "+ oldSize+" la diferencia es: "+ladiferencia+" que es un:"+elpercent+"%");

        //End Testing Datos proceso

        if(desc.isProcesado()) System.out.println("El archivo se ha descomprimido correctamente");
    }
}

