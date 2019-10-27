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
    protected Algoritmos tipoC;
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
        switch (tipoC) {
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
                throw new EnumConstantNotPresentException(Algoritmos.class, "El tipo de compresor" + tipoC + "no existe");
        }
    }

    public abstract void ejecutarProceso() throws Exception;

    public abstract Algoritmos[] tiposPosibles();

    public boolean isProcesado() {
        return procesado;
    }

    public void setFicheroIn(File ficheroIn) {
        this.ficheroIn = ficheroIn;
    }

    public void setFicheroOut(File ficheroOut) {
        this.ficheroOut = ficheroOut;
    }

    public void setTipoC(Algoritmos tipoC) {
        this.tipoC = tipoC;
        asignarAlgoritmo();
    }

    public File getFicheroIn() {
        return ficheroIn;
    }

    public File getFicheroOut() {
        return ficheroOut;
    }

    public Algoritmos getTipoC() {
        return tipoC;
    }

    private void guardaDatos() throws IOException {
        File estadistica = new File( System.getProperty("user.dir") +"/resources/estadistica"+tipoC+".txt");
        DatosProceso newDP = getDatosProceso();
        List<Integer> datosAntiguos = new ArrayList<Integer>(); //
        long a = newDP.getTiempo();
        long b = newDP.getAntiguoTamaño();
        long c = newDP.getNuevoTamaño();
        long d = newDP.diffTam();
        long numDatos = c;
        long tiempoMedio = a;
        long AvgMedio = d;

        if (estadistica.createNewFile()) { //Creamos el archivo si no existia
            FileReader fr = null;
            try {
                BufferedReader br = null;
                InputStream inputStream = new FileInputStream(estadistica);
                Reader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                br = new BufferedReader(inputStreamReader);
                double value = 0;
                while ((value = br.read()) != -1) {
                    datosAntiguos.add((int) value);
                }
                br.close();
                int count = 0;
                for (int di : datosAntiguos) { //Se supone que hay 3 valores
                    if (count == 0) numDatos = di + c;
                    count++;
                    if (count == 1) tiempoMedio = di + a;
                    count++;
                    if (count == 2) AvgMedio = di + d;
                    count++;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        try {
            RandomAccessFile archivo = new RandomAccessFile(estadistica, "rw");
            archivo.seek(0);
            archivo.writeBytes(numDatos +" "+ tiempoMedio +" "+ AvgMedio);
            archivo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            FileWriter fw=new FileWriter(estadistica);
            BufferedWriter bw=new BufferedWriter(fw);
            bw.newLine();
            bw.write(a+" "+b+" "+c+" "+d);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
