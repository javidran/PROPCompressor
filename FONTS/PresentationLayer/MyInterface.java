package PresentationLayer;

import Controllers.CtrlProcesos;
import DomainLayer.Algoritmos.Algoritmo;
import Exceptions.FormatoErroneoException;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputMethodListener;
import java.io.File;


public class MyInterface extends JFrame {
    private JFrame myself;
    private JPanel panel;
    private JButton salirButton;

    private JTextField pathEntrada;
    private JButton explorarButton;

    private JButton comprimirButton;
    private JButton descomprimirButton;
    private JButton comprimirYDescomprimirButton;

    private JButton estadisticasButton;
    private JButton escogerPrederterminadoButton;
    private JTextField MostrarPredeterminado;

    private boolean invocarSelectorCalidad = false;
    private boolean esCarpeta = false;

    public MyInterface () {
        super ("PROPresor");
        myself = this;
        setContentPane(panel);

        Algoritmo PredetActual = CtrlProcesos.getAlgoritmoDeTextoPredeterminado();
        switch (PredetActual) {
            case LZSS:
                MostrarPredeterminado.setText("LZSS");
                break;
            case LZW:
                MostrarPredeterminado.setText("LZW");
                break;
            case LZ78:
                MostrarPredeterminado.setText("LZ78");
                break;
        }

        explorarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                seleccionDeArchivo();
            }
        });

        salirButton.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        pathEntrada.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                actualizarBotones();
            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                actualizarBotones();
            }

            @Override
            public void changedUpdate(DocumentEvent documentEvent) {
                actualizarBotones();
            }
        });
        comprimirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JDialog dialog = new SelectorAlgoritmo(myself, pathEntrada.getText(), invocarSelectorCalidad, true, esCarpeta);
                Dimension dimension = new Dimension(650, 300);
                dialog.setSize(dimension);
                dimension = new Dimension(500, 200);
                dialog.setMinimumSize(dimension);
                dialog.setLocationRelativeTo(myself);
                dialog.setResizable(true);
                dialog.setVisible(true);
            }
        });

        descomprimirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JDialog dialog = new SelectorAlgoritmo(myself, pathEntrada.getText(),invocarSelectorCalidad, false, esCarpeta);
                Dimension dimension = new Dimension(650, 300);
                dialog.setSize(dimension);
                dimension = new Dimension(500, 200);
                dialog.setMinimumSize(dimension);
                dialog.setLocationRelativeTo(myself);
                dialog.setResizable(true);
                dialog.setVisible(true);
            }
        });

        comprimirYDescomprimirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JDialog dialog = new SelectorAlgoritmo(myself,null,invocarSelectorCalidad, true, esCarpeta);
                Dimension dimension = new Dimension(650, 300);
                dialog.setSize(dimension);
                dimension = new Dimension(500, 200);
                dialog.setMinimumSize(dimension);
                dialog.setLocationRelativeTo(myself);
                dialog.setResizable(true);
                dialog.setVisible(true);
            }
        });
        estadisticasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog dialog;
                dialog = new Estadisticas(myself);
                Dimension dimension = new Dimension(600, 300);
                dialog.setSize(dimension);
                dimension = new Dimension(600, 300);
                dialog.setMinimumSize(dimension);
                dialog.setLocationRelativeTo(myself);
                dialog.setResizable(true);
                dialog.setVisible(true);
            }
        });

        escogerPrederterminadoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog dialog;
                dialog = new SelectorAlgortimoPredeterminado(myself);
                Dimension dimension = new Dimension(600, 200);
                dialog.setSize(dimension);
                dimension = new Dimension(600, 200);
                dialog.setMinimumSize(dimension);
                dialog.setLocationRelativeTo(myself);
                dialog.setResizable(true);
                dialog.setVisible(true);
                Algoritmo PredetActual = CtrlProcesos.getAlgoritmoDeTextoPredeterminado();
                switch (PredetActual) {
                    case LZSS:
                        MostrarPredeterminado.setText("LZSS");
                        break;
                    case LZW:
                        MostrarPredeterminado.setText("LZW");
                        break;
                    case LZ78:
                        MostrarPredeterminado.setText("LZ78");
                        break;
                }
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
        int result = chooser.showOpenDialog(null);
        if(result == JFileChooser.APPROVE_OPTION)
            pathEntrada.setText(chooser.getSelectedFile().getAbsolutePath());
    }

    private void actualizarBotones() {
        String[] splitP = pathEntrada.getText().split("\\.");
        String type = splitP[splitP.length-1];

        //TODO Detectar correctamente carpetas aunque estas tengan puntos

        if(splitP.length == 1 && !pathEntrada.getText().endsWith(".")) {
            comprimirYDescomprimirButton.setEnabled(false);
            comprimirButton.setEnabled(true);
            descomprimirButton.setEnabled(false);
            invocarSelectorCalidad = false;
            esCarpeta = true;
        }
        else switch (type) {
            case "lzss":
            case "lzw":
            case "lz78":
            case "imgc":
            case "comp":
                comprimirYDescomprimirButton.setEnabled(false);
                comprimirButton.setEnabled(false);
                descomprimirButton.setEnabled(true);
                invocarSelectorCalidad = false;
                break;
            case "txt":
                comprimirYDescomprimirButton.setEnabled(true);
                comprimirButton.setEnabled(true);
                descomprimirButton.setEnabled(false);
                invocarSelectorCalidad = false;
                break;
            case "ppm":
                comprimirYDescomprimirButton.setEnabled(true);
                comprimirButton.setEnabled(true);
                descomprimirButton.setEnabled(false);
                invocarSelectorCalidad = true;

                break;
            default:
                comprimirYDescomprimirButton.setEnabled(false);
                comprimirButton.setEnabled(false);
                descomprimirButton.setEnabled(false);
                break;
        }
    }

}

