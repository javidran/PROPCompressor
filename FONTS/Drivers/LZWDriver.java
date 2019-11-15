//Creado por Sheida Vanesa Alfaro Taco
package Drivers;

import DomainLayer.Algoritmos.LZW;
import DomainLayer.Algoritmos.OutputAlgoritmo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Scanner;

public class LZWDriver {
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
                            File fileIn = new File(s); // custom output format
                            FileInputStream entrada = new FileInputStream(fileIn);
                            byte[] data = new byte[(int)fileIn.length()];
                            int readb = entrada.read(data);
                            entrada.close();
                            System.out.println("Se inicia el proceso");
                            OutputAlgoritmo oa = LZW.getInstance().comprimir(data);
                            String newpath = s.replace(".txt", ".lzw");
                            File fileOut = new File(newpath); // custom output format
                            if (!fileOut.exists()) fileOut.createNewFile();
                            FileOutputStream salida = new FileOutputStream(fileOut);
                            salida.write(oa.output);
                            System.out.println("El archivo " + s + " se ha comprimido correctamente!\n" +
                                    "Ha tardado "+oa.tiempo+" nanosegundos y se ha guardado en " + newpath);
                        } else System.out.println("El formato del fichero debe de ser .txt!");
                        break;
                    case "descomprimir":
                    case "2":
                        System.out.println("Escriba el path relativo de un fichero .lzw a descomprimir:");
                        s = System.getProperty("user.dir");
                        s += s.contains("/")?"/":"\\";
                        s += scanner.nextLine();
                        if (s.endsWith(".lzw")) {
                            File fileIn = new File(s); // custom output format
                            FileInputStream entrada = new FileInputStream(fileIn);
                            byte[] data = new byte[(int)fileIn.length()];
                            int readb = entrada.read(data);
                            entrada.close();
                            System.out.println("Se inicia el proceso");
                            OutputAlgoritmo oa = LZW.getInstance().descomprimir(data);
                            String [] pathSplit = s.split("\\.");
                            String newpath = s.replace("."+pathSplit[pathSplit.length - 1], "_out.txt");
                            File fileOut = new File(newpath);
                            if (!fileOut.exists()) fileOut.createNewFile();
                            FileOutputStream salida = new FileOutputStream(fileOut);
                            salida.write(oa.output);
                            System.out.println("El archivo se ha descomprimido correctamente!\n" +
                                    "Ha tardado "+oa.tiempo+" nanosegundos y se ha guardado en " + newpath);
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
