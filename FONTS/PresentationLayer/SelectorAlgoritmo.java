package PresentationLayer;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class SelectorAlgoritmo extends JDialog {
    private JComboBox comboBox1;
    private JButton procesarButton;
    private JPanel panel;
    private JSlider slider1;
    private JPanel calidad;
    private JPanel procesar;
    private JPanel algoritmo;
    private JButton cancelarButton;
    private JPanel selectorSalida;
    private JTextField pathSalida;
    private JButton explorarButton;
    private JDialog myself;


    SelectorAlgoritmo(Frame owner, String path, Boolean mostrarSlider) {
        super (owner, true);
        myself = this;
        setContentPane(panel);
        algoritmo.setVisible(!mostrarSlider);
        calidad.setVisible(mostrarSlider);
        selectorSalida.setVisible(path != null);
        //procesar.setVisible(true);
        procesarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //System.exit(0);
                boolean existe = (new File(pathSalida.getText())).exists();
                if (existe) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "El fichero resultante del proceso sobrescribirá uno ya existente, ¿desea sobrescribirlo?", "Sobrescribir",
                            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (respuesta == JOptionPane.NO_OPTION) {
                        //Se cierra el cuadro de dialogo y se le da al usuario la posibilidad de escoger un nuevo path
                    } else if (respuesta == JOptionPane.YES_OPTION) {
                        //PROCESAR EL FICHERO
//LLAMADA A PROCESAR EL FICHERO --> AQUI
                    } else if (respuesta == JOptionPane.CLOSED_OPTION) {
                        //Si se cierra con la cruz, se procede igual que si ha dicho que no quiere sobrescribir
                    }
                } else {
                    // File or directory does not exist
                    System.exit(0);
                }
            }
        });
        cancelarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                myself.setVisible(false);
            }
        });

        explorarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                    seleccionDeArchivo();
            }
        });
    }

    private void seleccionDeArchivo() {
        JFileChooser chooser = new JFileChooser();
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
        int result = chooser.showSaveDialog(null);
        if(result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();

            String path = file.getAbsolutePath();
            String[] splitP = path.split("\\.");
            String type = splitP[splitP.length-1];
            if (type.equalsIgnoreCase("txt")) {
                // filename is OK as-is
            } else {
                path = path.replace(type, "extensionchunga"); // ALTERNATIVELY: remove the extension (if any) and replace it with ".xml"
            }
            pathSalida.setText(path);
        }
    }
}
