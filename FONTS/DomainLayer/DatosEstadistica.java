package DomainLayer;

public class DatosEstadistica {

    private int archivosComprimidos;

    private int archivosDescomprimidos;

    private long tiempoCompresion;

    private long tiempoDescompresion;

    private double RatioCompresion;

    private double RatioDescompresion;

    public DatosEstadistica(int comprimidos, long tiempoC, double ratioC, int descomprimidos, long tiempoD, double ratioD) {
        archivosComprimidos = comprimidos;
        tiempoCompresion = tiempoC;
        RatioCompresion = ratioC;
        archivosDescomprimidos = descomprimidos;
        tiempoDescompresion = tiempoD;
        RatioDescompresion = ratioD;
    }


    public int getArchivosComprimidos() {
        return archivosComprimidos;
    }

    public int getArchivosDescomprimidos() {
        return archivosDescomprimidos;
    }

    public long getTiempoCompresion() {
        return tiempoCompresion;
    }

    public long getTiempoDescompresion() {
        return tiempoDescompresion;
    }

    public double getRatioCompresion() {
        return RatioCompresion;
    }

    public double getRatioDescompresion() {
        return RatioDescompresion;
    }
}
