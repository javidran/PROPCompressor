package domainLayer.algoritmos;

import java.io.File;
import java.io.IOException;

public interface CompresorDecompresor {

    public int comprimir(File input, File output);

    public int descomprimir(File input, File output) throws IOException;
}
