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

    public Estadisticas(Boolean MostrarAlgoritmo) {
        super ("PROPresor");
        setContentPane(Estadisticas);
        Algoritmo.setVisible(true);
        Estats.setVisible(!MostrarAlgoritmo);
        procesarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Algoritmo.setVisible(!MostrarAlgoritmo);
                Estats.setVisible(MostrarAlgoritmo);
                procesarButton.setVisible(false);
            }
        });
    }
}
