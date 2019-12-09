package Controllers;

import DataLayer.*;
import DomainLayer.Proceso.DatosProceso;
import Enumeration.Algoritmo;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CtrlDatos {
    /**
     * Instancia de CtrlDatos para garantizar que es una clase Singleton
     */
    private static CtrlDatos instance = null;

    /**
     * Instancia de GestorCarpeta que se utilizará para leer y guardar archivos o carpetas de la carpeta que se procese
     */
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
     * Llama a GestorArchivo para que verifique si el archivo indicado en el path existe
     * @param path path que deberia indicar la posicion que puede existir o no
     * @return Un boolean indicando si el archivo indicado por el path existe
     */
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
     * @throws IOException No se ha podido escribir en el archivo por el path indicado.
     */
    public void guardaArchivo (byte[] data, String path) throws IOException {
        GestorArchivo.guardaArchivo(data, path);
    }


    /**
     * Se crea una isnatncia del gestor de carpetas que gestionará la carpeta que se pasa por parámetro para generar el .comp
     * @param pathOriginal path donde se encuentra la carpeta que el usuario desea comprimir
     * @param pathSalida path donde el usuario desea dejar la caprpeta comprimida
     * @throws FileNotFoundException la capreta no existe o no se encuentra en el path indicado
     */
    public void crearGestorCarpetaComprimir(String pathOriginal, String pathSalida) throws FileNotFoundException {
        gestorCarpeta = new GestorCarpetaComprimir(pathOriginal, pathSalida);
    }

    /**
     * Se crea una instancia del gestor de carpetas que gestionará el archivo .comp para generar la carpeta descomprimida
     * @param pathOriginal path donde se encuentra la carpeta que el usuario desea descomprimir
     * @throws FileNotFoundException la capreta no existe o no se encuentra en el path indicado
     */
    public void crearGestorCarpetaDescomprimir(String pathOriginal, String pathSalida) throws FileNotFoundException {
        gestorCarpeta = new GestorCarpetaDescomprimir(pathOriginal, pathSalida);
    }


    /**
     * Obtiene el path del siguiente archivo de la carpeta que se está procesando
     * @return el path del proximo archivo en la carpeta
     * @throws IOException
     */
    public String leerPathProximoArchivo() throws IOException {
        return gestorCarpeta.pathProximoArchivo();
    }

    /**
     * Obtiene el bytearray con el contenido del siguiente archivo de la carpeta que se está procesando
     * @return el path del proximo archivo en la carpeta
     * @throws IOException
     */
    public byte[] leerProximoArchivo() throws IOException {
        return gestorCarpeta.leerProximoArchivo();
    }

    /**
     * Crea un archivo en el path indicado en el parámetro y almacena en él el contenido del byte array.
     * @param data contenido a almacenar en el archivo
     * @param path path donde se tiene que guardar la infromación almacenada en el anterior parámetro
     * @throws IOException
     */
    public void guardaProximoArchivo(byte[] data, String path) throws IOException {
        gestorCarpeta.guardaProximoArchivo(data, path);
    }

    /**
     * Crea una carpeta en el path indicado
     * @param path donde se debe crear la carpeta correspondiente
     * @throws IOException
     */
    public void guardaCarpeta(String path) throws IOException {
        gestorCarpeta.guardaCarpeta(path);
    }

    /**
     * Se cierra el gestor de carpetas
     * @throws IOException
     */
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

    /**
     * elimina el archivo que se indica en el path que se pasa por parámetro
     * @param path path del archivo que se desea eliminar
     */
    public void eliminaArchivo(String path) {
        GestorArchivo.eliminaArchivo(path);
    }

    /**
     * Función utilizada para mostrar el contenido de un archivo indicado por el path en formato de tabla
     * @param path indica el archivo que se debe mostrar en una tabla
     * @param titleBar el título que se mostrará en la columna que contiene el archivo de texto
     * @return la estructura de tabla para mostrar en una vista de comparación de ficheros
     * @throws IOException
     */
    public TableModel getArchivoAsModel(String path, String titleBar) throws IOException {
        FileReader fileReader = new FileReader(path);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        List<String> list = new ArrayList<>();
        String str;
        while((str=bufferedReader.readLine())!= null) list.add(str);
        bufferedReader.close();
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn(titleBar, list.toArray());
        return model;
    }
}
