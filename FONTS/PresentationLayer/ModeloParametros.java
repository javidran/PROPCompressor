package PresentationLayer;

import Enumeration.Algoritmo;

public class ModeloParametros {
    private VistaInicio vistaInicio;
    private VistaSelectorAlgoritmo vistaSelectorAlgoritmo;

    private boolean compresion;
    private Algoritmo algoritmo;
    private String pathOriginal;
    private String pathResultado;
    private boolean conGuardado;

    public void setVistaInicio(VistaInicio vistaInicio) {
        this.vistaInicio = vistaInicio;
    }

    public void setVistaSelectorAlgoritmo(VistaSelectorAlgoritmo vistaSelectorAlgoritmo, boolean compresionYDescompresion) {
        this.vistaSelectorAlgoritmo = vistaSelectorAlgoritmo;
        this.conGuardado = compresionYDescompresion;
        if(conGuardado) this.vistaSelectorAlgoritmo.mostrarSelectorDePath(pathResultado);


        if((algoritmo.equals(Algoritmo.JPEG) || algoritmo.equals(Algoritmo.CARPETA)) && compresion) this.vistaSelectorAlgoritmo.mostrarSliderDeCalidad();
        else if(!algoritmo.equals(Algoritmo.CARPETA) && (this.isCompresion())) this.vistaSelectorAlgoritmo.mostrarSelectorAlgoritmo();
    }

    public boolean isCompresion() {
        return compresion;
    }

    public void setCompresion(boolean compresion) {
        this.compresion = compresion;
        vistaInicio.botonComprimir(compresion);
    }

    public Algoritmo getAlgoritmo() {
        return algoritmo;
    }

    public void setAlgoritmo(Algoritmo algoritmo) {
        this.algoritmo = algoritmo;
        if(algoritmo.equals(Algoritmo.CARPETA))  vistaInicio.botoncomprimirYDescomprimir(false);
        else vistaInicio.botoncomprimirYDescomprimir(compresion);
    }

    public String getPathOriginal() {
        return pathOriginal;
    }

    public void setPathOriginal(String pathOriginal) {
        this.pathOriginal = pathOriginal;
    }

    public String getPathResultado() {
        return pathResultado;
    }

    public void setPathResultado(String pathResultado) {
        this.pathResultado = pathResultado;
        if(vistaSelectorAlgoritmo!=null) vistaSelectorAlgoritmo.pathResultadoCambiado(pathResultado);
    }

    public boolean isConGuardado() {
        return conGuardado;
    }
}
