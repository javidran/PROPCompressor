// Creado por Javier Cabrera Rodriguez
package DomainLayer.Algoritmos.LZ78;

import DomainLayer.Algoritmos.CompresorDecompresor;
import DomainLayer.Algoritmos.LZ78.CompileTrie.CompileTrie;
import DomainLayer.Algoritmos.LZ78.DecompileTrie.DecompileTrie;
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
        try {
            long startTime = System.nanoTime();

            FileInputStream fin = new FileInputStream(fileIn);
            BufferedInputStream bfin = new BufferedInputStream(fin);

            File fileOut = new File(fileIn.getAbsolutePath().replace(".txt", "_out.lz78")); //custom output format
            BufferedWriter bfw = new BufferedWriter(new FileWriter(fileOut));

            int i;
            byte symbol;
            CompileTrie dictionary = new CompileTrie();

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
                int lastIndex0 = lastIndex & 0x000F;
                int lastIndex1 = (lastIndex >> 4) & 0x000F;
                int lastIndex2 = (lastIndex >> 8) & 0x000F;
                int lastIndex3 = (lastIndex >> 12) & 0x000F;
                bfw.write(lastIndex0);
                bfw.write(lastIndex1);
                bfw.write(lastIndex2);
                bfw.write(lastIndex3);
                bfw.write(word.get(size - 1));
            }

            bfw.close();
            long endTime = System.nanoTime();
            return new OutputAlgoritmo(endTime - startTime, fileOut);
        }
        catch (Exception e) {
            System.out.println("ERROR");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public OutputAlgoritmo descomprimir(File fileIn) throws Exception {
        try {
            long startTime = System.nanoTime();

            FileInputStream fin = new FileInputStream(fileIn);
            BufferedInputStream bfin = new BufferedInputStream(fin);

            File fileOut = new File(fileIn.getAbsolutePath().replace(".lz78", ".txt")); //custom output format
            BufferedWriter bfw = new BufferedWriter(new FileWriter(fileOut));

            int i;
            byte symbol;
            DecompileTrie dictionary = new DecompileTrie();

            while ((i = bfin.read()) != -1) {
                int lastIndex0 = i;
                int lastIndex1 = bfin.read() << 4;
                int lastIndex2 = bfin.read() << 8;
                int lastIndex3 = bfin.read() << 12;
                int lastIndex = lastIndex0 + lastIndex1 + lastIndex2 + lastIndex3;

                symbol = (byte) bfin.read();

                List<Byte> word = dictionary.insertAfterIndex(symbol, lastIndex);
                if(word == null) throw new Exception( ((char) symbol) + " con indice " + lastIndex + " no ha podido generar correctamente la palabra");
                int wordPos = word.size()-1;
                while(wordPos >= 0) {
                    bfw.write(word.get(wordPos));
                    --wordPos;
                }
            }

            bfw.close();

            long endTime = System.nanoTime();
            return new OutputAlgoritmo(endTime - startTime, fileOut);
        }
        catch (Exception e) {
                System.out.println("ERROR");
                e.printStackTrace();
                throw e;
        }
    }

}
