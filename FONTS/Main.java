// Creado por Joan Gamez Rodriguez
import Controllers.CtrlDatos;
import Controllers.CtrlProcesos;
import DomainLayer.Algoritmos.*;

import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.printf("Bienvenido a PROPresor!\n\n");
        CtrlProcesos ctrlProcesos = CtrlProcesos.getInstance();
        boolean b = true;
        while (b) {
            Algoritmos tipoCompresor;
            System.out.printf("Introduzca uno de los siguientes comandos disponibles:\n\ncomprimir\ndescomprimir\ncompdesc (PARA TESTING)\nsalir\n");
            Scanner scanner = new Scanner(System.in);
            String comando = scanner.nextLine();
            switch (comando) {
                case "comprimir":
                case "1":
                    String s;
                    if (args.length == 0) {
                        System.out.println("Escriba el path absoluto del fichero a comprimir (.txt o .ppm!)");
                        s = scanner.nextLine();
                    }
                    else s = args[0];
                    File fileInComp = new File(s);
                    if (fileInComp.getName().endsWith(".txt")) {
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
                        File fileOutComp = ctrlProcesos.comprimirArchivo(fileInComp, tipoCompresor);
                        if (fileOutComp != null)
                            System.out.println("El archivo " + fileInComp.getName() + " se ha comprimido correctamente!\n");
                    }
                    else if (fileInComp.getName().endsWith(".ppm")) {
                        System.out.println("Indique la calidad de compresión a usar (del 1 al 8)");
                        ctrlProcesos.setCalidadJPEG(scanner.nextInt() * 10);
                        File fileOutComp = ctrlProcesos.comprimirArchivo(fileInComp);
                        if (fileOutComp != null)
                            System.out.println("El archivo " + fileInComp.getName() + " se ha comprimido correctamente!\n");
                    }
                    else System.out.println("El formato del fichero debe de ser .txt o .ppm!");
                    break;
                case "descomprimir":
                case "2":
                    String str;
                    if (args.length == 0) {
                        System.out.println("Escriba el path absoluto del fichero a descomprimir (.lzss, .lz78, lzw o .imgc!)");
                        str = scanner.nextLine();
                    }
                    else str = args[0];
                    File fileInDesc = new File(str);
                    File fileOutDesc;
                    fileOutDesc = ctrlProcesos.descomprimirArchivo(fileInDesc);
                    if(fileOutDesc != null) System.out.println("El archivo se ha descomprimido correctamente!\n");
                    break;
                case "compdesc":
                case "3":
                    String string;
                    if (args.length == 0) {
                        System.out.println("Escriba el path absoluto del fichero a comprimir (.txt o .ppm!)");
                        string = scanner.nextLine();
                    }
                    else string = args[0];
                    File fileInCompDesc = new File(string), fileComp = null, fileOutCompDesc;
                    if (fileInCompDesc.getName().endsWith(".txt")) {
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
                        fileComp = ctrlProcesos.comprimirArchivo(fileInCompDesc, tipoCompresor);
                        if (fileComp != null)
                            System.out.println("El archivo " + fileInCompDesc.getName() + " se ha comprimido correctamente!\n");
                    }
                    else if (fileInCompDesc.getName().endsWith(".ppm")) {
                        System.out.println("Indique la calidad de compresión a usar (del 1 al 8)");
                        ctrlProcesos.setCalidadJPEG(scanner.nextInt() * 10);
                        fileComp = ctrlProcesos.comprimirArchivo(fileInCompDesc);
                        if (fileComp != null)
                            System.out.println("El archivo " + fileInCompDesc.getName() + " se ha comprimido correctamente!\n");

                    }
                    fileOutCompDesc = ctrlProcesos.descomprimirArchivo(fileComp);
                    if(fileOutCompDesc != null) System.out.println("El archivo "+fileComp.getName()+" se ha descomprimido correctamente!\n");
                    break;
                case "salir":
                case "4":
                    b = false;
                    break;
                default:
                    System.out.printf("Comando incorrecto!\n");
            }
        }
        /*
        CtrlDatos ctrlDatos = CtrlDatos.getInstance();
        ctrlDatos.estadisticasGlobales(Algoritmos.LZSS, true);
         */
    }
}

