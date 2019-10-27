// Creado por Javier Cabrera Rodriguez
package DomainLayer.Proceso;

import DomainLayer.Algoritmos.*;

import java.io.File;

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
}
