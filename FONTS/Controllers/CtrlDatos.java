// Creado por yes:D
package Controllers;


import Controllers.CtrlProcesos


import java.io.File;

public class CtrlDatos {
    private static CtrlDatos instance = null;

    public static CtrlDatos getInstance()
    {
        if (instance == null)
            instance = new CtrlDatos();

        return instance;
    }

    /*
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

     */
}
