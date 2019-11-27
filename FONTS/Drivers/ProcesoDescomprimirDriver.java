package Drivers;

import Enumeration.Algoritmo;
import DomainLayer.Proceso.DatosProceso;
import DomainLayer.Proceso.ProcesoDescomprimir;

import java.io.*;
import java.util.Scanner;

public class ProcesoDescomprimirDriver {
    public static void main(String[] args) {
        System.out.print("Bienvenido al driver de ProcesoDescomprimir.\n\n");
        boolean b = true;
        while (b) {
            try {
                Algoritmo tipoCompresor;
                System.out.print("Introduzca uno de los siguientes comandos disponibles:\n\n1. descomprimir\n2. salir\n");
                Scanner scanner = new Scanner(System.in);
                String comando = scanner.nextLine();
                String s;
                switch (comando) {
                    case "descomprimir":
                    case "1":
                        if (args.length == 0) {
                            System.out.println("Escriba el path relativo del fichero a descomprimir (.lzss, .lz78, .lzw o .imgc!)");
                            s = System.getProperty("user.dir");
                            s += s.contains("/")?"/":"\\";
                            s += scanner.nextLine();
                        } else s = args[0];
                        if (s.endsWith(".lzss") || s.endsWith(".lz78") || s.endsWith(".lzw")) {
                            String pathSplit[] = s.split("\\.");
                            String algoritmoComp = pathSplit[pathSplit.length - 1];
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
                            ProcesoDescomprimir procesoDescomprimir = new ProcesoDescomprimir(data, tipoCompresor);
                            procesoDescomprimir.ejecutarProceso();
                            String newpath = s.replace(extension, "_out.txt");
                            File fileOut = new File(newpath);
                            if (!fileOut.exists()) fileOut.createNewFile();
                            FileOutputStream out = new FileOutputStream(fileOut);
                            BufferedOutputStream salida = new BufferedOutputStream(out);
                            salida.write(procesoDescomprimir.getOutput());
                            DatosProceso datosProceso = procesoDescomprimir.getDatosProceso();
                            long oldSize = data.length, newSize = datosProceso.getNewSize();
                            double timeSeconds = (double)datosProceso.getTiempo() / 1000000000;
                            System.out.println("El archivo " + s + " se ha descomprimido correctamente!\n" +
                                    "Ha tardado "+timeSeconds+"s y se ha guardado en " + newpath +".\n"+
                                    "El cambio de tamaño pasa de " + oldSize + "B a " + newSize + "B con diferencia de " +
                                    datosProceso.getDiffSize() + "B que resulta en un " +
                                    datosProceso.getDiffSizePercentage() + "% del archivo original.");
                        } else if (s.endsWith(".imgc")) {
                            System.out.println("Se inicia el proceso");
                            File fileIn = new File(s);
                            FileInputStream in = new FileInputStream(fileIn);
                            BufferedInputStream entrada = new BufferedInputStream(in);
                            byte[] data = new byte[(int)fileIn.length()];
                            entrada.read(data);
                            entrada.close();
                            ProcesoDescomprimir procesoDescomprimir = new ProcesoDescomprimir(data, Algoritmo.JPEG);
                            procesoDescomprimir.ejecutarProceso();
                            String newpath = s.replace(".imgc", "_out.ppm");
                            File fileOut = new File(newpath);
                            if (!fileOut.exists()) fileOut.createNewFile();
                            FileOutputStream out = new FileOutputStream(fileOut);
                            BufferedOutputStream salida = new BufferedOutputStream(out);
                            salida.write(procesoDescomprimir.getOutput());
                            DatosProceso datosProceso = procesoDescomprimir.getDatosProceso();
                            long oldSize = data.length, newSize = datosProceso.getNewSize();
                            double timeSeconds = (double)datosProceso.getTiempo() / 1000000000;
                            System.out.println("El archivo " + s + " se ha descomprimido correctamente!\n" +
                                    "Ha tardado "+timeSeconds+"s y se ha guardado en " + newpath +".\n"+
                                    "El cambio de tamaño pasa de " + oldSize + "B a " + newSize + "B con diferencia de " +
                                    datosProceso.getDiffSize() + "B / " +
                                    datosProceso.getDiffSizePercentage() + "%");
                        } else System.out.println("El formato del fichero debe de ser .lzss, .lz78, .lzw o .ppm!");
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
