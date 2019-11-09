// Creado por Sheida Vanesa Alfaro Taco
package DataLayer;

import DomainLayer.Algoritmos.Algoritmos;
import DomainLayer.Proceso.DatosProceso;

import java.io.*;

public class GestorEstadisticas {
    private static GestorEstadisticas instance = null;

    public static GestorEstadisticas getInstance()
    {
        if (instance == null)
            instance = new GestorEstadisticas();

        return instance;
    }

    protected void guardaDatos(DatosProceso dp, Algoritmos alg, boolean esCompresion) throws IOException {
        File estadistica = new File( System.getProperty("user.dir") +"/resources/estadistica_"+(esCompresion? "1":"0")+"_"+alg+".txt");
        long a = dp.getTiempo();
        long b = dp.getOldSize();
        long c = dp.getNewSize();
        long d = dp.getDiffSize();
        long numDatos = 1;
        long tiempoMedio = a;
        long AvgMedio = d;

        if (estadistica.exists()) {
            RandomAccessFile archivo = new RandomAccessFile(estadistica, "rw");
            archivo.seek(0);
            String buff = archivo.readLine();
            String[] parts = buff.split(" ");
            numDatos = (Long.parseLong(parts[0]) + 1);
            long aux = ((Long.parseLong( parts[1])*(numDatos-1))/numDatos + a/numDatos);
            tiempoMedio = aux;
            aux =  ((Long.parseLong(parts[2])*(numDatos-1))/numDatos + d/numDatos);
            AvgMedio = aux;
            archivo.seek(0);
            archivo.writeBytes(numDatos +" "+ tiempoMedio +" "+ AvgMedio);
            archivo.close();

            BufferedWriter bw = new BufferedWriter(new FileWriter(estadistica, true));
            CharSequence cs = (a + " " + b + " " + c + " " + d);
            bw.newLine();
            bw.append(cs);
            bw.close();
        } else {
            estadistica.createNewFile();
            BufferedWriter bw = new BufferedWriter(new FileWriter(estadistica));
            bw.write(numDatos + " " + tiempoMedio + " " + AvgMedio);
            bw.newLine();
            bw.close();

            BufferedWriter bw2 = new BufferedWriter(new FileWriter(estadistica, true));
            CharSequence cs = (a + " " + b + " " + c + " " + d);
            bw2.append(cs);
            bw2.close();
        }
    }
}
