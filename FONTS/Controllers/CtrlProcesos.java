package Controllers;

import DomainLayer.Algoritmos.JPEG;
import DomainLayer.Proceso.DatosProceso;
import DomainLayer.Proceso.ProcesoComprimir;
import DomainLayer.Proceso.ProcesoDescomprimir;
import DomainLayer.Proceso.ProcesoFichero;
import Enumeration.Algoritmo;
import Exceptions.FormatoErroneoException;

/**
 * La clase Singleton CtrlProcesos es el Controlador de Dominio del programa, y la encargada de crear procesos de compresión y/o descompresión, además de interactuar con las capas de datos y presentación.
 */
public class CtrlProcesos {
    /**
     * Instancia de CtrlProcesos para garantizar que es una clase Singleton.
     */
    private static CtrlProcesos instance = null;
    /**
     * Algortimo de texto predeterminado de entre los tres posibles.
     * <p>
     *     Por defecto es LZSS, pero se puede modificar llamando a setAlgoritmoDeTextoPredeterminado(Enumeration.Algoritmo).
     * </p>
     */
    private static Algoritmo algoritmoDeTextoPredeterminado = Algoritmo.LZSS;

    /**
     * Getter de la instancia Singleton de CtrlProcesos.
     * @return La instancia Singleton de CtrlProcesos.
     */
    public static CtrlProcesos getInstance()
    {
        if (instance == null)
            instance = new CtrlProcesos();

        return instance;
    }

    /**
     * Crea y ejecuta un proceso de compresión para un fichero, el cual se comprimirá con un algoritmo, ambos pasados por parámetro.
     * <p>
     *     El path del archivo debe seguir el formato general de cualquier tipo de path de archivo, y debe ser relativo.
     * </p>
     * <p>
     *     El algoritmo es capaz de comprimir el formato del fichero.
     * </p>
     *
     * @param pathOriginal El path donde se encuentra el fichero a comprimir.
     * @param pathResultado El path donde se guardará el fichero comprimido.
     * @param tipoAlgoritmo El algoritmo a usar para la compresión del fichero.
     * @throws Exception El proceso de compresión no se ha podido llevar a cabo.
     */
    public DatosProceso comprimirArchivo(String pathOriginal, String pathResultado, Algoritmo tipoAlgoritmo) throws Exception {
        CtrlDatos ctrlDatos = CtrlDatos.getInstance();
        ProcesoFichero comp = new ProcesoComprimir(ctrlDatos.leerArchivo(pathOriginal), tipoAlgoritmo);
        comp.ejecutarProceso();
        ctrlDatos.guardaArchivo(comp.getOutput(), pathResultado);
        return comp.getDatosProceso();
    }

    /**
     * Crea y ejecuta un proceso de descompresión para un fichero pasado por parámetro, el cual se descomprimirá con su algoritmo, seleccionado automáticamente en función de su extensión.
     * <p>
     *     El path sigue del archivo debe seguir el formato general de cualquier tipo de path de archivo, y debe ser relativo.
     * </p>
     *
     * @param pathOriginal El path donde se encuentra el fichero a descomprimir.
     * @param pathResultado El path donde se guardará el fichero descomprimido.
     * @throws Exception El proceso de descompresión no se ha podido llevar a cabo.
     */
    public DatosProceso descomprimirArchivo(String pathOriginal, String pathResultado ) throws Exception {
        CtrlDatos ctrlDatos = CtrlDatos.getInstance();
        Algoritmo algoritmo = algoritmoPosible(pathOriginal);
        ProcesoFichero desc = new ProcesoDescomprimir(ctrlDatos.leerArchivo(pathOriginal), algoritmo);
        desc.ejecutarProceso();
        ctrlDatos.guardaArchivo(desc.getOutput(), pathResultado);
        DatosProceso dp = desc.getDatosProceso();
        System.out.println("El proceso ha tardado " + dp.getTiempo()/1000000000.0 + "s. El cambio de tamaño pasa de " + dp.getOldSize() + "B a " + dp.getNewSize() + "B con diferencia de " + dp.getDiffSize() + "B que resulta en un " + dp.getDiffSizePercentage() + "% del archivo original.");
        if(dp.isSatisfactorio()) {
            ctrlDatos.actualizaEstadistica(dp, algoritmo, false);
        } else {
            System.out.println("El proceso de descompresión no ha resultado satisfactorio ya que el archivo descomprimido ocupa igual o menos que el archivo original. Se guardará igualmente.");
        }
        return dp;
    }

