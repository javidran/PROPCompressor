package Controllers;

import DomainLayer.Proceso.DatosProceso;
import Enumeration.Algoritmo;
import Exceptions.FormatoErroneoException;
import PresentationLayer.*;

import javax.swing.*;
import javax.swing.plaf.basic.BasicProgressBarUI;
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
    private VistaResultadoProceso vistaResultadoProceso;

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

    public void crearModeloParametros() {
        modeloParametros = new ModeloParametros();
    }

    public void crearVistaInicio() {
        SwingUtilities.invokeLater(() -> {
            vistaInicio = new VistaInicio();
            modeloParametros.setVistaInicio(vistaInicio);
            vistaInicio.algoritmoPredeterminado(CtrlProcesos.getAlgoritmoDeTextoPredeterminado());
            vistaInicio.setSize(new Dimension(650, 300));
            vistaInicio.setMinimumSize(new Dimension(500, 200));
            vistaInicio.setResizable(true);
            vistaInicio.setVisible(true);
            vistaInicio.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            vistaInicio.setLocationRelativeTo(null);
        });
    }

    public void cerrarVistaInicio() {
        vistaInicio = null;
    }

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

    public void escogerPredeterminadoPulsado() {
        JDialog dialog = new VistaSelectorAlgortimoPredeterminado(vistaInicio);
        dialog.setSize(new Dimension(600, 200));
        dialog.setMinimumSize(new Dimension(600, 200));
        dialog.setLocationRelativeTo(vistaInicio);
        dialog.setResizable(true);
        dialog.setVisible(true);
    }

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

    public void crearVistaSeleccionAlgoritmo(boolean conGuardado) {
        boolean existe = CtrlDatos.existeArchivo(modeloParametros.getPathOriginal());
        if (!existe) {
            JOptionPane.showConfirmDialog(null, "¡El fichero o carpeta que desea procesar no existe! Seleccione un archivo o carpeta existente", "¡No existe!", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
        }
        else{
            vistaSelectorAlgoritmo = new VistaSelectorAlgoritmo(vistaInicio);
            modeloParametros.setVistaSelectorAlgoritmo(vistaSelectorAlgoritmo, conGuardado);
            vistaSelectorAlgoritmo.setSize(new Dimension(650, 300));
            vistaSelectorAlgoritmo.setMinimumSize(new Dimension(500, 200));
            vistaSelectorAlgoritmo.setLocationRelativeTo(vistaInicio);
            vistaSelectorAlgoritmo.setResizable(true);
            vistaSelectorAlgoritmo.setVisible(true);
        }
    }

    public void cerrarVistaSeleccionAlgoritmo() {
        vistaSelectorAlgoritmo = null;
    }

    public void pulsarCerradoVistaSeleccionAlgoritmo() {
        algoritmoSeleccionado("Predeterminado"); //Para que en caso de volver atras se quede con el algoritmo predeterminado por si vuelves a entrar al selctor de algoritmo
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

    public void iniciarProceso() throws Exception {
        boolean existe = (new File(modeloParametros.getPathResultado())).exists();
        if (existe) {
            int respuesta = JOptionPane.showConfirmDialog(null, "El fichero resultante del proceso sobrescribirá uno ya existente, ¿desea sobrescribirlo?", "Sobrescribir",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            cerrarVistaSeleccionAlgoritmo();
            if (respuesta == JOptionPane.NO_OPTION) {
                crearVistaSeleccionAlgoritmo(modeloParametros.isConGuardado());
            }
        }
        else  cerrarVistaSeleccionAlgoritmo();

        procesar();
    }

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
        dialog.pack();
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
                            dp[0] =ctrlProcesos.descomprimirCarpeta(modeloParametros.getPathOriginal(), modeloParametros.getPathResultado());
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
        else {
            //TODO mostrar vistas y todo eso.
        }
    }

    public String getEstadisticas(String data) throws IOException {
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

    public void crearVistaEstadisticas() {
        vistaEstadisticas = new VistaEstadisticas(vistaInicio);
        vistaEstadisticas.setSize(new Dimension(600, 300));
        vistaEstadisticas.setMinimumSize(new Dimension(500, 250));
        vistaEstadisticas.setLocationRelativeTo(vistaInicio);
        vistaEstadisticas.setResizable(true);
        vistaEstadisticas.setVisible(true);
    }

    public void mostrarEstadisticas(String data) {
        try {
            vistaEstadisticas.mostrarEstadisticasSelecionadas(getEstadisticas(data));
        } catch (IOException e) {
            JOptionPane.showConfirmDialog(null, "Ha habido un problema al acceder a los datos estadísticos. Por favor, vuelva a intentarlo más tarde.",null, JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
        }
    }

    public void CalidadModificada(int value) {
        CtrlProcesos.setCalidadJPEG(value);
    }

}
