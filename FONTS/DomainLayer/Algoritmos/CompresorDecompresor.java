package DomainLayer.Algoritmos;

import Exceptions.FormatoErroneoException;

/**
 * Interfaz que define los métodos básicos de un algoritmo de compresión.
 */
public interface CompresorDecompresor {

    /**
     * Comprime la secuencia de bytes correspondiente al contenido de un archivo.
     * @param datosInput Secuencia de bytes a comprimir.
     * @return Devuelve una instancia de OutputAlgoritmo con el tiempo que ha tardado la compresión y con la secuencia de bytes comprimida.
     * @throws FormatoErroneoException En caso de que el formato del archivo no sea el adecuado para ser comprimido.
     */
    public OutputAlgoritmo comprimir(byte[] datosInput) throws FormatoErroneoException;

    /**
     * Descomprime la secuencia de bytes correspondiente al contenido de un archivo.
     * @param datosInput Secuencia de bytes a descomprimir.
     * @return Devuelve una instancia de OutputAlgoritmo con el tiempo que ha tardado la descompresión y con la secuencia de bytes descomprimida.
     * @throws FormatoErroneoException En caso de que el formato del archivo no sea el adecuado para ser descomprimido.
     */
    public OutputAlgoritmo descomprimir(byte[] datosInput) throws FormatoErroneoException;
}
