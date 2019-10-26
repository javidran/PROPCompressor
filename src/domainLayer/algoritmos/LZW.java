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
    public OutputAlgoritmo comprimir(File input) {
        return null;
    }

    @Override
    public OutputAlgoritmo descomprimir(File input) {
        return null;
    }
}
