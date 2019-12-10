package Controllers;

import DomainLayer.DatosEstadistica;
import DomainLayer.Proceso.DatosProceso;
import Enumeration.Algoritmo;
import Exceptions.FormatoErroneoException;
import PresentationLayer.Helpers.HelpVistaEstadisticas;
import PresentationLayer.Helpers.HelpVistaInicio;
import PresentationLayer.Helpers.HelpVistaSelectorAlgoritmo;
import PresentationLayer.*;

import javax.swing.*;
import javax.swing.plaf.basic.BasicProgressBarUI;
import javax.swing.table.TableModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class CtrlPresentacion {
    /**
     * Instancia de CtrlPresentacion para garantizar que es una clase Singleton
     */
    private static CtrlPresentacion instance = null;
    private ModeloParametros modeloParametros;
    private VistaInicio vistaInicio;
    private VistaSelectorAlgoritmo vistaSelectorAlgoritmo;
    private VistaEstadisticas vistaEstadisticas;

    /**
     * Getter de la instancia Singleton de CtrlPresentacion
     * @return La instancia Singleton de CtrlPresentacion
     */
    public static CtrlPresentacion getInstance()
    {
        if (instance == null)
            instance = new CtrlPresentacion();
        return instance;
    }

    /**
     * Crea la instancia ModeloParametros
     */
    public void crearModeloParametros() {
        modeloParametros = new ModeloParametros();
    }

    /**
     * Crea la vista VistaInicio
     */
    public void crearVistaInicio() {
        SwingUtilities.invokeLater(() -> {
            vistaInicio = new VistaInicio();
            modeloParametros.setVistaInicio(vistaInicio);
            vistaInicio.algoritmoPredeterminado(CtrlProcesos.getAlgoritmoDeTextoPredeterminado());
            vistaInicio.setSize(new Dimension(650, 300));
            vistaInicio.setMinimumSize(new Dimension(510, 250));
            vistaInicio.setResizable(true);
            vistaInicio.setVisible(true);
            vistaInicio.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            vistaInicio.setLocationRelativeTo(null);
        });
    }

    /**
     * Cierra la vista VistaInicio
     */
    public void cerrarVistaInicio() {
        vistaInicio = null;
    }

    /**
     * Crea la vista VistaSeleccionAlgoritmo
     * @param conGuardado Indica si es Compresión/Descompresión o no
     */
    public void crearVistaSeleccionAlgoritmo(boolean conGuardado) {
        boolean existe = CtrlDatos.existeArchivo(modeloParametros.getPathOriginal());
        if (!existe) {
            JOptionPane.showConfirmDialog(null, "¡El fichero o carpeta que desea procesar no existe! Seleccione un archivo o carpeta existente", "¡No existe!", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
        }
        else {
            vistaSelectorAlgoritmo = new VistaSelectorAlgoritmo(vistaInicio);
            modeloParametros.setVistaSelectorAlgoritmo(vistaSelectorAlgoritmo, conGuardado);
            if (!conGuardado) vistaSelectorAlgoritmo.setSize(new Dimension(650, 190));
            else {
                if (modeloParametros.isCompresion()) vistaSelectorAlgoritmo.setSize(new Dimension(650, 210));
                if (modeloParametros.getAlgoritmo().equals(Algoritmo.CARPETA)) vistaSelectorAlgoritmo.setSize(new Dimension(650, 210));
            }
            vistaSelectorAlgoritmo.setMinimumSize(new Dimension(600, 180));
            vistaSelectorAlgoritmo.setLocationRelativeTo(vistaInicio);
            vistaSelectorAlgoritmo.setResizable(true);
            vistaSelectorAlgoritmo.setVisible(true);
        }
    }

    /**
     * Cierra la vista VistaSeleccionAlgoritmo
     */
    public void cerrarVistaSeleccionAlgoritmo() {
        vistaSelectorAlgoritmo = null;
    }

    /**
     * Crea la vista VistaResultadoProceso
     * @param dp DatosProceso del proceso ejecutado
     */
    public void crearVistaResultadoProceso(DatosProceso dp) {
        VistaResultadoProceso vistaResultadoProceso = new VistaResultadoProceso(vistaInicio, dp);
        vistaResultadoProceso.setSize(new Dimension(450, 250));
        vistaResultadoProceso.setMinimumSize(new Dimension(450, 250));
        vistaResultadoProceso.setLocationRelativeTo(vistaInicio);
        vistaResultadoProceso.setResizable(true);
        vistaResultadoProceso.pack();
        vistaResultadoProceso.setVisible(true);
    }

    /**
     * Crea la vista ComparacionProceso
     * @param dp Datos de la compresión y la descompresión del fichero
     */
    public void crearVistaComparacionProceso(DatosProceso[] dp) {
        VistaComparacionFichero vistaComparacionFichero = new VistaComparacionFichero(vistaInicio, dp);
        vistaComparacionFichero.setSize(new Dimension(800, 660));
        vistaComparacionFichero.setMinimumSize(new Dimension(700, 400));
        vistaComparacionFichero.setLocationRelativeTo(vistaInicio);
        vistaComparacionFichero.setResizable(true);
        CtrlProcesos ctrlProcesos = CtrlProcesos.getInstance();
        try {
            TableModel modelOriginal = ctrlProcesos.getArchivoAsModel(modeloParametros.getPathOriginal());
            vistaComparacionFichero.aplicaTextoOriginal(modelOriginal);
            TableModel modelResultado = ctrlProcesos.getArchivoAsModel(modeloParametros.getPathOriginal());
            vistaComparacionFichero.aplicaTextoResultante(modelResultado);
        } catch (Exception e) {
            JOptionPane.showConfirmDialog(null, "Ha ocurrido un error al intentar mostrar los archivos para comparación. Por favor, intentelo de nuevo.",null, JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
        } finally {
            ctrlProcesos.eliminaArchivoTemporal();
        }
        vistaComparacionFichero.pack();
        vistaComparacionFichero.setVisible(true);
    }

    /**
     * Crea la vista VistaCompDescImagen
     * @param dp Datos de la compresión y la descompresión de la imagen
     */
    public void crearVistaCompDescImagen(DatosProceso[] dp) {

        VistaCompDescImagen vistaCompDescImagen = new VistaCompDescImagen(vistaInicio);
        vistaCompDescImagen.setSize(new Dimension(800, 570));
        vistaCompDescImagen.setLocationRelativeTo(vistaInicio);
        vistaCompDescImagen.setResizable(false);
        CtrlProcesos ctrlProcesos = CtrlProcesos.getInstance();
        try {
            vistaCompDescImagen.setImagenes(ctrlProcesos.getImage(modeloParametros.getPathOriginal()), ctrlProcesos.getImage(CtrlProcesos.getArchivoTemporal()), dp);
        } catch (Exception e) {
            JOptionPane.showConfirmDialog(null, "Ha ocurrido un error al intentar mostrar los archivos para comparación. Por favor, intentelo de nuevo.",null, JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
        } finally {
            ctrlProcesos.eliminaArchivoTemporal();
        }
        vistaCompDescImagen.pack();
        vistaCompDescImagen.setVisible(true);
    }

    /**
     * Crea la vista VistaEstadísticas
     */
    public void crearVistaEstadisticas() {
        vistaEstadisticas = new VistaEstadisticas(vistaInicio);
        vistaEstadisticas.setSize(new Dimension(600, 200));
        vistaEstadisticas.setLocationRelativeTo(vistaInicio);
        vistaEstadisticas.setResizable(true);
        vistaEstadisticas.setVisible(true);
    }

    /**
     * Crea la vista VistaAyudaInicio
     */
    public void crearVistaAyudaInicio() {
        HelpVistaInicio helpVistaInicio = new HelpVistaInicio(vistaInicio);
        helpVistaInicio.setSize(new Dimension(475, 550));
        helpVistaInicio.setLocationRelativeTo(vistaInicio);
        helpVistaInicio.setResizable(false);
        helpVistaInicio.pack();
        helpVistaInicio.setVisible(true);
    }
    /**
     * Crea la vista VistaAyudaEstadisticas
     */
    public void crearVistaAyudaEstadisticas() {
        HelpVistaEstadisticas helpVistaEstadisticas = new HelpVistaEstadisticas(vistaEstadisticas);
        helpVistaEstadisticas.setSize(new Dimension(400, 300));
        helpVistaEstadisticas.setLocationRelativeTo(vistaEstadisticas);
        helpVistaEstadisticas.setResizable(false);
        helpVistaEstadisticas.pack();
        helpVistaEstadisticas.setVisible(true);
    }
    /**
     * Crea la vista VistaAyudaSelectorAlgoritmo
     */
    public void crearVistaAyudaSelectorAlgoritmo() {
        HelpVistaSelectorAlgoritmo helpVistaSelectorAlgoritmo = new HelpVistaSelectorAlgoritmo(vistaSelectorAlgoritmo);
        helpVistaSelectorAlgoritmo.setSize(new Dimension(400, 300));
        helpVistaSelectorAlgoritmo.setLocationRelativeTo(vistaSelectorAlgoritmo);
        helpVistaSelectorAlgoritmo.setResizable(false);
        helpVistaSelectorAlgoritmo.pack();
        helpVistaSelectorAlgoritmo.setVisible(true);
    }

    /**
     * Guarda la configuración del archivo a procesar según la extensión del algoritmo. También genera un path resultante estándar.
     * @param path path del archivo o carpeta a procesar seleccionado.
     */
    public void pathCambiado(String path) {
        modeloParametros.setPathOriginal(path);
        try {
            modeloParametros.setCompresion(CtrlProcesos.esComprimible(path));
            modeloParametros.setAlgoritmo(CtrlProcesos.algoritmoPosible(path));
        } catch (FormatoErroneoException e) {
            vistaInicio.deshabilitarBotones();
        }
        actualizarPathSalida(path);
   }

    /**
     * Crea el diálogo que permite seleccionar el algoritmo de texto predeterminado.
     */
    public void escogerPredeterminadoPulsado() {
        JDialog dialog = new VistaSelectorAlgortimoPredeterminado(vistaInicio);
        dialog.setSize(new Dimension(600, 200));
        dialog.setMinimumSize(new Dimension(600, 200));
        dialog.setLocationRelativeTo(vistaInicio);
        dialog.setResizable(true);
        dialog.pack();
        dialog.setVisible(true);
    }

    /**
     * Guarda el algoritmo de texto seleccionado como el nuevo algoritmo de texto predeterminado.
     * @param algoritmo Algoritmo de texto escogido.
     */
    public void algoritmoPredeterminadoEscogido(Algoritmo algoritmo) {
        CtrlProcesos.setAlgoritmoDeTextoPredeterminado(algoritmo);
        if(modeloParametros.isCompresion()) {
            switch (modeloParametros.getAlgoritmo()) {
                case LZSS:
                case LZW:
                case LZ78:
                    modeloParametros.setAlgoritmo(algoritmo);
                    actualizarPathSalida(modeloParametros.getPathResultado());
                    break;
            }
        }
        if(vistaInicio != null) vistaInicio.algoritmoPredeterminado(CtrlProcesos.getAlgoritmoDeTextoPredeterminado());
    }

    /**
     * Cierra correctamente la vista de selección de algoritmo.
     */
    public void pulsarCerradoVistaSeleccionAlgoritmo() {
        switch (modeloParametros.getAlgoritmo()) {
            case LZSS:
            case LZW:
            case LZ78:
                algoritmoSeleccionado("Predeterminado");
                break;
        }
        cerrarVistaSeleccionAlgoritmo();
    }

    /**
     * Actualiza el path pasado por parámetro para que sea el path de salida del fichero procesado.
     * @param path El path del archivo antes de ser procesado.
     */
    public void actualizarPathSalida(String path) {
        path = CtrlProcesos.calcularPathSalida(path, modeloParametros.getAlgoritmo(), modeloParametros.isCompresion());
        modeloParametros.setPathResultado(path);
    }

    /**
     * Actualiza en modeloParametros el nuevo algoritmo seleccionado
     * @param algoritmo Uno de los tres algoritmos de textos que se pueden seleccionar
     */
    public void algoritmoSeleccionado(String algoritmo) {
        Algoritmo tipoAlgoritmo;
        switch (algoritmo) {
            case "Predeterminado":
                tipoAlgoritmo = CtrlProcesos.getAlgoritmoDeTextoPredeterminado();
                break;
            case "LZSS":
                tipoAlgoritmo = Algoritmo.LZSS;
                break;
            case "LZW":
                tipoAlgoritmo = Algoritmo.LZW;
                break;
            case "LZ78":
                tipoAlgoritmo = Algoritmo.LZ78;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + algoritmo);
        }
        modeloParametros.setAlgoritmo(tipoAlgoritmo);
        actualizarPathSalida(modeloParametros.getPathResultado());
    }

    /**
     * Comprueba si el fichero existe y si se va a sobreescribir para iniciar el proceso
     */
    public void iniciarProceso() {
        boolean existe = (new File(modeloParametros.getPathResultado())).exists();
        cerrarVistaSeleccionAlgoritmo();
        if (existe && modeloParametros.isConGuardado()) {
            int respuesta = JOptionPane.showConfirmDialog(null, "El fichero resultante del proceso sobrescribirá uno ya existente, ¿desea sobrescribirlo?", "Sobrescribir",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (respuesta == JOptionPane.NO_OPTION) {
                crearVistaSeleccionAlgoritmo(modeloParametros.isConGuardado());
            }
            else procesar();
        }
        else procesar();
    }

    /**
     * Se ejecuta el proceso de comprimir, descomprimir o ComprimirYDescomprimir
     * y se indica que se ha de crear una de las vistas que muestra resultados
     */
    private void procesar(){

        final JDialog dialog = new JDialog(vistaInicio, true); // modal
        dialog.setResizable(false);
        dialog.setUndecorated(true);
        JProgressBar bar = new JProgressBar();
        bar.setIndeterminate(true);
        bar.setStringPainted(true);
        bar.setBorderPainted(true);

        bar.setString("Procesando...");
        dialog.add(bar);
        bar.setBackground(Color.white);
        bar.setUI(new BasicProgressBarUI() {
            protected Color getSelectionBackground() { return Color.black; }
            protected Color getSelectionForeground() { return Color.white; }
        });
        dialog.setSize(new Dimension(300, 50));
        dialog.setLocationRelativeTo(vistaInicio);

        final DatosProceso[] dp = new DatosProceso[2];
        final Exception[] exceptionProceso = {null};

        SwingWorker<Void,Void> worker = new SwingWorker<Void,Void>()
        {
            @Override
            protected Void doInBackground()
            {
                try {
                    CtrlProcesos ctrlProcesos = CtrlProcesos.getInstance();
                    if (!modeloParametros.isConGuardado()) {
                        DatosProceso[] multiplesDatos = ctrlProcesos.comprimirDescomprimirArchivo(modeloParametros.getPathOriginal(), modeloParametros.getAlgoritmo());
                        dp[0] = multiplesDatos[0];
                        dp[1] = multiplesDatos[1];
                    } else if (modeloParametros.isCompresion()) {
                        if (modeloParametros.getAlgoritmo().equals(Algoritmo.CARPETA))
                            dp[0] = ctrlProcesos.comprimirCarpeta(modeloParametros.getPathOriginal(), modeloParametros.getPathResultado());
                        else
                            dp[0] = ctrlProcesos.comprimirArchivo(modeloParametros.getPathOriginal(), modeloParametros.getPathResultado(), modeloParametros.getAlgoritmo());
                    } else {
                        if (modeloParametros.getAlgoritmo().equals(Algoritmo.CARPETA))
                            dp[0] = ctrlProcesos.descomprimirCarpeta(modeloParametros.getPathOriginal(), modeloParametros.getPathResultado());
                        else
                            dp[0] = ctrlProcesos.descomprimirArchivo(modeloParametros.getPathOriginal(), modeloParametros.getPathResultado());
                    }
                }
                catch (Exception e){
                    exceptionProceso[0] = e;
                }
                return null;
            }

            @Override
            protected void done() {
                dialog.dispose();
            }
        };
        worker.execute();
        dialog.setVisible(true); // will block but with a responsive GUI

        if(exceptionProceso[0]!= null) JOptionPane.showConfirmDialog(null, "Se ha dado el siguente error durante el proceso:\n"+exceptionProceso[0].getMessage(),null, JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
        else if (modeloParametros.isConGuardado()) crearVistaResultadoProceso(dp[0]);
        else if(modeloParametros.getAlgoritmo().equals(Algoritmo.JPEG)) crearVistaCompDescImagen(dp);
        else crearVistaComparacionProceso(dp);
    }

    /**
     * @param value El valor de calidad con el que se quiere comprimir con el algoritmo jpeg
     */
    public void calidadModificada(int value) {
        CtrlProcesos.setCalidadJPEG(value);
    }

    /**
     * Obtención de las estadísticas de unos de los 4 algoritmos posibles
     * @param data Indica de que algoritmo queremos las estadísticas
     * @return DatosEstadistica devuelve todos los datos globales del algoritmo seleccionado
     * @throws IOException excepción si hay un error con la lectura de las estadísticas globales
     */
    public DatosEstadistica getEstadisticas(String data) throws IOException {
        CtrlEstadistica ce = CtrlEstadistica.getInstance();
        Algoritmo alg;
        switch (data) {
            case "JPEG":
                alg = Algoritmo.JPEG;
                break;
            case "LZSS":
                alg = Algoritmo.LZSS;
                break;
            case "LZW":
                alg = Algoritmo.LZW;
                break;
            case "LZ78":
                alg = Algoritmo.LZ78;
                break;
            default:
                throw new EnumConstantNotPresentException(Algoritmo.class, " El tipo de compresor " + data + " no existe.");
        }
        return ce.estadisticas(alg);
    }

    /**
     * Muestra las estadísticas en la vista
     * @param data algoritmo escogido para visualizar las estadísticas de este
     */
    public void mostrarEstadisticas(String data) {
        try {
            vistaEstadisticas.mostrarEstadisticasSelecionadas(getEstadisticas(data));
            vistaEstadisticas.pack();
        } catch (IOException e) {
            JOptionPane.showConfirmDialog(null, "Ha habido un problema al acceder a los datos estadísticos. Por favor, vuelva a intentarlo más tarde.",null, JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Volver para ver los algoritmos posibles de los cuales visualizar estadísticas
     */
    public void volverAEscogerEstadistica() {
        vistaEstadisticas.mostrarSelectorAlgoritmo();
        vistaEstadisticas.pack();
    }
}
