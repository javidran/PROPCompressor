package PresentationLayer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ResultadoFichero extends JDialog {
    private JTextArea original;
    private JTextArea resultante;
    private JButton finalizarButton;
    private JScrollPane scrollPanel;
    private JDialog myself = this;

    public ResultadoFichero(){
        scrollPanel.getVerticalScrollBar().setUnitIncrement(16);
        finalizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myself.setVisible(false);
            }
        });
    }

}
