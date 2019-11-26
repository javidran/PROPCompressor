package Controllers;

import DomainLayer.Algoritmos.Algoritmo;
import PresentationLayer.MyInterface;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class CtrlPresentacion {
    /**
     * Instancia de CtrlDatos para garantizar que es una clase Singleton
     */
    private static CtrlPresentacion instance = null;

    /**
     * Getter de la instancia Singleton de CtrlPresentacion
     * @return La instancia Singleton de CtrlPresentacion
     */
    public static CtrlPresentacion getInstance()
    {
        if (instance == null)
            instance = new CtrlPresentacion();
        return instance;
    }

    public String getEstadisticas(String data) throws IOException {
        CtrlEstadistica ce = CtrlEstadistica.getInstance();
        Algoritmo alg;
        switch (data) {
            case "JPEG":
                alg = Algoritmo.JPEG;
                break;
            case "LZSS":
                alg = Algoritmo.LZSS;
                break;
            case "LZW":
                alg = Algoritmo.LZW;
                break;
            case "LZ78":
                alg = Algoritmo.LZ78;
                break;
            default:
                throw new EnumConstantNotPresentException(Algoritmo.class, " El tipo de compresor " + data + " no existe.");
        }
        return ce.estadisticas(alg);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new MyInterface();
                Dimension dimension = new Dimension(650, 300);
                frame.setSize(dimension);
                dimension = new Dimension(500, 200);
                frame.setMinimumSize(dimension);
                frame.setResizable(true);
                frame.setVisible(true);
                frame.setLocationRelativeTo(null);
            }
        });
    }
}
