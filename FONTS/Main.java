// Creado por Joan Gamez Rodriguez
import Controllers.CtrlProcesos;
import DomainLayer.Algoritmos.*;
import DomainLayer.Proceso.DatosProceso;
import DomainLayer.Proceso.ProcesoComprimir;
import DomainLayer.Proceso.ProcesoDescomprimir;
import DomainLayer.Proceso.ProcesoFichero;

import java.io.File;


public class Main {
    public static void main(String[] args) throws Exception {
        File fileIn = new File(args[0]);
        String algoritmo = args[1];
        CtrlProcesos ctrlProcesos = CtrlProcesos.getInstance();
        Algoritmos tipoCompresor;
        switch (algoritmo) {
            case "jpeg":
                tipoCompresor = Algoritmos.JPEG;
                break;
            case "lzss":
                tipoCompresor = Algoritmos.LZSS;
                break;
            case "lzw":
                tipoCompresor = Algoritmos.LZW;
                break;
            case "lz78":
                tipoCompresor = Algoritmos.LZ78;
                break;
            case "predeterminado":
                tipoCompresor = Algoritmos.PREDETERMINADO;
                break;
            default:
                throw new EnumConstantNotPresentException(Algoritmos.class, "El tipo de compresor " + algoritmo + " no existe.\n");
        }
        File fileComp = ctrlProcesos.comprimirArchivo(fileIn, tipoCompresor);
        if(fileComp != null) System.out.println("El archivo se ha comprimido correctamente");
        File fileDesc = ctrlProcesos.descomprimirArchivo(fileComp);
        if(fileDesc != null) System.out.println("El archivo se ha descomprimido correctamente");
    }
}

