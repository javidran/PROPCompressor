package domainLayer.proceso;

import domainLayer.TipoCompresor;
import domainLayer.algoritmos.CompresorDecompresor;
import domainLayer.algoritmos.JPEG;
import domainLayer.algoritmos.LZ78;
import domainLayer.algoritmos.LZSS;

import java.io.File;

public class ProcesoFichero {
    protected File ficheroIn;
    protected File ficheroOut;
    protected TipoCompresor tipoC;
    protected CompresorDecompresor compresorDecompresor;
    protected boolean procesado;

    protected ProcesoFichero(File input) {
        ficheroIn = input;
        procesado = false;
    }

    public TipoCompresor[] tiposPosibles() {
        if (ficheroIn.getAbsolutePath().endsWith(".txt") ) {
            return new TipoCompresor[] {TipoCompresor.LZ78, TipoCompresor.LZW, TipoCompresor.LZSS};
        }
        else if (ficheroIn.getAbsolutePath().endsWith(".ppm")) {
            return new TipoCompresor[] {TipoCompresor.JPEG};
        }
        return new TipoCompresor[0];
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
                throw new EnumConstantNotPresentException(TipoCompresor.class, "El tipo de compresor" + tipoC + "no existe");
        }
    }

    public boolean isProcesado() {
        return procesado;
    }

    public void setFicheroIn(File ficheroIn) {
        this.ficheroIn = ficheroIn;
    }

    public void setFicheroOut(File ficheroOut) {
        this.ficheroOut = ficheroOut;
    }

    public void setTipoC(TipoCompresor tipoC) {
        this.tipoC = tipoC;
    }

    public File getFicheroIn() {
        return ficheroIn;
    }

    public File getFicheroOut() {
        return ficheroOut;
    }

    public TipoCompresor getTipoC() {
        return tipoC;
    }
}
