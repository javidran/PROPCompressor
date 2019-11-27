package DataLayer;

import DomainLayer.Proceso.DatosProceso;
import Enumeration.Algoritmo;

import java.io.*;

/**
 * Clase utilizada por todos los algoritmos tanto al comprimir como al descomprimir que guarda los diferentes datos estadísiticos que generan los procesos.
 */
public class GestorEstadisticas {

    /**
     * Actualiza el fichero de estadística de compresión o descompresión del algoritmo correspondiente con los datos del nuevo archivo procesado.
     * @param dp DatosProceso para conseguir la información estadística del proceso.
     * @param algoritmo Enumeration.Algoritmo del que actualizamos el fichero estadística.
     * @param esCompresion Indica si el fichero es de compresión o descompresión.
     */
    public static void actualizarEstadistica(DatosProceso dp, Algoritmo algoritmo, boolean esCompresion) {
        File estadistica = new File( System.getProperty("user.dir") +"/resources/estadistica_"+(esCompresion? "1":"0")+"_"+algoritmo+".txt");
        long time = dp.getTiempo();
        long oldSize = dp.getOldSize();
        long newSize = dp.getNewSize();
        double diffSizePercentage = dp.getDiffSizePercentage();
        int numDatos = 1;

        StringBuilder newContent = new StringBuilder();
        String line;
        try {
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
                estadistica.createNewFile();
                newContent.append(numDatos).append(" ").append(time).append(" ").append(diffSizePercentage).append("\n");
                newContent.append(time).append(" ").append(oldSize).append(" ").append(newSize).append(" ").append(diffSizePercentage).append("\n");
            }

            BufferedWriter bw = new BufferedWriter(new FileWriter(estadistica));
            bw.write(newContent.toString());
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtiene el número de elementos que contiene la estadística para compresión o descompresión del algoritmo seleccionado.
     * @param algoritmo El algoritmo que se quiere consultar.
     * @param esCompresion El tipo de proceso que se quiere, consultar, si de compresión o descompresión.
     * @return El número de elementos que contiene la estadística indicada.
     * @throws IOException Ha existido algún tipo de problema accediendo al archivo o no existe.
     */
    public static int getNumeroElementos (Algoritmo algoritmo, boolean esCompresion) throws IOException {
        File estadistica = new File( System.getProperty("user.dir") +"/resources/estadistica_"+(esCompresion? "1":"0")+"_"+algoritmo+".txt");
        BufferedReader br = new BufferedReader(new FileReader(estadistica));
        String line = br.readLine();
        String[] parts = line.split(" ");
        br.close();
        return Integer.parseInt(parts[0]);
    }

    /**
     * Obtiene el tiempo medio de la estadística para compresión o descompresión del algoritmo seleccionado.
     * @param algoritmo El algoritmo que se quiere consultar.
     * @param esCompresion El tipo de proceso que se quiere, consultar, si de compresión o descompresión.
     * @return El tiempo medio de todos los procesos de la estadística indicada.
     * @throws IOException Ha existido algún tipo de problema accediendo al archivo o no existe.
     */
    public static long getTiempoMedio (Algoritmo algoritmo, boolean esCompresion) throws IOException {
        File estadistica = new File( System.getProperty("user.dir") +"/resources/estadistica_"+(esCompresion? "1":"0")+"_"+algoritmo+".txt");
        BufferedReader br = new BufferedReader(new FileReader(estadistica));
        String line = br.readLine();
        String[] parts = line.split(" ");
        br.close();
        return Long.parseLong(parts[1]);
    }

    /**
     * Obtiene el porcentaje medio que representan los archivos procesados respecto al original de la estadística para compresión o descompresión del algoritmo seleccionado.
     * @param algoritmo El algoritmo que se quiere consultar.
     * @param esCompresion El tipo de proceso que se quiere, consultar, si de compresión o descompresión.
     * @return El porcentaje medio que representan los archivos procesados respecto al original.
     * @throws IOException Ha existido algún tipo de problema accediendo al archivo o no existe.
     */
    public static double getPorcentajeAhorradoMedio (Algoritmo algoritmo, boolean esCompresion) throws IOException {
        File estadistica = new File( System.getProperty("user.dir") +"/resources/estadistica_"+(esCompresion? "1":"0")+"_"+algoritmo+".txt");
        BufferedReader br = new BufferedReader(new FileReader(estadistica));
        String line = br.readLine();
        String[] parts = line.split(" ");
        br.close();
        return Double.parseDouble(parts[2]);
    }
}
