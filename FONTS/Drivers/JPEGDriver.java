package Drivers;// Creado por Joan Gamez Rodriguez

import Controllers.CtrlProcesos;
import DomainLayer.Algoritmos.Algoritmo;

import java.util.Scanner;

public class JPEGDriver {
    public static void main(String[] args) {
        System.out.print("Bienvenido al driver para el algoritmo de JPEG\n\n");
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
                        System.out.println("Escriba el path absoluto del fichero .ppm a comprimir:");
                        s = scanner.nextLine();
                        if (s.endsWith(".ppm")) {
                            System.out.println("Indique la calidad de compresión a usar (del 1 al 7)");
                            ctrlProcesos.setCalidadJPEG(scanner.nextInt() * 10);
                            System.out.println("Se inicia el proceso");
                            ctrlProcesos.comprimirArchivo(s, Algoritmo.JPEG);
                            System.out.println("El archivo " + s + " se ha comprimido correctamente!\n");
                        } else System.out.println("El formato del fichero debe de ser .ppm!");
                        break;
                    case "descomprimir":
                    case "2":
                        System.out.println("Escriba el path absoluto del fichero .imgc a descomprimir:");
                        s = scanner.nextLine();
                        if (s.endsWith(".imgc")) {
                            System.out.println("Se inicia el proceso");
                            ctrlProcesos.descomprimirArchivo(s);
                            System.out.println("El archivo se ha descomprimido correctamente!\n");
                        } else System.out.println("El formato del fichero debe de ser .imgc!");
                        break;
                    case "comprimirYdescomprimir":
                    case "3":
                        System.out.println("Escriba el path absoluto del fichero .ppm a comprimir");
                        s = scanner.nextLine();
                        if (s.endsWith(".ppm")) {
                            System.out.println("Indique la calidad de compresión a usar (del 1 al 7)");
                            ctrlProcesos.setCalidadJPEG(scanner.nextInt() * 10);
                            System.out.println("Se inicia el proceso");
                            ctrlProcesos.comprimirDescomprimirArchivo(s, Algoritmo.JPEG);
                            System.out.println("El archivo " + s + " se ha comprimido y descomprimido correctamente!\n");
                        } else System.out.println("El formato del fichero debe de ser .ppm!");
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

