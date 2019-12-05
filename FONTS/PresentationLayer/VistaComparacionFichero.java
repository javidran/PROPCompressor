package PresentationLayer;

import javax.swing.*;

public class VistaComparacionFichero extends JDialog {
    private JTextArea original;
    private JTextArea resultante;
    private JButton finalizarButton;
    private JScrollPane scrollPanel;

    public VistaComparacionFichero(){
        scrollPanel.getVerticalScrollBar().setUnitIncrement(16);
        finalizarButton.addActionListener(e -> dispose());
    }

}
