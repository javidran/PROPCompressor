package PresentationLayer.Helpers;

import javax.swing.*;
import java.awt.*;
import java.awt.peer.DialogPeer;


public class HelpVistaEstadisticas extends JDialog {

    private JTextArea textoAyuda;
    private JButton cerrarButton;
    private JPanel AyudaPrincipal;
    private JPanel Ayuda;
    private JPanel OkPanel;


    public HelpVistaEstadisticas(Dialog owner) {
        super (owner, true);
        setContentPane(AyudaPrincipal);
        //OkPanel.setVisible(true);
        textoAyuda.setOpaque(false);
        textoAyuda.setBackground(new Color(0,0,0,0));

        cerrarButton.addActionListener(e -> dispose());
    }
}