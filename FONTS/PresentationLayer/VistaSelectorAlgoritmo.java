package PresentationLayer;

import Controllers.CtrlPresentacion;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Objects;

public class VistaSelectorAlgoritmo extends JDialog {
    /**
     * Instancia de CtrlPresentacion que contiene gran parte del codigo que altera la vista y lleva a cabo gran parte de las acciones al pulsar botones
     */
    private CtrlPresentacion ctrlPresentacion;

    private JPanel panel;
    private JPanel calidad;
    private JPanel algoritmo;
    private JPanel selectorSalida;

    private JTextField pathSalida;
    /**
     * ComboBox para escoger uno de los algoritmos posibles
     */
    private JComboBox comboBox1;
    /**
     * Botón para procesar el archivo seleccionado en la vista anterior
     */
    private JButton procesarButton;
    /**
     * Selector de calidad en forma de slide que se muestra en el caso de comprimir una imagen
     */
    private JSlider sliderCalidad;
    /**
     * Botón para cancelar el proceso actual
     */
    private JButton cancelarButton;
    /**
     * Botón para abrir el explorador y seleccionar un nuevo path de salida que no sea el inicial
     */
    private JButton explorarButton;
    /**
     * botón para iniciar la craeción de una vista con ayuda sobre la vista actual
     */
    private JButton InfoButton;
    private JButton InfoButton2;
    /**
     * Elemento para la selección de archivo o carpeta a procesar
     */
    private JFileChooser chooser;

    /**
     * Creación de una vista modal que permite seleccionar el algoritmo con el que se comprimirá el archivo o la calidad de compresión si es una imagen.Tambien permite seleccionar un nuevo path de salida
     * @param owner vista propietaria de esta nueva vista
     */
    public VistaSelectorAlgoritmo(Frame owner) {
        super (owner, "Configurador del proceso", true);
        ctrlPresentacion = CtrlPresentacion.getInstance();
        setContentPane(panel);

        selectorSalida.setVisible(false);
        algoritmo.setVisible(false);
        calidad.setVisible(false);

        procesarButton.addActionListener(actionEvent -> {
            setVisible(false);
            ctrlPresentacion.iniciarProceso();
        });

        cancelarButton.addActionListener(actionEvent -> {
            setVisible(false);
            ctrlPresentacion.pulsarCerradoVistaSeleccionAlgoritmo();
        });

        explorarButton.addActionListener(actionEvent -> seleccionDeArchivo());

        comboBox1.addActionListener(actionEvent -> {
            String algoritmo = Objects.requireNonNull(comboBox1.getSelectedItem()).toString();
            ctrlPresentacion.algoritmoSeleccionado(algoritmo);
        });

        sliderCalidad.addChangeListener(e -> {
            if (!sliderCalidad.getValueIsAdjusting())
                ctrlPresentacion.calidadModificada( sliderCalidad.getValue());
        });
        InfoButton.setBorderPainted(false);
        InfoButton.setBorder(null);
        InfoButton.setMargin(new Insets(0, 0, 0, 0));
        InfoButton.setContentAreaFilled(false);
        InfoButton.addActionListener(e -> ctrlPresentacion.crearVistaAyudaSelectorAlgoritmo());
        InfoButton2.setBorderPainted(false);
        InfoButton2.setBorder(null);
        InfoButton2.setMargin(new Insets(0, 0, 0, 0));
        InfoButton2.setContentAreaFilled(false);
        InfoButton2.addActionListener(e -> ctrlPresentacion.crearVistaAyudaSelectorAlgoritmo());
    }

    /**
     * Operación para selección de un algotitmo de compresión y la modificación del path de salida para reflejar el algoritmo seleccionado
     */
    private void seleccionDeArchivo() {
        String pathOut = pathSalida.getText();
        File f = new File(pathOut);
        String dir = pathOut.replace(f.getName(), "");
        chooser = new JFileChooser(new File(dir));
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int result = chooser.showSaveDialog(null);
        if(result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            ctrlPresentacion.actualizarPathSalida(file.getAbsolutePath());
        }
    }

    /**
     * Operación para mostrar el selector de path de salida del resultado del proceso
     * @param pathResultado path en el que quedará el archivo
     */
    public void mostrarSelectorDePath(String pathResultado) {
        selectorSalida.setVisible(true);
        pathResultadoCambiado(pathResultado);
    }

    /**
     * Opreación para actualizar el path de salida que aparece en la vista
     * @param pathResultado el nuevo path a mostrar
     */
    public void pathResultadoCambiado(String pathResultado) {
        pathSalida.setText(pathResultado);
    }

    /**
     * Operación para mostrar el selector de calidad en forma de slider en caso de querer procesar una imagen
     */
    public void mostrarSliderDeCalidad() {
        calidad.setVisible(true);
    }

    /**
     * Operación para habilitar el selector de algoritmo de comrpesión en el caso de tratar un archivo .txt
     */
    public void mostrarSelectorAlgoritmo() {
        algoritmo.setVisible(true);
    }
}
