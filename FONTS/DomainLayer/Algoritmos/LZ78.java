package DomainLayer.Algoritmos;

import Exceptions.FormatoErroneoException;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que implementa los métodos necesarios para comprimir y descomprimir secuencias de bytes con el algoritmo de compresión LZ78.
 */
public class LZ78 implements CompresorDecompresor {
    /**
     * Instancia de LZ78 para garantizar que es una clase Singleton
     */
    private static LZ78 instance = null;

    /**
     * Constructor de la clase LZ78.
     * <p>Su visibilidad es privada para impedir que se tengan multiples instancias de la clase y satisfacer las propiedades de Singleton.</p>
     */
    private LZ78() {}


    /**
     * Método que devuelve la instancia la instancia de la clase LZ78.
     * @return Devuelve la instancia de la clase LZ78.
     */
    public static LZ78 getInstance()
    {
        if (instance == null)
            instance = new LZ78();

        return instance;
    }

    /**
     * Comprime la secuencia de bytes aportada según el patrón de compresión LZ78.
     * @param datosInput Secuencia de bytes a comprimir.
     * @return Devuelve una instancia de OutputAlgoritmo compuesta del tiempo que ha tardado el método en ejecutarse y la secuencia de bytes comprimida.
     */
    @Override
    public OutputAlgoritmo comprimir(byte[] datosInput) {
        long startTime = System.nanoTime();

        List<Byte> output = new ArrayList<>();

        boolean reseted = false;
        Trie dictionary = new Trie();
        byte symbol;

        for (int i=0; i<datosInput.length; ++i) {
            if(dictionary.isFull()) {
                reseted = true;
                dictionary = new Trie();
            }

            symbol = datosInput[i];

            List<Byte> word = new ArrayList<>();
            word.add(symbol);

            boolean finished = false;
            while (dictionary.search(word) && !finished) {
                ++i;
                if (i < datosInput.length) {
                    symbol = datosInput[i];
                    word.add(symbol);
                } else finished = true;
            }
            int index = dictionary.insert(word);

            List<Integer> indexPart = new ArrayList<>();
            indexPart.add(index & 0xFF);
            if(index >= 0x3F) {
                indexPart.add((index >> 6) & 0xFF);
                if(index >= 0x3FFF) {
                    indexPart.add((index >> 14) & 0xFF);
                }
            }

            for(int it = 0; it < indexPart.size(); ++it ) {
                int ind = indexPart.get(it);
                if(it == 0) {
                    if(reseted) {
                        ind = 0xC0 | (ind & 0x3F);
                        reseted = false;
                    }
                    else {
                        switch (indexPart.size()) { //case 1 is 0x00 | ind
                            case 2:
                                ind = 0x40 | (ind & 0x3F);
                                break;
                            case 3:
                                ind = 0x80 | (ind & 0x3F);
                                break;
                        }
                    }
                }
                output.add((byte) ind);
            }
            output.add(word.get(word.size()-1));
        }

        byte[] outputArray = new byte[output.size()];
        for(int i=0; i<output.size(); ++i) {
            outputArray[i] = output.get(i);
        }

        long endTime = System.nanoTime();
        return new OutputAlgoritmo(endTime - startTime, outputArray);
    }

    /**
     * Descomprime la secuencia de bytes aportada según el patrón de compresión LZ78.
     * <p>La secuencia debe haber sido comprimida por el método {@link #comprimir(byte[])} para que el proceso de descompresión sea satisfactorio.</p>
     * @param datosInput Secuencia de bytes a descomprimir.
     * @return Devuelve una instancia de OutputAlgoritmo compuesta del tiempo que ha tardado el método en ejecutarse y la secuencia de bytes descomprimida.
     * @throws FormatoErroneoException El formato en el que está codificado el texto no es correcto.
     */
    @Override
    public OutputAlgoritmo descomprimir(byte[] datosInput) throws FormatoErroneoException {
        try {
            long startTime = System.nanoTime();

            List<Byte> output = new ArrayList<>();

            byte symbol;
            List<Pair> dictionary = new ArrayList<>();
            dictionary.add(null);

            for (int i = 0; i < datosInput.length; ++i) {
                int index = datosInput[i++] & 0xFF;
                int flag = index & 0xC0;
                index = index & 0x3F;
                if (flag == 0xC0) {
                    dictionary = new ArrayList<>();
                    dictionary.add(null);
                } else if (flag == 0x40) {
                    index += (datosInput[i++] & 0xFF) << 6;
                } else if (flag == 0x80) {
                    index += (datosInput[i++] & 0xFF) << 6;
                    index += (datosInput[i++] & 0xFF) << 14;
                }
                symbol = datosInput[i];

                dictionary.add(new Pair(index, symbol));
                List<Byte> word = new ArrayList<>();
                word.add(symbol);
                while (index != 0) {
                    Pair pair = dictionary.get(index);
                    index = pair.index;
                    word.add(pair.b);
                }

                int wordPos = word.size() - 1;
                while (wordPos >= 0) {
                    byte res = word.get(wordPos);
                    output.add(res);
                    --wordPos;
                }
            }

            byte[] outputArray = new byte[output.size()];
            for (int i = 0; i < outputArray.length; ++i) {
                outputArray[i] = output.get(i);
            }

            long endTime = System.nanoTime();
            return new OutputAlgoritmo(endTime - startTime, outputArray);
        } catch (Exception e) {
            throw new FormatoErroneoException("El archivo a descomprimir está corrupto o no se ha generado adecuadamente.");
        }
    }


    /**
     * Clase Pair usada en {@link #descomprimir(byte[])} para procesar todos los indices y bytes de la secuencia de bytes comprimidos.
     */
    static class Pair {
        /**
         * Indice del byte predecesor.
         */
        int index;
        /**
         * Byte que representa el componente final de una secuencia de bytes comprimida.
         */
        byte b;

        /**
         * Constructora.
         * @param index Indice del byte predecesor.
         * @param b Byte que representa el componente final de una secuencia de bytes comprimida.
         */
        Pair(int index, byte b) {
            this.index = index;
            this.b = b;
        }
    }
}
