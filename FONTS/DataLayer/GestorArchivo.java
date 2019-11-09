package DataLayer;

import Exceptions.ArchivoYaExisteException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class GestorArchivo {

    public static byte[] leeArchivo(String path) throws IOException {
        List<Byte> srclist = new ArrayList<>();// List to store bytes

        File fileIn = new File(path); // custom output format
        FileInputStream fis = new FileInputStream(fileIn);
        BufferedInputStream entrada = new BufferedInputStream(fis);
        byte b;
        while ((b = (byte) entrada.read()) != -1) {
            srclist.add(b);
        }
        entrada.close();

        byte[] data = new byte[srclist.size()];
        int i = 0;
        for (byte d : srclist) {
            data[i] = d;
            ++i;
        }

        return data;

    }

    public static void guardaArchivo(byte[] data, String path, boolean sobrescribir) throws IOException {
        //Miramos si existe el path
        File tempFile = new File(path);
        if (tempFile.exists() && !sobrescribir) throw new ArchivoYaExisteException("No se desea sobrescribir pero el fichero a escribir ya existe");

        File fileOut = new File(path); //custom output format
        FileOutputStream fout = null;
        fout = new FileOutputStream(fileOut);
        BufferedOutputStream bfout = new BufferedOutputStream(fout);
        for (byte b : data) {
            bfout.write(b);
        }
        bfout.close();
    }

}


