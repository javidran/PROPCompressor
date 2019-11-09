// Creado por Jan Escorza Fuertes
package DomainLayer.Proceso;

public class DatosProceso {
    private long tiempo;
    private long oldSize;
    private long newSize;

    protected DatosProceso(long time, long oldSize, long newSize) {
        tiempo = time;
        this.oldSize = oldSize;
        this.newSize = newSize;
        System.out.println("El proceso ha tardado " + time/1000000000.0 + "s. El cambio de tamaño pasa de " + oldSize + "B a " + newSize + "B con diferencia de " + diffTam() + "B / " + diffTamPercent() + "%");
    }

    public long diffTam() {
        long resta_compr = oldSize - newSize;
        if(resta_compr>=0){//es una compresión
            return resta_compr;
        }
        else{ //es una descompresion
            long resta_decompr = newSize - oldSize;
            return resta_decompr;
        }
    }

    public double diffTamPercent(){
        long resta_compr = oldSize - newSize;
        if(resta_compr<0){//es una compresión
            return Math.floor((oldSize /(double) newSize)*100);
        }
        else{ //es una descompresion
            //long resta_decompr = nuevoTamaño - antiguoTamaño;
            return Math.floor((newSize /(double) oldSize)*100);
        }
    }

    public long getTiempo() { return tiempo; }
    public long getNewSize() { return newSize; }
    public long getOldSize() { return oldSize; }




}
