// Creado por Sheida Vanesa Alfaro Taco
package Controllers;

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
    public byte[] leerArchivo (String path) {
        File archivo = new File(path);
        byte[] archivoBytes = new byte[(int) archivo.length()];
        try
        {
            FileInputStream fis = new FileInputStream(archivo);
            int numBytes = fis.read(archivoBytes);
            fis.close();
        } catch (FileNotFoundException e) {
            System.out.println("No se ha encontrado el archivo.");
        } catch (IOException e) {
            System.out.println("No se ha podido leer el archivo.");
        }
        return archivoBytes;
    }

    public void guardaArchivo (byte[] Data, String path, boolean sobreescribir) {
        File archivo = new File(path);
        try {
            FileOutputStream fos = new FileOutputStream(archivo);
            fos.write(Data);
            fos.close();
        } catch (FileNotFoundException e) {
            System.out.println("No se ha encontrado el archivo.");
        } catch (IOException e) {
            System.out.println("No se ha podido escribir en el archivo.");
        }
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
