// Creado por Javier Cabrera Rodriguez
package DomainLayer.Proceso;

import DomainLayer.Algoritmos.*;
import DomainLayer.Algoritmos.LZ78.LZ78;

public abstract class ProcesoFichero {
    protected byte[] input;
    protected byte[] output = null;
    protected Algoritmo tipoAlgoritmo;
    protected CompresorDecompresor compresorDecompresor;
    protected boolean procesado;
    protected DatosProceso datos;

    protected ProcesoFichero(byte[] input, Algoritmo algoritmo) {
        procesado = false;
        this.input = input;
        output = null;
        tipoAlgoritmo = algoritmo;
        asignarAlgoritmo();
    }

    public DatosProceso getDatosProceso() {
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
                throw new EnumConstantNotPresentException(Algoritmo.class, "El tipo de compresor" + tipoAlgoritmo + "no existe");
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

    public Algoritmo gettipoCompresor() {
        return tipoAlgoritmo;
    }

}
