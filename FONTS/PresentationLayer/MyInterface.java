package PresentationLayer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.*;
import java.awt.event.*;

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

        salirButton.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }
}

