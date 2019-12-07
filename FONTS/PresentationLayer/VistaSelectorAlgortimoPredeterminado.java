package PresentationLayer;

import Controllers.CtrlPresentacion;
import Enumeration.Algoritmo;

import javax.swing.*;
import java.awt.*;

public class VistaSelectorAlgortimoPredeterminado extends JDialog {
    private CtrlPresentacion ctrlPresentacion;

    private JPanel algoritmo;
    private JButton CancelButton;
    private JRadioButton LZSSRadioButton;
    private JRadioButton LZWRadioButton;
    private JRadioButton LZ78RadioButton;
    private JPanel boton;
    private JPanel Predeterminado;
    private ButtonGroup algortimos;

    public VistaSelectorAlgortimoPredeterminado(Frame owner) {
        super (owner, "Selector de algoritmo predeterminado", true);
        setContentPane(Predeterminado);
        ctrlPresentacion = CtrlPresentacion.getInstance();

        LZSSRadioButton.addActionListener(actionEvent -> {
            ctrlPresentacion.algoritmoPredeterminadoEscogido(Algoritmo.LZSS);
            dispose();
        });

        LZWRadioButton.addActionListener(actionEvent -> {
            ctrlPresentacion.algoritmoPredeterminadoEscogido(Algoritmo.LZW);
            dispose();
        });

        LZ78RadioButton.addActionListener(actionEvent -> {
            ctrlPresentacion.algoritmoPredeterminadoEscogido(Algoritmo.LZ78);
            dispose();
        });

        CancelButton.addActionListener(actionEvent -> dispose());
    }

}
