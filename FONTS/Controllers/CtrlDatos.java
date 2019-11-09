// Creado por Sheida Vanesa Alfaro Taco
package Controllers;

import DataLayer.GestorArchivo;
import DataLayer.GestorEstadisticas;
import DomainLayer.Algoritmos.Algoritmo;
import DomainLayer.Proceso.DatosProceso;

import java.io.*;

public class CtrlDatos {
    private static CtrlDatos instance = null;

    public static CtrlDatos getInstance()
    {
        if (instance == null)
            instance = new CtrlDatos();

        return instance;
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
        String [] pathSplitted = path.split("\\.");
        switch (algoritmo) {
            case LZW:
                if(esCompresion) path = path.replace(".txt", ".lzw");
                else path = path.replace("."+pathSplitted[pathSplitted.length - 1], "_out.txt");
                break;
            case LZSS:
                if(esCompresion) path = path.replace(".txt", ".lzss");
                else path = path.replace("."+pathSplitted[pathSplitted.length - 1], "_out.txt");
                break;
            case LZ78:
                if(esCompresion) path = path.replace(".txt", ".lz78");
                else path = path.replace("."+pathSplitted[pathSplitted.length - 1], "_out.txt");
                break;
            case JPEG:
                if(esCompresion) path = path.replace(".ppm", ".imgc");
                else path = path.replace("."+pathSplitted[pathSplitted.length - 1], "_out.ppm");
                break;
        }
        GestorArchivo.guardaArchivo(data, path, sobreescribir);
    }

    public void actualizaEstadistica(DatosProceso dp, Algoritmo algoritmo, boolean esCompresion) {
        //GestorEstadisticas.actualizarEstadistica(dp,algoritmo,esCompresion);
    }

    public void obtenerDatosPrincipales(Algoritmo alg, boolean esCompresion) {}

    public int getNumeroElementos(Algoritmo alg, boolean esCompresion) {
        return 0;
    }

    public int getTiempoMedio(Algoritmo alg, boolean esCompresion) {
        return 0;
    }

    public int getPorcentajeAhorradoMedio(Algoritmo alg, boolean esCompresion) {
        return 0;
    }

    //public HashMap<int, long[3]> getDatosEstadistica(Algoritmos alg, boolean esCompresion) {  }

    public File estadisticasGlobales(Algoritmo algoritmo, boolean esCompresion) {
        File file = new File(System.getProperty("user.dir") + "/resources/estadistica_" + (esCompresion? "1":"0") + "_" + algoritmo);
        //System.out.println(file.getAbsolutePath() + "       " + file.exists());
        return file;
    }
}
