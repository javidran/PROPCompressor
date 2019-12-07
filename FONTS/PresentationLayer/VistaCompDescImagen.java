package PresentationLayer;

import DomainLayer.Proceso.DatosProceso;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

public class VistaCompDescImagen extends JDialog {
    private JPanel PanelGeneral;
    private JPanel Imagenes;
    private JPanel Botones;
    private JButton salirButton;
    private JLabel original;
    private JLabel nuevo;
    private JLabel tiempoD;
    private JLabel OldSizeD;
    private JLabel NewSizeD;
    private JLabel DiffSizeD;
    private JLabel RatioD;
    private JLabel tiempoC;
    private JLabel OldSizeC;
    private JLabel NewSizeC;
    private JLabel DiffSizeC;
    private JLabel RatioC;

    public VistaCompDescImagen(Frame owner){
        //, Byte[] Original, Byte[] Nuevo
        super (owner, "Comparar imágenes",true);
        setContentPane(PanelGeneral);

        salirButton.addActionListener(e -> dispose());
    }

    public void setImagenes(Image bufferedImageOriginal, Image bufferedImageNuevo, DatosProceso[] dp) {
        original.setIcon(new ImageIcon(bufferedImageOriginal));
        nuevo.setIcon(new ImageIcon(bufferedImageNuevo));
        DecimalFormat df = new DecimalFormat("#.####");
        tiempoC.setText(df.format((double)dp[0].getTiempo() / 1000000000.0));
        OldSizeC.setText(Long.toString(dp[0].getOldSize()));
        NewSizeC.setText(Long.toString(dp[0].getNewSize()));
        DiffSizeC.setText(Long.toString(dp[0].getDiffSize()));
        RatioC.setText(Double.toString(dp[0].getDiffSizePercentage()));

        tiempoD.setText(df.format((double)dp[1].getTiempo() / 1000000000.0));
        OldSizeD.setText(Long.toString(dp[1].getOldSize()));
        NewSizeD.setText(Long.toString(dp[1].getNewSize()));
        DiffSizeD.setText(Long.toString(dp[1].getDiffSize()));
        RatioD.setText(Double.toString(dp[1].getDiffSizePercentage()));
    }
}
