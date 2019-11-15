// Creado por Joan Gamez Rodriguez
package Drivers;

import DomainLayer.Algoritmos.JPEG;
import DomainLayer.Algoritmos.OutputAlgoritmo;

import java.io.*;
import java.util.Scanner;

public class JPEGDriver {
    public static void main(String[] args) {
        System.out.print("Bienvenido al driver para el algoritmo de JPEG\n\n");
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
                        System.out.println("Escriba el path relativo de un fichero .ppm a comprimir:");
                        s = System.getProperty("user.dir");
                        s += s.contains("/")?"/":"\\";
                        s += scanner.nextLine();
                        if (s.endsWith(".ppm")) {
                            System.out.println("Indique la calidad de compresión a usar (del 1 al 7)");
                            JPEG jpeg = JPEG.getInstance();
                            jpeg.setCalidad(scanner.nextInt());
                            System.out.println("Se inicia el proceso");
                            File fileIn = new File(s);
                            FileInputStream in = new FileInputStream(fileIn);
                            BufferedInputStream entrada = new BufferedInputStream(in);
                            byte[] data = new byte[(int)fileIn.length()];
                            entrada.read(data);
                            entrada.close();
                            OutputAlgoritmo oa = jpeg.comprimir(data);
                            String newpath = s.replace(".ppm", ".imgc");
                            File fileOut = new File(newpath);
                            if (!fileOut.exists()) fileOut.createNewFile();
                            FileOutputStream out = new FileOutputStream(fileOut);
                            BufferedOutputStream salida = new BufferedOutputStream(out);
                            salida.write(oa.output);
                            double timeSeconds = (double)oa.tiempo / 1000000000;
                            System.out.println("El archivo " + s + " se ha comprimido correctamente!\n" +
                                    "Ha tardado "+timeSeconds+" segundos y se ha guardado en " + newpath);
                        } else System.out.println("El formato del fichero debe de ser .ppm!");
                        break;
                    case "descomprimir":
                    case "2":
                        System.out.println("Escriba el path relativo de un fichero .imgc a descomprimir:");
                        s = System.getProperty("user.dir");
                        s += s.contains("/")?"/":"\\";
                        s += scanner.nextLine();
                        if (s.endsWith(".imgc")) {
                            System.out.println("Se inicia el proceso");
                            File fileIn = new File(s);
                            FileInputStream in = new FileInputStream(fileIn);
                            BufferedInputStream entrada = new BufferedInputStream(in);
                            byte[] data = new byte[(int)fileIn.length()];
                            entrada.read(data);
                            entrada.close();
                            OutputAlgoritmo oa = JPEG.getInstance().descomprimir(data);
                            String [] pathSplit = s.split("\\.");
                            String newpath = s.replace("."+pathSplit[pathSplit.length - 1], "_out.ppm");
                            File fileOut = new File(newpath);
                            if (!fileOut.exists()) fileOut.createNewFile();
                            FileOutputStream out = new FileOutputStream(fileOut);
                            BufferedOutputStream salida = new BufferedOutputStream(out);
                            salida.write(oa.output);
                            double timeSeconds = (double)oa.tiempo / 1000000000;
                            System.out.println("El archivo se ha descomprimido correctamente!\n" +
                                    "Ha tardado "+timeSeconds+" segundos y se ha guardado en " + newpath);
                        } else System.out.println("El formato del fichero debe de ser .imgc!");
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

