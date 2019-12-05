package DataLayer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GestorCarpetaDescomprimir extends GestorCarpeta {
    private BufferedInputStream lector;

    public GestorCarpetaDescomprimir(String pathOriginal) throws FileNotFoundException {
        super(pathOriginal);
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
        GestorArchivo.guardaArchivo(data, path);
    }

    @Override
    public void guardaCarpeta(String path) {
        new File(path).mkdirs();
    }

    @Override
    public void finalizarGestor() throws IOException {
        lector.close();
    }
}
