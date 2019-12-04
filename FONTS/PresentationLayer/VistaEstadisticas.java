package PresentationLayer;

import Controllers.CtrlPresentacion;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class VistaEstadisticas extends JDialog {
    private CtrlPresentacion ctrlPresentacion;

    private JPanel Estadisticas;
    private JPanel Algoritmo;
    private JComboBox comboBox1;
    private JButton cancelarButton;
    private JPanel Estats;
    private JButton procesarButton;
    private JTextArea status;

    public VistaEstadisticas(Frame owner) {
        super (owner, true);
        ctrlPresentacion = CtrlPresentacion.getInstance();
        setContentPane(Estadisticas);

        Estats.setVisible(false);

        procesarButton.addActionListener(e -> {
            String data =  Objects.requireNonNull(comboBox1.getSelectedItem()).toString();
            try {
                ctrlPresentacion.mostrarEstadisticas(data);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        cancelarButton.addActionListener(e -> dispose());
    }

    public void mostrarEstadisticasSelecionadas(String mensaje) {
        status.setText(mensaje);
        Estats.setVisible(true);
        status.setOpaque(false);
        Algoritmo.setVisible(false);
        status.setBackground(new Color(0,0,0,0));
        procesarButton.setVisible(false);
    }

}