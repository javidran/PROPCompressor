package Controllers;

import DomainLayer.Algoritmos.Algoritmo;
import DomainLayer.Algoritmos.JPEG;
import DomainLayer.Proceso.DatosProceso;
import DomainLayer.Proceso.ProcesoComprimir;
import DomainLayer.Proceso.ProcesoDescomprimir;
import DomainLayer.Proceso.ProcesoFichero;

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
     *     Por defecto es LZSS, pero se puede modificar llamando a setAlgoritmoDeTextoPredeterminado(Algoritmo).
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
     * @param path El path donde se encuentra el fichero a comprimir.
     * @param tipoAlgoritmo El algoritmo a usar para la compresión del fichero.
     * @throws Exception El proceso de compresión no se ha podido llevar a cabo.
     */
    public void comprimirArchivo(String path, Algoritmo tipoAlgoritmo) throws Exception {
        CtrlDatos ctrlDatos = CtrlDatos.getInstance();
        if(tipoAlgoritmo == Algoritmo.PREDETERMINADO) {
            Algoritmo[] algoritmos = CtrlDatos.algoritmosPosibles(path);
            if(algoritmos[0] == Algoritmo.JPEG) tipoAlgoritmo = Algoritmo.JPEG;
            else tipoAlgoritmo = algoritmoDeTextoPredeterminado;
        }
        ProcesoFichero comp = new ProcesoComprimir(ctrlDatos.leerArchivo(path), tipoAlgoritmo);
        comp.ejecutarProceso();
        ctrlDatos.guardaArchivo(comp.getOutput(), path, tipoAlgoritmo, true, true);
        DatosProceso dp = comp.getDatosProceso();
        System.out.println("El proceso ha tardado " + dp.getTiempo()/1000000000.0 + "s. El cambio de tamaño pasa de " + dp.getOldSize() + "B a " + dp.getNewSize() + "B con diferencia de " + dp.getDiffSize() + "B que resulta en un " + dp.getDiffSizePercentage() + "% del archivo original.");
        if(dp.isSatisfactorio()) {
            ctrlDatos.actualizaEstadistica(dp, tipoAlgoritmo, true);
        } else {
            System.out.println("El proceso de compresión no ha resultado satisfactorio ya que el archivo comprimido ocupa igual o más que el archivo original. Se guardará igualmente.");
        }
    }

    /**
     * Crea y ejecuta un proceso de descompresión para un fichero pasado por parámetro, el cual se descomprimirá con su algoritmo, seleccionado automáticamente en función de su extensión.
     * <p>
     *     El path sigue del archivo debe seguir el formato general de cualquier tipo de path de archivo, y debe ser relativo.
     * </p>
     * @param path El path donde se encuentra el fichero a descomprimir.
     * @throws Exception El proceso de descompresión no se ha podido llevar a cabo.
     */
    public void descomprimirArchivo(String path) throws Exception {
        CtrlDatos ctrlDatos = CtrlDatos.getInstance();
        Algoritmo[] algoritmos = CtrlDatos.algoritmosPosibles(path);
        ProcesoFichero desc = new ProcesoDescomprimir(ctrlDatos.leerArchivo(path), algoritmos[0]);
        desc.ejecutarProceso();
        ctrlDatos.guardaArchivo(desc.getOutput(), path, algoritmos[0], false, true);
        DatosProceso dp = desc.getDatosProceso();
        System.out.println("El proceso ha tardado " + dp.getTiempo()/1000000000.0 + "s. El cambio de tamaño pasa de " + dp.getOldSize() + "B a " + dp.getNewSize() + "B con diferencia de " + dp.getDiffSize() + "B que resulta en un " + dp.getDiffSizePercentage() + "% del archivo original.");
        if(dp.isSatisfactorio()) {
            ctrlDatos.actualizaEstadistica(dp, algoritmos[0], false);
        } else {
            System.out.println("El proceso de descompresión no ha resultado satisfactorio ya que el archivo descomprimido ocupa igual o menos que el archivo original. Se guardará igualmente.");
        }
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
    public void comprimirDescomprimirArchivo(String path, Algoritmo tipoAlgoritmo) throws Exception {
        CtrlDatos ctrlDatos = CtrlDatos.getInstance();
        if (tipoAlgoritmo == Algoritmo.PREDETERMINADO) {
            Algoritmo[] algoritmos = CtrlDatos.algoritmosPosibles(path);
            if (algoritmos[0] == Algoritmo.JPEG) tipoAlgoritmo = Algoritmo.JPEG;
            else tipoAlgoritmo = algoritmoDeTextoPredeterminado;
        }
        ProcesoFichero comp = new ProcesoComprimir(ctrlDatos.leerArchivo(path), tipoAlgoritmo);
        comp.ejecutarProceso();
        DatosProceso dp = comp.getDatosProceso();
        System.out.println("El proceso ha tardado " + dp.getTiempo() / 1000000000.0 + "s. El cambio de tamaño pasa de " + dp.getOldSize() + "B a " + dp.getNewSize() + "B con diferencia de " + dp.getDiffSize() + "B que resulta en un " + dp.getDiffSizePercentage() + "% del archivo original.");
        if (dp.isSatisfactorio()) {
            ctrlDatos.actualizaEstadistica(dp, tipoAlgoritmo, true);
        } else {
            System.out.println("El proceso de compresión no ha resultado satisfactorio ya que el archivo comprimido ocupa igual o más que el archivo original. Se guardará igualmente.");
        }
        ProcesoFichero desc = new ProcesoDescomprimir(comp.getOutput(), tipoAlgoritmo);
        desc.ejecutarProceso();
        ctrlDatos.guardaArchivo(desc.getOutput(), path, tipoAlgoritmo, false, true);
        dp = desc.getDatosProceso();
        System.out.println("El proceso ha tardado " + dp.getTiempo() / 1000000000.0 + "s. El cambio de tamaño pasa de " + dp.getOldSize() + "B a " + dp.getNewSize() + "B con diferencia de " + dp.getDiffSize() + "B que resulta en un " + dp.getDiffSizePercentage() + "% del archivo original.");
        if (dp.isSatisfactorio()) {
            ctrlDatos.actualizaEstadistica(dp, tipoAlgoritmo, false);
        }
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
     * Asigna un valor de calidad de compresión al Singleton de JPEG
     * @param calidadJPEG La calidad de compresión de JPEG. A mayor valor, más alta será la calidad de la compresión.
     */
    public void setCalidadJPEG(int calidadJPEG) {
        JPEG.getInstance().setCalidad(calidadJPEG);
    }

}
