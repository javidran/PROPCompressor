// Creado por Javier Cabrera Rodriguez
package DomainLayer.Proceso;

import DomainLayer.Algoritmos.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public abstract class ProcesoFichero {
    protected File ficheroIn;
    protected File ficheroOut;
    protected Algoritmos tipoAlgoritmo;
    protected CompresorDecompresor compresorDecompresor;
    protected boolean procesado;
    protected DatosProceso datos;

    protected ProcesoFichero(File input) {
        ficheroIn = input;
        procesado = false;
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

    public abstract Algoritmos[] tiposPosibles();

    public abstract boolean esComprimir();

    public boolean isProcesado() {
        return procesado;
    }

    public void setFicheroIn(File ficheroIn) {
        this.ficheroIn = ficheroIn;
    }

    public void setFicheroOut(File ficheroOut) {
        this.ficheroOut = ficheroOut;
    }

    public boolean setTipoAlgoritmo(Algoritmos tipoAlgoritmo) {
        Algoritmos[] algoritmos = tiposPosibles();
        boolean esCompatible = false;
        for(Algoritmos a : algoritmos) {
            if(a == tipoAlgoritmo) esCompatible = true;
        }
        if(esCompatible) {
            this.tipoAlgoritmo = tipoAlgoritmo;
            asignarAlgoritmo();
        }
        return esCompatible;
    }

    public File getFicheroIn() {
        return ficheroIn;
    }

    public File getFicheroOut() {
        return ficheroOut;
    }

    public Algoritmos gettipoCompresor() {
        return tipoAlgoritmo;
    }

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

        if (!estadistica.createNewFile()) {
            System.out.println("Ya estaba el File estadistica creado");
                BufferedReader br = new BufferedReader (new FileReader(estadistica));
                String buff = br.readLine();
                String[] parts = buff.split(" ");
                numDatos = Integer.parseInt(parts[0]) + 1;
                tiempoMedio = (Integer.parseInt(parts[1])*numDatos-1)/numDatos + a/numDatos;;
                AvgMedio = (Integer.parseInt(parts[2])*numDatos-1)/numDatos + d/numDatos;
                br.close();

            RandomAccessFile archivo = new RandomAccessFile(estadistica, "w");
            archivo.seek(0);
            archivo.writeBytes(numDatos +" "+ tiempoMedio +" "+ AvgMedio);
            archivo.close();
        }
        else {
            System.out.println("File vacio");
            BufferedWriter bw = new BufferedWriter(new FileWriter(estadistica));
            bw.write(numDatos +" "+ tiempoMedio +" "+ AvgMedio);
            bw.newLine();
            bw.close();
        }
        System.out.println("Común File estadistica");
        BufferedWriter bw = new BufferedWriter(new FileWriter(estadistica));
        bw.write(a+" "+b+" "+c+" "+d);
        bw.close();
    }
}
