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

    public void setImagenes(BufferedImage bufferedImageOriginal, BufferedImage bufferedImageNuevo) {
        original.setIcon(new ImageIcon(bufferedImageOriginal));
        nuevo.setIcon(new ImageIcon(bufferedImageNuevo));
    }
}
