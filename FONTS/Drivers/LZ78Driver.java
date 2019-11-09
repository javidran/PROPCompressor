package Drivers;// Creado por Joan Gamez Rodriguez

import Controllers.CtrlProcesos;
import DomainLayer.Algoritmos.Algoritmo;

import java.util.Scanner;

public class LZ78Driver {
    public static void main(String[] args) throws Exception {
        System.out.print("Bienvenido al driver para el algoritmo de LZ78\n\n");
        CtrlProcesos ctrlProcesos = CtrlProcesos.getInstance();
        boolean b = true;
        while (b) {
            System.out.print("Introduzca uno de los siguientes comandos disponibles:\n\n1. comprimir\n2. descomprimir\n3. comprimirYdescomprimir\n4. salir\n");
            Scanner scanner = new Scanner(System.in);
            String comando = scanner.nextLine();
            String s;
            switch (comando) {
                case "comprimir":
                case "1":
                    if (args.length == 0) {
                        System.out.println("Escriba el path absoluto del fichero .txt a comprimir:");
                        s = scanner.nextLine();
                    } else s = args[0];
                    if (s.endsWith(".txt")) {
                        ctrlProcesos.comprimirArchivo(s, Algoritmo.LZ78);
                        String[] ss = s.split("/");
                        System.out.println("El archivo " + ss[ss.length - 1] + " se ha comprimido correctamente!\n");
                    } else System.out.println("El formato del fichero debe de ser .txt o .ppm!");
                    break;
                case "descomprimir":
                case "2":
                    if (args.length == 0) {
                        System.out.println("Escriba el path absoluto del fichero .lz78 a descomprimir:");
                        s = scanner.nextLine();
                    } else s = args[0];
                    ctrlProcesos.descomprimirArchivo(s);
                    System.out.println("El archivo se ha descomprimido correctamente!\n");
                    break;
                case "comprimirYdescomprimir":
                case "3":
                    if (args.length == 0) {
                        System.out.println("Escriba el path absoluto del fichero .txt a comprimir");
                        s = scanner.nextLine();
                    } else s = args[0];
                    if (s.endsWith(".txt")) {
                        ctrlProcesos.comprimirDescomprimirArchivo(s, Algoritmo.LZ78);
                        String[] ss = s.split("/");
                        System.out.println("El archivo " + ss[ss.length - 1] + " se ha comprimido y descomprimido correctamente!\n");
                    } else System.out.println("El formato del fichero debe de ser .txt");
                    break;
                case "salir":
                case "4":
                    b = false;
                    break;
                default:
                    System.out.print("Comando incorrecto!\n");
            }
        }
    }
}

