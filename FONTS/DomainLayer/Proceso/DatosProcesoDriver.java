package DomainLayer.Proceso;// Creado por Joan Gamez Rodriguez

//import Controllers.CtrlProcesos;

import java.util.Scanner;

public class DatosProcesoDriver {
    public static void main(String[] args) throws Exception {
        System.out.print("Bienvenido al driver para DatosProceso\n\n");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Introduzca a continuación un time (por ejemplo 123):");
        long time = Long.parseLong(scanner.nextLine());
        System.out.println("Introduzca a continuación un oldSize (por ejemplo 1000):");
        long oldSize = Long.parseLong(scanner.nextLine());
        System.out.println("Introduzca a continuación un newSize (por ejemplo 500):");
        long newSize = Long.parseLong(scanner.nextLine());
        System.out.println("Introduzca a continuación esCompresion (1= true, 0=false) Un 1 o un 0:");
        int esCompresionInteger = Integer.parseInt(scanner.nextLine());
        boolean esCompresion;
        if (esCompresionInteger == 0){
            esCompresion=false;
        }
        else esCompresion=true;

        System.out.println("A continuación se creará DatosProceso con time:"+time+" oldsize:"+oldSize+" newSize:"+newSize+" y boolean esCompresion"+esCompresion);
        DatosProceso DatosProcesonew = new  DatosProceso(time,oldSize,newSize,esCompresion);

        System.out.println("Ahora se probará el getDiffSize:");
        long resultado = DatosProcesonew.getDiffSize();
        System.out.println("El resulatdo de DiffSize es:"+resultado);

        System.out.println("Ahora se probará el getDiffSizePercentage:");
        double resultado2 = DatosProcesonew.getDiffSizePercentage();
        System.out.println("El resulatdo de getDiffSizePercentage es:"+resultado2);

        System.out.println("Ahora se probará el getTiempo:");
        long resultado3 = DatosProcesonew.getTiempo();
        System.out.println("El resulatdo de getTiempo es:"+resultado3);

        System.out.println("Ahora se probará el getNewSize:");
        long resultado4 = DatosProcesonew.getNewSize();
        System.out.println("El resulatdo de getNewSize es:"+resultado4);

        System.out.println("Ahora se probará el getOldSize:");
        long resultado5 = DatosProcesonew.getOldSize();
        System.out.println("El resulatdo de getOldSize es:"+resultado5);

    }
}

