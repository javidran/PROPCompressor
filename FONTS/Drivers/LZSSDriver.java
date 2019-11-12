package Drivers;// Creado por Jan Escorza Fuertes

import Controllers.CtrlProcesos;
import DomainLayer.Algoritmos.Algoritmo;

import java.util.Scanner;

public class LZSSDriver {
    public static void main(String[] args) {
        System.out.print("Bienvenido al driver para el algoritmo de LZSS\n\n");
        CtrlProcesos ctrlProcesos = CtrlProcesos.getInstance();
        boolean b = true;
        while (b) {
            try {
                System.out.print("Introduzca uno de los siguientes comandos disponibles:\n\n1. comprimir\n2. descomprimir\n3. comprimirYdescomprimir\n4. salir\n");
                Scanner scanner = new Scanner(System.in);
                String comando = scanner.nextLine();
                String s;
                switch (comando) {
                    case "comprimir":
                    case "1":
                        System.out.println("Escriba el nombre del fichero .txt a comprimir:");
                        s = System.getProperty("user.dir");
                        s += s.contains("/")?"/":"\\";
                        s += scanner.nextLine();
                        if (s.endsWith(".txt")) {
                            System.out.println("Se inicia el proceso");
                            ctrlProcesos.comprimirArchivo(s, Algoritmo.LZSS);
                            System.out.println("El archivo " + s + " se ha comprimido correctamente!\n");
                        } else System.out.println("El formato del fichero debe de ser .txt!");
                        break;
                    case "descomprimir":
                    case "2":
                        System.out.println("Escriba el nombre del fichero .lzss a descomprimir:");
                        s = System.getProperty("user.dir");
                        s += s.contains("/")?"/":"\\";
                        s += scanner.nextLine();
                        if (s.endsWith(".lzss")) {
                            System.out.println("Se inicia el proceso");
                            ctrlProcesos.descomprimirArchivo(s);
                            System.out.println("El archivo se ha descomprimido correctamente!\n");
                        } else System.out.println("El formato del fichero debe de ser .lzss!");
                        break;
                    case "comprimirYdescomprimir":
                    case "3":
                        System.out.println("Escriba el nombre del fichero .txt a comprimir");
                        s = System.getProperty("user.dir");
                        s += s.contains("/")?"/":"\\";
                        s += scanner.nextLine();
                        if (s.endsWith(".txt")) {
                            System.out.println("Se inicia el proceso");
                            ctrlProcesos.comprimirDescomprimirArchivo(s, Algoritmo.LZSS);
                            System.out.println("El archivo " + s + " se ha comprimido y descomprimido correctamente!\n");
                        } else System.out.println("El formato del fichero debe de ser .txt!");
                        break;
                    case "salir":
                    case "4":
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

