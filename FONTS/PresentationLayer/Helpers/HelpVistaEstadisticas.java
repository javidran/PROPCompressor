package PresentationLayer.Helpers;

import javax.swing.*;
import java.awt.*;

/**
 * Clase para la creación de una vista que contiene la información de ayuda para la vista de selección de estadísticas del PROPresor
 */
public class HelpVistaEstadisticas extends JDialog {
    private JTextArea textoAyuda;
    /**
     * Botón para cerrar la vista de ayuda
     */
    private JButton cerrarButton;
    private JPanel AyudaPrincipal;


    /**
     * Creación de una vista modal que contiene la ayuda de la vista de selección de estadísticas del PROPresor
     * @param owner dialog propietario de esta nueva vista
     */
    public HelpVistaEstadisticas(Dialog owner) {
        super (owner, "Ayuda", true);
        setContentPane(AyudaPrincipal);
        textoAyuda.setOpaque(false);
        textoAyuda.setBackground(new Color(0,0,0,0));

        cerrarButton.addActionListener(e -> dispose());
    }
}