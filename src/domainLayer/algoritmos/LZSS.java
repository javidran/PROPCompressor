package domainLayer.algoritmos;

import java.io.File;

public class LZSS implements CompresorDecompresor {
    private static LZSS instance = null;

    // private constructor restricted to this class itself
    private LZSS() {
    }

    // static method to create instance of Singleton class
    public static LZSS getInstance()
    {
        if (instance == null)
            instance = new LZSS();

        return instance;
    }

    @Override
    public int comprimir(File input, File output) {
        return 0;
    }

    @Override
    public int descomprimir(File input, File output) {
        return 0;
    }


}
