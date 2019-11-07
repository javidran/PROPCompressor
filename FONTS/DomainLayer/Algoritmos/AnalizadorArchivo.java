package DomainLayer.Algoritmos;

import Exceptions.FormatoErroneoException;

import java.io.File;

public class AnalizadorArchivo {
    public static Algoritmos[] algoritmosPosibles(String path) throws FormatoErroneoException {
        String[] splittedPath = path.split("\\.");
        String type = splittedPath[splittedPath.length-1];

        switch (type) {
            case "txt":
                return new Algoritmos[] {Algoritmos.LZSS, Algoritmos.LZW, Algoritmos.LZ78};
            case "ppm":
            case "imgc":
                return new Algoritmos[] {Algoritmos.JPEG};
            case "lzss":
                return new Algoritmos[] {Algoritmos.LZSS};
            case "lz78":
                return new Algoritmos[] {Algoritmos.LZ78};
            case "lzw":
                return new Algoritmos[] {Algoritmos.LZW};
            default:
                throw new FormatoErroneoException("No hay ningun tipo de algoritmo compatible");
        }
    }

    public static boolean esComprimible(String path) throws FormatoErroneoException {
        String[] splitP = path.split("\\.");
        String type = splitP[splitP.length-1];

        switch (type) {
            case "txt":
            case "ppm":
                return true;
            case "imgc":
            case "lzss":
            case "lz78":
            case "lzw":
                return false;
            default:
                throw new FormatoErroneoException("No hay ningun tipo de algoritmo compatible");
        }
    }
}
