package PresentationLayer.Helpers;

import javax.swing.*;
import java.awt.*;


public class HelpVistaInicio extends JDialog {
    private JTextArea textoAyuda;
    private JButton cerrarButton;
    private JPanel AyudaPrincipal;

    public HelpVistaInicio(Frame owner) {
        super (owner, "Ayuda", true);
        setContentPane(AyudaPrincipal);
        textoAyuda.setOpaque(false);
        textoAyuda.setBackground(new Color(0,0,0,0));
        textoAyuda.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        cerrarButton.addActionListener(e -> dispose());
    }
}
