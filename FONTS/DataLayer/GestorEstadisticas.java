// Creado por Sheida Vanesa Alfaro Taco
package DataLayer;

import DomainLayer.Algoritmos.Algoritmo;
import DomainLayer.Proceso.DatosProceso;

import java.io.*;

public class GestorEstadisticas {

    public static void actualizarEstadistica(DatosProceso dp, Algoritmo algoritmo, boolean esCompresion) throws IOException {
        File estadistica = new File( System.getProperty("user.dir") +"/resources/estadistica_"+(esCompresion? "1":"0")+"_"+algoritmo+".txt");
        long time = dp.getTiempo();
        long oldSize = dp.getOldSize();
        long newSize = dp.getNewSize();
        double diffSizePercentage = dp.getDiffSizePercentage();
        int numDatos = 1;

        StringBuilder newContent = new StringBuilder();
        String line;

        if (estadistica.exists()) {
            BufferedReader br = new BufferedReader(new FileReader(estadistica));
            line = br.readLine();
            String[] parts = line.split(" ");
            numDatos += Integer.parseInt(parts[0]);
            long avgTime = Long.parseLong(parts[1]);
            avgTime += (time-avgTime)/numDatos;
            double avgPercentage = Double.parseDouble(parts[2]);
            avgPercentage += Math.floor((diffSizePercentage-avgPercentage)/numDatos);

            newContent.append(numDatos).append(" ").append(avgTime).append(" ").append(avgPercentage).append("\n");
            while((line = br.readLine()) != null) {
                newContent.append(line).append("\n");
            }
            br.close();
            newContent.append(time).append(" ").append(oldSize).append(" ").append(newSize).append(" ").append(diffSizePercentage).append("\n");
        } else {
            newContent.append(numDatos).append(" ").append(time).append(" ").append(diffSizePercentage).append("\n");
            newContent.append(time).append(" ").append(oldSize).append(" ").append(newSize).append(" ").append(diffSizePercentage).append("\n");
        }

        BufferedWriter bw = new BufferedWriter(new FileWriter(estadistica));
        bw.write(newContent.toString());
        bw.close();
    }

    public static int getNumeroElementos (Algoritmo algoritmo, boolean esCompresion) throws IOException {
        File estadistica = new File( System.getProperty("user.dir") +"/resources/estadistica_"+(esCompresion? "1":"0")+"_"+algoritmo+".txt");
        BufferedReader br = new BufferedReader(new FileReader(estadistica));
        String line = br.readLine();
        String[] parts = line.split(" ");
        br.close();
        return Integer.parseInt(parts[0]);
    }

    public static long getTiempoMedio (Algoritmo algoritmo, boolean esCompresion) throws IOException {
        File estadistica = new File( System.getProperty("user.dir") +"/resources/estadistica_"+(esCompresion? "1":"0")+"_"+algoritmo+".txt");
        BufferedReader br = new BufferedReader(new FileReader(estadistica));
        String line = br.readLine();
        String[] parts = line.split(" ");
        br.close();
        return Long.parseLong(parts[1]);
    }

    public static double getPorcentajeAhorradoMedio (Algoritmo algoritmo, boolean esCompresion) throws IOException {
        File estadistica = new File( System.getProperty("user.dir") +"/resources/estadistica_"+(esCompresion? "1":"0")+"_"+algoritmo+".txt");
        BufferedReader br = new BufferedReader(new FileReader(estadistica));
        String line = br.readLine();
        String[] parts = line.split(" ");
        br.close();
        return Double.parseDouble(parts[2]);
    }
}
