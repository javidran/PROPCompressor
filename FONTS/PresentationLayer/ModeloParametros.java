package PresentationLayer;

import Enumeration.Algoritmo;

/**
 * Clase utilizada para guardar los parámetros clave que se necesitan para poder realizar correctamente un proceso de compresión o descompresión.
 * Estos son recopilados y modificados hasta que se procede a procesar un archivo o carpeta.
 */
public class ModeloParametros {
    /**
     * Instancia de la vista inicial.
     */
    private VistaInicio vistaInicio;
    /**
     * Instancia de la vista de selección de algoritmo.
     */
    private VistaSelectorAlgoritmo vistaSelectorAlgoritmo;

    /**
     * Indica si se trata de un proceso de compresión o no.
     */
    private boolean compresion;
    /**
     * Algoritmo a utilizar durante el proceso.
     */
    private Algoritmo algoritmo;
    /**
     * Path del archivo o carpeta a procesar.
     */
    private String pathOriginal;
    /**
     * Path resultante del archivo o carpeta procesado.
     */
    private String pathResultado;
    /**
     * Indica si se trata de una funcionalidad con guardado o no.
     */
    private boolean conGuardado;

    /**
     * Agrega una instancia de vistaInicio al modelo para poder realizar actualizaciones a la misma.
     * @param vistaInicio Instancia de la vista principal.
     */
    public void setVistaInicio(VistaInicio vistaInicio) {
        this.vistaInicio = vistaInicio;
    }

    /**
     * Agrega una instancia de VistaSelectorAlgoritmo al modelo para poder realizar actualizaciones a la misma y habilita los paneles mostrado según los parámetros introducidos hasta el momento.
     * @param vistaSelectorAlgoritmo Instancia de la vista de Selector de algoritmo.
     * @param conGuardado booleano indicador de si se trata de una funcionalidad con guardado de archivo.
     */
    public void setVistaSelectorAlgoritmo(VistaSelectorAlgoritmo vistaSelectorAlgoritmo, boolean conGuardado) {
        this.vistaSelectorAlgoritmo = vistaSelectorAlgoritmo;
        this.conGuardado = conGuardado;
        if(conGuardado) this.vistaSelectorAlgoritmo.mostrarSelectorDePath(pathResultado);

        if((algoritmo.equals(Algoritmo.JPEG) || algoritmo.equals(Algoritmo.CARPETA)) && compresion) this.vistaSelectorAlgoritmo.mostrarSliderDeCalidad();
        else if(!algoritmo.equals(Algoritmo.CARPETA) && (this.isCompresion())) this.vistaSelectorAlgoritmo.mostrarSelectorAlgoritmo();
    }

    /**
     * Indica si es un proceso de compresión o no.
     * @return Devuelve si es un proceso de compresión o no.
     */
    public boolean isCompresion() {
        return compresion;
    }

    /**
     * Setea el parámetro que indica si es un proceso de compresión o no.
     * @param compresion Booleano que indica si es un proceso de compresión.
     */
    public void setCompresion(boolean compresion) {
        this.compresion = compresion;
        vistaInicio.botonComprimir(compresion);
    }

    /**
     * Indica el algoritmo seleccionado para el proceso a realizar.
     * @return Devuelve el algoritmo a utilizar.
     */
    public Algoritmo getAlgoritmo() {
        return algoritmo;
    }

    /**
     * Setea el parámetro que indica el algoritmo que se utilizará.
     * @param algoritmo Algoritmo con el que realizar el proceso.
     */
    public void setAlgoritmo(Algoritmo algoritmo) {
        this.algoritmo = algoritmo;
        if(algoritmo.equals(Algoritmo.CARPETA))  vistaInicio.botoncomprimirYDescomprimir(false);
        else vistaInicio.botoncomprimirYDescomprimir(compresion);
    }

    /**
     * Devuelve el path de origen del archivo a procesar.
     * @return El path de origen del proceso a realizar.
     */
    public String getPathOriginal() {
        return pathOriginal;
    }

    /**
     * Setea el path de origen del archivo a procesar.
     * @param pathOriginal Path de origen del proceso a realizar.
     */
    public void setPathOriginal(String pathOriginal) {
        this.pathOriginal = pathOriginal;
    }

    /**
     * Devuelve el path del resultado después de procesar el archivo.
     * @return El path del resultado obtenido tras el proceso.
     */
    public String getPathResultado() {
        return pathResultado;
    }

    /**
     * Setea el path del resultado del archivo procesado.
     * @param pathResultado El path del resultado del proceso.
     */
    public void setPathResultado(String pathResultado) {
        this.pathResultado = pathResultado;
        if(vistaSelectorAlgoritmo!=null) vistaSelectorAlgoritmo.pathResultadoCambiado(pathResultado);
    }

    /**
     * Indica si es un proceso con guardado o de lo contrario es la funcionalidad comprimirYDescomprimir.
     * @return Si es un proceso con guardado.
     */
    public boolean isConGuardado() {
        return conGuardado;
    }
}
