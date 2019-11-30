package Controllers;

import Enumeration.Algoritmo;
import PresentationLayer.*;

import javax.swing.*;
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
        //TODO Mover comprobaciones a capa de Datos
        modeloParametros.setPathOriginal(path);
        String[] splitP = path.split("\\.");
        String type = splitP[splitP.length-1];

        //TODO Detectar correctamente carpetas aunque estas tengan puntos

        if(splitP.length == 1 && !path.endsWith(".")) {
            modeloParametros.setCompresion(true);
            modeloParametros.setAlgoritmo(Algoritmo.CARPETA);
        }
        else switch (type) {
            case "imgc":
                modeloParametros.setCompresion(false);
                modeloParametros.setAlgoritmo(Algoritmo.JPEG);
                break;
            case "comp":
                modeloParametros.setCompresion(false);
                modeloParametros.setAlgoritmo(Algoritmo.CARPETA);
                break;
            case "lzss":
            case "lzw":
            case "lz78":
                modeloParametros.setCompresion(false);
                modeloParametros.setAlgoritmo(CtrlProcesos.getAlgoritmoDeTextoPredeterminado());
                break;
            case "txt":
                modeloParametros.setCompresion(true);
                modeloParametros.setAlgoritmo(CtrlProcesos.getAlgoritmoDeTextoPredeterminado());
                break;
            case "ppm":
                modeloParametros.setCompresion(true);
                modeloParametros.setAlgoritmo(Algoritmo.JPEG);
                break;
            default:
                vistaInicio.deshabilitarBotones();
                break;
        }
        actualizarPathSalida(path);
    }

    public void estadisticasPulsado() {

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
        if(vistaInicio != null) vistaInicio.algoritmoPredeterminado(CtrlProcesos.getAlgoritmoDeTextoPredeterminado());
    }

    public void cerrarVistaSeleccionAlgoritmo() {
        //Next Change Area
        vistaSelectorAlgoritmo = null;
    }

    private String extension() {
        String extension = null;
        Algoritmo tipoAlgoritmo = modeloParametros.getAlgoritmo();
        if(modeloParametros.isCompresion()) {
            switch (tipoAlgoritmo) {
                case LZW:
                    extension = "lzw";
                    break;
                case LZSS:
                    extension = "lzss";
                    break;
                case LZ78:
                    extension = "lz78";
                    break;
                case JPEG:
                    extension = "imgc";
                    break;
                case CARPETA:
                    extension = "comp";
                    break;
            }
        } else {
            switch (tipoAlgoritmo) {
                case LZW:
                case LZSS:
                case LZ78:
                    extension = "txt";
                    break;
                case JPEG:
                    extension = "ppm";
                    break;
                case CARPETA:
                    extension = "";
                    break;
            }
        }
        return extension;
    }

    public void actualizarPathSalida(String path) {
        String[] splitP = path.split("\\.");
        String type = splitP[splitP.length-1];
        String ext = extension();
        if(!path.contains(".")) path = path + "." + ext;
        else if(splitP.length==1) path = path + ext;
        else if (!type.equalsIgnoreCase(ext)) {
            if(modeloParametros.getAlgoritmo().equals(Algoritmo.CARPETA) && !modeloParametros.isCompresion()) path = path.replace("." + type, ext);
            else path = path.replace(type, ext);
        }
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

    public void iniciarProceso() {
        boolean existe = (new File(modeloParametros.getPathResultado())).exists();
        if (existe) {
            int respuesta = JOptionPane.showConfirmDialog(null, "El fichero resultante del proceso sobrescribirá uno ya existente, ¿desea sobrescribirlo?", "Sobrescribir",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            cerrarVistaSeleccionAlgoritmo();
            if (respuesta == JOptionPane.NO_OPTION) {
                crearVistaSeleccionAlgoritmo(modeloParametros.isConGuardado());
            }
        }
        else cerrarVistaSeleccionAlgoritmo();
        //TODO Procesar El Fichero
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
        vistaEstadisticas.setMinimumSize(new Dimension(500, 200)); //600 300
        vistaEstadisticas.setLocationRelativeTo(vistaInicio);
        vistaEstadisticas.setResizable(true);
        vistaEstadisticas.setVisible(true);
    }

    public void crearVistaSeleccionAlgoritmo(boolean conGuardado) {
        vistaSelectorAlgoritmo = new VistaSelectorAlgoritmo(vistaInicio);
        modeloParametros.setVistaSelectorAlgoritmo(vistaSelectorAlgoritmo, conGuardado);
        vistaSelectorAlgoritmo.setSize(new Dimension(650, 300));
        vistaSelectorAlgoritmo.setMinimumSize(new Dimension(500, 200));
        vistaSelectorAlgoritmo.setLocationRelativeTo(vistaInicio);
        vistaSelectorAlgoritmo.setResizable(true);
        vistaSelectorAlgoritmo.setVisible(true);
    }

    public void mostrarEstadisticas(String data) throws IOException {
        String mensaje = getEstadisticas(data);
        vistaEstadisticas.mostrarEstadisticasSelecionadas(mensaje);
    }
}
