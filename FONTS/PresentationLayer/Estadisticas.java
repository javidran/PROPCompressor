package PresentationLayer;

import Controllers.CtrlPresentacion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Estadisticas extends JFrame {
    private JPanel Estadisticas;
    private JPanel Algoritmo;
    private JComboBox comboBox1;
    private JButton salirButton;
    private JPanel Estats;
    private JButton procesarButton;
    private JLabel status;

    Estadisticas() {
        super ("PROPresor");
        setContentPane(Estadisticas);
        Estats.setVisible(false);
        procesarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
            }
        });
        salirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

}
