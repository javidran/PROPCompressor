package PresentationLayer;

import DomainLayer.Proceso.DatosProceso;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

/**
 * Clase para la implementación de la vista que permite comparar un fichero original con el resultante de aplicar la comprsesión y descompresión con un algoritmo determinado.
 */
public class VistaComparacionFichero extends JDialog {
    public JTable original;

    /**
     * Botón para finalizar la visualización de la vista
     */
    private JButton finalizarButton;
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
    private JScrollPane originalPanel;
    private JScrollPane resultadoPanel;
    public JTable resultado;

    /**
     * Constructora de la vista para la comparación de un fichero con ese mismo procesado.
     * <p>
     *     Esta vista no solo permite la visualización de ambos textos sinó que tambien da al usuario datos del proceso de compresión y descompresión
     * </p>
     * @param owner vista propietaria de esta nueva vista
     * @param dp array que contiene los datos de la compresión y la descompresión del archivo mostrado
     */
    public VistaComparacionFichero(Frame owner, DatosProceso[] dp){
        super (owner, "Comparar textos",true);
        setContentPane(panel);
        originalPanel.getVerticalScrollBar().setUnitIncrement(16);
        originalPanel.getHorizontalScrollBar().setUnitIncrement(16);
        resultadoPanel.getVerticalScrollBar().setUnitIncrement(16);
        resultadoPanel.getHorizontalScrollBar().setUnitIncrement(16);

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

        finalizarButton.addActionListener(e -> dispose());
    }

}
