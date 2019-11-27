package Controllers;

import Enumeration.Algoritmo;

import java.io.FileNotFoundException;
import java.io.IOException;

    /**
     * La clase Singleton CtrlEstadistica es el Controlador encargado de comunicar los datos estadísticos de cada uno de los Algoritmos del programa entre las capas de presentación y datos.
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

    /**
     * Se encarga de construir un mensaje con toda la información relevante de compresión y descompresión del algoritmo solicitado.
     *
     * @param algoritmo El tipo del algoritmo del que se desa obtener las estadisticas
     * @return Devuelve un String con toda la información preparada para ser presentada, de tal manera que se pueda visualizar fácilmente los datos interesantes de la estadística.
     * @throws IOException En caso de haber algún error de lectura de archivos que impida el correcto funcionamiento del método.
     */
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
            resultado.append("Porcentaje medio de tamaño respecto al original: ").append(ctrlDatos.getPorcentajeAhorradoMedio(algoritmo, true)).append(" %\n");
        } catch (FileNotFoundException e) {
            resultado.append("Archivos comprimidos: 0\n");
        }
        resultado.append("\n");
        try {
            int num = ctrlDatos.getNumeroElementos(algoritmo, false);
            resultado.append("Archivos descomprimidos: ").append(num).append("\n");
            resultado.append("Tiempo medio de descompresión: ").append(ctrlDatos.getTiempoMedio(algoritmo, false)).append(" ns\n");
            resultado.append("Porcentaje medio de tamaño respecto al original: ").append(ctrlDatos.getPorcentajeAhorradoMedio(algoritmo, false)).append(" %\n");

        } catch (FileNotFoundException e) {
            resultado.append("Archivos descomprimidos: 0\n");
        }
        resultado.append("------------------------------------------------------------------\n");

        return resultado.toString();
    }
}
