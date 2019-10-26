package domainLayer.algoritmos;

import java.io.File;

public class OutputAlgoritmo {
    public int tiempo;
    public File outputFile;

    public OutputAlgoritmo(int tiempo, File outputFile) {
        this.tiempo = tiempo;
        this.outputFile = outputFile;
    }
}
