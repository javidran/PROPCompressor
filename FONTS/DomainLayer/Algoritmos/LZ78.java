// Creado por Javier Cabrera Rodriguez
package DomainLayer.Algoritmos;

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
    public OutputAlgoritmo comprimir(File input) {
        return null;
    }

    @Override
    public OutputAlgoritmo descomprimir(File input) {
        return null;
    }

}
