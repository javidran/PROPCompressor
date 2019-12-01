package DataLayer;

import Controllers.CtrlDatos;
import DomainLayer.Algoritmos.Algoritmo;

import java.io.*;

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
        BufferedReader r = new BufferedReader(new InputStreamReader(lector));
        path = r.readLine();
        if(path == null) return null;
        Algoritmo[] algoritmosPosibles = CtrlDatos.algoritmosPosibles(path);
        algoritmo = algoritmosPosibles[0];
        r.close();
        return algoritmo;
    }

    @Override
    public byte[] leerProximoArchivo() throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(lector));
        int size = Integer.parseInt(r.readLine());

        byte[] data = new byte[size];
        lector.read(data);
        r.close();
        return data;
    }

    @Override
    public void guardaProximoArchivo(byte[] data) throws IOException {
        String pathCarpeta = carpeta.getAbsolutePath();
        String pathResultado = CtrlDatos.actualizarPathSalida(path,algoritmo,false);
        String pathCompleto = pathCarpeta + (pathCarpeta.contains("/")?"/":"\\") + pathResultado;
        GestorArchivo.guardaArchivo(data,pathCompleto, true);
    }
}
