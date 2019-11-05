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

        while ((i = bfin.read()) != -1 && !dictionary.full()) {
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
            int lastIndex = dictionary.insert(word);

            int size = word.size();
            int lastIndex0 = lastIndex & 0xFF;
            int lastIndex1 = (lastIndex >> 8) & 0xFF;
            int lastIndex2 = (lastIndex >> 16) & 0xFF;
            int lastIndex3 = (lastIndex >> 24) & 0xFF;

            bfout.write(lastIndex0);
            bfout.write(lastIndex1);
            bfout.write(lastIndex2);
            bfout.write(lastIndex3);
            bfout.write(word.get(size -1));
        }

        bfout.close();
        long endTime = System.nanoTime();
        return new OutputAlgoritmo(endTime - startTime, fileOut);
    }

    @Override
    public OutputAlgoritmo descomprimir(File fileIn) throws Exception {
        long startTime = System.nanoTime();

        FileInputStream fin = new FileInputStream(fileIn);
        BufferedInputStream bfin = new BufferedInputStream(fin);

        File fileOut = new File(fileIn.getAbsolutePath().replace(".lz78", ".txt")); //custom output format
        FileOutputStream fout = new FileOutputStream(fileOut);
        BufferedOutputStream bfout = new BufferedOutputStream(fout);

        int i;
        byte symbol;
        DecompileTree dictionary = new DecompileTree();

        while ((i = bfin.read()) != -1) {
            int lastIndex0 = i;
            int lastIndex1 = bfin.read() << 8;
            int lastIndex2 = bfin.read() << 16;
            int lastIndex3 = bfin.read() << 24;
            int lastIndex = lastIndex0 + lastIndex1 + lastIndex2 + lastIndex3;
            symbol = (byte) bfin.read();

            List<Byte> word = dictionary.insertAfterIndex(symbol, lastIndex);
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
