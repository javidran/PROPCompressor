package DataLayer;

import Controllers.CtrlDatos;
import DomainLayer.Algoritmos.Algoritmo;

import java.io.*;
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
        if(algoritmosPosibles[0].equals(Algoritmo.CARPETA)) {
            guardaCarpeta();
            return algoritmoProximoArchivo();
        }
        algoritmo = algoritmosPosibles[0];
        return algoritmo;
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
    public void guardaProximoArchivo(byte[] data) throws IOException {
        String pathCarpeta = carpeta.getAbsolutePath().replace(".comp", "");
        String pathResultado = CtrlDatos.actualizarPathSalida(path,algoritmo,false);
        String pathCompleto = pathCarpeta + pathResultado;
        GestorArchivo.guardaArchivo(data,pathCompleto, true);
    }

    private void guardaCarpeta() {
        String pathCarpeta = carpeta.getAbsolutePath().replace(".comp", "");
        String pathCompleto = pathCarpeta + path;
        new File(pathCompleto).mkdirs();
    }

    @Override
    public void finalizarGestor() throws IOException {
        lector.close();
    }
}
