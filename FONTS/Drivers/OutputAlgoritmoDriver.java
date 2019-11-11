package Drivers; //Creado por Joan Gamez Rodriguez

import DomainLayer.Algoritmos.OutputAlgoritmo;

import java.util.Arrays;
import java.util.Scanner;

public class OutputAlgoritmoDriver {
    public static void main(String[] args) {
        try {
            System.out.print("Bienvenido al driver para OutputAlgoritmo\n\n");
            Scanner scanner = new Scanner(System.in);
            System.out.println("Introduzca a continuación una cifra (por ejemplo 123):");
            long time = Long.parseLong(scanner.nextLine());
            System.out.println("Introduzca a continuación una tira de texto (por ejemplo asdf1234):");
            String s = scanner.nextLine();
            byte[] b = s.getBytes();

            System.out.println("A continuación se creará OutputAlgoritmo con tiempo: " + time + " y output: " + s);
            OutputAlgoritmo outputAlgoritmo = new OutputAlgoritmo(time, b);

            System.out.println("Ahora se probará el valor del tiempo:");
            long resultado = outputAlgoritmo.tiempo;
            System.out.println("El resulatdo de tiempo es: " + resultado);

            System.out.println("Ahora se probará el valor del output:");
            String resultado2 = new String(outputAlgoritmo.output);
            System.out.println("El resultado de output es: " + resultado2);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
