package PresentationLayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JRadioButton;

public class SelectorAlgortimoPredeterminado extends JDialog {
    private JPanel algoritmo;
    private JButton okButton;
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
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) { myself.setVisible(false); }
        });
    }

}
