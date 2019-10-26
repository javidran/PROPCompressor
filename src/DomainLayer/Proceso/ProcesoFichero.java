package DomainLayer.Proceso;

import DomainLayer.Algoritmos.*;

import java.io.File;

public abstract class ProcesoFichero {
    protected File ficheroIn;
    protected File ficheroOut;
    protected Algoritmos tipoC;
    protected CompresorDecompresor compresorDecompresor;
    protected boolean procesado;

    protected ProcesoFichero(File input) {
        ficheroIn = input;
        procesado = false;
    }

    protected void asignarAlgoritmo() {
        switch (tipoC) {
            case JPEG:
                compresorDecompresor = JPEG.getInstance();
                break;
            case LZW:
                // TODO
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
}
