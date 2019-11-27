package Drivers;

import Controllers.CtrlEstadistica;
import Enumeration.Algoritmo;

import java.util.Scanner;

public class CtrlEstadisticaDriver {
    public static void main(String[] args) {
        System.out.print("Bienvenido al driver de CtrlEstadistica.\n\n");
        CtrlEstadistica ctrlEstadistica = CtrlEstadistica.getInstance();
        boolean b = true;
        while (b) {
            try {
                System.out.print("Escriba el algoritmo de compresión que quiera consultar, de entre los siguientes:\n1. jpeg\n2. lzss\n3. lz78\n4. lzw\n5. salir\n");
                Scanner scanner = new Scanner(System.in);
                String algoritmoComp = scanner.nextLine();
                switch (algoritmoComp) {
                    case "jpeg":
                    case "1":
                        System.out.print(ctrlEstadistica.estadisticas(Algoritmo.JPEG));
                        break;
                    case "lzss":
                    case "2":
                        System.out.print(ctrlEstadistica.estadisticas(Algoritmo.LZSS));
                        break;
                    case "lzw":
                    case "3":
                        System.out.print(ctrlEstadistica.estadisticas(Algoritmo.LZ78));
                        break;
                    case "lz78":
                    case "4":
                        System.out.print(ctrlEstadistica.estadisticas(Algoritmo.LZW));
                        break;
                    case "salir":
                    case "5":
                        b = false;
                        break;
                    default:
                        throw new EnumConstantNotPresentException(Algoritmo.class, " El tipo de compresor " + algoritmoComp + " no existe.");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}

