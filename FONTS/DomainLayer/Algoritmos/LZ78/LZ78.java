// Creado por Javier Cabrera Rodriguez
package DomainLayer.Algoritmos.LZ78;

import DomainLayer.Algoritmos.CompresorDecompresor;
import DomainLayer.Algoritmos.OutputAlgoritmo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LZ78 implements CompresorDecompresor {
    private static LZ78 instance = null;

    // private constructor restricted to this class itself
    private LZ78() {
    }

    // static method to create instance of Singleton class
    public static LZ78 getInstance()
    {
        if (instance == null)
            instance = new LZ78();

        return instance;
    }

    @Override
    public OutputAlgoritmo comprimir(byte[] datosInput) throws IOException {
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

    @Override
    public OutputAlgoritmo descomprimir(byte[] datosInput) throws IOException {
        long startTime = System.nanoTime();

        List<Byte> output = new ArrayList<>();

        byte symbol;
        List<Pair> dictionary = new ArrayList<>();
        dictionary.add(null);

        for (int i=0; i<datosInput.length; ++i) {
            int index = datosInput[++i];
            int flag = index & 0xC0;
            index = index & 0x3F;
            if(flag == 0xC0) {
                dictionary = new ArrayList<>();
                dictionary.add(null);
            }
            else if (flag == 0x40) {
                index += datosInput[++i] << 6;
            }
            else if(flag == 0x80) {
                index += datosInput[++i] << 6;
                index += datosInput[++i] << 14;
            }
            symbol = datosInput[++i];

            dictionary.add(new Pair(index, symbol));
            List<Byte> word = new ArrayList<>();
            word.add(symbol);
            while(index != 0) {
                Pair pair = dictionary.get(index);
                index = pair.index;
                word.add(pair.b);
            }

            int wordPos = word.size()-1;
            while(wordPos >= 0) {
                byte res = word.get(wordPos);
                output.add(res);
                --wordPos;
            }
        }

        byte[] outputArray = new byte[output.size()];
        for(int i=0; i<outputArray.length; ++i) {
            outputArray[i] = output.get(i);
        }

        long endTime = System.nanoTime();
        return new OutputAlgoritmo(endTime - startTime, outputArray);
    }

    static class Pair {
        int index;
        byte b;

        Pair(int index, byte b) {
            this.index = index;
            this.b = b;
        }
    }
}
