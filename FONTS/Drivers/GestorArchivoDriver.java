package Drivers;

import Controllers.CtrlDatos;
import DataLayer.GestorArchivo;

import java.util.Scanner;

public class GestorArchivoDriver {
    public static void main(String[] args) {
        System.out.print("Bienvenido al driver para GestorArchivo.\n\n");
        Scanner scanner = new Scanner(System.in);
        boolean b = true;
        while (b) {
            try {
                System.out.print("Introduzca uno de los siguientes comandos disponibles:\n\n1. leeArchivo\n2. guardaArchivo\n3. salir\n");
                String comando = scanner.nextLine();
                String s;
                switch (comando) {
                    case "leeArchivo":
                    case "1":
                        System.out.println("Ahora se probara la capacidad de leer un archivo");
                        System.out.println("Escriba el nombre del fichero a leer (.txt o .ppm!)");
                        s = System.getProperty("user.dir");
                        s += s.contains("/")?"/":"\\";
                        s += scanner.nextLine();
                        if ((s.endsWith(".txt"))||(s.endsWith(".ppm"))) {
                            System.out.println("Se inicia el proceso");
                            byte[] data = GestorArchivo.leeArchivo(s);
                            System.out.println("El archivo " + s + " se ha leido correctamente!\n");
                        } else System.out.println("El formato del fichero debe de ser .txt o .ppm!");
                        break;
                    case "guardaArchivo":
                    case "2":
                        System.out.println("Ahora se probara la capacidad de escribir en un archivo");
                        System.out.println("Escriba el path absoluto donde quiere escribir el fichero incluyendo el nombre(.txt)");
                        s = scanner.nextLine();
                        if (s.endsWith(".txt")) {
                            System.out.println("Introduzca a continuación una tira de texto (por ejemplo 12345679meEncantaPROP):");
                            String s1 = scanner.nextLine();
                            byte[] b1 = s1.getBytes();
                            System.out.println("Es imporante decidir si se desea sobrescribir el fichero en caso que exista el que decide crear");
                            System.out.println("Introduzca a continuación si desea sobrescribir('1') o no ('0'):");
                            int sobrescribirInteger = Integer.parseInt(scanner.nextLine());
                            boolean sobrescribir;
                            if (sobrescribirInteger == 0) {
                                sobrescribir = false;
                            } else sobrescribir = true;
                            GestorArchivo.guardaArchivo(b1, s);
                            System.out.println("El archivo " + s + " se ha escrito correctamente!\n");
                        } else System.out.println("El formato del fichero debe de ser .txt");
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

