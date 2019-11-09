// Creado por Javier Cabrera Rodriguez
package DomainLayer.Proceso;

import DomainLayer.Algoritmos.*;
import DomainLayer.Algoritmos.LZ78.LZ78;
import Exceptions.FormatoErroneoException;

import java.io.*;

public abstract class ProcesoFichero {
    protected byte[] input;
    protected byte[] output = null;
    protected Algoritmos tipoAlgoritmo;
    protected CompresorDecompresor compresorDecompresor;
    protected boolean procesado;
    protected DatosProceso datos;

    protected ProcesoFichero(byte[] input, Algoritmos algoritmo) {
        procesado = false;
        this.input = input;
        output = null;
        tipoAlgoritmo = algoritmo;
        asignarAlgoritmo();
    }

    public DatosProceso getDatosProceso(){
        return datos;
    }

    protected void asignarAlgoritmo() {
        switch (tipoAlgoritmo) {
            case JPEG:
                compresorDecompresor = JPEG.getInstance();
                break;
            case LZW:
                compresorDecompresor = LZW.getInstance();
                break;
            case LZSS:
                compresorDecompresor = LZSS.getInstance();
                break;
            case LZ78:
                compresorDecompresor = LZ78.getInstance();
                break;
            default:
                throw new EnumConstantNotPresentException(Algoritmos.class, "El tipo de compresor" + tipoAlgoritmo + "no existe");
        }
    }

    public abstract void ejecutarProceso() throws Exception;

    public abstract boolean esComprimir();

    public boolean isProcesado() {
        return procesado;
    }

    public byte[] getInput() {
        return input;
    }

    public byte[] getOutput() {
        return output;
    }

    public Algoritmos gettipoCompresor() {
        return tipoAlgoritmo;
    }

    /*
    protected void guardaDatos() throws IOException {
        File estadistica = new File( System.getProperty("user.dir") +"/resources/estadistica_"+(esComprimir()? "1":"0")+"_"+tipoAlgoritmo+".txt");
        DatosProceso newDP = getDatosProceso();
        long a = newDP.getTiempo();
        long b = newDP.getAntiguoTamaño();
        long c = newDP.getNuevoTamaño();
        long d = newDP.diffTam();
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
    */

}
