package PresentationLayer;

import Controllers.CtrlPresentacion;
import Enumeration.Algoritmo;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;


/**
 * Clase para la implementación de la vista inicial del PROPresor
 */
public class VistaInicio extends JFrame {
    /**
     * Instancia de CtrlPresentacion que contiene gran parte del codigo que altera la vista y lleva a cabo gran parte de las acciones al pulsar botones
     */
    private CtrlPresentacion ctrlPresentacion;

    private JPanel panel;
    /**
     * Botón para cerrar la vista de inicio
     */
    private JButton salirButton;

    private JTextField pathEntrada;
    /**
     * Botón para abrir el explorador de archivos y así poder seleccionar un archivo a procesar
     */
    private JButton explorarButton;
    /**
     * Botón para iniciar la compresión del archivo seleccioando
     */
    private JButton comprimirButton;
    /**
     * Botón para iniciar la descompresión del archivo seleccioando
     */
    private JButton descomprimirButton;
    /**
     * Botón para iniciar la compresión y posterior descompresión del archivo seleccioando
     */
    private JButton comprimirYDescomprimirButton;

    private JButton estadisticasButton;
    /**
     * botón para crear nueva vista para selección de un algoritmo predeterminado para la compresión de texto
     */
    private JButton escogerPrederterminadoButton;
    private JTextField mostrarPredeterminado;
    /**
     * botón para iniciar la creación de una vista con ayuda sobre la vista actual
     */
    private JButton helpButton;
    /**
     * Elemento para la selección de archivo o carpeta a procesar
     */
    private JFileChooser chooser;

    /**
     * Creación de la vista principal del PROPpresor
     */
    public VistaInicio() {
        super ("PROPresor");

        this.ctrlPresentacion = CtrlPresentacion.getInstance();

        setContentPane(panel);

        chooser = new JFileChooser(new File(System.getProperty("user.dir")));

        explorarButton.addActionListener(actionEvent -> seleccionDeArchivo());

        salirButton.addActionListener(e -> {
            ctrlPresentacion.cerrarVistaInicio();
            dispose();
        });

        pathEntrada.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                ctrlPresentacion.pathCambiado(pathEntrada.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                ctrlPresentacion.pathCambiado(pathEntrada.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent documentEvent) {
                ctrlPresentacion.pathCambiado(pathEntrada.getText());
            }
        });

        comprimirButton.addActionListener(actionEvent -> ctrlPresentacion.crearVistaSeleccionAlgoritmo(true));

        descomprimirButton.addActionListener(actionEvent -> ctrlPresentacion.crearVistaSeleccionAlgoritmo(true));

        comprimirYDescomprimirButton.addActionListener(actionEvent -> ctrlPresentacion.crearVistaSeleccionAlgoritmo(false));

        estadisticasButton.addActionListener(e -> ctrlPresentacion.crearVistaEstadisticas());

        escogerPrederterminadoButton.addActionListener(e -> ctrlPresentacion.escogerPredeterminadoPulsado());


        helpButton.setBorderPainted(false);
        helpButton.setBorder(null);
        helpButton.setMargin(new Insets(0, 0, 0, 0));
        helpButton.setContentAreaFilled(false);
        helpButton.addActionListener(e -> ctrlPresentacion.crearVistaAyudaInicio());

    }


    /**
     * Selección del archivoque el usuario desea procesar, dejando el path de este archivo en pathEntrada
     */
    private void seleccionDeArchivo() {
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        FileFilter fileFilter = new FileFilter() {
            @Override
            public boolean accept(File file) {
                if(file.isDirectory()) return true;

                String path = file.getName();
                String[] splitP = path.split("\\.");
                String type = splitP[splitP.length-1];

                switch (type) {
                    case "txt":
                    case "ppm":
                    case "imgc":
                    case "lzss":
                    case "lz78":
                    case "lzw":
                    case "comp":
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public String getDescription() {
                return "*.txt,*.ppm,*.imgc,*.lzss,*.lz78,*.lzw,*.comp";
            }
        };

        chooser.setFileFilter(fileFilter);
        int result = chooser.showOpenDialog(null);
        if(result == JFileChooser.APPROVE_OPTION)
            pathEntrada.setText(chooser.getSelectedFile().getAbsolutePath());
    }

    /**
     * Gestión de los botones de compresió y descompresión
     * <p>
     *    Al habilitar el botón de compresión, el botón de descompresión se deshabilita y viceversa
     * </p>
     * @param habilitar indica si se desea habilitar el botón de compresión, o en caso de que sea falso habilitar el de compresión
     */
    public void botonComprimir(boolean habilitar){
        comprimirButton.setEnabled(habilitar);
        descomprimirButton.setEnabled(!habilitar);
    }

    /**
     * Establece si el botón de compresión y desocmpresión es visible o no lo es en función del boolean que se pasa por parámetro
     * @param habilitar indica si se debe habilitar o no el boton de compresión y descompresión
     */
    public void botoncomprimirYDescomprimir(boolean habilitar){
        comprimirYDescomprimirButton.setEnabled(habilitar);
    }

    /**
     * Dehabilita inicialmente todos los botones de procesado de la interfaz
     */
    public void deshabilitarBotones() {
        comprimirYDescomprimirButton.setEnabled(false);
        comprimirButton.setEnabled(false);
        descomprimirButton.setEnabled(false);
    }

    /**
     * Establece el algoritmo que se le indica por parámetro en el campo de texto de la vista
     * @param algoritmo indica el algoritmo que se debe establecer como predeterminado
     */
    public void algoritmoPredeterminado(Algoritmo algoritmo) {
        mostrarPredeterminado.setText(String.valueOf(algoritmo));
    }

}

