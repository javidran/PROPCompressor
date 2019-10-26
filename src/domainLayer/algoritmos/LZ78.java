package domainLayer.algoritmos;

import java.io.File;

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
    public int comprimir(File input, File output) {
        return 0;
    }

    @Override
    public int descomprimir(File input, File output) {
        return 0;
    }


}
