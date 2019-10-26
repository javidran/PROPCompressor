// Creado por Javier Cabrera Rodriguez
package DomainLayer.Algoritmos;

import java.io.File;

public interface CompresorDecompresor {

    public OutputAlgoritmo comprimir(File input);

    public OutputAlgoritmo descomprimir(File input);
}
