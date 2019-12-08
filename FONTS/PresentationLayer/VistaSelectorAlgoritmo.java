package PresentationLayer;

import Controllers.CtrlPresentacion;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Objects;

public class VistaSelectorAlgoritmo extends JDialog {
    private CtrlPresentacion ctrlPresentacion;

    private JComboBox comboBox1;
    private JButton procesarButton;
    private JPanel panel;
    private JSlider sliderCalidad;
    private JPanel calidad;
    private JPanel algoritmo;
    private JButton cancelarButton;
    private JPanel selectorSalida;
    private JTextField pathSalida;
    private JButton explorarButton;
    private JButton helpButton;
    private JFileChooser chooser;

    public VistaSelectorAlgoritmo(Frame owner) {
        super (owner, "Configurador del proceso", true);
        ctrlPresentacion = CtrlPresentacion.getInstance();
        setContentPane(panel);

        selectorSalida.setVisible(false);
        algoritmo.setVisible(false);
        calidad.setVisible(false);

        procesarButton.addActionListener(actionEvent -> {
            setVisible(false);
            ctrlPresentacion.iniciarProceso();
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

        sliderCalidad.addChangeListener(e -> {
            if (!sliderCalidad.getValueIsAdjusting())
                ctrlPresentacion.calidadModificada( sliderCalidad.getValue());
        });
        helpButton.setBorderPainted(false);
        helpButton.setBorder(null);
        helpButton.setMargin(new Insets(0, 0, 0, 0));
        helpButton.setContentAreaFilled(false);
        helpButton.addActionListener(e -> ctrlPresentacion.crearVistaAyudaSelectorAlgoritmo());
    }

    private void seleccionDeArchivo() {
        String pathOut = pathSalida.getText();
        File f = new File(pathOut);
        String dir = pathOut.replace(f.getName(), "");
        chooser = new JFileChooser(new File(dir));
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
