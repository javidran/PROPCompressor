// Creado por Jan Escorza Fuertes
package DomainLayer.Proceso;

public class DatosProceso {
    private long tiempo;
    private long oldSize;
    private long newSize;
    boolean esCompresion;

    protected DatosProceso(long time, long oldSize, long newSize, boolean esCompresion) throws Exception {
        tiempo = time;
        this.oldSize = oldSize;
        this.newSize = newSize;
        this.esCompresion = esCompresion;
        System.out.println("El proceso ha tardado " + time/1000000000.0 + "s. El cambio de tamaño pasa de " + oldSize + "B a " + newSize + "B con diferencia de " + getDiffSize() + "B / " + getDiffSizePercentage() + "%");
        if(getDiffSize() < 0) {
            System.out.println("El proceso de" + (esCompresion?"compresión":"descompresión") + " no ha resultado satisfactorio ya que ocupa " + (esCompresion?"más":"menos") + " que el archivo original. Se guardará igualmente.");
            throw new Exception("El proceso no ha sido satisfactorio");
        }
    }

    public long getDiffSize() {
        if(esCompresion) return oldSize - newSize;
        else return newSize - oldSize;
    }

    public double getDiffSizePercentage() {
        if(esCompresion) return Math.floor((newSize /(double) oldSize)*100);
        else return Math.floor((oldSize /(double) newSize)*100);
    }

    public long getTiempo() { return tiempo; }
    public long getNewSize() { return newSize; }
    public long getOldSize() { return oldSize; }
}
