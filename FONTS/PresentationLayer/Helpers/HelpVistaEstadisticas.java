package PresentationLayer.Helpers;

import javax.swing.*;
import java.awt.*;


public class HelpVistaEstadisticas extends JDialog {
    private JTextArea textoAyuda;
    private JButton cerrarButton;
    private JPanel AyudaPrincipal;


    public HelpVistaEstadisticas(Dialog owner) {
        super (owner, "Ayuda", true);
        setContentPane(AyudaPrincipal);
        //OkPanel.setVisible(true);
        textoAyuda.setOpaque(false);
        textoAyuda.setBackground(new Color(0,0,0,0));

        cerrarButton.addActionListener(e -> dispose());
    }
}