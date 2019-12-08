package PresentationLayer;

import DomainLayer.Proceso.DatosProceso;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

/**
 * Clase para la implementación de la vista que permite comparar un fichero original con el resultante de aplicar la comprsesión y descompresión con un algoritmo determinado.
 */
public class VistaCompDescImagen extends JDialog {
    private JPanel PanelGeneral;
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

    /**
     * Constructora de la vista para la comparación de un imagen con esa misma pero procesada.
     * <p>
     *     Esta vista no solo permite la visualización de ambas imagenes sinó que tambien da al usuario datos del proceso de compresión y descompresión
     * </p>
     * @param owner vista propietaria de esta nueva vista
     *
     */
    public VistaCompDescImagen(Frame owner){
        //, Byte[] Original, Byte[] Nuevo
        super (owner, "Comparar imágenes",true);
        setContentPane(PanelGeneral);

        salirButton.addActionListener(e -> dispose());
    }
    /**
     * Constructora de la vista para la comparación de un imagenes con esa misma pero procesada.
     * @param bufferedImageOriginal Imagen original en el formato adecuado para ser impreso en la vista
     * @param bufferedImageNuevo Imagen procesada en el formato adecuado para ser impreso en la vista
     * @param dp array que contiene los datos de la compresión y la descompresión de la imagen mostrado
     */
    public void setImagenes(Image bufferedImageOriginal, Image bufferedImageNuevo, DatosProceso[] dp) {
        original.setIcon(new ImageIcon(bufferedImageOriginal));
        original.setMaximumSize(new Dimension(300, 200));
        nuevo.setIcon(new ImageIcon(bufferedImageNuevo));
        nuevo.setMaximumSize(new Dimension(300, 200));
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
