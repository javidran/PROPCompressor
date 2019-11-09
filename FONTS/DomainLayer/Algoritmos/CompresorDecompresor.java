// Creado por Javier Cabrera Rodriguez
package DomainLayer.Algoritmos;

import java.io.File;

public interface CompresorDecompresor {

    public OutputAlgoritmo comprimir(byte[] datosInput) throws Exception;

    public OutputAlgoritmo descomprimir(byte[] datosInput) throws Exception;
}
