package DomainLayer.Algoritmos;

import Exceptions.FormatoErroneoException;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LZW implements CompresorDecompresor {
    private static LZW instance = null;

    // private constructor restricted to this class itself
    private LZW() { }

    // static method to create instance of Singleton class
    public static LZW getInstance()
    {
        if (instance == null)
            instance = new LZW();

        return instance;
    }

    /**
     * Comprime el array de bytes que se pasa por parametro aplicando el algoritmo de compresión LZW.
     * <p>
     *     El array de bytes debería idealmente ser de una longitud significativa.
     * </p>
     * @param entrada El array de bytes que se ha obtenido del contenido del fichero.
     * @return Una instancia de la clase OutputAlgoritmo que contiene el tiempo en el que se ha realizado la compresión
     * y el byte [] de la compresión del byte [] de entrada.
     */
    @Override
    public OutputAlgoritmo comprimir(byte[] entrada) {
        long startTime = System.nanoTime();
        List<Byte> salida = new ArrayList<>();
        double MAX_MAP_SIZE = Math.pow(2, 31);

        Map<ByteBuffer , Integer> mapa = new HashMap<ByteBuffer, Integer>();
        for (int i = 0; i < 256; i++) {
            byte[] y = new byte[1];
            y[0] =  (byte) (i & 0xFF);
            mapa.put(ByteBuffer.wrap(y), i);
        }

        int num = 256;
        List<Byte> w = new ArrayList<Byte>();
        if (entrada.length > 0) w.add(entrada[0]);
        else {
            long endTime = System.nanoTime(), totalTime = endTime - startTime;
            return new OutputAlgoritmo((int)totalTime, entrada);
        }
        byte[] wBBArray = new byte[w.size()];
        for(int it = 0; it < wBBArray.length; ++it) {
            wBBArray[it] = w.get(it);
        }

        ByteBuffer wBB = ByteBuffer.wrap(wBBArray);
        ByteBuffer wkBB;

        for ( int x = 1; x < entrada.length; x++) {
            byte k = entrada[x];
            w.add(k);

            byte[] wkBBArray = new byte[w.size()];
            for(int it = 0; it< wkBBArray.length; ++it) wkBBArray[it] = w.get(it);
            wkBB = ByteBuffer.wrap(wkBBArray);

            if (mapa.containsKey(wkBB) && mapa.get(wkBB) != null) {
                wBB = wkBB;
            }
            else {
                int n = mapa.get(wBB);
                byte[] array = {(byte)(n >> 24), (byte)(n >> 16), (byte)(n >> 8), (byte)n };
                for (byte b : array) salida.add( b);
                if ( num < MAX_MAP_SIZE) mapa.put(wkBB, num++);
                w.clear();
                w.add(k);

                byte[] wBBifArray = new byte[w.size()];
                for(int it = 0; it < wBBifArray.length; ++it) wBBifArray[it] = w.get(it);
                wBB = ByteBuffer.wrap(wBBifArray);
            }
        }
        byte[] wBBfiArray = new byte[w.size()];
        for(int it = 0; it < wBBfiArray.length; ++it) wBBfiArray[it] = w.get(it);
        wBB = ByteBuffer.wrap(wBBfiArray);

        int n = mapa.get(wBB);
        byte[] array = {(byte)(n >> 24), (byte)(n >> 16), (byte)(n >> 8), (byte)n };
        for (byte b : array) salida.add((byte) b);

        byte [] result = new byte[salida.size()];
        int it = 0;
        for (Byte aByte : salida) result[it++] = aByte;

        long endTime = System.nanoTime(), totalTime = endTime - startTime;
        return new OutputAlgoritmo((int)totalTime, result);
    }

    /**
     * Desomprime el array de bytes que se pasa por parametro aplicando el algoritmo de descompresión LZW.
     * <p>
     *     El array de bytes debe estar comprimido con el algoritmo de compresión LZW.
     * </p>
     * @param entrada El array de bytes que se ha obtenido del contenido del fichero .lzw o de la compresión directamente.
     * @return Una instancia de la clase OutputAlgoritmo que contiene el tiempo en el que se ha realizado la descompresión
     * y el byte [] de la compresión del byte [] de entrada.
     * @throws FormatoErroneoException El formato en el que está codificado el texto no es correcto.
     */
    @Override
    public OutputAlgoritmo descomprimir(byte[] entrada) throws FormatoErroneoException {
        try {
            long startTime = System.nanoTime();

            Map<Integer, ByteBuffer> mapa = new HashMap<Integer, ByteBuffer>();
            for (int i = 0; i < 256; i++) {
                byte[] y = new byte[1];
                y[0] = (byte) (i & 0xFF); // Del 0 to 127 and -128 to -1
                mapa.put(i, ByteBuffer.wrap(y));
            }

            Integer oldC = null, newC;
            byte caracter = 0;
            List<Byte> letras = new ArrayList<Byte>();
            int num = 256;
            List<Byte> salida = new ArrayList<Byte>();

            byte[] c = new byte[4];
            Integer ni;
            int i = 0;
            boolean first = true;
            for (int x = 0; i < entrada.length; x++) {
                c[x] = entrada[i++];
                if (x == 3) {
                    ni = ((c[0] & 0xFF) << 24) | ((c[1] & 0xFF) << 16) | ((c[2] & 0xFF) << 8) | ((c[3] & 0xFF));
                    if (first) {
                        oldC = ni;
                        caracter = (byte) (mapa.get(oldC).array()[0] & 0xFF);
                        salida.add(caracter);
                        first = false;
                    } else {
                        newC = ni;
                        if (mapa.get(newC) == null) {
                            letras.clear();
                            for (byte byt : mapa.get(oldC).array()) letras.add(byt); //cadena=Traducir(cód_viejo)
                            letras.add(caracter); //concat
                        } else {
                            letras.clear();
                            for (byte byt : mapa.get(newC).array()) letras.add(byt);
                        }
                        salida.addAll(letras);
                        if (letras.size() >= 1) {
                            caracter = (byte) (letras.get(0) & 0xFF); //carácter=Primer carácter de cadena
                        }

                        byte[] wArray = new byte[mapa.get(oldC).array().length + 1];
                        int it = 0;
                        for (int l = 0; l < mapa.get(oldC).array().length; ++l) {
                            wArray[it] = mapa.get(oldC).array()[l];
                            it++;
                        }
                        wArray[it] = caracter;
                        mapa.put(num++, (ByteBuffer.wrap(wArray)));
                        oldC = newC;
                    }
                    x = -1;
                }
            }

            byte[] salid = new byte[salida.size()];
            int j = 0;
            for (byte x : salida) salid[j++] = x;

            long endTime = System.nanoTime(), totalTime = endTime - startTime;
            return new OutputAlgoritmo((int) totalTime, salid);
        } catch (Exception e) {
            throw new FormatoErroneoException("El archivo a descomprimir está corrupto o no se ha generado adecuadamente.");
        }
    }
}
