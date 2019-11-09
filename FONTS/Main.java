// Creado por Joan Gamez Rodriguez
import Controllers.CtrlDatos;
import Controllers.CtrlProcesos;
import DomainLayer.Algoritmos.*;
import Exceptions.FormatoErroneoException;

import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.printf("Bienvenido a PROPresor!\n\n");
        CtrlProcesos ctrlProcesos = CtrlProcesos.getInstance();
        boolean b = true;
        while (b) {
            try {
                Algoritmos tipoCompresor;
                System.out.printf("Introduzca uno de los siguientes comandos disponibles:\n\ncomprimir\ndescomprimir\ncompdesc (PARA TESTING)\nsalir\n");
                Scanner scanner = new Scanner(System.in);
                String comando = scanner.nextLine();
                String s;
                switch (comando) {
                    case "comprimir":
                    case "1":
                        if (args.length == 0) {
                            System.out.println("Escriba el path absoluto del fichero a comprimir (.txt o .ppm!)");
                            s = scanner.nextLine();
                        } else s = args[0];
                        if (s.endsWith(".txt")) {
                            System.out.printf("Escriba el algoritmo de compresión que quiera usar, de entre los siguientes:\npredeterminado\nlzss\nlz78\nlzw\n");
                            String algoritmoComp = scanner.nextLine();
                            switch (algoritmoComp) {
                                case "lzss":
                                    tipoCompresor = Algoritmos.LZSS;
                                    break;
                                case "lzw":
                                    tipoCompresor = Algoritmos.LZW;
                                    break;
                                case "lz78":
                                    tipoCompresor = Algoritmos.LZ78;
                                    break;
                                case "predeterminado":
                                    tipoCompresor = Algoritmos.PREDETERMINADO;
                                    break;
                                default:
                                    throw new EnumConstantNotPresentException(Algoritmos.class, "El tipo de compresor " + algoritmoComp + " no existe o no está disponible para un archivo .txt\n");
                            }
                            ctrlProcesos.comprimirArchivo(s, tipoCompresor);
                            System.out.println("El archivo " + s + " se ha comprimido correctamente!\n");
                        } else if (s.endsWith(".ppm")) {
                            System.out.println("Indique la calidad de compresión a usar (del 1 al 7)");
                            ctrlProcesos.setCalidadJPEG(scanner.nextInt() * 10);
                            ctrlProcesos.comprimirArchivo(s, Algoritmos.JPEG);
                            System.out.println("El archivo " + s + " se ha comprimido correctamente!\n");
                        } else System.out.println("El formato del fichero debe de ser .txt o .ppm!");
                        break;
                    case "descomprimir":
                    case "2":
                        if (args.length == 0) {
                            System.out.println("Escriba el path absoluto del fichero a descomprimir (.lzss, .lz78, lzw o .imgc!)");
                            s = scanner.nextLine();
                        } else s = args[0];
                        ctrlProcesos.descomprimirArchivo(s);
                        System.out.println("El archivo se ha descomprimido correctamente!\n");
                        break;
                    case "compdesc":
                    case "3":
                        if (args.length == 0) {
                            System.out.println("Escriba el path absoluto del fichero a comprimir (.txt o .ppm!)");
                            s = scanner.nextLine();
                        } else s = args[0];
                        if (s.endsWith(".txt")) {
                            System.out.printf("Escriba el algoritmo de compresión que quiera usar, de entre los siguientes:\npredeterminado\nlzss\nlz78\nlzw\n");
                            String algoritmoComp = scanner.nextLine();
                            switch (algoritmoComp) {
                                case "lzss":
                                    tipoCompresor = Algoritmos.LZSS;
                                    break;
                                case "lzw":
                                    tipoCompresor = Algoritmos.LZW;
                                    break;
                                case "lz78":
                                    tipoCompresor = Algoritmos.LZ78;
                                    break;
                                case "predeterminado":
                                    tipoCompresor = Algoritmos.PREDETERMINADO;
                                    break;
                                default:
                                    throw new EnumConstantNotPresentException(Algoritmos.class, "El tipo de compresor " + algoritmoComp + " no existe o no está disponible para un archivo .txt\n");
                            }
                            ctrlProcesos.comprimirDescomprimirArchivo(s, tipoCompresor);
                        } else if (s.endsWith(".ppm")) {
                            System.out.println("Indique la calidad de compresión a usar (del 1 al 7)");
                            ctrlProcesos.setCalidadJPEG(scanner.nextInt() * 10);
                            ctrlProcesos.comprimirDescomprimirArchivo(s, Algoritmos.JPEG);
                        }
                        System.out.println("El archivo " + s + " se ha comprimido y descomprimido correctamente!\n");
                        break;
                    case "salir":
                    case "4":
                        b = false;
                        break;
                    default:
                        System.out.printf("Comando incorrecto!\n");
                }
            } catch (FormatoErroneoException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}

