package PresentationLayer;

import javax.swing.*;

public class MyInterface extends JFrame {
    private JPanel panel;
    private JButton salirButton;
    private JButton comprimirYDescomprimirButton;
    private JTextField textField1;
    private JButton variableButton;
    private JButton estadísticasButton;

    public MyInterface () {
        super ("Ejemplo interfaz.");
        setContentPane(panel);
    }
}

