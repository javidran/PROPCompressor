package PresentationLayer;

import Controllers.CtrlPresentacion;
import DomainLayer.Proceso.DatosProceso;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VistaResultadoProceso extends JDialog{

    private JLabel tiempo;
    private JLabel OldSize;
    private JLabel NewSize;
    private JLabel diffSize;
    private JLabel Ratio;
    private JPanel Result;
    private JButton OKButton;
    private JPanel panel;

    public VistaResultadoProceso(Frame owner, DatosProceso dp){
        super (owner, true);
        setContentPane(panel);
        tiempo.setText(Long.toString(dp.getTiempo()));
        OldSize.setText(Long.toString(dp.getOldSize()));
        NewSize.setText(Long.toString(dp.getNewSize()));
        diffSize.setText(Long.toString(dp.getDiffSize()));
        Ratio.setText(Double.toString(dp.getDiffSizePercentage()));
        OKButton.addActionListener(e -> {
            dispose();
        });
    }
}
