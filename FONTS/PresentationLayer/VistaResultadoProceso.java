package PresentationLayer;

import DomainLayer.Proceso.DatosProceso;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

/**
 * Clase para la implementación de la vista que permite ver los resultados estadísticos de un proceso
 */
public class VistaResultadoProceso extends JDialog {
    private JLabel tiempo;
    private JLabel OldSize;
    private JLabel NewSize;
    private JLabel diffSize;
    private JLabel Ratio;
    /**
     * Botón para finalizar la visualización de la vista
     */
    private JButton OKButton;
    private JPanel panel;

    /**
     * Creadora de una vista que permite al usuario ver los resultados estadísticos de un proceso una vez ha terminado
     * @param owner vista propietaria de esta nueva vista
     * @param dp instancia de DatosProceso que contiene los datos del proceso realizado
     */
    public VistaResultadoProceso(Frame owner, DatosProceso dp){
        super (owner, "Proceso completado",true);
        setContentPane(panel);
        DecimalFormat df = new DecimalFormat("#.####");
        tiempo.setText(df.format((double)dp.getTiempo() / 1000000000.0));
        OldSize.setText(Long.toString(dp.getOldSize()));
        NewSize.setText(Long.toString(dp.getNewSize()));
        diffSize.setText(Long.toString(dp.getDiffSize()));
        Ratio.setText(Double.toString(dp.getDiffSizePercentage()));

        OKButton.addActionListener(e -> {
            dispose();
        });
    }
}
