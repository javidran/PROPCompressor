package PresentationLayer.Helpers;

import javax.swing.*;
import java.awt.*;

public class HelpVistaSelectorAlgoritmo extends JDialog {
    private JTextArea textoAyuda;
    private JButton cerrarButton;
    private JPanel AyudaPrincipal;
    private JPanel Ayuda;
    private JPanel OkPanel;

    public HelpVistaSelectorAlgoritmo(Dialog owner) {
        super (owner, true);
        setContentPane(AyudaPrincipal);
        textoAyuda.setOpaque(false);
        textoAyuda.setBackground(new Color(0,0,0,0));

        cerrarButton.addActionListener(e -> dispose());
    }


}