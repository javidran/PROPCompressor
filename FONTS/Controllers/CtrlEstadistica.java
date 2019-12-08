package Controllers;

import DomainLayer.DatosEstadistica;
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
    public DatosEstadistica estadisticas(Algoritmo algoritmo) throws IOException {

        CtrlDatos ctrlDatos = CtrlDatos.getInstance();
        int comprimidos;
        long tiempoC;
        double ratioC;
        try {
            comprimidos = ctrlDatos.getNumeroElementos(algoritmo, true);
            tiempoC = ctrlDatos.getTiempoMedio(algoritmo, true);
            ratioC = ctrlDatos.getPorcentajeAhorradoMedio(algoritmo, true);
        } catch (FileNotFoundException e) {
            comprimidos = 0;
            tiempoC = 0;
            ratioC = 0.0;
        }

        int descomprimidos;
        long tiempoD;
        double ratioD;
        try {
            descomprimidos = ctrlDatos.getNumeroElementos(algoritmo, false);
            tiempoD = ctrlDatos.getTiempoMedio(algoritmo, false);
            ratioD = ctrlDatos.getPorcentajeAhorradoMedio(algoritmo, false);
        } catch (FileNotFoundException e) {
            descomprimidos = 0;
            tiempoD = 0;
            ratioD = 0.0;
        }
        return new DatosEstadistica(comprimidos,tiempoC,ratioC,descomprimidos,tiempoD,ratioD);
    }
}
