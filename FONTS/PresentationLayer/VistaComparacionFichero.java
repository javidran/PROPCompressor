package PresentationLayer;

import DomainLayer.Proceso.DatosProceso;

import javax.swing.*;
import java.awt.*;

public class VistaComparacionFichero extends JDialog {
    public JTextArea original;
    public JTextArea resultante;
    private JButton finalizarButton;
    private JScrollPane scrollPanel;
    private JPanel panel;

    public VistaComparacionFichero(Frame owner, DatosProceso[] dp){
        super (owner, "Proceso completado",true);
        setContentPane(panel);
        scrollPanel.getVerticalScrollBar().setUnitIncrement(16);

        //TODO mostrar cosas de datos proceso


        finalizarButton.addActionListener(e -> dispose());
    }

}
