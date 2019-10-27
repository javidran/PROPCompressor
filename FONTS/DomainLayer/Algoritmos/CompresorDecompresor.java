// Creado por Javier Cabrera Rodriguez
package DomainLayer.Algoritmos;

import java.io.File;

public interface CompresorDecompresor {

    public OutputAlgoritmo comprimir(File input) throws Exception;

    public OutputAlgoritmo descomprimir(File input) throws Exception;
}
