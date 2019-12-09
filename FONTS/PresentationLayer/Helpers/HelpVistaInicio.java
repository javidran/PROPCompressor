package PresentationLayer.Helpers;

import javax.swing.*;
import java.awt.*;


/**
 * Clase para la creación de una vista que contiene la información de ayuda de la primera vista del PROPresor
 */
public class HelpVistaInicio extends JDialog {
    private JPanel panel;
    private JTextArea textoAyuda;
    /**
     * Botón para cerrar la vista de ayuda
     */
    private JButton cerrarButton;

    /**
     * Creación de una interfaz modal que contentiene la ayuda de la interfaz principal del PROPpresor
     * @param owner frame propietario de esta nueva vista
     */
    public HelpVistaInicio(Frame owner) {
        super (owner, "Ayuda", true);
        setContentPane(panel);
        textoAyuda.setOpaque(false);
        textoAyuda.setBackground(new Color(0,0,0,0));
        textoAyuda.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        cerrarButton.addActionListener(e -> dispose());
    }
}
