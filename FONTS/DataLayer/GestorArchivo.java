package DataLayer;

public class GestorArchivo {
    private static GestorArchivo instance = null;

    public static GestorArchivo getInstance(){
        if (instance == null)
            instance = new GestorArchivo();
        return instance;
    }

    public byte[] leeArchivo(String path) {
        byte[] temporal = new byte[1];
        return temporal;
    }

    public void guardaArchivo(byte[] Data, String path, boolean proceso){

    }

}


