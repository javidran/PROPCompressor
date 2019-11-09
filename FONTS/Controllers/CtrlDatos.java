// Creado por Jan Escorza Fuertes
package Controllers;

import DomainLayer.Algoritmos.Algoritmos;
import DomainLayer.Proceso.DatosProceso;

import java.io.File;

public class CtrlDatos {
    private static CtrlDatos instance = null;

    public static CtrlDatos getInstance()
    {
        if (instance == null)
            instance = new CtrlDatos();

        return instance;
    }
    public byte[] leerArchivo (String path) {
        byte[] ret = new byte[0];
        return ret;
    }
    
    public void guardaArchivo (byte[] Data, String path, boolean b) {
    }

    public void actualizaEstadistica(DatosProceso dp, Algoritmos alg, boolean esCompresion) {}

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
