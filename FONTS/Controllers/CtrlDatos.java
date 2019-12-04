package Controllers;

import DataLayer.*;
import DomainLayer.Proceso.DatosProceso;
import Enumeration.Algoritmo;
import Exceptions.FormatoErroneoException;

import java.io.FileNotFoundException;
import java.io.IOException;

public class CtrlDatos {
    /**
     * Instancia de CtrlDatos para garantizar que es una clase Singleton
     */
    private static CtrlDatos instance = null;


    private GestorCarpeta gestorCarpeta;


    /**
     * Getter de la instancia Singleton de CtrlDatoos
     * @return La instancia Singleton de CtrlDatos
     */
    public static CtrlDatos getInstance()
    {
        if (instance == null)
            instance = new CtrlDatos();

        return instance;
    }

    /**
     * Comprueba qué algoritmos se pueden usar para comprimir o descomprimir un fichero, el cual se pasa por parámetro.
     * <p>
     *     El path del archivo debe seguir el formato general de cualquier tipo de path de archivo y puede ser relativo o absoluto.
     * </p>
     * @param path El path del archivo a comprobar
     * @return Un vector de algoritmos posibles a ejecutar para comprimir o descomprimir el fichero
     * @throws FormatoErroneoException No hay ningún algoritmo compatible con la extensión del archivo
     */
    public static Algoritmo[] algoritmosPosibles(String path) throws FormatoErroneoException {
        if (!path.contains(".")) return new Algoritmo[] {Algoritmo.CARPETA};
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
            case "comp":
                return new Algoritmo[] {Algoritmo.CARPETA};
            default:
                throw new FormatoErroneoException("No hay ningun tipo de algoritmo compatible");
        }
    }

    /**
     * Comprueba si el archivo es capaz de ser comprimido según la extensión del mismo.
     * <p>
     *     El path del archivo debe seguir el formato general de cualquier tipo de path de archivo y puede ser relativo o absoluto.
     * </p>
     * @param path El path del archivo que se quiere comprobar
     * @return Un booleano que indica si el archivo es comprimible
     * @throws FormatoErroneoException No hay ningún algoritmo compatible con la extensión del archivo
     */
    public static boolean esComprimible(String path) throws FormatoErroneoException {
        if(!path.contains(".")) return true;
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
            case "comp":
                return false;
            default:
                throw new FormatoErroneoException("No hay ningun tipo de algoritmo compatible");
        }
    }

    public static boolean existeArchivo(String path) {
        return GestorArchivo.existeArchivo(path);
    }

    /**
     * Llama a la clase GestorArchivo para que lea el archivo del path.
     * <p>
     *     El path del archivo debe seguir el formato general de cualquier tipo de path de archivo y puede ser relativo o absoluto.
     * </p>
     *
     * @param path El path del archivo que se quiere leer.
     * @return Un byte[] del contenido del archivo del path.
     * @throws IOException No hay ningún archivo en el path que se pasa o no se ha leído bien.
     */
    public byte[] leerArchivo (String path) throws IOException {
        return GestorArchivo.leeArchivo(path);
    }

    /**
     * Llama a la clase GestorArchivo para que guarde el archivo del path.
     * <p>
     *     El path del archivo debe seguir el formato general de cualquier tipo de path de archivo y puede ser relativo o absoluto.
     * </p>
     * @param data Los datos que se guardaran en el archivo.
     * @param path El path del archivo donde se quiere guardar.
     * @param algoritmo El algoritmo con el qual se ha comprimido o descomprimido el archivo.
     * @param esCompresion Un boolean que indica si se ha hecho compresión o descompresión.
     * @param sobreescribir Un boolean que indica si se va a sobreescribir.
     * @throws IOException No se ha podido escribir en el archivo por el path indicado.
     */
    public void guardaArchivo (byte[] data, String path, Algoritmo algoritmo, boolean esCompresion, boolean sobreescribir) throws IOException {
        path = actualizarPathSalida(path, algoritmo, esCompresion);
        GestorArchivo.guardaArchivo(data, path, sobreescribir);
    }

    private static String extension(Algoritmo algoritmo, boolean esCompresion) {
        String extension = null;
        if(esCompresion) {
            switch (algoritmo) {
                case LZW:
                    extension = "lzw";
                    break;
                case LZSS:
                    extension = "lzss";
                    break;
                case LZ78:
                    extension = "lz78";
                    break;
                case JPEG:
                    extension = "imgc";
                    break;
                case CARPETA:
                    extension = "comp";
                    break;
            }
        } else {
            switch (algoritmo) {
                case LZW:
                case LZSS:
                case LZ78:
                    extension = "txt";
                    break;
                case JPEG:
                    extension = "ppm";
                    break;
                case CARPETA:
                    extension = "";
                    break;
            }
        }
        return extension;
    }

    /**
     * Actualiza el path pasado por parámetro para que sea el path de salida del fichero procesado.
     * @param path El path del archivo antes de ser procesado.
     * @param algoritmo El algorimo usado en el proceso.
     * @param esCompresion Indicador de si el proceso ha sido de compresión o no (y por tanto de descompresión).
     * @return Path del archivo procesado, con su correspondiente extensión.
     */
    public static String actualizarPathSalida(String path, Algoritmo algoritmo, boolean esCompresion) {
        String[] splitP = path.split("\\.");
        String type = splitP[splitP.length-1];
        String ext = extension(algoritmo, esCompresion);
        if(!path.contains(".")) path = path + "." + ext;
        else if(splitP.length==1) path = path + ext;
        else if (!type.equalsIgnoreCase(ext)) {
            if(algoritmo.equals(Algoritmo.CARPETA) && !esCompresion) path = path.replace("." + type, ext);
            else path = path.replace(type, ext);
        }
        return path;
    }

    public void crearGestorCarpeta(String path, boolean comprimir, Algoritmo algoritmoDeTexto) throws FileNotFoundException {
        if(comprimir) gestorCarpeta = new GestorCarpetaComprimir(path, algoritmoDeTexto);
        else gestorCarpeta = new GestorCarpetaDescomprimir(path);
    }

    public Algoritmo leerAlgoritmoProximoArchivo() throws IOException {
        return gestorCarpeta.algoritmoProximoArchivo();
    }

    public byte[] leerProximoArchivo() throws IOException {
        return gestorCarpeta.leerProximoArchivo();
    }

    public void guardaProximoArchivo(byte[] data) throws IOException {
        gestorCarpeta.guardaProximoArchivo(data);
    }

    public void finalizarGestorCarpeta() throws IOException {
        gestorCarpeta.finalizarGestor();
        gestorCarpeta = null;
    }

    /**
     * Actualiza el fichero de estadística de compresión o descompresión del algoritmo correspondiente con los datos del nuevo archivo procesado.
     * @param dp DatosProceso para conseguir la información estadística del proceso.
     * @param algoritmo Enumeration.Algoritmo del que actualizamos el fichero estadística.
     * @param esCompresion Indica si el fichero estadística es de compresión o descompresión.
     */
    public void actualizaEstadistica(DatosProceso dp, Algoritmo algoritmo, boolean esCompresion) {
        GestorEstadisticas.actualizarEstadistica(dp,algoritmo,esCompresion);
    }

    /**
     * Obtiene el número de elementos que contiene la estadística para compresión o descompresión del algoritmo seleccionado.
     * @param algoritmo El algoritmo que se quiere consultar.
     * @param esCompresion El tipo de proceso que se quiere, consultar, si de compresión o descompresión.
     * @return El número de elementos que contiene la estadística indicada.
     * @throws IOException Ha existido algún tipo de problema accediendo al archivo o no existe.
     */
    public int getNumeroElementos(Algoritmo algoritmo, boolean esCompresion) throws IOException {
        return GestorEstadisticas.getNumeroElementos(algoritmo, esCompresion);
    }

    /**
     * Obtiene el tiempo medio de la estadística para compresión o descompresión del algoritmo seleccionado.
     * @param algoritmo El algoritmo que se quiere consultar.
     * @param esCompresion El tipo de proceso que se quiere, consultar, si de compresión o descompresión.
     * @return El tiempo medio de todos los procesos de la estadística indicada.
     * @throws IOException Ha existido algún tipo de problema accediendo al archivo o no existe.
     */
    public long getTiempoMedio(Algoritmo algoritmo, boolean esCompresion) throws IOException {
        return GestorEstadisticas.getTiempoMedio(algoritmo, esCompresion);
    }

    /**
     * Obtiene el porcentaje medio que representan los archivos procesados respecto al original de la estadística para compresión o descompresión del algoritmo seleccionado.
     * @param algoritmo El algoritmo que se quiere consultar.
     * @param esCompresion El tipo de proceso que se quiere, consultar, si de compresión o descompresión.
     * @return El porcentaje medio que representan los archivos procesados respecto al original.
     * @throws IOException Ha existido algún tipo de problema accediendo al archivo o no existe.
     */
    public double getPorcentajeAhorradoMedio(Algoritmo algoritmo, boolean esCompresion) throws IOException {
        return GestorEstadisticas.getPorcentajeAhorradoMedio(algoritmo,esCompresion);
    }

}
