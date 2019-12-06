package PresentationLayer;

import DomainLayer.Proceso.DatosProceso;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

public class VistaCompDescImagen extends JDialog {
    private JPanel PanelGeneral;
    private JPanel Imagenes;
    private JPanel Botones;
    private JButton salirButton;
    private JLabel original;
    private JLabel nuevo;

    public VistaCompDescImagen(Frame owner, Byte[] Original, Byte[] Nuevo){
        //, Byte[] Original, Byte[] Nuevo
        super (owner, "Comparar imágenes",true);
        setContentPane(PanelGeneral);

        salirButton.addActionListener(e -> dispose());
    }
}
