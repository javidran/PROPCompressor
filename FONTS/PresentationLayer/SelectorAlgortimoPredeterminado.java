package PresentationLayer;

import Controllers.CtrlProcesos;
import Enumeration.Algoritmo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SelectorAlgortimoPredeterminado extends JDialog {
    private JPanel algoritmo;
    private JButton CancelButton;
    private JRadioButton LZSSRadioButton;
    private JRadioButton LZWRadioButton;
    private JRadioButton LZ78RadioButton;
    private JPanel boton;
    private JPanel Predeterminado;
    private ButtonGroup algortimos;
    private JDialog myself = this;


    public SelectorAlgortimoPredeterminado(Frame owner) {
        super (owner, true);
        setContentPane(Predeterminado);
        LZSSRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                CtrlProcesos.setAlgoritmoDeTextoPredeterminado(Algoritmo.LZSS);
                myself.setVisible(false);
            }
        });

        LZWRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                CtrlProcesos.setAlgoritmoDeTextoPredeterminado(Algoritmo.LZW);
                myself.setVisible(false);
            }
        });

        LZ78RadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                CtrlProcesos.setAlgoritmoDeTextoPredeterminado(Algoritmo.LZ78);
                myself.setVisible(false);
            }
        });
        CancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                myself.setVisible(false);
            }
        });
    }

}
