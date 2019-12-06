package PresentationLayer;

import javax.swing.*;

public class VistaResultadoFichero extends JDialog {
    private JTextArea original;
    private JTextArea resultante;
    private JButton finalizarButton;
    private JScrollPane scrollPanel;

    public VistaResultadoFichero(){
        scrollPanel.getVerticalScrollBar().setUnitIncrement(16);
        finalizarButton.addActionListener(e -> dispose());
    }

}
