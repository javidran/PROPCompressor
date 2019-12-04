package Drivers;

import Controllers.CtrlEstadistica;
import Controllers.CtrlProcesos;
import DomainLayer.Proceso.DatosProceso;
import Enumeration.Algoritmo;
import PresentationLayer.VistaInicio;

import javax.swing.*;
import java.awt.*;
import java.util.Scanner;

public class MainDriver {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new VistaInicio();
                Dimension dimension = new Dimension(650, 300);
                frame.setSize(dimension);
                dimension = new Dimension(500, 200);
                frame.setMinimumSize(dimension);
                frame.setResizable(true);
                frame.setVisible(true);
            }
        });
        System.out.print("Bienvenido a PROPresor!\n\n");
        CtrlProcesos ctrlProcesos = CtrlProcesos.getInstance();
        DatosProceso dp;
        boolean b = true;
        while (b) {
            try {
                Algoritmo tipoCompresor;
                System.out.print("Introduzca uno de los siguientes comandos disponibles:\n\n1. comprimirArchivo\n2. descomprimirArchivo\n3. comprimirYdescomprimirArchivo\n4. comprimirCarpeta\n5. descomprimirCarpeta\n6. estadisticas\n7. cambiarAlgoritmoTextoPredeterminado\n8. salir\n");
                Scanner scanner = new Scanner(System.in);
                String comando = scanner.nextLine();
                String s;
                switch (comando) {
                    case "comprimirArchivo":
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
                            switch (algoritmoComp) {
                                case "lzss":
                                    tipoCompresor = Algoritmo.LZSS;
                                    break;
                                case "lzw":
                                    tipoCompresor = Algoritmo.LZW;
                                    break;
                                case "lz78":
                                    tipoCompresor = Algoritmo.LZ78;
                                    break;
                                case "predeterminado":
                                    tipoCompresor = Algoritmo.PREDETERMINADO;
                                    break;
                                default:
                                    throw new EnumConstantNotPresentException(Algoritmo.class, "El tipo de compresor " + algoritmoComp + " no existe o no está disponible para un archivo .txt\n");
                            }
                            System.out.println("Se inicia el proceso");
                            dp = ctrlProcesos.comprimirArchivo(s, CtrlProcesos.calcularPathSalida(s, tipoCompresor, true), tipoCompresor);
                            System.out.println("El archivo " + s + " se ha comprimido correctamente!\n");
                        } else if (s.endsWith(".ppm")) {
                            System.out.println("Indique la calidad de compresión a usar (del 1 al 7)");
                            ctrlProcesos.setCalidadJPEG(scanner.nextInt());
                            System.out.println("Se inicia el proceso");
                            dp = ctrlProcesos.comprimirArchivo(s, CtrlProcesos.calcularPathSalida(s, Algoritmo.JPEG, true), Algoritmo.JPEG);
                            System.out.println("El archivo " + s + " se ha comprimido correctamente!\n");
                        } else System.out.println("El formato del fichero debe de ser .txt o .ppm!");
                        break;
                    case "descomprimirArchivo":
                    case "2":
                        if (args.length == 0) {
                            System.out.println("Escriba el path relativo del fichero a descomprimir (.lzss, .lz78, lzw o .imgc!)");
                            s = System.getProperty("user.dir");
                            s += s.contains("/")?"/":"\\";
                            s += scanner.nextLine();
                        } else s = args[0];
                        if (s.endsWith(".lz78") || s.endsWith(".lzss") ||  s.endsWith(".lzw") ||  s.endsWith(".imgc")) {
                            System.out.println("Se inicia el proceso");
                            dp = ctrlProcesos.descomprimirArchivo(s, CtrlProcesos.calcularPathSalida(s, CtrlProcesos.algoritmoPosible(s), false));
                            System.out.println("El archivo se ha descomprimido correctamente!\n");
                        } else System.out.println("El formato del fichero debe de ser .lz78, .lzss, .lzw o .imgc!");
                        break;
                    case "comprimirYdescomprimirArchivo":
                    case "3":
                        if (args.length == 0) {
                            System.out.println("Escriba el path relativo del fichero a comprimir (.txt o .ppm!)");
                            s = System.getProperty("user.dir");
                            s += s.contains("/")?"/":"\\";
                            s += scanner.nextLine();
                        } else s = args[0];
                        if (s.endsWith(".txt")) {
                            System.out.print("Escriba el algoritmo de compresión que quiera usar, de entre los siguientes:\npredeterminado\nlzss\nlz78\nlzw\n");
                            String algoritmoComp = scanner.nextLine();
                            switch (algoritmoComp) {
                                case "lzss":
                                    tipoCompresor = Algoritmo.LZSS;
                                    break;
                                case "lzw":
                                    tipoCompresor = Algoritmo.LZW;
                                    break;
                                case "lz78":
                                    tipoCompresor = Algoritmo.LZ78;
                                    break;
                                case "predeterminado":
                                    tipoCompresor = Algoritmo.PREDETERMINADO;
                                    break;
                                default:
                                    throw new EnumConstantNotPresentException(Algoritmo.class, "El tipo de compresor " + algoritmoComp + " no existe o no está disponible para un archivo .txt\n");
                            }
                            System.out.println("Se inicia el proceso");
                            dp = ctrlProcesos.comprimirDescomprimirArchivo(s, tipoCompresor);
                            System.out.println("El archivo " + s + " se ha comprimido y descomprimido correctamente!\n");
                        } else if (s.endsWith(".ppm")) {
                            System.out.println("Indique la calidad de compresión a usar (del 1 al 7)");
                            ctrlProcesos.setCalidadJPEG(scanner.nextInt());
                            System.out.println("Se inicia el proceso");
                            dp = ctrlProcesos.comprimirDescomprimirArchivo(s, Algoritmo.JPEG);
                            System.out.println("El archivo " + s + " se ha comprimido y descomprimido correctamente!\n");
                        } else System.out.println("El formato del fichero debe de ser .txt o .ppm!");
                        break;
                    case "comprimirCarpeta":
                    case "4":
                        if (args.length == 0) {
                            System.out.println("Escriba el path relativo de la carpeta a comprimir");
                            s = System.getProperty("user.dir");
                            s += s.contains("/")?"/":"\\";
                            s += scanner.nextLine();
                        } else s = args[0];
                        System.out.print("Escriba el algoritmo de compresión de texto que quiera usar, de entre los siguientes:\npredeterminado\nlzss\nlz78\nlzw\n");
                        String algoritmoComp = scanner.nextLine();
                        switch (algoritmoComp) {
                            case "lzss":
                                tipoCompresor = Algoritmo.LZSS;
                                break;
                            case "lzw":
                                tipoCompresor = Algoritmo.LZW;
                                break;
                            case "lz78":
                                tipoCompresor = Algoritmo.LZ78;
                                break;
                            case "predeterminado":
                                tipoCompresor = Algoritmo.PREDETERMINADO;
                                break;
                            default:
                                throw new EnumConstantNotPresentException(Algoritmo.class, "El tipo de compresor " + algoritmoComp + " no existe o no está disponible para un archivo .txt\n");
                        }
                        System.out.println("Indique la calidad de compresión a usar para las imágenes (del 1 al 7)");
                        ctrlProcesos.setCalidadJPEG(scanner.nextInt());
                        System.out.println("Se inicia el proceso");
                        ctrlProcesos.comprimirCarpeta(s, s+".comp");
                        System.out.println("El archivo " + s + " se ha comprimido correctamente!\n");
                        break;
                    case "descomprimirCarpeta":
                    case "5":
                        if (args.length == 0) {
                            System.out.println("Escriba el path relativo de la carpeta a descomprimir");
                            s = System.getProperty("user.dir");
                            s += s.contains("/")?"/":"\\";
                            s += scanner.nextLine();
                        } else s = args[0];
                        System.out.println("Se inicia el proceso");
                        ctrlProcesos.descomprimirCarpeta(s, s.replace(".comp", ""));
                        System.out.println("El archivo " + s + " se ha descomprimido correctamente!\n");
                        break;
                    case "estadisticas":
                    case "6":
                        System.out.print("Escriba el algoritmo de compresión que quiera consultar, de entre los siguientes:\njpeg\nlzss\nlz78\nlzw\n");
                        algoritmoComp = scanner.nextLine();
                        switch (algoritmoComp) {
                            case "jpeg":
                                tipoCompresor = Algoritmo.JPEG;
                                break;
                            case "lzss":
                                tipoCompresor = Algoritmo.LZSS;
                                break;
                            case "lzw":
                                tipoCompresor = Algoritmo.LZW;
                                break;
                            case "lz78":
                                tipoCompresor = Algoritmo.LZ78;
                                break;
                            default:
                                throw new EnumConstantNotPresentException(Algoritmo.class, " El tipo de compresor " + algoritmoComp + " no existe.");
                        }
                        CtrlEstadistica ctrlEstadistica = CtrlEstadistica.getInstance();
                        System.out.print(ctrlEstadistica.estadisticas(tipoCompresor));
                        break;
                    case "cambiarAlgoritmoTextoPredeterminado":
                    case "7":
                        System.out.println("El algoritmo de compresión de texto predeterminado es " + CtrlProcesos.getAlgoritmoDeTextoPredeterminado() + ".");
                        System.out.print("Escriba el algoritmo de compresión que quiera usar de manera predeterminada, de entre los siguientes:\nlzss\nlz78\nlzw\n");
                        String algoritmoCompresion = scanner.nextLine();
                        Algoritmo tipoAlgoritmo;
                        switch (algoritmoCompresion) {
                            case "lzss":
                                tipoAlgoritmo = Algoritmo.LZSS;
                                break;
                            case "lzw":
                                tipoAlgoritmo = Algoritmo.LZW;
                                break;
                            case "lz78":
                                tipoAlgoritmo = Algoritmo.LZ78;
                                break;
                            default:
                                throw new EnumConstantNotPresentException(Algoritmo.class, "El tipo de compresor " + algoritmoCompresion + " no existe o no está disponible para tratar un archivo .txt\n");
                        }
                        CtrlProcesos.setAlgoritmoDeTextoPredeterminado(tipoAlgoritmo);
                        System.out.println("Enumeration.Algoritmo de texto predeterminado cambiado a " + tipoAlgoritmo + ".");
                        break;
                    case "salir":
                    case "8":
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

