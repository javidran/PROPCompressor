package domainLayer.algoritmos;

import java.io.File;

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
    public int comprimir(File input, File output) {
        return 0;
    }

    @Override
    public int descomprimir(File input, File output) {
        return 0;
    }

}
