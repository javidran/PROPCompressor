// Creado por Sheida Vanesa Alfaro Taco
package Controllers;

import DataLayer.GestorArchivo;
import DataLayer.GestorEstadisticas;
import DomainLayer.Algoritmos.Algoritmos;
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

    public void guardaArchivo (byte[] data, String path, Algoritmos alg, boolean esCompresion, boolean sobreescribir) throws IOException {
        switch (alg) {
            case LZW:
                if(esCompresion) path = path.replace(".txt", ".lzw");
                else path = path.replace(".lzw", ".txt");
                break;
            case LZSS:
                if(esCompresion) path = path.replace(".txt", ".lzss");
                else path = path.replace(".lzss", ".txt");
                break;
            case LZ78:
                if(esCompresion) path = path.replace(".txt", ".lz78");
                else path = path.replace(".lz78", ".txt");
                break;
            case JPEG:
                if(esCompresion) path = path.replace(".ppm", ".imgc");
                else path = path.replace(".imgc", ".ppm");
                break;
        }
        GestorArchivo.guardaArchivo(data, path, sobreescribir);
    }

    public void actualizaEstadistica(DatosProceso dp, Algoritmos alg, boolean esCompresion) {

    }

    public void obtenerDatosPrincipales(Algoritmos alg, boolean esCompresion) {}

    public int getNumeroElementos(Algoritmos alg, boolean esCompresion) {
        return 0;
    }

    public int getTiempoMedio(Algoritmos alg, boolean esCompresion) {
        return 0;
    }

    public int getPorcentajeAhorradoMedio(Algoritmos alg, boolean esCompresion) {
        return 0;
    }

    //public HashMap<int, long[3]> getDatosEstadistica(Algoritmos alg, boolean esCompresion) {  }

    public File estadisticasGlobales(Algoritmos algoritmo, boolean esCompresion) {
        File file = new File(System.getProperty("user.dir") + "/resources/estadistica_" + (esCompresion? "1":"0") + "_" + algoritmo);
        //System.out.println(file.getAbsolutePath() + "       " + file.exists());
        return file;
    }

}
