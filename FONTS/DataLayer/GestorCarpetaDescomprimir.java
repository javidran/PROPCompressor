package DataLayer;

import Controllers.CtrlDatos;
import DomainLayer.Algoritmos.Algoritmo;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GestorCarpetaDescomprimir extends GestorCarpeta {
    private BufferedInputStream lector;
    private String path;
    private Algoritmo algoritmo;

    public GestorCarpetaDescomprimir(String path) throws FileNotFoundException {
        super(path);
        lector = new BufferedInputStream(new FileInputStream(carpeta));
    }

    @Override
    public Algoritmo algoritmoProximoArchivo() throws IOException {
        byte endOfLine = '\n';
        byte b;
        List<Byte> byteList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        while((b=(byte)lector.read())!= endOfLine) {
            if(b == -1)
                return null;
            byteList.add(b);
        }
        byte[] byteArray = new byte[byteList.size()];
        for(int i=0; i<byteArray.length; ++i) {
            byteArray[i] = byteList.get(i);
        }
        path = new String(byteArray);
        Algoritmo[] algoritmosPosibles = CtrlDatos.algoritmosPosibles(path);
        algoritmo = algoritmosPosibles[0];
        return algoritmo;
    }

    @Override
    public byte[] leerProximoArchivo() throws IOException {
        byte endOfLine = '\n';
        byte b;
        List<Byte> byteList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        while((b=(byte)lector.read())!= endOfLine) {
            if(b == -1)
                return null;
            byteList.add(b);
        }
        byte[] byteArray = new byte[byteList.size()];
        for(int i=0; i<byteArray.length; ++i) {
            byteArray[i] = byteList.get(i);
        }
        String sizeText = new String(byteArray);
        int size = Integer.parseInt(sizeText);

        byte[] data = new byte[size];
        lector.read(data);
        lector.readNBytes(1);
        return data;
    }

    @Override
    public void guardaProximoArchivo(byte[] data) throws IOException {
        String pathCarpeta = carpeta.getAbsolutePath().replace(".comp", "");
        String pathResultado = CtrlDatos.actualizarPathSalida(path,algoritmo,false);
        String pathCompleto = pathCarpeta + pathResultado;
        GestorArchivo.guardaArchivo(data,pathCompleto, true);
    }

    @Override
    public void finalizarGestor() throws IOException {
        lector.close();
    }
}
