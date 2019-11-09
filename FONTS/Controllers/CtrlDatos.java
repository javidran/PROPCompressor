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
    public byte[] leerArchivo (String path) throws IOException {
        return GestorArchivo.leeArchivo(path);
    }

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

    public void actualizaEstadistica(DatosProceso dp, Algoritmo algoritmo, boolean esCompresion) throws IOException {
        GestorEstadisticas.actualizarEstadistica(dp,algoritmo,esCompresion);
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
