package Controllers;

import DataLayer.*;
import DomainLayer.Algoritmos.Algoritmo;
import DomainLayer.Proceso.DatosProceso;
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
        if(!path.contains("\\.")) return true;
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

    /**
     * Comprueba si el archivo es capaz de ser descomprimido según la extensión del mismo.
     * <p>
     *     El path del archivo debe seguir el formato general de cualquier tipo de path de archivo y puede ser relativo o absoluto.
     * </p>
     * @param path El path del archivo que se quiere comprobar
     * @return Un booleano que indica si el archivo es descomprimible
     * @throws FormatoErroneoException No hay ningún algoritmo compatible con la extensión del archivo
     */
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
            case "comp":
                return true;
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

    /**
     * Actualiza el path pasado por parámetro para que sea el path de salida del fichero procesado.
     * @param path El path del archivo antes de ser procesado.
     * @param algoritmo El algorimo usado en el proceso.
     * @param esCompresion Indicador de si el proceso ha sido de compresión o no (y por tanto de descompresión).
     * @return Path del archivo procesado, con su correspondiente extensión.
     */
    public static String actualizarPathSalida(String path, Algoritmo algoritmo, boolean esCompresion) {
        String [] pathSplitted = path.split("\\.");
        switch (algoritmo) {
            case LZW:
                if(esCompresion) path = path.replace(".txt", ".lzw");
                else path = path.replace("." + pathSplitted[pathSplitted.length - 1], ".txt");
                break;
            case LZSS:
                if(esCompresion) path = path.replace(".txt", ".lzss");
                else path = path.replace("." + pathSplitted[pathSplitted.length - 1], ".txt");
                break;
            case LZ78:
                if(esCompresion) path = path.replace(".txt", ".lz78");
                else path = path.replace("." + pathSplitted[pathSplitted.length - 1], ".txt");
                break;
            case JPEG:
                if(esCompresion) path = path.replace(".ppm", ".imgc");
                else path = path.replace("." + pathSplitted[pathSplitted.length - 1], ".ppm");
                break;
            case CARPETA:
                if (esCompresion) path += ".comp";
                else path = path.replace(pathSplitted[pathSplitted.length - 1], "");
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
     * @param algoritmo Algoritmo del que actualizamos el fichero estadística.
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
