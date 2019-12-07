package PresentationLayer;

import Controllers.CtrlPresentacion;
import DataLayer.DatosEstadistica;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.Objects;

public class VistaEstadisticas extends JDialog {
    private CtrlPresentacion ctrlPresentacion;

    private JPanel Estadisticas;
    private JPanel Algoritmo;
    private JComboBox comboBox1;
    private JButton cancelarButton;
    private JPanel Estats;
    private JButton procesarButton;
    private JButton helpButton;
    private JButton atrasButton;
    private JLabel alg;
    private JLabel archivosC;
    private JLabel tiempoC;
    private JLabel ratioC;
    private JLabel archivosD;
    private JLabel tiempoD;
    private JLabel ratioD;

    public VistaEstadisticas(Frame owner) {
        super (owner, true);
        ctrlPresentacion = CtrlPresentacion.getInstance();
        setContentPane(Estadisticas);

        Estats.setVisible(false);
        atrasButton.setVisible(false);

        procesarButton.addActionListener(e -> {
            String data =  Objects.requireNonNull(comboBox1.getSelectedItem()).toString();
            ctrlPresentacion.mostrarEstadisticas(data);
        });
        cancelarButton.addActionListener(e -> dispose());
        helpButton.addActionListener(e -> ctrlPresentacion.crearVistaAyudaEstadisticas());
        atrasButton.addActionListener(e -> ctrlPresentacion.volverAEscogerEstadistica());
    }

    public void mostrarEstadisticasSelecionadas(DatosEstadistica de) {
        setSize(375, 350);
        setLocationRelativeTo(getOwner());
        DecimalFormat df = new DecimalFormat("#.####");
        alg.setText(Objects.requireNonNull(comboBox1.getSelectedItem()).toString());
        archivosC.setText(Integer.toString(de.getArchivosComprimidos()));
        tiempoC.setText(df.format((double) de.getTiempoCompresión()/ 1000000000.0) + " s");
        ratioC.setText(de.getRatioCompresión() + " %");
        archivosD.setText(Integer.toString(de.getArchivosDescomprimidos()));
        tiempoD.setText(df.format((double)de.getTiempoDescompresión()/ 1000000000.0) + " s");
        ratioD.setText(de.getRatioDescompresión() + " %");
        Estats.setVisible(true);
        Algoritmo.setVisible(false);
        procesarButton.setVisible(false);
        helpButton.setVisible(false);
        atrasButton.setVisible(true);
    }

    public void mostrarSelectorAlgoritmo() {
        setSize(500, 200);
        setLocationRelativeTo(getOwner());
        Estats.setVisible(false);
        Algoritmo.setVisible(true);
        procesarButton.setVisible(true);
        helpButton.setVisible(true);
        atrasButton.setVisible(false);
    }

}
