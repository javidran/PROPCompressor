package DomainLayer.Proceso;

import java.util.Scanner;

public class DatosProcesoDriver {
    public static void main(String[] args) {
        System.out.print("Bienvenido al driver para DatosProceso\n");
        boolean bucle = true;
        while(bucle) {
            try {
                System.out.println("\nEscoja una opción:\n 1. Crear una instancia de DatosProceso\n 2. Salir");
                Scanner scanner = new Scanner(System.in);
                String comando = scanner.nextLine();
                switch (comando) {
                    case "1":
                        scanner = new Scanner(System.in);
                        System.out.println("Introduzca a continuación un time (por ejemplo 123):");
                        long time = Long.parseLong(scanner.nextLine());
                        System.out.println("Introduzca a continuación un oldSize (por ejemplo 1000):");
                        long oldSize = Long.parseLong(scanner.nextLine());
                        System.out.println("Introduzca a continuación un newSize (por ejemplo 500):");
                        long newSize = Long.parseLong(scanner.nextLine());
                        System.out.println("Introduzca a continuación esCompresion, si es compresión--> '1' y si no lo es--> '0'");
                        boolean esCompresion = Integer.parseInt(scanner.nextLine()) != 0;

                        System.out.println("A continuación se creará una instancia de DatosProceso con time :" + time + " oldsize: " + oldSize + " newSize: " + newSize + " y boolean esCompresion " + esCompresion);
                        DatosProceso datosProcesoNew = new DatosProceso(time, oldSize, newSize, esCompresion);
                        System.out.println("El proceso ha tardado " + datosProcesoNew.getTiempo() / 1000000000.0 + "s. El cambio de tamaño pasa de " + datosProcesoNew.getOldSize() + "B a " + datosProcesoNew.getNewSize() + "B con diferencia de " + datosProcesoNew.getDiffSize() + "B  que resulta en un  " + datosProcesoNew.getDiffSizePercentage() + "% del tamaño original");
                        if (!datosProcesoNew.isSatisfactorio())
                            System.out.println("Se considera que el proceso no ha resultado satisfactorio, ya que el archivo " + (esCompresion ? "comprimido" : "descomprimido") + " ocupa igual o " + (esCompresion ? "más" : "menos") + " que el archivo " + (esCompresion ? "original" : "comprimido") + ".");

                        System.out.println("Ahora se probará el getDiffSize:");
                        long resultado = datosProcesoNew.getDiffSize();
                        System.out.println("El resulatdo de DiffSize es: " + resultado);

                        System.out.println("Ahora se probará el getDiffSizePercentage:");
                        double resultado2 = datosProcesoNew.getDiffSizePercentage();
                        System.out.println("El resulatdo de getDiffSizePercentage es: " + resultado2);

                        System.out.println("Ahora se probará el getTiempo:");
                        long resultado3 = datosProcesoNew.getTiempo();
                        System.out.println("El resulatdo de getTiempo es: " + resultado3);

                        System.out.println("Ahora se probará el getNewSize:");
                        long resultado4 = datosProcesoNew.getNewSize();
                        System.out.println("El resulatdo de getNewSize es: " + resultado4);

                        System.out.println("Ahora se probará el getOldSize:");
                        long resultado5 = datosProcesoNew.getOldSize();
                        System.out.println("El resulatdo de getOldSize es: " + resultado5);
                        break;
                    case "2":
                        bucle = false;
                        break;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}

