// Creado por Joan Gamez Rodriguez
package Controllers;

import DomainLayer.Algoritmos.Algoritmo;
import DomainLayer.Algoritmos.JPEG;
import DomainLayer.Proceso.DatosProceso;
import DomainLayer.Proceso.ProcesoComprimir;
import DomainLayer.Proceso.ProcesoDescomprimir;
import DomainLayer.Proceso.ProcesoFichero;
import Exceptions.FormatoErroneoException;

public class CtrlProcesos {
    private static CtrlProcesos instance = null;
    private static Algoritmo algoritmoDeTextoPredeterminado = Algoritmo.LZSS;

    public static CtrlProcesos getInstance()
    {
        if (instance == null)
            instance = new CtrlProcesos();

        return instance;
    }

    public void comprimirArchivo(String path, Algoritmo tipoAlgoritmo) throws Exception {
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
        Algoritmo[] algoritmos = algoritmosPosibles(path);
        ProcesoFichero desc = new ProcesoDescomprimir(ctrlDatos.leerArchivo(path), algoritmos[0]);
        desc.ejecutarProceso();
        ctrlDatos.guardaArchivo(desc.getOutput(), path, algoritmos[0], false, true);
        DatosProceso dp = desc.getDatosProceso();
        if(dp != null) {
            ctrlDatos.actualizaEstadistica(dp, algoritmos[0], false);
        }
    }

    public void comprimirDescomprimirArchivo(String path, Algoritmo tipoAlgoritmo) throws Exception {
        CtrlDatos ctrlDatos = CtrlDatos.getInstance();
        ProcesoFichero comp = new ProcesoComprimir(ctrlDatos.leerArchivo(path), tipoAlgoritmo);
        comp.ejecutarProceso();
        DatosProceso dp = comp.getDatosProceso();
        if(dp != null) {
            ctrlDatos.actualizaEstadistica(dp, tipoAlgoritmo, true);
        }
        ProcesoFichero desc = new ProcesoDescomprimir(comp.getOutput(), tipoAlgoritmo);
        desc.ejecutarProceso();
        ctrlDatos.guardaArchivo(desc.getOutput(), path, tipoAlgoritmo, false, true);
        dp = desc.getDatosProceso();
        if(dp != null) {
            ctrlDatos.actualizaEstadistica(dp, tipoAlgoritmo, false);
        }
    }

    public void setAlgoritmoDeTextoPredeterminado(Algoritmo algoritmoDeTextoPredeterminado) {
        CtrlProcesos.algoritmoDeTextoPredeterminado = algoritmoDeTextoPredeterminado;
    }

    public Algoritmo getAlgoritmoDeTextoPredeterminado() {
        return algoritmoDeTextoPredeterminado;
    }

    public void setCalidadJPEG(int calidadJPEG) {
        JPEG.getInstance().setCalidad(calidadJPEG);
    }

    public static Algoritmo[] algoritmosPosibles(String path) throws FormatoErroneoException {
        String[] splittedPath = path.split("\\.");
        String type = splittedPath[splittedPath.length-1];

        switch (type) {
            case "txt":
                return new Algoritmo[] {Algoritmo.LZSS, Algoritmo.LZW, Algoritmo.LZ78};
            case "ppm":
            case "imgc":
                return new Algoritmo[] {Algoritmo.JPEG};
            case "lzss":
                return new Algoritmo[] {Algoritmo.LZSS};
            case "lz78":
                return new Algoritmo[] {Algoritmo.LZ78};
            case "lzw":
                return new Algoritmo[] {Algoritmo.LZW};
            default:
                throw new FormatoErroneoException("No hay ningun tipo de algoritmo compatible");
        }
    }

    /**
     * Comprueba si el archivo es capaz de ser comprimido según la extensión del mismo.
     * <p>
     *     El path del archivo debe seguir el formato general de cualquier tipo de path de archivo y puede ser relativo o absoluto.
     * </p>
     *
     * @param path El path del archivo que se quiere comprobar.
     * @return Un booleano que indica si el archivo es comprimible.
     * @throws FormatoErroneoException No hay ningún algoritmo compatible con la extensión del archivo.
     */
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
