package Controllers;

import DomainLayer.Algoritmos.Algoritmo;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
     * La clase Singleton CtrlEstadistica es el Controlador del Programa encargado de mostrar los datos estadísticos de cada uno de los Algoritmos del programa.
     */
public class CtrlEstadistica {
    /**
     * Instancia de CtrlEstadistica para garantizar que es una clase Singleton
     */
    private static CtrlEstadistica instance = null;

    /**
     * Getter de la instancia Singleton de CtrlEstadistica
     * @return La instancia Singleton de CtrlEstadistica
     */
    public static CtrlEstadistica getInstance()
    {
        if (instance == null)
            instance = new CtrlEstadistica();

        return instance;
    }

    public String estadisticas(Algoritmo algoritmo) throws IOException {
        String resultado = "";

        CtrlDatos ctrlDatos = CtrlDatos.getInstance();
        resultado += "------------------------------------------------------------------\n";
        resultado += "Estadísticas generales del algoritmo " + algoritmo + ":\n";
        resultado += "------------------------------------------------------------------\n";
        try {
            int num = ctrlDatos.getNumeroElementos(algoritmo, true);
            resultado += "Archivos comprimidos: " + num + "\n";
            resultado += "Tiempo medio de compresión: " + ctrlDatos.getTiempoMedio(algoritmo, true) + " ns\n";
            resultado += "Porcentaje medio de compresión: " + ctrlDatos.getPorcentajeAhorradoMedio(algoritmo, true) + " %\n";
        } catch (FileNotFoundException e) {
            resultado += "Archivos comprimidos: 0\n";
        }
        resultado += "\n";
        try {
            int num = ctrlDatos.getNumeroElementos(algoritmo, false);
            resultado += "Archivos descomprimidos: " + num + "\n";
            resultado += "Tiempo medio de descompresión: " + ctrlDatos.getTiempoMedio(algoritmo, false) + " ns\n";
            resultado += "Porcentaje medio de descompresión: " + ctrlDatos.getPorcentajeAhorradoMedio(algoritmo, false) + " %\n";

        } catch (FileNotFoundException e) {
            resultado += "Archivos descomprimidos: 0\n";
        }
        resultado += "------------------------------------------------------------------\n";

        return resultado;
    }
}
