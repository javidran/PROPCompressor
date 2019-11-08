// Creado por Sheida Vanesa Alfaro Taco
package DomainLayer.Algoritmos;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
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
        File fileOut = new File(input.getAbsolutePath().replace(".txt", ".lzw")); // custom output format

        Map<ByteBuffer , Integer> mapa = new HashMap<ByteBuffer, Integer>();
        for (int i = 0; i < 256; i++) {
            byte[] y = new byte[1];
            y[0] =  (byte) (i & 0xFF); // Del 0 to 127 and -128 to -1
            mapa.put(ByteBuffer.wrap(y), i); // Ini map con ASCII
            // En el map estan del 0 to 127 and -128 to -1 , Integer del 0 al 255
            //TEST JAN
            //byte[] bytesArray = ByteBuffer.wrap(y).array();
            //for(byte hey : bytesArray ) System.out.println("the byte is --> "+(char)hey + " " + hey);
        }

        FileInputStream fis;
        FileOutputStream fos;
        try {
            fis = new FileInputStream(input);
            BufferedInputStream entrada = new BufferedInputStream(fis);
            fos = new FileOutputStream(fileOut);
            BufferedOutputStream salida = new BufferedOutputStream(fos);
            int PrimerByte = entrada.read();
            byte i = (byte) PrimerByte;
            int num = 256;
            List<Byte> w = new ArrayList<Byte>();
            w.add(i);
            ByteBuffer wBB = null;
            ByteBuffer wkBB;

            int k;
            while ((k = entrada.read()) != -1) {

                byte[] wBBArray = new byte[w.size()];
                for(int it = 0; it < wBBArray.length; ++it) {
                    wBBArray[it] = w.get(it);
                }
                wBB = ByteBuffer.wrap(wBBArray); //Para el else

                w.add((byte)k);
                byte[] wkBBArray = new byte[w.size()];
                for(int it = 0; it< wkBBArray.length; ++it) {
                    wkBBArray[it] = w.get(it);
                }
                wkBB = ByteBuffer.wrap(wkBBArray); //Para el if

                if (mapa.containsKey(wkBB) && mapa.get(wkBB) != null) {
                    w.clear();
                    for(byte byt : wkBB.array()) {
                        w.add(byt);
                    } //w = w+k
                }
                else {
                    System.out.println();
                    int n = mapa.get(wBB); //PORQUE NO TIENE EL W
                    //System.out.println("En el else\n" +"Salida se añade el int " + n);
                    byte[] array = {(byte)(n >> 24), (byte)(n >> 16), (byte)(n >> 8), (byte)n };
                    for (byte b : array) {
                        salida.write((byte) b);
                    }
                    mapa.put(wkBB, num++);
                    w.clear();
                    w.add((byte)k);
                }
            }
            byte[] wBBArray = new byte[w.size()];
            for(int it = 0; it < wBBArray.length; ++it) {
                wBBArray[it] = w.get(it);
            }
            wBB = ByteBuffer.wrap(wBBArray);
            int n = mapa.get(wBB); //PORQUE NO TIENE EL W
            //System.out.println("En el else\n" +"Salida se añade el int " + n);
            byte[] array = {(byte)(n >> 24), (byte)(n >> 16), (byte)(n >> 8), (byte)n };
            for (byte b : array) {
                salida.write((byte) b);
            }
            entrada.close();
            salida.close();
        } catch ( IOException e) {
            e.printStackTrace();
        }

        long endTime = System.nanoTime(), totalTime = endTime - startTime;
        OutputAlgoritmo OutA = new OutputAlgoritmo((int)totalTime, fileOut);;
        return OutA;
    }

    @Override
    public OutputAlgoritmo descomprimir(File input) {
        long startTime = System.nanoTime();
        File fileOut = new File(input.getAbsolutePath().replace(".lzw", "_out.txt")); // custom output format

        Map<Integer, ByteBuffer> mapa = new HashMap<Integer, ByteBuffer>();
        for (int i = 0; i < 256; i++) { // Inicializar alfabeto ASCII
            byte[] y = new byte[1];
            y[0] =  (byte) (i & 0xFF); // Del 0 to 127 and -128 to -1
            mapa.put(i,ByteBuffer.wrap(y));
        }

        FileInputStream fis;
        List<Integer> entrada = new ArrayList<Integer>();
        try {
            fis = new FileInputStream(input);
            BufferedInputStream bis = new BufferedInputStream(fis);
            byte[] c = new byte[4];
            int i;
            ByteBuffer wrapped;
            Integer ni;
            for (int x=0; (i = bis.read()) != -1; x++) {
                c[x] = (byte)i;
                if (x == 3) {
                    ni = ((c[0] & 0xFF) << 24) | ((c[1] & 0xFF) << 16) | ((c[2] & 0xFF) << 8 ) | ((c[3] & 0xFF));
                    entrada.add(ni);
                    x=-1;
                }
            }
            //Creo que no se añade un ultimo ni
            bis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Iterator<Integer> nombreIterator = entrada.iterator();
        Integer oldC, newC;
        byte caracter; //bb
        List<Byte> letras = new ArrayList<Byte>(); //bb
        int num = 256;
        oldC = nombreIterator.next(); //Leer input
        caracter = (byte)(mapa.get(oldC).array()[0] & 0xFF);
        List<Byte> salida = new ArrayList<Byte>();
        salida.add(caracter);

        while(nombreIterator.hasNext()){
            newC = nombreIterator.next(); //System.out.println("CodNuevo: " + oldC + " y CodViejo:" +newC);
            if (mapa.get(newC) == null) { //NO esta en el dic
                letras.clear();
                for(byte byt : mapa.get(oldC).array()) letras.add(byt); //cadena=Traducir(cód_viejo)
                letras.add(caracter); //concat
            }
            else {
                letras.clear();
                for(byte byt : mapa.get(newC).array()) letras.add(byt);
            }
            salida.addAll(letras);
            if(letras.size()>= 1) {
                caracter = (byte)(letras.get(0) &0xFF); //carácter=Primer carácter de cadena
            }

            byte[] wArray = new byte[mapa.get(oldC).array().length + 1];
            int it=0;
            for(int l = 0; l< mapa.get(oldC).array().length; ++l) {
                wArray[it] = mapa.get(oldC).array()[l];
                it++;
            }
            wArray[it] = caracter;
            mapa.put(num++, ( ByteBuffer.wrap(wArray)));
            oldC = newC;
        }
        try {
            byte[] salid = new byte[salida.size()];
            int j = 0;
            for (byte x : salida) {
                salid[j] = x;
                j++;
            }
            String sal = new String(salid);
            char [] outs =  sal.toCharArray();

            BufferedWriter writer = new BufferedWriter(new FileWriter(fileOut));
            for (char x : outs) writer.write(x);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        long endTime = System.nanoTime(), totalTime = endTime - startTime;
        OutputAlgoritmo OutA = new OutputAlgoritmo((int)totalTime, fileOut);
        return OutA;

    }
}
