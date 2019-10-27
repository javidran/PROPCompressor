// Creado por Jan Escorza Fuertes
package Controllers;

import DomainLayer.Algoritmos.Algoritmos;

import java.io.File;

public class CtrlDatos {
    private static CtrlDatos instance = null;

    public static CtrlDatos getInstance()
    {
        if (instance == null)
            instance = new CtrlDatos();

        return instance;
    }

    public File estadisticasGlobales(Algoritmos algoritmo, boolean esCompresion) {
        File file = new File(System.getProperty("user.dir") + "/resources/estadistica_" + (esCompresion? "1":"0") + algoritmo);
        //System.out.println(file.getAbsolutePath() + "       " + file.exists());
        return file;
    }
}
