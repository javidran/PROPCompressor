import domainLayer.algoritmos.CompresorDecompresor;
import domainLayer.algoritmos.JPEG;
import domainLayer.algoritmos.OutputAlgoritmo;

import java.io.File;


public class Main {
    public static void main(String[] args) {
        File fileIn = new File(args[0]);
        File fileComp = null, fileOut = null;
        CompresorDecompresor compresordecompresor = JPEG.getInstance();
        OutputAlgoritmo comp = compresordecompresor.comprimir(fileIn);
        OutputAlgoritmo desc = compresordecompresor.descomprimir(comp.outputFile);
        System.out.println("Compression time: "+comp.tiempo+"\nDecompression time: "+desc.tiempo+"\n");
    }
}

