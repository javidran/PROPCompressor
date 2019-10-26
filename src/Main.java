import domainLayer.algoritmos.*;

import java.io.File;


public class Main {
    public static void main(String[] args) {
        File fileIn = new File(args[0]);
        File fileComp = null, fileOut = null;
        OutputAlgoritmo comp;
        OutputAlgoritmo desc;
        TipoCompresor tipoCompresor = TipoCompresor.LZSS;
        switch (tipoCompresor) {
            case JPEG:
                CompresorDecompresor compresorDecompresor = JPEG.getInstance();
                comp = compresorDecompresor.comprimir(fileIn);
                desc = compresorDecompresor.descomprimir(comp.outputFile);
                System.out.println("Compression time: " + comp.tiempo + "\nDecompression time: " + desc.tiempo + "\n");
                break;
            case LZSS:
                CompresorDecompresor compresordecompresor = LZSS.getInstance();
                comp = compresordecompresor.comprimir(fileIn);
                desc = compresordecompresor.descomprimir(comp.outputFile);
                System.out.println("Compression time: " + comp.tiempo + "\nDecompression time: " + desc.tiempo + "\n");
        }
    }
}

