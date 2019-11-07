// Creado por Joan Gamez Rodriguez
package Controllers;

import DomainLayer.Algoritmos.Algoritmos;
import DomainLayer.Algoritmos.JPEG;
import DomainLayer.Proceso.ProcesoComprimir;
import DomainLayer.Proceso.ProcesoDescomprimir;
import DomainLayer.Proceso.ProcesoFichero;

import java.io.File;

public class CtrlProcesos {
    private static CtrlProcesos instance = null;
    private static Algoritmos algoritmoPredeterminadoTexto = Algoritmos.LZSS;

    public static CtrlProcesos getInstance()
    {
        if (instance == null)
            instance = new CtrlProcesos();

        return instance;
    }

    public File comprimirArchivo(File fileIn, Algoritmos tipoAlgoritmo) throws Exception {
        ProcesoFichero comp = new ProcesoComprimir(fileIn);
        if (tipoAlgoritmo != Algoritmos.PREDETERMINADO)  comp.setTipoAlgoritmo(tipoAlgoritmo);
        comp.ejecutarProceso();
        return comp.getFicheroOut();
    }

    public File comprimirArchivo(File fileIn) throws Exception {
        ProcesoFichero comp = new ProcesoComprimir(fileIn);
        comp.ejecutarProceso();
        return comp.getFicheroOut();
    }

    public File descomprimirArchivo(File fileIn) throws Exception {
        ProcesoFichero desc = new ProcesoDescomprimir(fileIn);
        desc.ejecutarProceso();
        return desc.getFicheroOut();
    }

    public static void setAlgoritmoPredeterminadoTexto(Algoritmos algoritmoPredeterminadoTexto) {
        CtrlProcesos.algoritmoPredeterminadoTexto = algoritmoPredeterminadoTexto;
    }

    public static Algoritmos getAlgoritmoPredeterminadoTexto() {
        return algoritmoPredeterminadoTexto;
    }

    public void setCalidadJPEG(int calidadJPEG) {
        JPEG.getInstance().setCalidad(calidadJPEG);
    }
}
