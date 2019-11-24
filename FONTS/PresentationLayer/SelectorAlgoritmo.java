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

    public SelectorAlgoritmo(Boolean MostrarSlider, Boolean MostrarPath) {
        super ("PROPresor");
        setContentPane(panel);
        algoritmo.setVisible(!MostrarSlider);
        calidad.setVisible(MostrarSlider);
        //procesar.setVisible(true);

    }

    public SelectorAlgoritmo(String path, Boolean MostrarSlider, Boolean MostrarPath) {
        super ("PROPresor");
        setContentPane(panel);
        algoritmo.setVisible(!MostrarSlider);
        calidad.setVisible(MostrarSlider);
        //procesar.setVisible(true);

    }
}
