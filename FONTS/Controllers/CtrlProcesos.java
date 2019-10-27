package Controllers;

import DomainLayer.Algoritmos.Algoritmos;
import DomainLayer.Proceso.ProcesoComprimir;
import DomainLayer.Proceso.ProcesoDescomprimir;
import DomainLayer.Proceso.ProcesoFichero;

import java.io.File;

public class CtrlProcesos {
    private static CtrlProcesos instance = null;

    public static CtrlProcesos getInstance()
    {
        if (instance == null)
            instance = new CtrlProcesos();

        return instance;
    }

    public File comprimirArchivo(File fileIn, Algoritmos tipoC) throws Exception {
        ProcesoFichero comp = new ProcesoComprimir(fileIn);
        if (tipoC != Algoritmos.PREDETERMINADO) comp.setTipoC(tipoC);
        comp.ejecutarProceso();
        return comp.getFicheroOut();
    }

    public File descomprimirArchivo(File fileIn, Algoritmos tipoC) throws Exception {
        ProcesoFichero desc = new ProcesoDescomprimir(fileIn);
        if (tipoC != Algoritmos.PREDETERMINADO) desc.setTipoC(tipoC);
        desc.ejecutarProceso();
        return desc.getFicheroOut();
    }
}
