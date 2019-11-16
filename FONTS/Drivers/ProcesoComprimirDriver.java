package Drivers;

import DomainLayer.Algoritmos.Algoritmo;
import DomainLayer.Algoritmos.JPEG;
import DomainLayer.Proceso.DatosProceso;
import DomainLayer.Proceso.ProcesoComprimir;

import java.io.*;
import java.util.Scanner;

public class ProcesoComprimirDriver {
    public static void main(String[] args) {
        System.out.print("Bienvenido al driver de ProcesoComprimir.\n\n");
        boolean b = true;
        while (b) {
            try {
                Algoritmo tipoCompresor;
                System.out.print("Introduzca uno de los siguientes comandos disponibles:\n\n1. comprimir\n2. salir\n");
                Scanner scanner = new Scanner(System.in);
                String comando = scanner.nextLine();
                String s;
                switch (comando) {
                    case "comprimir":
                    case "1":
                        if (args.length == 0) {
                            System.out.println("Escriba el path relativo del fichero a comprimir (.txt o .ppm!)");
                            s = System.getProperty("user.dir");
                            s += s.contains("/")?"/":"\\";
                            s += scanner.nextLine();
                        } else s = args[0];
                        if (s.endsWith(".txt")) {
                            System.out.print("Escriba el algoritmo de compresión que quiera usar, de entre los siguientes:\npredeterminado\nlzss\nlz78\nlzw\n");
                            String algoritmoComp = scanner.nextLine();
                            String extension = "";
                            switch (algoritmoComp) {
                                case "lzss":
                                    tipoCompresor = Algoritmo.LZSS;
                                    extension = ".lzss";
                                    break;
                                case "lzw":
                                    tipoCompresor = Algoritmo.LZW;
                                    extension = ".lzw";
                                    break;
                                case "lz78":
                                    tipoCompresor = Algoritmo.LZ78;
                                    extension = ".lz78";
                                    break;
                                default:
                                    throw new EnumConstantNotPresentException(Algoritmo.class, "El tipo de compresor " + algoritmoComp + " no existe o no está disponible para un archivo .txt\n");
                            }
                            System.out.println("Se inicia el proceso");
                            File fileIn = new File(s);
                            FileInputStream in = new FileInputStream(fileIn);
                            BufferedInputStream entrada = new BufferedInputStream(in);
                            byte[] data = new byte[(int)fileIn.length()];
                            entrada.read(data);
                            entrada.close();
                            ProcesoComprimir procesoComprimir = new ProcesoComprimir(data, tipoCompresor);
                            procesoComprimir.ejecutarProceso();
                            String newpath = s.replace(".txt", extension);
                            File fileOut = new File(newpath);
                            if (!fileOut.exists()) fileOut.createNewFile();
                            FileOutputStream out = new FileOutputStream(fileOut);
                            BufferedOutputStream salida = new BufferedOutputStream(out);
                            salida.write(procesoComprimir.getOutput());
                            DatosProceso datosProceso = procesoComprimir.getDatosProceso();
                            long oldSize = data.length, newSize = datosProceso.getNewSize();
                            double timeSeconds = (double)datosProceso.getTiempo() / 1000000000;
                            System.out.println("El archivo " + s + " se ha comprimido correctamente!\n" +
                                    "Ha tardado "+timeSeconds+"s y se ha guardado en " + newpath +".\n"+
                                    "El cambio de tamaño pasa de " + oldSize + "B a " + newSize + "B con diferencia de " +
                                    datosProceso.getDiffSize() + "B que resulta en un " +
                                    datosProceso.getDiffSizePercentage() + "% del archivo original.");
                        } else if (s.endsWith(".ppm")) {
                            System.out.println("Indique la calidad de compresión a usar (del 1 al 7)");
                            JPEG.getInstance().setCalidad(scanner.nextInt());
                            System.out.println("Se inicia el proceso");
                            File fileIn = new File(s);
                            FileInputStream in = new FileInputStream(fileIn);
                            BufferedInputStream entrada = new BufferedInputStream(in);
                            byte[] data = new byte[(int)fileIn.length()];
                            entrada.read(data);
                            entrada.close();
                            ProcesoComprimir procesoComprimir = new ProcesoComprimir(data, Algoritmo.JPEG);
                            procesoComprimir.ejecutarProceso();
                            String newpath = s.replace(".ppm", ".imgc");
                            File fileOut = new File(newpath);
                            if (!fileOut.exists()) fileOut.createNewFile();
                            FileOutputStream out = new FileOutputStream(fileOut);
                            BufferedOutputStream salida = new BufferedOutputStream(out);
                            salida.write(procesoComprimir.getOutput());
                            DatosProceso datosProceso = procesoComprimir.getDatosProceso();
                            long oldSize = data.length, newSize = datosProceso.getNewSize();
                            double timeSeconds = (double)datosProceso.getTiempo() / 1000000000;
                            System.out.println("El archivo " + s + " se ha comprimido correctamente!\n" +
                                    "Ha tardado "+timeSeconds+"s y se ha guardado en " + newpath +".\n"+
                                    "El cambio de tamaño pasa de " + oldSize + "B a " + newSize + "B con diferencia de " +
                                    datosProceso.getDiffSize() + "B que resulta en un " +
                                    datosProceso.getDiffSizePercentage() + "% del archivo original.");
                        } else System.out.println("El formato del fichero debe de ser .txt o .ppm!");
                        break;
                    case "salir":
                    case "2":
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
