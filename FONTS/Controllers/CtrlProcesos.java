// Creado por Joan Gamez Rodriguez
package Controllers;

import DomainLayer.Algoritmos.Algoritmos;
import DomainLayer.Algoritmos.JPEG;
import DomainLayer.Proceso.ProcesoComprimir;
import DomainLayer.Proceso.ProcesoDescomprimir;
import DomainLayer.Proceso.ProcesoFichero;
import Exceptions.FormatoErroneoException;

import java.io.File;

public class CtrlProcesos {
    private static CtrlProcesos instance = null;
    private static Algoritmos algoritmoPredeterminadoTexto = Algoritmos.LZSS;

    public static CtrlProcesos getInstance()
    {
        if (instance == null)
            instance = new CtrlProcesos();

        return instance;
    }

    public File comprimirArchivo(String path, Algoritmos tipoAlgoritmo) throws Exception {
        ProcesoFichero comp = new ProcesoComprimir(path);
        if (tipoAlgoritmo != Algoritmos.PREDETERMINADO)  comp.setTipoAlgoritmo(tipoAlgoritmo);
        comp.ejecutarProceso();
        return comp.getFicheroOut();
    }

    public File comprimirArchivo(String path) throws Exception {
        ProcesoFichero comp = new ProcesoComprimir(path);
        comp.ejecutarProceso();
        return comp.getFicheroOut();
    }

    public File descomprimirArchivo(String path) throws Exception {
        ProcesoFichero desc = new ProcesoDescomprimir(path);
        desc.ejecutarProceso();
        return desc.getFicheroOut();
    }

    public static void setAlgoritmoPredeterminadoTexto(Algoritmos algoritmoPredeterminadoTexto) {
        CtrlProcesos.algoritmoPredeterminadoTexto = algoritmoPredeterminadoTexto;
    }

    public static Algoritmos getAlgoritmoPredeterminadoTexto() {
        return algoritmoPredeterminadoTexto;
    }

    public void setCalidadJPEG(int calidadJPEG) {
        JPEG.getInstance().setCalidad(calidadJPEG);
    }

    public static Algoritmos[] algoritmosPosibles(String path) throws FormatoErroneoException {
        String[] splittedPath = path.split("\\.");
        String type = splittedPath[splittedPath.length-1];

        switch (type) {
            case "txt":
                return new Algoritmos[] {Algoritmos.LZSS, Algoritmos.LZW, Algoritmos.LZ78};
            case "ppm":
            case "imgc":
                return new Algoritmos[] {Algoritmos.JPEG};
            case "lzss":
                return new Algoritmos[] {Algoritmos.LZSS};
            case "lz78":
                return new Algoritmos[] {Algoritmos.LZ78};
            case "lzw":
                return new Algoritmos[] {Algoritmos.LZW};
            default:
                throw new FormatoErroneoException("No hay ningun tipo de algoritmo compatible");
        }
    }

    public static boolean esComprimible(String path) throws FormatoErroneoException {
        String[] splitP = path.split("\\.");
        String type = splitP[splitP.length-1];

        switch (type) {
            case "txt":
            case "ppm":
                return true;
            case "imgc":
            case "lzss":
            case "lz78":
            case "lzw":
                return false;
            default:
                throw new FormatoErroneoException("No hay ningun tipo de algoritmo compatible");
        }
    }

    public static boolean esDesomprimible(String path) throws FormatoErroneoException {
        String[] splitP = path.split("\\.");
        String type = splitP[splitP.length-1];

        switch (type) {
            case "txt":
            case "ppm":
                return false;
            case "imgc":
            case "lzss":
            case "lz78":
            case "lzw":
                return true;
            default:
                throw new FormatoErroneoException("No hay ningun tipo de algoritmo compatible");
        }
    }
}
