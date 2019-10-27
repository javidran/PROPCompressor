// Creado por Jan Escorza Fuertes
package DomainLayer.Proceso;

import java.io.File;

public class DatosProceso {
    private long tiempo;
    private long antiguoTamaño;
    private long nuevoTamaño;

    protected DatosProceso(long time, long oldSize, long newSize) {
        tiempo = time;
        antiguoTamaño = oldSize;
        nuevoTamaño = newSize;
        System.out.println("El proceso ha tardado " + time/1000000000.0 + "s. El cambio de tamaño es " + oldSize + "B -> " + newSize + "B con diferencia de " + diffTam() + "B / " + diffTamPercent() + "%");
    }

    public long diffTam(){
        long resta_compr = antiguoTamaño - nuevoTamaño;
        if(resta_compr>=0){//es una compresión
            return resta_compr;
        }
        else{ //es una descompresion
            long resta_decompr = nuevoTamaño - antiguoTamaño;
            return resta_decompr;
        }
    }

    public double diffTamPercent(){
        long resta_compr = antiguoTamaño - nuevoTamaño;
        if(resta_compr<0){//es una compresión
            return (antiguoTamaño/(double) nuevoTamaño)*100;
        }
        else{ //es una descompresion
            //long resta_decompr = nuevoTamaño - antiguoTamaño;
            return (nuevoTamaño/(double) antiguoTamaño)*100;
        }
    }

    // TO - DO
    /*
    private void actualizarArchivo(){

    }
     */
    //End of TO-DO

    public long getTiempo() { return tiempo; }
    public long getNuevoTamaño() { return nuevoTamaño; }
    public long getAntiguoTamaño() { return antiguoTamaño; }




}
