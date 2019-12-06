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
    private JLabel tiempoC;
    private JLabel OldSizeC;
    private JLabel NewSizeC;
    private JLabel DiffSizeC;
    private JLabel RatioC;
    private JLabel tiempoD;
    private JLabel OldSizeD;
    private JLabel NewSizeD;
    private JLabel DiffSizeD;
    private JLabel RatioD;

    public VistaComparacionFichero(Frame owner, DatosProceso[] dp){
        super (owner, "Proceso completado",true);
        setContentPane(panel);
        scrollPanel.getVerticalScrollBar().setUnitIncrement(16);
        tiempoC.setText(Long.toString(dp[0].getTiempo()));
        OldSizeC.setText(Long.toString(dp[0].getOldSize()));
        NewSizeC.setText(Long.toString(dp[0].getNewSize()));
        DiffSizeC.setText(Long.toString(dp[0].getDiffSize()));
        RatioC.setText(Double.toString(dp[0].getDiffSizePercentage()));

        tiempoD.setText(Long.toString(dp[1].getTiempo()));
        OldSizeD.setText(Long.toString(dp[1].getOldSize()));
        NewSizeD.setText(Long.toString(dp[1].getNewSize()));
        DiffSizeD.setText(Long.toString(dp[1].getDiffSize()));
        RatioD.setText(Double.toString(dp[1].getDiffSizePercentage()));



        finalizarButton.addActionListener(e -> dispose());
    }

}
