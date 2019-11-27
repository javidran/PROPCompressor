package PresentationLayer;

import Controllers.CtrlProcesos;
import DomainLayer.Algoritmos.Algoritmo;

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

    private Algoritmo tipoAlgoritmo;
    private boolean esCompresion;
    private boolean esCarpeta;

    SelectorAlgoritmo(Frame owner, String path, Boolean mostrarSlider, boolean esCompresion, boolean esCarpeta) {
        super (owner, true);
        myself = this;
        setContentPane(panel);
        algoritmo.setVisible(!mostrarSlider && !esCarpeta);
        calidad.setVisible(mostrarSlider);

        this.esCompresion = esCompresion;
        this.esCarpeta = esCarpeta;
        if(esCarpeta) tipoAlgoritmo = Algoritmo.CARPETA;
        else if(mostrarSlider) tipoAlgoritmo = Algoritmo.JPEG;
        else tipoAlgoritmo = CtrlProcesos.getAlgoritmoDeTextoPredeterminado();

        if(path != null) {
            selectorSalida.setVisible(true);
            actualizarPathSalida(path);
        }

        procesarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //System.exit(0);
                boolean existe = (new File(pathSalida.getText())).exists();
                if (existe) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "El fichero resultante del proceso sobrescribirá uno ya existente, ¿desea sobrescribirlo?", "Sobrescribir",
                            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION) {
                        //PROCESAR EL FICHERO
                        System.exit(0);//Temporal
                    }
                } else {
                    // File or directory does not exist
                    System.exit(0);//temporal
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
        comboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String alg = comboBox1.getSelectedItem().toString();
                switch (alg) {
                    case "PREDETERMINADO":
                        tipoAlgoritmo = CtrlProcesos.getAlgoritmoDeTextoPredeterminado();
                        break;
                    case "LZSS":
                        tipoAlgoritmo = Algoritmo.LZSS;
                        break;
                    case "LZW":
                        tipoAlgoritmo = Algoritmo.LZW;
                        break;
                    case "LZ78":
                        tipoAlgoritmo = Algoritmo.LZ78;
                        break;
                }
                actualizarPathSalida(pathSalida.getText());
            }
        });
        cancelarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myself.setVisible(false);
            }
        });
    }

    private void seleccionDeArchivo() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int result = chooser.showSaveDialog(null);
        if(result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            actualizarPathSalida(file.getAbsolutePath());
        }
    }

    private String extension() {
        String extension = null;
        if(tipoAlgoritmo == Algoritmo.PREDETERMINADO) tipoAlgoritmo = CtrlProcesos.getAlgoritmoDeTextoPredeterminado();
        if(esCompresion) {
            switch (tipoAlgoritmo) {
                case LZW:
                    extension = "lzw";
                    break;
                case LZSS:
                    extension = "lzss";
                    break;
                case LZ78:
                    extension = "lz78";
                    break;
                case JPEG:
                    extension = "imgc";
                    break;
                case CARPETA:
                    extension = "comp";
            }
        } else {
            switch (tipoAlgoritmo) {
                case LZW:
                case LZSS:
                case LZ78:
                    extension = "txt";
                    break;
                case JPEG:
                    extension = "ppm";
                    break;
                case CARPETA:
                    extension = "";
            }
        }
        return extension;
    }

    private void actualizarPathSalida(String path) {
        String[] splitP = path.split("\\.");
        String type = splitP[splitP.length-1];
        String ext = extension();
        if(!path.contains(".")) path = path + "." + ext;
        else if(splitP.length==1) path = path + ext;
        else if (!type.equalsIgnoreCase(ext)) {
            if(esCarpeta && !esCompresion) path = path.replace("." + type, ext);
            else path = path.replace(type, ext);
        }
        pathSalida.setText(path);
    }
}
