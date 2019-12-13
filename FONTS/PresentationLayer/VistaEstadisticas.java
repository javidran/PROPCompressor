package PresentationLayer;

import Controllers.CtrlPresentacion;
import DomainLayer.DatosEstadistica;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.Objects;

public class VistaEstadisticas extends JDialog {
    /**
     * Instancia de CtrlPresentacion que contiene gran parte del codigo que altera la vista y lleva a cabo gran parte de las acciones al pulsar botones
     */
    private CtrlPresentacion ctrlPresentacion;

    private JPanel panel;
    private JPanel Algoritmo;
    private JPanel Estats;
    /**
     * ComboBox para escoger uno de los algoritmos posibles
     */
    private JComboBox comboBox1;
    /**
     * Botón para cancelar el proceso actual
     */
    private JButton cancelarButton;
    /**
     * Botón para visualizar las estadísticas del algoritmo escogido
     */
    private JButton visualizarButton;
    /**
     * Botón de información que explica que se puede hacer en esta vista
     */
    private JButton InfoButton;
    /**
     * Botón para volver a la ventana anterior
     */
    private JButton atrasButton;

    private JLabel alg;
    private JLabel archivosC;
    private JLabel tiempoC;
    private JLabel ratioC;
    private JLabel archivosD;
    private JLabel tiempoD;
    private JLabel ratioD;
    private JLabel velocidadC;
    private JLabel velocidadD;

    /**
     * Creadora de la vista estadística que permite al usuario ver las estadísticos globales de un algoritmo escogido
     * @param owner vista propietaria de esta nueva vista
     */
    public VistaEstadisticas(Frame owner) {
        super (owner, "Estadísticas", true);
        ctrlPresentacion = CtrlPresentacion.getInstance();
        setContentPane(panel);

        Estats.setVisible(false);
        atrasButton.setVisible(false);

        visualizarButton.addActionListener(e -> {
            String data =  Objects.requireNonNull(comboBox1.getSelectedItem()).toString();
            ctrlPresentacion.mostrarEstadisticas(data);
        });
        cancelarButton.addActionListener(e -> dispose());
        atrasButton.addActionListener(e -> ctrlPresentacion.volverAEscogerEstadistica());

        InfoButton.setBorderPainted(false);
        InfoButton.setBorder(null);
        InfoButton.setMargin(new Insets(0, 0, 0, 0));
        InfoButton.setContentAreaFilled(false);
        InfoButton.addActionListener(e -> ctrlPresentacion.crearVistaAyudaEstadisticas());
    }

    /**
     * Muestra las estadísticas que se le pasa por paràmetro
     * @param de Es una instancia de DatosEstadística del algoritmo seleccionado
     */
    public void mostrarEstadisticasSelecionadas(DatosEstadistica de) {
        DecimalFormat df = new DecimalFormat("#.####");
        alg.setText(Objects.requireNonNull(comboBox1.getSelectedItem()).toString());
        archivosC.setText(Integer.toString(de.getArchivosComprimidos()));
        tiempoC.setText(df.format((double) de.getTiempoCompresion()/ 1000000000.0) + " s");
        ratioC.setText(de.getRatioCompresion() + " %");
        archivosD.setText(Integer.toString(de.getArchivosDescomprimidos()));
        tiempoD.setText(df.format((double)de.getTiempoDescompresion()/ 1000000000.0) + " s");
        ratioD.setText(de.getRatioDescompresion() + " %");
        velocidadC.setText(df.format(de.getVelocidadCompresion()) + " B/s");
        velocidadD.setText(df.format(de.getVelocidadDescompresion()) + " B/s");
        Estats.setVisible(true);
        Algoritmo.setVisible(false);
        visualizarButton.setVisible(false);
        InfoButton.setVisible(false);
        atrasButton.setVisible(true);
        setSize(375, 350);
        pack();
        setLocationRelativeTo(getOwner());
    }

    /**
     * Muestra los algoritmos possibles de los cuales obtener las estadísticas
     */
    public void mostrarSelectorAlgoritmo() {
        Estats.setVisible(false);
        Algoritmo.setVisible(true);
        visualizarButton.setVisible(true);
        InfoButton.setVisible(true);
        atrasButton.setVisible(false);
        setSize(500, 200);
        pack();
        setLocationRelativeTo(getOwner());
    }

}
