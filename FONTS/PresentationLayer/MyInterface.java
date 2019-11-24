package PresentationLayer;

import Controllers.CtrlProcesos;
import Exceptions.FormatoErroneoException;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MyInterface extends JFrame {
    private JPanel panel;
    private JButton salirButton;
    private JButton comprimirYDescomprimirButton;
    private JTextField textField1;
    private JButton comprimirButton;
    private JButton estadísticasButton;
    private JButton explorarButton;
    private JButton descomprimirButton;

    public MyInterface () {
        super ("PROPresor");
        setContentPane(panel);
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

        textField1.getDocument().addDocumentListener(new DocumentListener() {
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
            textField1.setText(chooser.getSelectedFile().getAbsolutePath());
    }

    private void actualizarBotones() {
        String[] splitP = textField1.getText().split("\\.");
        String type = splitP[splitP.length-1];

        switch (type) {
            case "lzss":
            case "lzw":
            case "lz78":
            case "imgc":
            case "comp":
                comprimirYDescomprimirButton.setEnabled(false);
                comprimirButton.setEnabled(false);
                descomprimirButton.setEnabled(true);
                break;
            case "txt":
            case "ppm":
                comprimirYDescomprimirButton.setEnabled(true);
                comprimirButton.setEnabled(true);
                descomprimirButton.setEnabled(false);
                break;
            default:
                comprimirYDescomprimirButton.setEnabled(false);
                comprimirButton.setEnabled(true);
                descomprimirButton.setEnabled(false);
                break;

        }
    }
}