    /**
     * Crea y ejecuta un proceso de compresión y descompresión para un fichero, el cual se comprimirá con un algoritmo, ambos pasados por parámetro, y el cual se descomprimirá con su algoritmo, seleccionado automáticamente en función de su extensión.
     * <p>
     *     El path sigue del archivo debe seguir el formato general de cualquier tipo de path de archivo, y debe ser relativo.
     * </p>
     * <p>
     *     El algoritmo es capaz de comprimir el formato del fichero.
     * </p>
     * @param path El path donde se encuentra el fichero a comprimir y descomprimir.
     * @param tipoAlgoritmo El algoritmo a usar para la compresión del fichero.
     * @throws Exception El proceso de compresión y descompresión no se ha podido llevar a cabo.
     */
    public DatosProceso[] comprimirDescomprimirArchivo(String path, String pathTemporal, Algoritmo tipoAlgoritmo) throws Exception {
        DatosProceso[] dp = new DatosProceso[2];
        CtrlDatos ctrlDatos = CtrlDatos.getInstance();
        ProcesoFichero comp = new ProcesoComprimir(ctrlDatos.leerArchivo(path), tipoAlgoritmo);
        comp.ejecutarProceso();
        dp[0] = comp.getDatosProceso();
        System.out.println("El proceso ha tardado " + dp[0].getTiempo() / 1000000000.0 + "s. El cambio de tamaño pasa de " + dp[0].getOldSize() + "B a " + dp[0].getNewSize() + "B con diferencia de " + dp[0].getDiffSize() + "B que resulta en un " + dp[0].getDiffSizePercentage() + "% del archivo original.");
        if (dp[0].isSatisfactorio()) {
            ctrlDatos.actualizaEstadistica(dp[0], tipoAlgoritmo, true);
        } else {
            System.out.println("El proceso de compresión no ha resultado satisfactorio ya que el archivo comprimido ocupa igual o más que el archivo original. Se guardará igualmente.");
        }

        ProcesoFichero desc = new ProcesoDescomprimir(comp.getOutput(), tipoAlgoritmo);
        desc.ejecutarProceso();
        ctrlDatos.guardaArchivo(desc.getOutput(), pathTemporal);
        dp[1] = desc.getDatosProceso();
        System.out.println("El proceso ha tardado " + dp[1].getTiempo() / 1000000000.0 + "s. El cambio de tamaño pasa de " + dp[1].getOldSize() + "B a " + dp[1].getNewSize() + "B con diferencia de " + dp[1].getDiffSize() + "B que resulta en un " + dp[1].getDiffSizePercentage() + "% del archivo original.");
        if (dp[1].isSatisfactorio()) {
            ctrlDatos.actualizaEstadistica(dp[1], tipoAlgoritmo, false);
        }
        return dp;
    }

    public DatosProceso comprimirCarpeta(String pathIn, String pathOut) throws Exception {
        long tiempo = 0, oldSize = 0, newSize = 0;
        CtrlDatos ctrlDatos = CtrlDatos.getInstance();
        ctrlDatos.crearGestorCarpetaComprimir(pathIn, pathOut);
        String pathArchivo;
        while((pathArchivo = ctrlDatos.leerPathProximoArchivo())!= null) {
            Algoritmo algoritmoArchivo = algoritmoPosible(pathArchivo);
            String pathArchivoOut;
            if (algoritmoArchivo.equals(Algoritmo.CARPETA)) {
                pathArchivoOut = pathArchivo.replace(pathIn, "");
                ctrlDatos.guardaCarpeta(pathArchivoOut);
            } else {
                ProcesoFichero comp = new ProcesoComprimir(ctrlDatos.leerProximoArchivo(), algoritmoArchivo);
                comp.ejecutarProceso();
                pathArchivoOut = calcularPathSalida(pathArchivo, algoritmoArchivo, true).replace(pathIn, "");
                ctrlDatos.guardaProximoArchivo(comp.getOutput(), pathArchivoOut);
                DatosProceso dp = comp.getDatosProceso();
                tiempo += dp.getTiempo();
                oldSize += dp.getOldSize();
                newSize += dp.getNewSize();
                if(dp.isSatisfactorio()) {
                    ctrlDatos.actualizaEstadistica(dp, algoritmoArchivo, true);
                }
            }
        }
        ctrlDatos.finalizarGestorCarpeta();

        long diffSize = oldSize - newSize;
        double diffSizePercentage = Math.floor((newSize /(double) oldSize)*100);
        System.out.println("El proceso ha tardado " + tiempo / 1000000000.0 + "s. El cambio de tamaño pasa de " + oldSize + "B a " + newSize + "B con diferencia de " + diffSize + "B que resulta en un " + diffSizePercentage + "% del archivo original.");

        return new DatosProceso(tiempo, oldSize, newSize, true);
    }

