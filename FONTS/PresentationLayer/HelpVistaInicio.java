package PresentationLayer;

import javax.swing.*;
import java.awt.*;


public class HelpVistaInicio extends JDialog {


    private JPanel OkPanel;
    private JPanel Ayuda;
    private JTextArea textoAyuda;
    private JButton cerrarButton;
    private JPanel AyudaPrincipal;


    public HelpVistaInicio(Frame owner) {
        super (owner, true);
        setContentPane(AyudaPrincipal);
        textoAyuda.setOpaque(false);
        textoAyuda.setBackground(new Color(0,0,0,0));

        cerrarButton.addActionListener(e -> dispose());
    }
}
