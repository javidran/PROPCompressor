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
        StringBuilder resultado = new StringBuilder();

        CtrlDatos ctrlDatos = CtrlDatos.getInstance();
        resultado.append("------------------------------------------------------------------\n");
        resultado.append("Estadísticas generales del algoritmo ").append(algoritmo).append(":\n");
        resultado.append("------------------------------------------------------------------\n");
        try {
            int num = ctrlDatos.getNumeroElementos(algoritmo, true);
            resultado.append("Archivos comprimidos: ").append(num).append("\n");
            resultado.append("Tiempo medio de compresión: ").append(ctrlDatos.getTiempoMedio(algoritmo, true)).append(" ns\n");
            resultado.append("Porcentaje medio de compresión: ").append(ctrlDatos.getPorcentajeAhorradoMedio(algoritmo, true)).append(" %\n");
        } catch (FileNotFoundException e) {
            resultado.append("Archivos comprimidos: 0\n");
        }
        resultado.append("\n");
        try {
            int num = ctrlDatos.getNumeroElementos(algoritmo, false);
            resultado.append("Archivos descomprimidos: ").append(num).append("\n");
            resultado.append("Tiempo medio de descompresión: ").append(ctrlDatos.getTiempoMedio(algoritmo, false)).append(" ns\n");
            resultado.append("Porcentaje medio de descompresión: ").append(ctrlDatos.getPorcentajeAhorradoMedio(algoritmo, false)).append(" %\n");

        } catch (FileNotFoundException e) {
            resultado.append("Archivos descomprimidos: 0\n");
        }
        resultado.append("------------------------------------------------------------------\n");

        return resultado.toString();
    }
}
