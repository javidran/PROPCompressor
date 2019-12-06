package DataLayer;

public class DatosEstadistica {

    private int archivosComprimidos;

    private int archivosDescomprimidos;

    private long tiempoCompresión;

    private long tiempoDescompresión;

    private double RatioCompresión;

    private double RatioDescompresión;

    public DatosEstadistica(int comprimidos, long tiempoC, double ratioC, int descomprimidos, long tiempoD, double ratioD) {
        archivosComprimidos = comprimidos;
        tiempoCompresión = tiempoC;
        RatioCompresión = ratioC;
        archivosDescomprimidos = descomprimidos;
        tiempoDescompresión = tiempoD;
        RatioDescompresión = ratioD;
    }


    public int getArchivosComprimidos() {
        return archivosComprimidos;
    }

    public int getArchivosDescomprimidos() {
        return archivosDescomprimidos;
    }

    public long getTiempoCompresión() {
        return tiempoCompresión;
    }

    public long getTiempoDescompresión() {
        return tiempoDescompresión;
    }

    public double getRatioCompresión() {
        return RatioCompresión;
    }

    public double getRatioDescompresión() {
        return RatioDescompresión;
    }
}
