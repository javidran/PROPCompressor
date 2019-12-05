package PresentationLayer;

import DomainLayer.Proceso.DatosProceso;

import javax.swing.*;
import java.awt.*;

public class VistaResultadoProceso extends JDialog{

    private JLabel tiempo;
    private JLabel OldSize;
    private JLabel NewSize;
    private JLabel diffSize;
    private JLabel Ratio;

    public VistaResultadoProceso(Frame owner, DatosProceso dp){
        String t = Long.toString(dp.getTiempo());
        String os = Long.toString(dp.getOldSize());
        String ns = Long.toString(dp.getNewSize());
        String df = Long.toString(dp.getDiffSize());
        String r = Double.toString(dp.getDiffSizePercentage());
        tiempo.setText(t);
        OldSize.setText(os);
        NewSize.setText(ns);
        diffSize.setText(df);
        Ratio.setText(r);
    }
}
