package DomainLayer.Algoritmos;

/**
 * Algortimo es un Enumeration que permite delimitar los tipos de algoritmos que manejan las clases de la capa de dominio
 * <p>
 *     Los tipos de algoritmos son JPEG, LZW, LZ78 y LZSS, además del algoritmo predeterminado, que será a su vez uno de los tres últimos
 * </p>
 */
public enum Algoritmo {
    JPEG, LZW, LZ78, LZSS, PREDETERMINADO
}
