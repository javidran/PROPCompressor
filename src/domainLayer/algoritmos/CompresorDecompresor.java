package domainLayer.algoritmos;

import java.io.File;
import java.io.IOException;

public interface CompresorDecompresor {

    public OutputAlgoritmo comprimir(File input);

    public OutputAlgoritmo descomprimir(File input);
}
