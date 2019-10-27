// Creado por Joan Gamez Rodriguez
package DomainLayer.Algoritmos;

import java.io.File;

public class OutputAlgoritmo {
    public long tiempo;
    public File outputFile;

    public OutputAlgoritmo(long tiempo, File outputFile) {
        this.tiempo = tiempo;
        this.outputFile = outputFile;
    }
}
