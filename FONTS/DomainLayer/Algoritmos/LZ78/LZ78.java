// Creado por Javier Cabrera Rodriguez
package DomainLayer.Algoritmos.LZ78;

import DomainLayer.Algoritmos.CompresorDecompresor;
import DomainLayer.Algoritmos.LZ78.CompileTree.CompileTree;
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
    public OutputAlgoritmo comprimir(File fileIn) throws IOException {
        long startTime = System.nanoTime();

        FileInputStream fin = new FileInputStream(fileIn);
        BufferedInputStream bfin = new BufferedInputStream(fin);

        File fileOut = new File(fileIn.getAbsolutePath().replace(".txt", "_out.lz78")); //custom output format
        FileOutputStream fout = new FileOutputStream(fileOut);
        BufferedOutputStream bfout = new BufferedOutputStream(fout);

        int i;
        byte symbol;
        boolean reseted = false;
        CompileTree dictionary = new CompileTree();

        while ((i = bfin.read()) != -1) {
            if(dictionary.isFull()) {
                reseted = true;
                dictionary = new CompileTree();
            }

            symbol = (byte) i;

            List<Byte> word = new ArrayList<>();
            word.add(symbol);

            boolean finished = false;
            while (dictionary.search(word) && !finished) {
                if ((i = bfin.read()) != -1) {
                    symbol = (byte) i;
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

            //bfout.write(indexPart.size());
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
                bfout.write(ind);
            }

            bfout.write(word.get(word.size()-1));
        }

        bfout.close();
        long endTime = System.nanoTime();
        return new OutputAlgoritmo(endTime - startTime, fileOut);
    }

    @Override
    public OutputAlgoritmo descomprimir(File fileIn) throws IOException {
        long startTime = System.nanoTime();

        FileInputStream fin = new FileInputStream(fileIn);
        BufferedInputStream bfin = new BufferedInputStream(fin);

        File fileOut = new File(fileIn.getAbsolutePath().replace(".lz78", ".txt")); //custom output format
        FileOutputStream fout = new FileOutputStream(fileOut);
        BufferedOutputStream bfout = new BufferedOutputStream(fout);

        int i;
        byte symbol;
        List<Pair> dictionary = new ArrayList<>();
        dictionary.add(null);

        while ((i = bfin.read()) != -1) {
            int flag = i & 0xC0;
            int index = i & 0x3F;
            if(flag == 0xC0) {
                dictionary = new ArrayList<>();
                dictionary.add(null);
            }
            else if (flag == 0x40) {
                index += bfin.read() << 6;
            }
            else if(flag == 0x80) {
                index += bfin.read() << 6;
                index += bfin.read() << 14;
            }
            symbol = (byte) bfin.read();

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
                bfout.write(res);
                --wordPos;
            }
        }

        bfout.close();

        long endTime = System.nanoTime();
        return new OutputAlgoritmo(endTime - startTime, fileOut);
    }

    class Pair {
        int index;
        byte b;

        public Pair(int index, byte b) {
            this.index = index;
            this.b = b;
        }
    }
}
