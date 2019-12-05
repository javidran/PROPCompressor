package DataLayer;

import Controllers.CtrlDatos;
import Enumeration.Algoritmo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GestorCarpetaDescomprimir extends GestorCarpeta {
    private BufferedInputStream lector;
    private Algoritmo algoritmoActual;

    public GestorCarpetaDescomprimir(String pathOriginal, String pathSalida) throws FileNotFoundException {
        super(pathOriginal, pathSalida);
        lector = new BufferedInputStream(new FileInputStream(carpeta));
    }

    @Override
    public String pathProximoArchivo() throws IOException {
        byte endOfLine = '\n';
        byte b;
        List<Byte> byteList = new ArrayList<>();
        while((b=(byte)lector.read())!= endOfLine) {
            if(b == -1)
                return null;
            byteList.add(b);
        }
        byte[] byteArray = new byte[byteList.size()];
        for(int i=0; i<byteArray.length; ++i) {
            byteArray[i] = byteList.get(i);
        }
        return new String(byteArray);
    }

    @Override
    public byte[] leerProximoArchivo() throws IOException {
        byte endOfLine = '\n';
        byte b;
        List<Byte> byteList = new ArrayList<>();
        while((b=(byte)lector.read())!= endOfLine) {
            if(b == -1)
                return null;
            byteList.add(b);
        }
        byte[] byteArray = new byte[byteList.size()];
        for(int i=0; i<byteArray.length; ++i) {
            byteArray[i] = byteList.get(i);
        }

        byte[] data = new byte[Integer.parseInt(new String(byteArray))];
        lector.read(data);
        return data;
    }

    @Override
    public void guardaProximoArchivo(byte[] data, String path) throws IOException {
        String pathCompleto = pathSalida + CtrlDatos.actualizarPathSalida(pathArchivoActual, algoritmoActual,false);
        GestorArchivo.guardaArchivo(data,pathCompleto);
    }

    @Override
    public void guardaCarpeta(String path) {
        new File(pathSalida + pathArchivoActual).mkdirs();
    }

    @Override
    public void finalizarGestor() throws IOException {
        lector.close();
    }
}
