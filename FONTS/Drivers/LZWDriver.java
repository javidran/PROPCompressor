//Creado por Sheida Vanesa Alfaro Taco
package Drivers;

import DomainLayer.Algoritmos.LZW;
import DomainLayer.Algoritmos.OutputAlgoritmo;

import java.io.*;
import java.util.Scanner;

public class LZWDriver {

    private static long getDiffSize(boolean esCompresion, long oldSize, long newSize) {
        if(esCompresion) return oldSize - newSize;
        else return newSize - oldSize;
    }

    private static double getDiffSizePercentage(boolean esCompresion, long oldSize, long newSize) {
        if(esCompresion) return Math.floor((newSize /(double) oldSize)*100);
        else return Math.floor((oldSize /(double) newSize)*100);
    }

    public static void main(String[] args) {
        System.out.print("Bienvenido al driver para el algoritmo de LZW\n\n");
        boolean b = true;
        while (b) {
            try {
                System.out.print("Introduzca uno de los siguientes comandos disponibles:\n\n1. comprimir\n2. descomprimir\n3. salir\n");
                Scanner scanner = new Scanner(System.in);
                String comando = scanner.nextLine();
                String s;
                switch (comando) {
                    case "comprimir":
                    case "1":
                        System.out.println("Escriba el path relativo de un fichero .txt a comprimir:");
                        s = System.getProperty("user.dir");
                        s += s.contains("/")?"/":"\\";
                        s += scanner.nextLine();
                        if (s.endsWith(".txt")) {
                            System.out.println("Se inicia el proceso");
                            File fileIn = new File(s);
                            FileInputStream in = new FileInputStream(fileIn);
                            BufferedInputStream entrada = new BufferedInputStream(in);
                            byte[] data = new byte[(int)fileIn.length()];
                            entrada.read(data);
                            entrada.close();
                            OutputAlgoritmo oa = LZW.getInstance().comprimir(data);
                            String newpath = s.replace(".txt", ".lzw");
                            File fileOut = new File(newpath);
                            if (!fileOut.exists()) fileOut.createNewFile();
                            FileOutputStream out = new FileOutputStream(fileOut);
                            BufferedOutputStream salida = new BufferedOutputStream(out);
                            salida.write(oa.output);
                            long oldSize = data.length, newSize = oa.output.length;
                            double timeSeconds = (double)oa.tiempo / 1000000000;
                            System.out.println("El archivo " + s + " se ha comprimido correctamente!\n" +
                                    "Ha tardado "+timeSeconds+"s y se ha guardado en " + newpath +".\n"+
                                    "El cambio de tamaño pasa de " + oldSize + "B a " + newSize + "B con diferencia de " +
                                    getDiffSize(true, oldSize, newSize) + "B / " +
                                    getDiffSizePercentage(true, oldSize, newSize) + "%");
                        } else System.out.println("El formato del fichero debe de ser .txt!");
                        break;
                    case "descomprimir":
                    case "2":
                        System.out.println("Escriba el path relativo de un fichero .lzw a descomprimir:");
                        s = System.getProperty("user.dir");
                        s += s.contains("/")?"/":"\\";
                        s += scanner.nextLine();
                        if (s.endsWith(".lzw")) {
                            System.out.println("Se inicia el proceso");
                            File fileIn = new File(s);
                            FileInputStream in = new FileInputStream(fileIn);
                            BufferedInputStream entrada = new BufferedInputStream(in);
                            byte[] data = new byte[(int)fileIn.length()];
                            entrada.read(data);
                            entrada.close();
                            OutputAlgoritmo oa = LZW.getInstance().descomprimir(data);
                            String [] pathSplit = s.split("\\.");
                            String newpath = s.replace("."+pathSplit[pathSplit.length - 1], "_out.txt");
                            File fileOut = new File(newpath);
                            if (!fileOut.exists()) fileOut.createNewFile();
                            FileOutputStream out = new FileOutputStream(fileOut);
                            BufferedOutputStream salida = new BufferedOutputStream(out);
                            salida.write(oa.output);
                            long oldSize = data.length, newSize = oa.output.length;
                            double timeSeconds = (double)oa.tiempo / 1000000000;
                            System.out.println("El archivo " + s + " se ha comprimido correctamente!\n" +
                                    "Ha tardado "+timeSeconds+"s y se ha guardado en " + newpath +".\n"+
                                    "El cambio de tamaño pasa de " + oldSize + "B a " + newSize + "B con diferencia de " +
                                    getDiffSize(false, oldSize, newSize) + "B / " +
                                    getDiffSizePercentage(false, oldSize, newSize) + "%");
                        } else System.out.println("El formato del fichero debe de ser .lzw!");
                        break;
                    case "salir":
                    case "3":
                        b = false;
                        break;
                    default:
                        System.out.print("Comando incorrecto!\n");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
