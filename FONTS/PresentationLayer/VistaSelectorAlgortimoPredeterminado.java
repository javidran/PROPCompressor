package PresentationLayer;

import Controllers.CtrlPresentacion;
import Enumeration.Algoritmo;

import javax.swing.*;
import java.awt.*;

/**
 * Clase para la creación de una vista que permite seleccionar el algoritmo predeterminado del PROPresor
 */
public class VistaSelectorAlgortimoPredeterminado extends JDialog {
    /**
     * Instancia de CtrlPresentacion que contiene gran parte del codigo que altera la vista y lleva a cabo gran parte de las acciones al pulsar botones
     */
    private CtrlPresentacion ctrlPresentacion;

    /**
     * Botón para cancelar el proceso seleccionado en la vista anterior
     */
    private JButton CancelButton;
    /**
     * Botón para la selección de del algoritmo de compresión de texto LZSS
     */
    private JRadioButton LZSSRadioButton;
    /**
     * Botón para la selección de del algoritmo de compresión de texto LZW
     */
    private JRadioButton LZWRadioButton;
    /**
     * Botón para la selección de del algoritmo de compresión de texto LZ78
     */
    private JRadioButton LZ78RadioButton;
    private JPanel Predeterminado;
    private ButtonGroup algoritmos;

    /**
     * Creación de una vista modal que permite seleccionar el algoritmo predeterminado del PROPresor
     * @param owner dialog propietario de esta nueva vista
     */
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
