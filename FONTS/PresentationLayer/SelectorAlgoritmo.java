package PresentationLayer;

import javax.swing.*;

public class SelectorAlgoritmo extends JFrame {
    private JComboBox comboBox1;
    private JButton procesarButton;
    private JPanel panel;
    private JSlider slider1;
    private JPanel calidad;
    private JPanel procesar;
    private JPanel algoritmo;

    public SelectorAlgoritmo() {
        super ("PROPresor");
        setContentPane(panel);
    }

    public SelectorAlgoritmo(String path) {
        super ("PROPresor");
        setContentPane(panel);
    }
}
