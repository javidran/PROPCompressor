package PresentationLayer;

import Controllers.CtrlPresentacion;

import javax.swing.*;
import java.awt.*;
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
    private JButton helpButton;
    private JButton atrasButton;

    public VistaEstadisticas(Frame owner) {
        super (owner, true);
        ctrlPresentacion = CtrlPresentacion.getInstance();
        setContentPane(Estadisticas);

        Estats.setVisible(false);
        atrasButton.setVisible(false);

        procesarButton.addActionListener(e -> {
            String data =  Objects.requireNonNull(comboBox1.getSelectedItem()).toString();
            ctrlPresentacion.mostrarEstadisticas(data);
        });
        cancelarButton.addActionListener(e -> dispose());
        helpButton.addActionListener(e -> ctrlPresentacion.crearVistaAyudaEstadisticas());
        atrasButton.addActionListener(e -> {
            ctrlPresentacion.volverAEscogerEstadística();
        });
    }

    public void mostrarEstadisticasSelecionadas(String mensaje) {
        setSize(375, 400);
        setLocationRelativeTo(getOwner());
        status.setText(mensaje);
        Estats.setVisible(true);
        status.setOpaque(false);
        Algoritmo.setVisible(false);
        status.setBackground(new Color(0,0,0,0));
        procesarButton.setVisible(false);
        helpButton.setVisible(false);
        atrasButton.setVisible(true);
    }

    public void mostrarSelectorAlgoritmo() {
        setSize(600, 200);
        setLocationRelativeTo(getOwner());
        Estats.setVisible(false);
        Algoritmo.setVisible(true);
        procesarButton.setVisible(true);
        helpButton.setVisible(true);
        atrasButton.setVisible(false);
    }
}
