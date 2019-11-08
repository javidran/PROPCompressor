// Creado por Javier Cabrera Rodriguez
package DomainLayer.Algoritmos.LZ78;

import DomainLayer.Algoritmos.CompresorDecompresor;
import DomainLayer.Algoritmos.LZ78.CompileTree.CompileTree;
import DomainLayer.Algoritmos.LZ78.DecompileTree.DecompileTree;
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
        CompileTree dictionary = new CompileTree();

        while ((i = bfin.read()) != -1 && !dictionary.isFull()) {
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
            if(index >= 256) {
                indexPart.add((index >> 8) & 0xFF);
                if(index > 65535) {
                    indexPart.add((index >> 16) & 0xFF);
                    if(index > 16777215) {
                        indexPart.add((index >> 24) & 0xFF);
                    }
                }
            }

            bfout.write(indexPart.size());
            for(int ind : indexPart) {
                bfout.write(ind);
            }

            bfout.write(word.get(word.size()-1));
        }
        if(dictionary.isFull()) {
            bfout.write(5);
            while((i = bfin.read()) != -1) {
                bfout.write(i);
            }
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

        int flag;
        byte symbol;
        DecompileTree dictionary = new DecompileTree();

        while ((flag = bfin.read()) != -1) {
            if(flag == 5) {
                int i;
                while((i = bfin.read()) != -1) {
                    bfout.write(i);
                }
                break;
            }
            int index = bfin.read();
            if(flag > 1) {
                index += bfin.read() << 8;
                if(flag > 2) {
                    index += bfin.read() << 16;
                    if(flag > 3) {
                        index += bfin.read() << 24;
                    }
                }
            }
            symbol = (byte) bfin.read();

            List<Byte> word = dictionary.insertAfterIndex(symbol, index);
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
}