    public DatosProceso descomprimirCarpeta(String pathIn, String pathOut) throws Exception {
        long tiempo = 0, oldSize = 0, newSize = 0;
        CtrlDatos ctrlDatos = CtrlDatos.getInstance();
        ctrlDatos.crearGestorCarpetaDescomprimir(pathIn);
        String pathArchivo;
        while((pathArchivo = ctrlDatos.leerPathProximoArchivo())!= null) {
            Algoritmo algoritmoArchivo = algoritmoPosible(pathArchivo);
            String pathArchivoOut = pathOut + calcularPathSalida(pathArchivo, algoritmoArchivo, false);
            if(algoritmoArchivo.equals(Algoritmo.CARPETA)) {
                ctrlDatos.guardaCarpeta(pathArchivoOut);
            } else {
                ProcesoFichero desc = new ProcesoDescomprimir(ctrlDatos.leerProximoArchivo(), algoritmoArchivo);
                desc.ejecutarProceso();
                ctrlDatos.guardaProximoArchivo(desc.getOutput(), pathArchivoOut);
                DatosProceso dp = desc.getDatosProceso();
                tiempo += dp.getTiempo();
                oldSize += dp.getOldSize();
                newSize += dp.getNewSize();
                if(dp.isSatisfactorio()) {
                    ctrlDatos.actualizaEstadistica(dp, algoritmoArchivo, false);
                }
            }
        }
        ctrlDatos.finalizarGestorCarpeta();

        long diffSize = newSize - oldSize;
        double diffSizePercentage = Math.floor((newSize /(double) oldSize)*100);
        System.out.println("El proceso ha tardado " + tiempo / 1000000000.0 + "s. El cambio de tamaño pasa de " + oldSize + "B a " + newSize + "B con diferencia de " + diffSize + "B que resulta en un " + diffSizePercentage + "% del archivo original.");

        return new DatosProceso(tiempo, oldSize, newSize, false);
    }


    /**
     * Asigna un algoritmo de texto predeterminado al Singleton de CtrlProcesos
     * <p>
     *     El algoritmoDeTextoPredeterminado es uno de los tres algoritmos de compresión de texto posibles
     * </p>
     * @param algoritmoDeTextoPredeterminado El algoritmo de texto que se usará por defecto para comprimir ficheros de texto
     */
    public static void setAlgoritmoDeTextoPredeterminado(Algoritmo algoritmoDeTextoPredeterminado) {
        CtrlProcesos.algoritmoDeTextoPredeterminado = algoritmoDeTextoPredeterminado;
    }

    /**
     * Obtiene el algoritmo de texto predeterminado del Singleton de CtrlProcesos
     * @return El algoritmoDeTextoPredeterminado que se usa por defecto para comprimir ficheros de texto
     */
    public static Algoritmo getAlgoritmoDeTextoPredeterminado() {
        return algoritmoDeTextoPredeterminado;
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

    /**
     * Comprueba qué algoritmos se pueden usar para comprimir o descomprimir un fichero, el cual se pasa por parámetro.
     * <p>
     *     El path del archivo debe seguir el formato general de cualquier tipo de path de archivo y puede ser relativo o absoluto.
     * </p>
     * @param path El path del archivo a comprobar
     * @return Un vector de algoritmos posibles a ejecutar para comprimir o descomprimir el fichero
     * @throws FormatoErroneoException No hay ningún algoritmo compatible con la extensión del archivo
     */
    public static Algoritmo algoritmoPosible(String path) throws FormatoErroneoException {
        if (!path.contains(".")) return Algoritmo.CARPETA;
        String[] splittedPath = path.split("\\.");
        String type = splittedPath[splittedPath.length-1];

        switch (type) {
            case "txt":
                return getAlgoritmoDeTextoPredeterminado();
            case "ppm":
            case "imgc":
                return Algoritmo.JPEG;
            case "lzss":
                return Algoritmo.LZSS;
            case "lz78":
                return Algoritmo.LZ78;
            case "lzw":
                return Algoritmo.LZW;
            case "comp":
                return Algoritmo.CARPETA;
            default:
                throw new FormatoErroneoException("No hay ningun tipo de algoritmo compatible");
        }
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
    public static String calcularPathSalida(String path, Algoritmo algoritmo, boolean esCompresion) {
        String[] splitP = path.split("\\.");
        String type = splitP[splitP.length-1];
        String ext = extension(algoritmo, esCompresion);
        if(!path.contains(".") && !(algoritmo.equals(Algoritmo.CARPETA) && !esCompresion)) path = path + "." + ext;
        else if (!type.equalsIgnoreCase(ext)) {
            if(algoritmo.equals(Algoritmo.CARPETA) && !esCompresion) path = path.replace("." + type, ext);
            else path = path.replace(type, ext);
        }
        return path;
    }

    public static String calcularPathTemporal(String path) {
        String[] splitP = path.split("\\.");
        String type = splitP[splitP.length-1];
        String ext = "temp";
        if(!path.contains(".")) path = path + "." + ext;
        else if (!type.equalsIgnoreCase(ext)) path = path.replace(type, ext);
        return path;
    }

    public static void eliminaArchivoTemporal(String path) {
        CtrlDatos ctrlDatos = CtrlDatos.getInstance();
        ctrlDatos.eliminaArchivo(path);
    }

    /**
     * Asigna un valor de calidad de compresión al Singleton de JPEG
     * @param calidadJPEG La calidad de compresión de JPEG. A mayor valor, más alta será la calidad de la compresión.
     */
    public static void setCalidadJPEG(int calidadJPEG) {
        JPEG.getInstance().setCalidad(calidadJPEG);
    }

}
