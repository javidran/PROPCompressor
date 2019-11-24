package PresentationLayer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Estadisticas extends JFrame {
    private JPanel Estadisticas;
    private JPanel Algoritmo;
    private JComboBox comboBox1;
    private JButton salirButton;
    private JPanel Options;
    private JPanel Estats;
    private JButton procesarButton;
    private JLabel statuslabel;

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
                    data = "Selected: "
                            + comboBox1.getItemAt(comboBox1.getSelectedIndex());
                }
                statuslabel.setText(data);
                Algoritmo.setVisible(false);
                procesarButton.setVisible(false);
            }
        });
    }

}
