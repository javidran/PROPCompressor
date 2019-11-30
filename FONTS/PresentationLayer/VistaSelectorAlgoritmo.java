package PresentationLayer;

import Controllers.CtrlPresentacion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.util.Objects;

public class VistaSelectorAlgoritmo extends JDialog {
    private CtrlPresentacion ctrlPresentacion;

    private JComboBox comboBox1;
    private JButton procesarButton;
    private JPanel panel;
    private JSlider sliderCalidad;
    private JPanel calidad;
    private JPanel procesar;
    private JPanel algoritmo;
    private JButton cancelarButton;
    private JPanel selectorSalida;
    private JTextField pathSalida;
    private JButton explorarButton;

    public VistaSelectorAlgoritmo(Frame owner) {
        super (owner, true);
        ctrlPresentacion = CtrlPresentacion.getInstance();
        setContentPane(panel);

        selectorSalida.setVisible(false);
        algoritmo.setVisible(false);
        calidad.setVisible(false);

        procesarButton.addActionListener(actionEvent -> {
            setVisible(false);
            try {
                ctrlPresentacion.iniciarProceso();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        cancelarButton.addActionListener(actionEvent -> {
            setVisible(false);
            ctrlPresentacion.pulsarCerradoVistaSeleccionAlgoritmo();
        });

        explorarButton.addActionListener(actionEvent -> seleccionDeArchivo());

        comboBox1.addActionListener(actionEvent -> {
            String algoritmo = Objects.requireNonNull(comboBox1.getSelectedItem()).toString();
            ctrlPresentacion.algoritmoSeleccionado(algoritmo);
        });

        sliderCalidad.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent componentEvent) {
                super.componentMoved(componentEvent);
                //TODO Implementar
            }
        });
    }

    private void seleccionDeArchivo() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int result = chooser.showSaveDialog(null);
        if(result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            ctrlPresentacion.actualizarPathSalida(file.getAbsolutePath());
        }
    }

    public void mostrarSelectorDePath(String pathResultado) {
        selectorSalida.setVisible(true);
        pathResultadoCambiado(pathResultado);
    }

    public void pathResultadoCambiado(String pathResultado) {
        pathSalida.setText(pathResultado);
    }

    public void mostrarSliderDeCalidad() {
        calidad.setVisible(true);
    }

    public void mostrarSelectorAlgoritmo() {
        algoritmo.setVisible(true);
    }
}
