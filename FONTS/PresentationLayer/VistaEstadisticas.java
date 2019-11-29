package PresentationLayer;

import Controllers.CtrlPresentacion;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class VistaEstadisticas extends JDialog {
    private JPanel Estadisticas;
    private JPanel Algoritmo;
    private JComboBox comboBox1;
    private JButton cancelarButton;
    private JPanel Estats;
    private JButton procesarButton;
    private JTextArea status;

    VistaEstadisticas(Frame owner) {
        super (owner, true);
        setContentPane(Estadisticas);
        Estats.setVisible(false);
        procesarButton.addActionListener(e -> {
            Estats.setVisible(true);
            String data = "";
            if (comboBox1.getSelectedIndex() != -1) {
                data += comboBox1.getItemAt(comboBox1.getSelectedIndex());
            }
            CtrlPresentacion cp = CtrlPresentacion.getInstance();
            try {
                String mensaje = cp.getEstadisticas(data);
                status.setText(mensaje);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            Algoritmo.setVisible(false);
            procesarButton.setVisible(false);
        });
        cancelarButton.addActionListener(e -> dispose());
    }

}
