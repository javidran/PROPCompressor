package domainLayer.algoritmos;

import java.io.*;
import java.util.*;

public class LZW implements CompresorDecompresor {
    private static LZW instance = null;

    // private constructor restricted to this class itself
    private LZW() {
    }

    // static method to create instance of Singleton class
    public static LZW getInstance()
    {
        if (instance == null)
            instance = new LZW();

        return instance;
    }

    @Override
    public OutputAlgoritmo comprimir(File input) {
        long startTime = System.nanoTime();
        //File fileOut = new File(System.getProperty("user.home")+"\\Desktop\\PROProject"+input.getName().replace(".txt", ".lzwc"));
        File fileOut = new File(System.getProperty("user.home")+"\\Desktop\\PROProject"+"output.txt");

        //Pasar File input a String
        String archivo = input.getPath();
        String cadena = "";
        try{
            InputStream is = new FileInputStream(archivo);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            cadena = new String(buffer);
            is.close();
        }
        catch(IOException e)
        {
            System.out.println("Error E: "+e);
        }

        //Ini ASCII
        Map<String, Integer> mapa = new HashMap<String, Integer>();
        for (int i = 0; i < 256; i++) {
            mapa.put(String.valueOf((char) i), i);
        }
        String w = "";
        String k;
        int num = 256;
        List<Integer> salida = new ArrayList<Integer>();
        for (char i : cadena.toCharArray()) {
            k = "" + i;
            if (mapa.get(w + k) != null) {
                w = w + k;
            } else {
                salida.add(mapa.get(w));
                mapa.put(w + k, num++);
                w = k;
            }
        }
        salida.add(mapa.get(w));

        FileOutputStream os = null;
        try {
            os = new FileOutputStream(fileOut, true);
        } catch (FileNotFoundException e) {
            System.err.println("Error File Output Stream: "+e);
        }
        BufferedOutputStream  out = new BufferedOutputStream(os);
        try {
            Iterator<Integer> Itr = salida.iterator();
            while (Itr.hasNext()) {
                out.write(Itr.next());
            }
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(mapa.size());
        System.out.println(Math.ceil(Math.log(mapa.size()) / Math.log(2)) );

        long endTime = System.nanoTime(), totalTime = endTime - startTime;
        OutputAlgoritmo OutA = new OutputAlgoritmo((int)totalTime, fileOut);;
        return OutA;
    }

    @Override
    public OutputAlgoritmo descomprimir(File input) {
        return null;
    }
}
