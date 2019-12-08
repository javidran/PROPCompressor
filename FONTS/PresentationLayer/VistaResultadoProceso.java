package PresentationLayer;

import DomainLayer.Proceso.DatosProceso;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

public class VistaResultadoProceso extends JDialog {
    private JLabel tiempo;
    private JLabel OldSize;
    private JLabel NewSize;
    private JLabel diffSize;
    private JLabel Ratio;
    private JButton OKButton;
    private JPanel panel;

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
