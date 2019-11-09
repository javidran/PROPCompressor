// Creado por Joan Gamez Rodriguez
package Controllers;

import DomainLayer.Algoritmos.Algoritmos;
import DomainLayer.Algoritmos.JPEG;
import DomainLayer.Proceso.DatosProceso;
import DomainLayer.Proceso.ProcesoComprimir;
import DomainLayer.Proceso.ProcesoDescomprimir;
import DomainLayer.Proceso.ProcesoFichero;
import Exceptions.ArchivoYaExisteException;
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

    public void comprimirArchivo(String path, Algoritmos tipoAlgoritmo) throws Exception {
        CtrlDatos ctrlDatos = CtrlDatos.getInstance();
        ProcesoFichero comp = new ProcesoComprimir(ctrlDatos.leerArchivo(path), tipoAlgoritmo);
        comp.ejecutarProceso();
        ctrlDatos.guardaArchivo(comp.getOutput(), path, tipoAlgoritmo, true, true);
        DatosProceso dp = comp.getDatosProceso();
        if(dp != null) {
            ctrlDatos.actualizaEstadistica(dp, tipoAlgoritmo, true);
        }
    }

    public void descomprimirArchivo(String path) throws Exception {
        CtrlDatos ctrlDatos = CtrlDatos.getInstance();
        Algoritmos[] algoritmos = algoritmosPosibles(path);
        ProcesoFichero desc = new ProcesoDescomprimir(ctrlDatos.leerArchivo(path), algoritmos[0]);
        desc.ejecutarProceso();
        ctrlDatos.guardaArchivo(desc.getOutput(), path, algoritmos[0], false, true);
        DatosProceso dp = desc.getDatosProceso();
        if(dp != null) {
            ctrlDatos.actualizaEstadistica(dp, algoritmos[0], false);
        }
    }

    public void comprimirDescomprimirArchivo(String path, Algoritmos tipoAlgoritmo) throws Exception {
        CtrlDatos ctrlDatos = CtrlDatos.getInstance();
        ProcesoFichero comp = new ProcesoComprimir(ctrlDatos.leerArchivo(path), tipoAlgoritmo);
        comp.ejecutarProceso();
        Algoritmos[] algoritmos = algoritmosPosibles(path);
        ProcesoFichero desc = new ProcesoDescomprimir(comp.getOutput(), algoritmos[0]);
        desc.ejecutarProceso();
        ctrlDatos.guardaArchivo(desc.getOutput(), path, algoritmos[0], false, true);
        DatosProceso dp = desc.getDatosProceso();
        if(dp != null) {
            ctrlDatos.actualizaEstadistica(dp, algoritmos[0], false);
        }
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

    public static boolean esDescomprimible(String path) throws FormatoErroneoException {
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
