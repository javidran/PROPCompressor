package DomainLayer.Algoritmos;

import Exceptions.FormatoErroneoException;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * La clase Singleton JPEG es la encargada de procesar archivos de imagen de extensión .ppm o .imgc y comprimirlos y descomprimirlos respectivamente
 */
public class JPEG implements CompresorDecompresor {
    /**
     * Instancia de JPEG para garantizar que es una clase Singleton
     */
    private static JPEG instance = null;
    /**
     * Escalar usado para modular la calidad de compresión y descompresión de las imágenes
     */
    private double calidad;
    /**
     * Valor de calidad usado para escribirlo en el header de las imágenes comprimidas con la intención de saber con qué calidad se tienen que descomprimir para que el resultado sea el esperado
     */
    private int calidadHeader;
    /**
     * Tabla de cuantización usada para comprimir y descomprimir los valores de luminosidad (Y) de los cuadrados de píxeles de dimensiones 8x8. Este atributo es estático
     */
    private static int[][] LuminanceQuantizationTable = new int[8][8];   //50% compression
    /**
     * Tabla de cuantización usada para comprimir y descomprimir los valores de crominancia (Cb y Cr) de los cuadrados de píxeles de dimensiones 8x8. Este atributo es estático
     */
    private static int[][] ChrominanceQuantizationTable = new int[8][8]; //50% compression
    /**
     * Tabla de valores estándar para la codificación según Huffman coding de los valores obtenidos con el RLE del algoritmo JPEG. Este atributo es estático
     */
    private static String[][] ACHuffmanTableLuminance = new String[15][11];
    /**
     * Tabla de valores estándar para la descodificación según Huffman coding de los valores obtenidos con el RLE del algoritmo JPEG. Este atributo es estático
     */
    private static Map<String, Pair> ACInverseHuffmanTable = new HashMap<>();
    /**
     * Tabla de valores estándar para la codificación según Huffman coding de los valores obtenidos con el RLE del algoritmo JPEG. Este atributo es estático
     */
    private static String[][] ACHuffmanTableChrominance;
    /**
     * Getter de la instancia Singleton de JPEG
     * @return La instancia Singleton de JPEG
     */
    public static JPEG getInstance()
    {
        if (instance == null)
            instance = new JPEG();

        return instance;
    }

    /**
     * Constructora de JPEG, la cual inicializa todos los atributos de clase excepto el propio instance, del cual se encarga getInstance()
     */
    private JPEG() {
        calidad = 1.0;
        calidadHeader = 5;
        LuminanceQuantizationTable = new int[][] {
                {16, 11, 10, 16,  24,  40,  51,  61},
                {12, 12, 14, 19,  26,  58,  60,  55},
                {14, 13, 16, 24,  40,  57,  69,  56},
                {14, 17, 22, 29,  51,  87,  80,  62},
                {18, 22, 37, 56,  68, 109, 103,  77},
                {24, 35, 55, 64,  81, 104, 113,  92},
                {49, 64, 78, 87, 103, 121, 120, 101},
                {72, 92, 95, 98, 112, 100, 103,  99}
        };
        ChrominanceQuantizationTable = new int[][] {
                {17, 18, 24, 47, 99, 99, 99, 99},
                {18, 21, 26, 66, 99, 99, 99, 99},
                {24, 26, 56, 99, 99, 99, 99, 99},
                {47, 66, 99, 99, 99, 99, 99, 99},
                {99, 99, 99, 99, 99, 99, 99, 99},
                {99, 99, 99, 99, 99, 99, 99, 99},
                {99, 99, 99, 99, 99, 99, 99, 99},
                {99, 99, 99, 99, 99, 99, 99, 99}
        };
        ACHuffmanTableLuminance = new String[][] {
                {"1010", "00", "01", "100", "1011", "11010", "1111000", "11111000", "1111110110", "1111111110000010", "1111111110000011"},
                {"", "1100", "11011", "1111001", "111110110", "11111110110", "1111111110000100", "1111111110000101", "1111111110000110", "1111111110000111", "1111111110001000"},
                {"", "11100", "11111001", "1111110111", "111111110100", "1111111110001001", "1111111110001010", "1111111110001011", "1111111110001100", "1111111110001101", "1111111110001110"},
                {"", "111010", "111110111", "111111110101", "1111111110001111", "1111111110010000", "1111111110010001", "1111111110010010", "1111111110010011", "1111111110010100", "1111111110010101"},
                {"", "111011", "1111111000", "1111111110010110", "1111111110010111", "1111111110011000", "1111111110011001", "1111111110011010", "1111111110011011", "1111111110011100", "1111111110011101"},
                {"", "1111010", "11111110111", "1111111110011110", "1111111110011111", "1111111110100000", "1111111110100001", "1111111110100010", "1111111110100011", "1111111110100100", "1111111110100101"},
                {"", "1111011", "111111110110", "1111111110100110", "1111111110100111", "1111111110101000", "1111111110101001", "1111111110101010", "1111111110101011", "1111111110101100", "1111111110101101"},
                {"", "11111010", "111111110111", "1111111110101110", "1111111110101111", "1111111110110000", "1111111110110001", "1111111110110010", "1111111110110011", "1111111110110100", "1111111110110101"},
                {"", "111111000", "111111111000000", "1111111110110110", "1111111110110111", "1111111110111000", "1111111110111001", "1111111110111010", "1111111110111011", "1111111110111100", "1111111110111101"},
                {"", "111111001", "1111111110111110", "1111111110111111", "1111111111000000", "1111111111000001", "1111111111000010", "1111111111000011", "1111111111000100", "1111111111000101", "1111111111000110"},
                {"", "111111010", "1111111111000111", "1111111111001000", "1111111111001001", "1111111111001010", "1111111111001011", "1111111111001100", "1111111111001101", "1111111111001110", "1111111111001111"},
                {"", "1111111001", "1111111111010000", "1111111111010001", "1111111111010010", "1111111111010011", "1111111111010100", "1111111111010101", "1111111111010110", "1111111111010111", "1111111111011000"},
                {"", "1111111010", "1111111111011001", "1111111111011010", "1111111111011011", "1111111111011100", "1111111111011101", "1111111111011110", "1111111111011111", "1111111111100000", "1111111111100001"},
                {"", "11111111000", "1111111111100010", "1111111111100011", "1111111111100100", "1111111111100101", "1111111111100110", "1111111111100111", "1111111111101000", "1111111111101001", "1111111111101010"},
                {"", "1111111111101011", "1111111111101100", "1111111111101101", "1111111111101110", "1111111111101111", "1111111111110000", "1111111111110001", "1111111111110010", "1111111111110011", "1111111111110100"},
                {"11111111001", "1111111111110101", "1111111111110110", "1111111111110111", "1111111111111000", "1111111111111001", "1111111111111010", "1111111111111011", "1111111111111100", "1111111111111101", "1111111111111110"}
        };
        ACInverseHuffmanTable = new HashMap<String, Pair>() {{
            put("1010", new Pair((byte) 0, (byte) 0)); put("00", new Pair((byte) 0, (byte) 1)); put("01", new Pair((byte) 0, (byte) 2)); put("100", new Pair((byte) 0, (byte) 3)); put("1011", new Pair((byte) 0, (byte) 4)); put("11010", new Pair((byte) 0, (byte) 5)); put("1111000", new Pair((byte) 0, (byte) 6)); put("11111000", new Pair((byte) 0, (byte) 7)); put("1111110110", new Pair((byte) 0, (byte) 8)); put("1111111110000010", new Pair((byte) 0, (byte) 9)); put("1111111110000011", new Pair((byte) 0, (byte) 10));
            put("1100", new Pair((byte) 1, (byte) 1)); put("11011", new Pair((byte) 1, (byte) 2)); put("1111001", new Pair((byte) 1, (byte) 3)); put("111110110", new Pair((byte) 1, (byte) 4)); put("11111110110", new Pair((byte) 1, (byte) 5)); put("1111111110000100", new Pair((byte) 1, (byte) 6)); put("1111111110000101", new Pair((byte) 1, (byte) 7)); put("1111111110000110", new Pair((byte) 1, (byte) 8)); put("1111111110000111", new Pair((byte) 1, (byte) 9)); put("1111111110001000", new Pair((byte) 1, (byte) 10));
            put("11100", new Pair((byte) 2, (byte) 1)); put("11111001", new Pair((byte) 2, (byte) 2)); put("1111110111", new Pair((byte) 2, (byte) 3)); put("111111110100", new Pair((byte) 2, (byte) 4)); put("1111111110001001", new Pair((byte) 2, (byte) 5)); put("1111111110001010", new Pair((byte) 2, (byte) 6)); put("1111111110001011", new Pair((byte) 2, (byte) 7)); put("1111111110001100", new Pair((byte) 2, (byte) 8)); put("1111111110001101", new Pair((byte) 2, (byte) 9)); put("1111111110001110", new Pair((byte) 2, (byte) 10));
            put("111010", new Pair((byte) 3, (byte) 1)); put("111110111", new Pair((byte) 3, (byte) 2)); put("111111110101", new Pair((byte) 3, (byte) 3)); put("1111111110001111", new Pair((byte) 3, (byte) 4)); put("1111111110010000", new Pair((byte) 3, (byte) 5)); put("1111111110010001", new Pair((byte) 3, (byte) 6)); put("1111111110010010", new Pair((byte) 3, (byte) 7)); put("1111111110010011", new Pair((byte) 3, (byte) 8)); put("1111111110010100", new Pair((byte) 3, (byte) 9)); put("1111111110010101", new Pair((byte) 3, (byte) 10));
            put("111011", new Pair((byte) 4, (byte) 1)); put("1111111000", new Pair((byte) 4, (byte) 2)); put("1111111110010110", new Pair((byte) 4, (byte) 3)); put("1111111110010111", new Pair((byte) 4, (byte) 4)); put("1111111110011000", new Pair((byte) 4, (byte) 5)); put("1111111110011001", new Pair((byte) 4, (byte) 6)); put("1111111110011010", new Pair((byte) 4, (byte) 7)); put("1111111110011011", new Pair((byte) 4, (byte) 8)); put("1111111110011100", new Pair((byte) 4, (byte) 9)); put("1111111110011101", new Pair((byte) 4, (byte) 10));
            put("1111010", new Pair((byte) 5, (byte) 1)); put("11111110111", new Pair((byte) 5, (byte) 2)); put("1111111110011110", new Pair((byte) 5, (byte) 3)); put("1111111110011111", new Pair((byte) 5, (byte) 4)); put("1111111110100000", new Pair((byte) 5, (byte) 5)); put("1111111110100001", new Pair((byte) 5, (byte) 6)); put("1111111110100010", new Pair((byte) 5, (byte) 7)); put("1111111110100011", new Pair((byte) 5, (byte) 8)); put("1111111110100100", new Pair((byte) 5, (byte) 9)); put("1111111110100101", new Pair((byte) 5, (byte) 10));
            put("1111011", new Pair((byte) 6, (byte) 1)); put("111111110110", new Pair((byte) 6, (byte) 2)); put("1111111110100110", new Pair((byte) 6, (byte) 3)); put("1111111110100111", new Pair((byte) 6, (byte) 4)); put("1111111110101000", new Pair((byte) 6, (byte) 5)); put("1111111110101001", new Pair((byte) 6, (byte) 6)); put("1111111110101010", new Pair((byte) 6, (byte) 7)); put("1111111110101011", new Pair((byte) 6, (byte) 8)); put("1111111110101100", new Pair((byte) 6, (byte) 9)); put("1111111110101101", new Pair((byte) 6, (byte) 10));
            put("11111010", new Pair((byte) 7, (byte) 1)); put("111111110111", new Pair((byte) 7, (byte) 2)); put("1111111110101110", new Pair((byte) 7, (byte) 3)); put("1111111110101111", new Pair((byte) 7, (byte) 4)); put("1111111110110000", new Pair((byte) 7, (byte) 5)); put("1111111110110001", new Pair((byte) 7, (byte) 6)); put("1111111110110010", new Pair((byte) 7, (byte) 7)); put("1111111110110011", new Pair((byte) 7, (byte) 8)); put("1111111110110100", new Pair((byte) 7, (byte) 9)); put("1111111110110101", new Pair((byte) 7, (byte) 10));
            put("111111000", new Pair((byte) 8, (byte) 1)); put("111111111000000", new Pair((byte) 8, (byte) 2)); put("1111111110110110", new Pair((byte) 8, (byte) 3)); put("1111111110110111", new Pair((byte) 8, (byte) 4)); put("1111111110111000", new Pair((byte) 8, (byte) 5)); put("1111111110111001", new Pair((byte) 8, (byte) 6)); put("1111111110111010", new Pair((byte) 8, (byte) 7)); put("1111111110111011", new Pair((byte) 8, (byte) 8)); put("1111111110111100", new Pair((byte) 8, (byte) 9)); put("1111111110111101", new Pair((byte) 8, (byte) 10));
            put("111111001", new Pair((byte) 9, (byte) 1)); put("1111111110111110", new Pair((byte) 9, (byte) 2)); put("1111111110111111", new Pair((byte) 9, (byte) 3)); put("1111111111000000", new Pair((byte) 9, (byte) 4)); put("1111111111000001", new Pair((byte) 9, (byte) 5)); put("1111111111000010", new Pair((byte) 9, (byte) 6)); put("1111111111000011", new Pair((byte) 9, (byte) 7)); put("1111111111000100", new Pair((byte) 9, (byte) 8)); put("1111111111000101", new Pair((byte) 9, (byte) 9)); put("1111111111000110", new Pair((byte) 9, (byte) 10));
            put("111111010", new Pair((byte) 10, (byte) 1)); put("1111111111000111", new Pair((byte) 10, (byte) 2)); put("1111111111001000", new Pair((byte) 10, (byte) 3)); put("1111111111001001", new Pair((byte) 10, (byte) 4)); put("1111111111001010", new Pair((byte) 10, (byte) 5)); put("1111111111001011", new Pair((byte) 10, (byte) 6)); put("1111111111001100", new Pair((byte) 10, (byte) 7)); put("1111111111001101", new Pair((byte) 10, (byte) 8)); put("1111111111001110", new Pair((byte) 10, (byte) 9)); put("1111111111001111", new Pair((byte) 10, (byte) 10));
            put("1111111001", new Pair((byte) 11, (byte) 1)); put("1111111111010000", new Pair((byte) 11, (byte) 2)); put("1111111111010001", new Pair((byte) 11, (byte) 3)); put("1111111111010010", new Pair((byte) 11, (byte) 4)); put("1111111111010011", new Pair((byte) 11, (byte) 5)); put("1111111111010100", new Pair((byte) 11, (byte) 6)); put("1111111111010101", new Pair((byte) 11, (byte) 7)); put("1111111111010110", new Pair((byte) 11, (byte) 8)); put("1111111111010111", new Pair((byte) 11, (byte) 9)); put("1111111111011000", new Pair((byte) 11, (byte) 10));
            put("1111111010", new Pair((byte) 12, (byte) 1)); put("1111111111011001", new Pair((byte) 12, (byte) 2)); put("1111111111011010", new Pair((byte) 12, (byte) 3)); put("1111111111011011", new Pair((byte) 12, (byte) 4)); put("1111111111011100", new Pair((byte) 12, (byte) 5)); put("1111111111011101", new Pair((byte) 12, (byte) 6)); put("1111111111011110", new Pair((byte) 12, (byte) 7)); put("1111111111011111", new Pair((byte) 12, (byte) 8)); put("1111111111100000", new Pair((byte) 12, (byte) 9)); put("1111111111100001", new Pair((byte) 12, (byte) 10));
            put("11111111000", new Pair((byte) 13, (byte) 1)); put("1111111111100010", new Pair((byte) 13, (byte) 2)); put("1111111111100011", new Pair((byte) 13, (byte) 3)); put("1111111111100100", new Pair((byte) 13, (byte) 4)); put("1111111111100101", new Pair((byte) 13, (byte) 5)); put("1111111111100110", new Pair((byte) 13, (byte) 6)); put("1111111111100111", new Pair((byte) 13, (byte) 7)); put("1111111111101000", new Pair((byte) 13, (byte) 8)); put("1111111111101001", new Pair((byte) 13, (byte) 9)); put("1111111111101010", new Pair((byte) 13, (byte) 10));
            put("1111111111101011", new Pair((byte) 14, (byte) 1)); put("1111111111101100", new Pair((byte) 14, (byte) 2)); put("1111111111101101", new Pair((byte) 14, (byte) 3)); put("1111111111101110", new Pair((byte) 14, (byte) 4)); put("1111111111101111", new Pair((byte) 14, (byte) 5)); put("1111111111110000", new Pair((byte) 14, (byte) 6)); put("1111111111110001", new Pair((byte) 14, (byte) 7)); put("1111111111110010", new Pair((byte) 14, (byte) 8)); put("1111111111110011", new Pair((byte) 14, (byte) 9)); put("1111111111110100", new Pair((byte) 14, (byte) 10));
            put("11111111001", new Pair((byte) 15, (byte) 0)); put("1111111111110101", new Pair((byte) 15, (byte) 1)); put("1111111111110110", new Pair((byte) 15, (byte) 2)); put("1111111111110111", new Pair((byte) 15, (byte) 3)); put("1111111111111000", new Pair((byte) 15, (byte) 4)); put("1111111111111001", new Pair((byte) 15, (byte) 5)); put("1111111111111010", new Pair((byte) 15, (byte) 6)); put("1111111111111011", new Pair((byte) 15, (byte) 7)); put("1111111111111100", new Pair((byte) 15, (byte) 8)); put("1111111111111101", new Pair((byte) 15, (byte) 9)); put("1111111111111110", new Pair((byte) 15, (byte) 10));
        }};

        ACHuffmanTableChrominance = new String[][] {
                {"00","01","100","1010","11000","11001","111000","1111000","111110100","1111110110","111111110100"},
                {"","1011","111001","11110110","111110101","11111110110","111111110101","1111111110001000","1111111110001001","1111111110001010","1111111110001011"},
                {"","11010","11110111","1111110111","111111110110","111111111000010","1111111110001100","1111111110001101","1111111110001110","1111111110001111","1111111110010000"},
                {"","11011","11111000","1111111000","111111110111","1111111110010001","1111111110010010","1111111110010011","1111111110010100","1111111110010101","1111111110010110"},
                {"","111010","111110110","1111111110010111","1111111110011000","1111111110011001","1111111110011010","1111111110011011","1111111110011100","1111111110011101","1111111110011110"},
                {"","111011","1111111001","1111111110011111","1111111110100000","1111111110100001","1111111110100010","1111111110100011","1111111110100100","1111111110100101","1111111110100110"},
                {"","1111001","11111110111","1111111110100111","1111111110101000","1111111110101001","1111111110101010","1111111110101011","1111111110101100","1111111110101101","1111111110101110"},
                {"", "1111010", "11111111000", "1111111110101111", "1111111110110000", "1111111110110001", "1111111110110010", "1111111110110011", "1111111110110100", "1111111110110101", "1111111110110110"},
                {"", "11111001", "1111111110110111", "1111111110111000", "1111111110111001", "1111111110111010", "1111111110111011", "1111111110111100", "1111111110111101", "1111111110111110", "1111111110111111"},
                {"", "111110111", "1111111111000000", "1111111111000001", "1111111111000010", "1111111111000011", "1111111111000100", "1111111111000101", "1111111111000110", "1111111111000111", "1111111111001000"},
                {"", "111111000", "1111111111001001", "1111111111001010", "1111111111001011", "1111111111001100", "1111111111001101", "1111111111001110", "1111111111001111", "1111111111010000", "1111111111010001"},
                {"", "111111001", "1111111111010010", "1111111111010011", "1111111111010100", "1111111111010101", "1111111111010110", "1111111111010111", "1111111111011000", "1111111111011001", "1111111111011010"},
                {"", "111111010", "1111111111011011", "1111111111011100", "1111111111011101", "1111111111011110", "1111111111011111", "1111111111100000", "1111111111100001", "1111111111100010", "1111111111100011"},
                {"", "11111111001", "1111111111100100", "1111111111100101", "1111111111100110", "1111111111100111", "1111111111101000", "1111111111101001", "1111111111101010", "1111111111101011", "1111111111101100"},
                {"", "11111111100000", "1111111111101101", "1111111111101110", "1111111111101111", "1111111111110000", "1111111111110001", "1111111111110010", "1111111111110011", "1111111111110100", "1111111111110101"},
                {"1111111010", "111111111000011", "1111111111110110", "1111111111110111", "1111111111111000", "1111111111111001", "1111111111111010", "1111111111111011", "1111111111111100", "1111111111111101", "1111111111111110"},
        };
    }

    /**
     * Cuenta el número de bits mínimos con los que representar el número pasado por parámetro
     * @param numero El número del cual queremos saber su tamaño en bits
     * @return El número de bits mínimos con los que representar el número pasado por parámetro
     */
    private static byte bitsNumero(byte numero) {
        byte bits = 0;
        int numeroUnsigned = 0xFF & numero; //avoiding shift logical malfunction with byte type
        while (numeroUnsigned != 0) { //counting minimum amount of bits to represent number passed as parameter
            bits++;
            numeroUnsigned >>>= 1;
        }
        return bits;
    }

    /**
     * Convierte un binary string en un byte array
     * <p>
     *     El binary string pasado por parámetro sólo contiene los caracteres '1' y '0'
     * </p>
     * @param binaryString El binary string en cuestión
     * @return El byte list deseado
     */
    private List<Byte> toByteList(String binaryString) {
        List<Byte> l = new ArrayList<>();
        byte n = 0;
        for (int i = 0; i < binaryString.length(); ++i) { //for each 8 chars, being '1' or '0', one byte is created and added to the byte list
            if (binaryString.charAt(i) == '1') n++;
            if (i % 8 != 7) n<<=1;
            else {
                l.add(n);
                n = 0;
            }
        }
        if (binaryString.length() % 8 != 0) { //if there remains some chars as offset of last byte, the highest '1' is shifted till it is the 8th highest value bit of the byte, and then the byte is appended to the list
            for (int i = 1; i < (8 - binaryString.length() % 8); ++i) n <<= 1;
            l.add(n);
        }
        return l;
    }

    /**
     * Asigna un valor de calidad de compresión al Singleton de JPEG
     * <p>
     *     Su valor se sitúa entre 1 y 7. En caso contrario de corrige al valor extremo más cercano
     * </p>
     * @param calidad La calidad de compresión de JPEG. A mayor valor, más alta será la calidad de la compresión.
     */
    public void setCalidad(int calidad) {
        if (calidad < 1) calidad = 1; //narrowing out-of-bounds quality preset to nearest value
        else if (calidad > 7) calidad = 7;
        if (calidad > 5) this.calidad = (10.0 - (double)calidad) / 5.0; //calculating new quality scalar and setting it
        else this.calidad = 5.0 / (double)calidad;
        calidadHeader = calidad; //setting new quality percentage (the one passed as parameter)
    }

    /**
     * Enumeration.Algoritmo de compresión JPEG
     * <p>
     *     La imagen de entrada tiene que ser .ppm con magic number P6 y valor máximo de RGB 255
     * </p>
     * @param datosInput Un byte array conteniente de una imagen a comprimir
     * @return Un byte array conteniente de una imagen comprimida
     * @throws FormatoErroneoException El formato en el que está codificada la imagen .ppm no es correcto
     */
    @Override
    public OutputAlgoritmo comprimir(byte[] datosInput) throws FormatoErroneoException {
        long startTime = System.nanoTime(); //starting time
        List<Byte> result = new ArrayList<>(); //data will be written here before passing it into output byte array
        int pos = 0, width, height;
        DatosHeaderComp datosHeaderComp = new DatosHeaderComp(datosInput, result, pos).readHeader();
        pos = datosHeaderComp.getPos();
        width = datosHeaderComp.getWidth();
        height = datosHeaderComp.getHeight();

        //start of pixelmap reading
        if (width * height > (datosInput.length - pos) / 3 || width * height < (datosInput.length - pos) / 3) throw new FormatoErroneoException("El formato de .ppm no es correcto!");
        int paddedWidth, paddedHeight;
        if (width % 8 != 0) paddedWidth = width + (8 - width % 8);
        else paddedWidth = width;
        if (height % 8 != 0) paddedHeight = height + (8 - height % 8);
        else paddedHeight = height;
        double[][] Y = new double[paddedHeight][paddedWidth];
        double[][] Cb = new double[paddedHeight][paddedWidth];
        double[][] Cr = new double[paddedHeight][paddedWidth];
        readY(datosInput, pos, width, height, paddedWidth, paddedHeight, Y, Cb, Cr);
        //end of pixelmap reading

        //start of image compression
        int downSampledPaddedHeight, downSampledPaddedWidth;
        if ((paddedHeight/2) % 8 == 0) downSampledPaddedHeight = paddedHeight/2;
        else downSampledPaddedHeight = (paddedHeight/2) + 4;
        if ((paddedWidth/2) % 8 == 0) downSampledPaddedWidth = paddedWidth/2;
        else downSampledPaddedWidth = (paddedWidth/2) + 4;
        double[][] downSampledCb = new double[downSampledPaddedHeight][downSampledPaddedWidth];
        double[][] downSampledCr = new double[downSampledPaddedHeight][downSampledPaddedWidth];
        downSamplingCbCr(paddedWidth, paddedHeight, Cb, Cr, downSampledPaddedHeight, downSampledPaddedWidth, downSampledCb, downSampledCr);

        List<Byte>[][] tempResultY = new List[paddedHeight/8][paddedWidth/8];
        procesarY(paddedWidth, paddedHeight, Y, tempResultY);
        List<Byte>[][] tempResultCbCr = new List[downSampledPaddedHeight/8][downSampledPaddedWidth/8];
        procesarCbCr(downSampledPaddedHeight, downSampledPaddedWidth, downSampledCb, downSampledCr, tempResultCbCr);
        //end of image compression

        escribirResult(result, paddedWidth, paddedHeight, downSampledPaddedHeight, downSampledPaddedWidth, tempResultY, tempResultCbCr);
        byte[] datosOutput = new byte[result.size()]; //writting image data in output byte array
        escribirDatosOutput(result, datosOutput);
        long endTime = System.nanoTime(), totalTime = endTime - startTime; //ending time and total execution time
        return new OutputAlgoritmo(totalTime, datosOutput); //returning output time and data byte array
    }

    /**
     * Escribe el resultado del procesado de la imagen en el byte array de salida.
     * @param result Datos procesados de la imagen.
     * @param datosOutput Byte array donde se escrive definitivamente el resultado del algoritmo.
     */
    private void escribirDatosOutput(List<Byte> result, byte[] datosOutput) {
        for (int i = 0; i < result.size(); ++i) {
            datosOutput[i] = result.get(i);
        }
    }

    /**
     * Escribe los resultados temporales de cada algoritmo en el resultado definitivo.
     * @param result Datos procesados y en orden correcto de la imagen.
     * @param paddedWidth Ancho de las dimensiones de la imagen múltiplo de 8 (para poder calcular DCT).
     * @param paddedHeight Alto de las dimensiones de la imagen múltiplo de 8 (para poder calcular DCT).
     * @param downSampledPaddedHeight Ancho de las dimensiones downsampled de la imagen múltiplo de 8 (para poder calcular DCT).
     * @param downSampledPaddedWidth Ancho de las dimensiones downsampled de la imagen múltiplo de 8 (para poder calcular DCT).
     * @param tempResultY Resultado temporal de los valores de la luminancia de cada bloque procesados.
     * @param tempResultCbCr Resultado temporal de los valores de la crominancia de cada bloque procesados.
     */
    private void escribirResult(List<Byte> result, int paddedWidth, int paddedHeight, int downSampledPaddedHeight, int downSampledPaddedWidth, List<Byte>[][] tempResultY, List<Byte>[][] tempResultCbCr) {
        for (int x = 0; x < paddedHeight/8; ++x) {
            for (int y = 0; y < paddedWidth/8; ++y) {
                result.addAll(tempResultY[x][y]);
            }
        }
        for (int x = 0; x < downSampledPaddedHeight/8; ++x) {
            for (int y = 0; y < downSampledPaddedWidth/8; ++y) {
                result.addAll(tempResultCbCr[x][y]);
            }
        }
    }

    /**
     * Cálculo de DCT, RLE y Huffman coding de las componentes de crominancia (CbCr) de la imagen.
     * @param downSampledPaddedHeight Ancho de las dimensiones downsampled de la imagen múltiplo de 8 (para poder calcular DCT).
     * @param downSampledPaddedWidth Ancho de las dimensiones downsampled de la imagen múltiplo de 8 (para poder calcular DCT).
     * @param downSampledCb Array de valores de la crominacia azul de la imagen tras el downsampling.
     * @param downSampledCr Array de valores de la crominacia roja de la imagen tras el downsampling.
     * @param tempResultCbCr Resultado temporal de los valores de la crominancia de cada bloque procesados.
     */
    private void procesarCbCr(int downSampledPaddedHeight, int downSampledPaddedWidth, double[][] downSampledCb, double[][] downSampledCr, List<Byte>[][] tempResultCbCr) {
        IntStream.range(0, downSampledPaddedHeight).parallel().filter(x -> x % 8 == 0).forEach(x -> { //image DCT-II and quantization (done in pixel squares of 8x8) for chrominance
            IntStream.range(0, downSampledPaddedWidth).parallel().filter(y -> y % 8 == 0).forEach(y -> { //for each chrominance pixel square of 8x8 of the image, DCT-II algorithm is applied, letting calculate the image frequencies
                double[][] buffCb = new double[8][8];
                double[][] buffCr = new double[8][8];
                tempResultCbCr[x /8][y/8] = new ArrayList<>();
                int topu = x + 8, topv = y + 8;
                DCT_II_CbCr(downSampledCb, downSampledCr, x, y, buffCb, buffCr, topu, topv);
                byte[] lineCb = new byte[64]; //linear vector for zigzagged elements of Cb before RLE
                byte[] lineCr = new byte[64]; //linear vector for zigzagged elements of Cr before RLE
                zigZagCbCr(x, y, buffCb, buffCr, topu, topv, lineCb, lineCr);
                String rleCb = ""; //RLE: lossless compression of 8x8 block values
                rleCb = RLE_Huffman(lineCb, rleCb);
                int sizeOfBlock = rleCb.length() / 8; //header of 8x8 Huffman block
                int offsetOfBlock = rleCb.length() % 8;
                if (rleCb.length() % 8 != 0) sizeOfBlock++; //if there is offset, then there is another byte to be added
                tempResultCbCr[x /8][y/8].add((byte)sizeOfBlock); //size in bits of block defined before reading each block in order to know how many bytes have to be read
                tempResultCbCr[x /8][y/8].add((byte)offsetOfBlock);
                //addition of block to result
                tempResultCbCr[x /8][y/8].addAll(toByteList(rleCb)); //dumping the Huffman block into result
                String rleCr = ""; //RLE: lossless compression of 8x8 block values
                rleCr = RLE_Huffman(lineCr, rleCr);
                sizeOfBlock = rleCr.length() / 8; //header of 8x8 Huffman block
                offsetOfBlock = rleCr.length() % 8;
                if (rleCr.length() % 8 != 0) sizeOfBlock++; //if there is offset, then there is another byte to be added
                tempResultCbCr[x /8][y/8].add((byte)sizeOfBlock); //size in bits of block defined before reading each block in order to know how many bytes have to be read
                tempResultCbCr[x /8][y/8].add((byte)offsetOfBlock);
                //addition of block to result
                tempResultCbCr[x /8][y/8].addAll(toByteList(rleCr)); //dumping the Huffman block into result
            });
        });
    }

    /**
     * Aplica el algoritmo DCT-II a la crominancia en compresión.
     * @param downSampledCb Array de valores de la crominacia azul de la imagen tras el downsampling.
     * @param downSampledCr Array de valores de la crominacia roja de la imagen tras el downsampling.
     * @param x Posición en el eje x en Cb y Cr.
     * @param y Posición en el eje y en Cb y Cr.
     * @param buffCb Buffer temporal del bloque 8x8 donde se guardan los cálculos de DCT antes de escribir los definitivos en Cb.
     * @param buffCr Buffer temporal del bloque 8x8 donde se guardan los cálculos de DCT antes de escribir los definitivos en Cr.
     * @param topu Posición límite del bloque en el eje x en Y (es x + 8).
     * @param topv Posición límite del bloque en el eje y en Y (es y + 8).
     */
    private void DCT_II_CbCr(double[][] downSampledCb, double[][] downSampledCr, int x, int y, double[][] buffCb, double[][] buffCr, int topu, int topv) {
        IntStream.range(x, topu).parallel().forEach(u -> {
            double alphau, alphav, cosu, cosv;
            if (u % 8 == 0) alphau = 1 / Math.sqrt(2);
            else alphau = 1;
            for (int v = y; v < topv; ++v) { //for each chrominance pixel of the 8x8 square, the DCT-II calculation is applied
                if (v % 8 == 0) alphav = 1 / Math.sqrt(2);
                else alphav = 1;
                buffCb[u%8][v%8] = 0;
                buffCr[u%8][v%8] = 0;
                for (int i = x; i < topu; ++i) {
                    cosu = Math.cos(((2 * (i % 8) + 1) * (u % 8) * Math.PI) / 16.0);
                    for (int j = y; j < topv; ++j) {
                        cosv = Math.cos(((2 * (j % 8) + 1) * (v % 8) * Math.PI) / 16.0);
                        buffCb[u%8][v%8] += downSampledCb[i][j] * cosu * cosv;
                        buffCr[u%8][v%8] += downSampledCr[i][j] * cosu * cosv;
                    }
                }
                buffCb[u%8][v%8] *= (alphau * alphav * 0.25);
                buffCb[u%8][v%8] /= (ChrominanceQuantizationTable[u%8][v%8] * calidad);
                buffCr[u%8][v%8] *= (alphau * alphav * 0.25);
                buffCr[u%8][v%8] /= (ChrominanceQuantizationTable[u%8][v%8] * calidad);
            }
        });
    }

    /**
     * Algoritmo de zig zag, que convierte el bloque 8x8 en una línea tras leerlo en zig zag.
     * @param x Posición en el eje x en Cb y Cr.
     * @param y Posición en el eje y en Cb y Cr.
     * @param buffCb Buffer temporal del bloque 8x8 donde se guardan los cálculos de DCT antes de escribir los definitivos en Cb.
     * @param buffCr Buffer temporal del bloque 8x8 donde se guardan los cálculos de DCT antes de escribir los definitivos en Cr.
     * @param topu Posición límite del bloque en el eje x en Y (es x + 8).
     * @param topv Posición límite del bloque en el eje y en Y (es y + 8).
     * @param lineCb Valores de la crominancia azul a pasar al bloque 8x8.
     * @param lineCr Valores de la crominancia roja a pasar al bloque 8x8.
     */
    private void zigZagCbCr(int x, int y, double[][] buffCb, double[][] buffCr, int topu, int topv, byte[] lineCb, byte[] lineCr) {
        boolean up = true;
        int i = x, j = y, it = 0;
        while (i < topu && j < topv) { //zig-zag, RLE and Huffman of Chrominance 8x8 square
            lineCb[it] = (byte)Math.round(buffCb[i%8][j%8]);
            lineCr[it++] = (byte)Math.round(buffCr[i%8][j%8]);
            if (i == x && j != topv - 1 && up) {
                ++j;
                up = false;
            }
            else if (j == y && i != topu - 1 && !up) {
                ++i;
                up = true;
            }
            else if (i == topu - 1 && j != topv - 1 && !up) {
                ++j;
                up = true;
            }
            else if (j == topv - 1 && i != topu - 1 && up) {
                ++i;
                up = false;
            }
            else if (i == topu - 1 && j == topv - 1) {
                ++i;
                ++j;
            }
            else if (up) {
                --i;
                ++j;
            }
            else {
                ++i;
                --j;
            }
        }
    }

    /**
     * Cálculo de DCT, RLE y Huffman coding de la componente de luminancia (Y) de la imagen.
     * @param paddedWidth Ancho de las dimensiones de la imagen múltiplo de 8 (para poder calcular DCT).
     * @param paddedHeight Alto de las dimensiones de la imagen múltiplo de 8 (para poder calcular DCT).
     * @param Y Array de valores de la luminancia de la imagen.
     * @param tempResultY Resultado temporal de los valores de la luminancia de cada bloque procesados.
     */
    private void procesarY(int paddedWidth, int paddedHeight, double[][] Y, List<Byte>[][] tempResultY) {
        IntStream.range(0, paddedHeight).parallel().filter(x -> x % 8 == 0).forEach(x -> { //image DCT-II and quantization (done in pixel squares of 8x8) for luminance
            IntStream.range(0, paddedWidth).parallel().filter(y -> y % 8 == 0).forEach(y -> {
                double[][] buffY = new double[8][8];
                tempResultY[x /8][y/8] = new ArrayList<>();
                int topu = x + 8, topv = y + 8;
                DCT_II_Y(Y, x, y, buffY, topu, topv);
                byte[] lineY = new byte[64]; //linear vector for zigzagged elements of Y before RLE
                zigZagY(x, y, buffY, topu, topv, lineY);
                String rleY = ""; //RLE: lossless compression of 8x8 block values
                rleY = RLE_Huffman(lineY, rleY);
                int sizeOfBlock = rleY.length() / 8; //header of 8x8 Huffman block
                int offsetOfBlock = rleY.length() % 8;
                if (rleY.length() % 8 != 0) sizeOfBlock++; //if there is offset, then there is another byte to be added
                tempResultY[x /8][y/8].add((byte)sizeOfBlock); //size in bits of block defined before reading each block in order to know how many bytes have to be read
                tempResultY[x /8][y/8].add((byte)offsetOfBlock);
                //addition of block to result
                tempResultY[x /8][y/8].addAll(toByteList(rleY)); //dumping the Huffman block into result
            });
        });
    }

    /**
     * Algoritmo RLE y Huffman para comprimir imágenes lossles.
     * @param lineY Datos de la imagen a codificar.
     * @param rleY Datos de la imagen codificados con RLE y Huffman Coding.
     * @return Los datos de la imagen codificados con RLE y Huffman Coding.
     */
    private String RLE_Huffman(byte[] lineY, String rleY) {
        byte howManyZeroes = 0; //how many zeroes have been ignored until a non zero value found in 8x8 block
        for (int k = 0; k < 64; ++k) {
            if (lineY[k] == 0) {
                howManyZeroes++;
                if (howManyZeroes > 15) k = 64;
            } else {
                //rle refines that each time a non zero value is found, is written how many zeroes have been ignored before and the size of the value in bits
                rleY = rleY.concat(ACHuffmanTableLuminance[howManyZeroes][bitsNumero(lineY[k])]); //substitution of those 2 values for Huffman value
                rleY = rleY.concat(Integer.toBinaryString(lineY[k] & 0xFF)); //then the non zero value is written (only 8 bits or less, the minimum possible to represent its value)
                howManyZeroes = 0;
            }
        }
        rleY = rleY.concat(ACHuffmanTableLuminance[0][0]); //end of block: (0,0)
        return rleY;
    }

    /**
     * Algoritmo de zig zag, que convierte el bloque 8x8 en una línea tras leerlo en zig zag.
     * @param x x Posición en el eje x en Y.
     * @param y Posición en el eje y en Y.
     * @param buffY Buffer temporal del bloque 8x8 donde se guardan los cálculos de DCT antes de escribir los definitivos en Y.
     * @param topu Posición límite del bloque en el eje x en Y (es x + 8).
     * @param topv Posición límite del bloque en el eje y en Y (es y + 8).
     * @param lineY Valores de la luminancia a pasar al bloque 8x8.
     */
    private void zigZagY(int x, int y, double[][] buffY, int topu, int topv, byte[] lineY) {
        boolean up = true;
        int i = x, j = y, it = 0;
        while (i < topu && j < topv) { //zig-zag, RLE and Huffman Coding of Luminance 8x8 square
            lineY[it++] = (byte)Math.round(buffY[i%8][j%8]); //.imgc extension determines that the pixelmap will contain first the luminance pixelmap and then the chrominance one
            if (i == x && j != topv - 1 && up) {
                ++j;
                up = false;
            }
            else if (j == y && i != topu - 1 && !up) {
                ++i;
                up = true;
            }
            else if (i == topu - 1 && j != topv - 1 && !up) {
                ++j;
                up = true;
            }
            else if (j == topv - 1 && i != topu - 1 && up) {
                ++i;
                up = false;
            }
            else if (i == topu - 1 && j == topv - 1) {
                ++i;
                ++j;
            }
            else if (up) {
                --i;
                ++j;
            }
            else {
                ++i;
                --j;
            }
        }
    }

    /**
     * Aplica el algoritmo DCT-II a la luminancia en compresión.
     * @param Y Componente de luminancia de JPEG.
     * @param x x Posición en el eje x en Y.
     * @param y Posición en el eje y en Y.
     * @param buffY Buffer temporal del bloque 8x8 donde se guardan los cálculos de DCT antes de escribir los definitivos en Y.
     * @param topu Posición límite del bloque en el eje x en Y (es x + 8).
     * @param topv Posición límite del bloque en el eje y en Y (es y + 8).
     */
    private void DCT_II_Y(double[][] Y, int x, int y, double[][] buffY, int topu, int topv) {
        IntStream.range(x, topu).parallel().forEach(u -> {
            double alphau, alphav, cosu, cosv;
            if (u % 8 == 0) alphau = 1 / Math.sqrt(2);
            else alphau = 1;
            for (int v = y; v < topv; ++v) { //for each luminance pixel of the 8x8 square, the DCT-II calculation is applied
                if (v % 8 == 0) alphav = 1 / Math.sqrt(2);
                else alphav = 1;
                buffY[u%8][v%8] = 0;
                for (int i = x; i < topu; ++i) {
                    cosu = Math.cos(((2 * (i % 8) + 1) * (u % 8) * Math.PI) / 16.0);
                    for (int j = y; j < topv; ++j) {
                        cosv = Math.cos(((2 * (j % 8) + 1) * (v % 8) * Math.PI) / 16.0);
                        buffY[u%8][v%8] += Y[i][j] * cosu * cosv;
                    }
                }
                buffY[u%8][v%8] *= (alphau * alphav * 0.25);
                buffY[u%8][v%8] /= (LuminanceQuantizationTable[u%8][v%8] * calidad);
            }
        });
    }

    /**
     * Downsampling de la crominancia de la imagen, el cual aporta entre un 42 y un 50% de compresión de la imagen.
     * <p>
     *     Se aplica un downsampling de 4:2:0.
     * </p>
     * @param paddedWidth Ancho de las dimensiones de la imagen múltiplo de 8 (para poder calcular DCT).
     * @param paddedHeight Alto de las dimensiones de la imagen múltiplo de 8 (para poder calcular DCT).
     * @param Cb Array de valores de la crominacia azul de la imagen.
     * @param Cr Array de valores de la crominacia roja de la imagen.
     * @param downSampledPaddedHeight Ancho de las dimensiones downsampled de la imagen múltiplo de 8 (para poder calcular DCT).
     * @param downSampledPaddedWidth Ancho de las dimensiones downsampled de la imagen múltiplo de 8 (para poder calcular DCT).
     * @param downSampledCb Array de valores de la crominacia azul de la imagen tras el downsampling.
     * @param downSampledCr Array de valores de la crominacia roja de la imagen tras el downsampling.
     */
    private void downSamplingCbCr(int paddedWidth, int paddedHeight, double[][] Cb, double[][] Cr, int downSampledPaddedHeight, int downSampledPaddedWidth, double[][] downSampledCb, double[][] downSampledCr) {
        for (int x = 0; x < paddedHeight; x += 2) { //Chrominance DownSampled to 25% each colour channel. Compressed image will be arround 50% less large than original thanks to this
            for (int y = 0; y < paddedWidth; y += 2) { //each color channel is extended to have multiple of 8 dimensions, which is needed to apply DCT-II
                if (x < paddedHeight - 2 && y < paddedWidth - 2) {
                    downSampledCb[x/2][y/2] = (Cb[x][y] + Cb[x][y+1] + Cb[x+1][y] + Cb[x+1][y+1]) / 4;
                    downSampledCr[x/2][y/2] = (Cr[x][y] + Cr[x][y+1] + Cr[x+1][y] + Cr[x+1][y+1]) / 4;
                } //when on image bounds, boundary values are extended till the image has multiple of 8 dimensions (image will be stored with multiple of 8 dimensions to ease decompression)
                else if (x < paddedHeight - 2 && y == paddedWidth - 2) {
                    downSampledCb[x/2][y/2] = (Cb[x][y] + Cb[x+1][y]) / 2;
                    downSampledCr[x/2][y/2] = (Cr[x][y] + Cr[x+1][y]) / 2;
                    for (int j = (y/2)+1; j < downSampledPaddedWidth; ++j) {
                        downSampledCb[x/2][j] = downSampledCb[x/2][y/2];
                        downSampledCr[x/2][j] = downSampledCr[x/2][y/2];
                    }
                }
                else if (x == paddedHeight - 2 && y < paddedWidth - 2) {
                    downSampledCb[x/2][y/2] = (Cb[x][y] + Cb[x][y+1]) / 2;
                    downSampledCr[x/2][y/2] = (Cr[x][y] + Cr[x][y+1]) / 2;
                    for (int i = (x/2)+1; i < downSampledPaddedHeight; ++i) {
                        downSampledCb[i][y/2] = downSampledCb[x/2][y/2];
                        downSampledCr[i][y/2] = downSampledCr[x/2][y/2];
                    }
                }
                else {
                    downSampledCb[x/2][y/2] = Cb[x][y];
                    downSampledCr[x/2][y/2] = Cr[x][y];
                    for (int i = x/2; i < downSampledPaddedHeight; ++i) {
                        for (int j = y/2; j < downSampledPaddedWidth; ++j) {
                            downSampledCb[i][j] = downSampledCb[x/2][y/2];
                            downSampledCr[i][j] = downSampledCr[x/2][y/2];
                        }
                    }
                }
            }
        }
    }

    /**
     * Lee una imagen PPM y la descompone en los tres componentes de color de JPEG (YCbCr).
     * @param datosInput Datos de entrada del archivo a leer.
     * @param pos Offset de lectura de datosInput.
     * @param width Ancho de las dimensiones de la imagen.
     * @param height Alto de las dimensiones de la imagen.
     * @param paddedWidth Ancho de las dimensiones de la imagen múltiplo de 8 (para poder calcular DCT).
     * @param paddedHeight Alto de las dimensiones de la imagen múltiplo de 8 (para poder calcular DCT).
     * @param Y Componente de luminancia de JPEG.
     * @param Cb Componente de crominancia azul de JPEG.
     * @param Cr Componente de crominancia roja de JPEG.
     */
    private void readY(byte[] datosInput, int pos, int width, int height, int paddedWidth, int paddedHeight, double[][] Y, double[][] Cb, double[][] Cr) {
        for (int x = 0; x < height; ++x) {//image color decomposition in YCbCr and centering values to 0 (range [-128,127]) (padding boundaries to have 8 multiple dimensions (needed for DCT))
            for (int y = 0; y < width; ++y) {
                double[] rgb = new double[3];//red green blue
                rgb[0] = ((int)datosInput[pos++] & 0xFF);
                rgb[1] = ((int)datosInput[pos++] & 0xFF);
                rgb[2] = ((int)datosInput[pos++] & 0xFF);
                Y[x][y] = 0.257 * rgb[0] + 0.504 * rgb[1] + 0.098 * rgb[2] + 16.0 - 128.0;
                Cb[x][y] = - 0.148 * rgb[0] - 0.291 * rgb[1] + 0.439 * rgb[2];
                Cr[x][y] = 0.439 * rgb[0] - 0.368 * rgb[1] - 0.071 * rgb[2];
                if (x < height - 1 && y == width - 1) {
                    for (int j = y; j < paddedWidth; ++j) {
                        Y[x][j] = Y[x][width-1];
                        Cb[x][j] = Cb[x][width-1];
                        Cr[x][j] = Cr[x][width-1];
                    }
                }
                else if (x == height - 1 && y < width - 1) {
                    for (int i = x; i < paddedHeight; ++i) {
                        Y[i][y] = Y[height-1][y];
                        Cb[i][y] = Cb[height-1][y];
                        Cr[i][y] = Cr[height-1][y];
                    }
                }
                else if (x == height - 1 && y == width - 1) {
                    for (int i = x; i < paddedHeight; ++i) {
                        for (int j = y; j < paddedWidth; ++j) {
                            Y[i][j] = Y[height-1][width-1];
                            Cb[i][j] = Cb[height-1][width-1];
                            Cr[i][j] = Cr[height-1][width-1];
                        }
                    }
                }
            }
        }
    }

    /**
     * Algoritmo de descompresión JPEG
     * <p>
     *     La imagen de entrada tiene que ser .imgc tal y como se ha definido el formato
     * </p>
     * @param datosInput Un byte array conteniente de una imagen a descomprimir
     * @return Un byte array conteniente de una imagen descomprimida
     * @throws FormatoErroneoException El formato en el que está codificada la imagen comprimida .imgc no es correcto
     */
    @Override
    public OutputAlgoritmo descomprimir(byte[] datosInput) throws FormatoErroneoException {
        long startTime = System.nanoTime(); //starting time
        List<Byte> result = new ArrayList<>(); //data will be written here before passing it into output byte array
        int pos = 0;
        DatosHeaderDesc datosHeaderDesc = new DatosHeaderDesc(datosInput, result, pos).readHeader();
        pos = datosHeaderDesc.getPos();
        String[] widthHeight = datosHeaderDesc.getWidthHeight();
        String rgbMVal = datosHeaderDesc.getRgbMVal();

        int width = Integer.parseInt(widthHeight[0]);  //string to int
        int height = Integer.parseInt(widthHeight[1]); //string to int
        int rgbMaxVal = Integer.parseInt(rgbMVal); //string to int of rgb maximum value per pixel

        int paddedWidth, paddedHeight; //height and width multiple of 8 (image has been stored like this, as explained in compression algorithm)
        if (width % 8 != 0) paddedWidth = width + (8 - width % 8);
        else paddedWidth = width;
        if (height % 8 != 0) paddedHeight = height + (8 - height % 8);
        else paddedHeight = height;
        int downSampledPaddedHeight, downSampledPaddedWidth;
        if ((paddedHeight/2) % 8 == 0) downSampledPaddedHeight = paddedHeight/2;
        else downSampledPaddedHeight = (paddedHeight/2) + 4;
        if ((paddedWidth/2) % 8 == 0) downSampledPaddedWidth = paddedWidth/2;
        else downSampledPaddedWidth = (paddedWidth/2) + 4;

        //start of image reading and decompression
        int[][] Y = new int[paddedHeight][paddedWidth];//luminance
        int[][] Cb = new int[downSampledPaddedHeight][downSampledPaddedWidth];//chrominance blue
        int[][] Cr = new int[downSampledPaddedHeight][downSampledPaddedWidth];//chrominance red
        pos = procesarY(datosInput, pos, paddedWidth, paddedHeight, Y);
        procesarCbCr(datosInput, pos, downSampledPaddedHeight, downSampledPaddedWidth, Cb, Cr);
        //end of image reading and decompression

        //start of image writting
        YCbCrToRGB(result, width, height, rgbMaxVal, Y, Cb, Cr);
        byte[] datosOutput = new byte[result.size()]; //writting image data in output byte array
        escribirDatosOutput(result, datosOutput);
        //end of image writting
        long endTime = System.nanoTime(), totalTime = endTime - startTime; //ending time and total execution time
        return new OutputAlgoritmo(totalTime, datosOutput); //returning output time and data byte array
    }

    /**
     * Convierte las componentes de color de YCbCr a RGB y las guarda en result.
     * @param result Datos de la imagen resultante.
     * @param width Ancho de la imagen.
     * @param height Alto de la imagen.
     * @param rgbMaxVal Valor de RGB máximo de la imagen.
     * @param Y Datos de la componente de luminancia Y.
     * @param Cb Datos de la componente de crominancia Cr.
     * @param Cr Datos de la componente de crominancia Cb.
     */
    private void YCbCrToRGB(List<Byte> result, int width, int height, int rgbMaxVal, int[][] Y, int[][] Cb, int[][] Cr) {
        int[] rgb = new int[3];
        for (int x = 0; x < height; ++x) { //converting YCbCr to RGB and changing range from [-128,127] to [0,255]
            for (int y = 0; y < width; ++y) {
                rgb[0] = (int)Math.round(1.164 * (double)(Y[x][y] - 16 + 128) + 1.596 * (double)(Cr[x/2][y/2]));
                rgb[1] = (int)Math.round(1.164 * (double)(Y[x][y] - 16 + 128) - 0.391 * (double)(Cb[x/2][y/2]) - 0.813 * (double)(Cr[x/2][y/2]));
                rgb[2] = (int)Math.round(1.164 * (double)(Y[x][y] - 16 + 128) + 2.018 * (double)(Cb[x/2][y/2]));
                result.add((byte)Math.max(0, Math.min(rgb[0], rgbMaxVal))); //controlling and correcting any value that is out of range (0 to rgbMaxVal)
                result.add((byte)Math.max(0, Math.min(rgb[1], rgbMaxVal)));
                result.add((byte)Math.max(0, Math.min(rgb[2], rgbMaxVal)));
            }
        }
    }

    /**
     * Lee la crominancia y le aplica DCT, RLE y Huffman coding
     * @param datosInput Byte array conteniente de una imagen.
     * @param pos Offset de lectura de datosInput.
     * @param downSampledPaddedHeight Ancho de las dimensiones downsampled de la imagen múltiplo de 8 (para poder calcular DCT).
     * @param downSampledPaddedWidth Ancho de las dimensiones downsampled de la imagen múltiplo de 8 (para poder calcular DCT).
     * @param Cb Array de valores de la crominacia azul de la imagen.
     * @param Cr Array de valores de la crominacia roja de la imagen.
     */
    private void procesarCbCr(byte[] datosInput, int pos, int downSampledPaddedHeight, int downSampledPaddedWidth, int[][] Cb, int[][] Cr) {
        int[][] rleSizeCb = new int[downSampledPaddedHeight/8][downSampledPaddedWidth/8];
        int[][] rleSizeCr = new int[downSampledPaddedHeight/8][downSampledPaddedWidth/8];
        int[][] offsetSizeCb = new int[downSampledPaddedHeight/8][downSampledPaddedWidth/8];
        int[][] offsetSizeCr = new int[downSampledPaddedHeight/8][downSampledPaddedWidth/8];
        StringBuilder[][] huffmanStringBuilderCb = new StringBuilder[downSampledPaddedHeight/8][downSampledPaddedWidth/8];
        StringBuilder[][] huffmanStringBuilderCr = new StringBuilder[downSampledPaddedHeight/8][downSampledPaddedWidth/8];
        readCbCr(datosInput, pos, downSampledPaddedHeight, downSampledPaddedWidth, rleSizeCb, rleSizeCr, offsetSizeCb, offsetSizeCr, huffmanStringBuilderCb, huffmanStringBuilderCr);
        IntStream.range(0, downSampledPaddedHeight).parallel().filter(x -> x % 8 == 0).forEach(x -> { //image inverse quantization and DCT-III (aka inverse DCT) (done in pixel squares of 8x8) for chrominance
            int topi = x + 8;                                      //for each chrominance pixel square of 8x8 of the image, inverse downsampling and DCT-III (aka inverse DCT) algorithm are applied, letting recover the image values
            IntStream.range(0, downSampledPaddedWidth).parallel().filter(y -> y % 8 == 0).forEach(y -> {
                double[][] buffCb = new double[8][8];
                double[][] buffCr = new double[8][8];
                int topj = y + 8;
                List<Byte> huffmanList = new ArrayList<>();
                inverseHuffman(huffmanList, rleSizeCb[x/8][y/8] * 8 - offsetSizeCb[x/8][y/8], huffmanStringBuilderCb[x/8][y/8]);
                byte[] lineCb = new byte[64];
                inverseRLE(huffmanList, lineCb);
                List<Byte> huffmanCrList = new ArrayList<>();
                inverseHuffman(huffmanCrList, rleSizeCr[x/8][y/8] * 8 - offsetSizeCr[x/8][y/8], huffmanStringBuilderCr[x/8][y/8]);
                byte[] lineCr = new byte[64];
                inverseRLE(huffmanCrList, lineCr);
                zigZagQuantizeCbCr(Cb, Cr, x, topi, y, buffCb, buffCr, topj, lineCb, lineCr);
                DCT_III_CbCr(Cb, Cr, x, topi, y, buffCb, buffCr, topj);
                writeCbCr(Cb, Cr, x, topi, y, buffCb, buffCr, topj);
            });
        });
    }

    /**
     * Lee la imagen comprimida y la deja en una estructura de datos temporal que permite hacer el proceso concurrente.
     * @param datosInput Byte array conteniente de una imagen.
     * @param pos Offset de lectura de datosInput.
     * @param downSampledPaddedHeight Ancho de las dimensiones downsampled de la imagen múltiplo de 8 (para poder calcular DCT).
     * @param downSampledPaddedWidth Ancho de las dimensiones downsampled de la imagen múltiplo de 8 (para poder calcular DCT).
     * @param rleSizeCb Tamaño de cada bloque de Cb a leer por Huffman y RLE.
     * @param rleSizeCr Tamaño de cada bloque de Cr a leer por Huffman y RLE.
     * @param offsetSizeCb Bits útiles del último byte de cada bloque de Cb a leer por Huffman y RLE.
     * @param offsetSizeCr Bits útiles del último byte de cada bloque de Cr a leer por Huffman y RLE.
     * @param huffmanStringBuilderCb Datos de cada bloque de Cb a leer por Huffman y RLE.
     * @param huffmanStringBuilderCr Datos de cada bloque de Cr a leer por Huffman y RLE.
     */
    private void readCbCr(byte[] datosInput, int pos, int downSampledPaddedHeight, int downSampledPaddedWidth, int[][] rleSizeCb, int[][] rleSizeCr, int[][] offsetSizeCb, int[][] offsetSizeCr, StringBuilder[][] huffmanStringBuilderCb, StringBuilder[][] huffmanStringBuilderCr) {
        for (int x = 0; x < downSampledPaddedHeight; x += 8) {
            for (int y = 0; y < downSampledPaddedWidth; y += 8) {
                rleSizeCb[x/8][y/8] = datosInput[pos++]; //reading Huffman block header
                offsetSizeCb[x/8][y/8] = datosInput[pos++];
                huffmanStringBuilderCb[x/8][y/8] = new StringBuilder();
                for (int it = 0; it < rleSizeCb[x/8][y/8]; ++it) {
                    huffmanStringBuilderCb[x/8][y/8].append(String.format("%8s", Integer.toBinaryString((datosInput[pos++] & 0xFF))).replace(' ', '0')); //converting input bytes into binary string
                }
                if (offsetSizeCb[x/8][y/8] != 0) {
                    for (int it = 0; it < (8 - offsetSizeCb[x/8][y/8]); ++it)
                        huffmanStringBuilderCb[x/8][y/8].deleteCharAt(huffmanStringBuilderCb[x/8][y/8].length() - 1); //deleting extra final bits of last byte of block till offset is fulfilled
                }
                rleSizeCr[x/8][y/8] = datosInput[pos++]; //reading Huffman block header
                offsetSizeCr[x/8][y/8] = datosInput[pos++];
                huffmanStringBuilderCr[x/8][y/8] = new StringBuilder();
                for (int it = 0; it < rleSizeCr[x/8][y/8]; ++it) {
                    huffmanStringBuilderCr[x/8][y/8].append(String.format("%8s", Integer.toBinaryString((datosInput[pos++] & 0xFF))).replace(' ', '0')); //converting input bytes into binary string
                }
                if (offsetSizeCr[x/8][y/8] != 0) {
                    for (int it = 0; it < (8 - offsetSizeCr[x/8][y/8]); ++it)
                        huffmanStringBuilderCr[x/8][y/8].deleteCharAt(huffmanStringBuilderCr[x/8][y/8].length() - 1); //deleting extra final bits of last byte of block till offset is fulfilled
                }
            }
        }
    }

    /**
     * Pasa los valores de la crominancia a un bloque 8x8 y hace su cuantización inversa.
     * @param Cb Componente de crominancia azul de JPEG.
     * @param Cr Componente de crominancia roja de JPEG.
     * @param x Posición en el eje x en Y.
     * @param topi Posición límite del bloque en el eje x en Y (es x + 8).
     * @param y Posición en el eje y en Y.
     * @param buffCb Buffer temporal del bloque 8x8 donde se guardan los cálculos de DCT antes de escribir los definitivos en Cb.
     * @param buffCr Buffer temporal del bloque 8x8 donde se guardan los cálculos de DCT antes de escribir los definitivos en Cr.
     * @param topj Posición límite del bloque en el eje y en Y (es y + 8).
     * @param lineCb Valores de la crominancia azul a pasar al bloque 8x8.
     * @param lineCr Valores de la crominancia roja a pasar al bloque 8x8.
     */
    private void zigZagQuantizeCbCr(int[][] Cb, int[][] Cr, int x, int topi, int y, double[][] buffCb, double[][] buffCr, int topj, byte[] lineCb, byte[] lineCr) {
        boolean up = true;
        int k = x, l = y;
        int lineCrit = 0;
        int lineCbit = 0;
        while (k < topi && l < topj) { //zig-zag reading Chrominance and inverse downsampling values
            buffCb[k%8][l%8] = (lineCb[lineCbit++]) * (ChrominanceQuantizationTable[k%8][l%8] * calidad); //(byte) because reads [0,255] but it's been stored as [-128,127]
            buffCr[k%8][l%8] = (lineCr[lineCrit++]) * (ChrominanceQuantizationTable[k%8][l%8] * calidad); //(byte) because reads [0,255] but it's been stored as [-128,127]
            Cb[k][l] = (int)Math.round(buffCb[k%8][l%8]);
            Cr[k][l] = (int)Math.round(buffCr[k%8][l%8]);
            if (k == x && l != topj - 1 && up) {
                ++l;
                up = false;
            }
            else if (l == y && k != topi - 1 && !up) {
                ++k;
                up = true;
            }
            else if (k == topi - 1 && l != topj- 1 && !up) {
                ++l;
                up = true;
            }
            else if (l == topj - 1 && k != topi - 1 && up) {
                ++k;
                up = false;
            }
            else if (k == topi - 1 && l == topj - 1) {
                ++k;
                ++l;
            }
            else if (up) {
                --k;
                ++l;
            }
            else {
                ++k;
                --l;
            }
        }
    }

    /**
     * Aplica el algoritmo DCT-III, conocido como Inverse DCT, a la crominancia en descompresión.
     * @param Cb Componente de crominancia azul de JPEG.
     * @param Cr Componente de crominancia roja de JPEG.
     * @param x Posición en el eje x en Y.
     * @param topi Posición límite del bloque en el eje x en Y (es x + 8).
     * @param y Posición en el eje y en Y.
     * @param buffCb Buffer temporal del bloque 8x8 donde se guardan los cálculos de DCT antes de escribir los definitivos en Cb.
     * @param buffCr Buffer temporal del bloque 8x8 donde se guardan los cálculos de DCT antes de escribir los definitivos en Cr.
     * @param topj Posición límite del bloque en el eje y en Y (es y + 8).
     */
    private void DCT_III_CbCr(int[][] Cb, int[][] Cr, int x, int topi, int y, double[][] buffCb, double[][] buffCr, int topj) {
        IntStream.range(x, topi).parallel().forEach(i -> {
            double alphau, alphav, cosu, cosv;
            for (int j = y; j < topj; ++j) { //for each chrominance pixel of the 8x8 square, the DCT-III calculation is applied
                buffCb[i%8][j%8] = 0;
                buffCr[i%8][j%8] = 0;
                for (int u = x; u < topi; ++u) {
                    if (u % 8 == 0) alphau = 1 / Math.sqrt(2);
                    else alphau = 1;
                    cosu = Math.cos(((2 * (i % 8) + 1) * (u % 8) * Math.PI) / 16.0);
                    for (int v = y; v < topj; ++v) {
                        if (v % 8 == 0) alphav = 1 / Math.sqrt(2);
                        else alphav = 1;
                        cosv = Math.cos(((2 * (j % 8) + 1) * (v % 8) * Math.PI) / 16.0);
                        buffCb[i%8][j%8] += alphau * alphav * (double) Cb[u][v] * cosu * cosv;
                        buffCr[i%8][j%8] += alphau * alphav * (double) Cr[u][v] * cosu * cosv;
                    }
                }
                buffCb[i%8][j%8] *= 0.25;
                buffCr[i%8][j%8] *= 0.25;
            }
        });
    }

    /**
     * Escribe los datos del bloque 8x8 procesado.
     * @param Cb Componente de crominancia azul de JPEG.
     * @param Cr Componente de crominancia roja de JPEG.
     * @param x Posición en el eje x en Y.
     * @param topi Posición límite del bloque en el eje x en Y (es x + 8).
     * @param y Posición en el eje y en Y.
     * @param buffCb Buffer temporal del bloque 8x8 donde se guardan los cálculos de DCT antes de escribir los definitivos en Cb.
     * @param buffCr Buffer temporal del bloque 8x8 donde se guardan los cálculos de DCT antes de escribir los definitivos en Cr.
     * @param topj Posición límite del bloque en el eje y en Y (es y + 8).
     */
    private void writeCbCr(int[][] Cb, int[][] Cr, int x, int topi, int y, double[][] buffCb, double[][] buffCr, int topj) {
        for (int i = x; i < topi; ++i) {
            for (int j = y; j < topj; ++j) {
                Cb[i][j] = (int)Math.round(buffCb[i%8][j%8]);
                Cr[i][j] = (int)Math.round(buffCr[i%8][j%8]);
            }
        }
    }

    /**
     * Lee la luminancia y le aplica DCT, RLE y Huffman coding.
     * @param datosInput Byte array conteniente de una imagen.
     * @param pos Offset de lectura de datosInput.
     * @param paddedWidth Ancho de las dimensiones de la imagen múltiplo de 8 (para poder calcular DCT).
     * @param paddedHeight Alto de las dimensiones de la imagen múltiplo de 8 (para poder calcular DCT).
     * @param Y Componente de luminancia de JPEG.
     * @return Offset del byte array en el que se ha quedado el algoritmo, para seguir leyendo a continuación.
     */
    private int procesarY(byte[] datosInput, int pos, int paddedWidth, int paddedHeight, int[][] Y) {
        int[][] rleSizeY = new int[paddedHeight/8][paddedWidth/8];
        int[][] offsetSizeY = new int[paddedHeight/8][paddedWidth/8];
        StringBuilder[][] huffmanStringBuilderY = new StringBuilder[paddedHeight/8][paddedWidth/8];
        pos = readY(datosInput, pos, paddedWidth, paddedHeight, rleSizeY, offsetSizeY, huffmanStringBuilderY);
        IntStream.range(0, paddedHeight).parallel().filter(x -> x % 8 == 0).forEach(x -> { //image inverse quantization and DCT-III (aka inverse DCT) (done in pixel squares of 8x8) for luminance
            int topi = x + 8;                           //for each luminance pixel square of 8x8 of the image, inverse downsampling and DCT-III (aka inverse DCT) algorithm are applied, letting recover the image value
            IntStream.range(0, paddedWidth).parallel().filter(y -> y % 8 == 0).forEach(y -> {
                double[][] buffY = new double[8][8];
                int topj = y + 8;
                List<Byte> huffmanList = new ArrayList<>();
                inverseHuffman(huffmanList, rleSizeY[x / 8][y / 8] * 8 - offsetSizeY[x / 8][y / 8], huffmanStringBuilderY[x / 8][y / 8]);
                byte[] lineY = new byte[64];
                inverseRLE(huffmanList, lineY);
                zigZagQuantizeY(Y, x, topi, y, buffY, topj, lineY);
                DCT_III_Y(Y, x, topi, y, buffY, topj);
                writeY(Y, x, topi, y, buffY, topj);
            });
        });
        return pos;
    }

    /**
     * Lee la imagen comprimida y la deja en una estructura de datos temporal que permite hacer el proceso concurrente.
     * @param datosInput Byte array conteniente de una imagen.
     * @param pos Offset de lectura de datosInput.
     * @param paddedWidth Ancho de las dimensiones de la imagen múltiplo de 8 (para poder calcular DCT).
     * @param paddedHeight Alto de las dimensiones de la imagen múltiplo de 8 (para poder calcular DCT).
     * @param rleSizeY Tamaño de cada bloque a leer por Huffman y RLE.
     * @param offsetSizeY Bits útiles del último byte de cada bloque a leer por Huffman y RLE.
     * @param huffmanStringBuilderY Datos de cada bloque a leer por Huffman y RLE.
     * @return Offset de lectura de datosInput actualizado.
     */
    private int readY(byte[] datosInput, int pos, int paddedWidth, int paddedHeight, int[][] rleSizeY, int[][] offsetSizeY, StringBuilder[][] huffmanStringBuilderY) {
        for (int x = 0; x < paddedHeight; x += 8) {
            for (int y = 0; y < paddedWidth; y += 8) {
                rleSizeY[x/8][y/8] = datosInput[pos++]; //reading Huffman block header
                offsetSizeY[x/8][y/8] = datosInput[pos++];
                huffmanStringBuilderY[x/8][y/8] = new StringBuilder();
                for (int it = 0; it < rleSizeY[x/8][y/8]; ++it) {
                    huffmanStringBuilderY[x/8][y/8].append(String.format("%8s", Integer.toBinaryString((datosInput[pos++] & 0xFF))).replace(' ', '0')); //converting input bytes into binary string
                }
                if (offsetSizeY[x/8][y/8] != 0) {
                    for (int it = 0; it < (8 - offsetSizeY[x/8][y/8]); ++it)
                        huffmanStringBuilderY[x/8][y/8].deleteCharAt(huffmanStringBuilderY[x/8][y/8].length() - 1); //deleting extra final bits of last byte of block till offset is fulfilled
                }
            }
        }
        return pos;
    }

    /**
     * Algoritmo inverso de Huffman, que decodifica los valores de la imagen para luego ser otra vez decodificador por el RLE inverso.
     * @param huffmanList Lista donde se escribirán los valores decodificados.
     * @param i Límite del bloque a leer.
     * @param stringBuilder Valores a leer y convertir
     */
    private void inverseHuffman(List<Byte> huffmanList, int i, StringBuilder stringBuilder) {
        String huffmanBuff = "";
        for (int it = 0; it < i; ++it) { //getting RLE values from Huffman block after offset applied
            huffmanBuff += stringBuilder.charAt(it);
            Pair pair = ACInverseHuffmanTable.get(huffmanBuff); //check if Huffman code exists
            if (pair != null) { //if exists, get RLE value and store it in zigzag line of block
                byte runlength = pair.getKey(); //getting runlength and size of RLE value
                huffmanList.add(runlength);
                byte size = pair.getValue();
                huffmanList.add(size);
                StringBuilder amplitude = new StringBuilder();
                for (int n = 0; n < size; ++n) {
                    amplitude.append(stringBuilder.charAt(++it)); //binary string of non-zero value
                }
                if (runlength != 0 || size != 0)
                    huffmanList.add((byte) Integer.parseInt(amplitude.toString(), 2)); //binary string (8 bits long) to byte
                else it = i;
                huffmanBuff = "";
            }
        }
    }

    /**
     * Aplicación de RLE inverso a los datos de la imagen comprimida.
     * @param huffmanList Datos decodificados con el algoritmo de Huffman Coding.
     * @param lineY Datos decodificados con el algoritmo inverso de RLE.
     */
    private void inverseRLE(List<Byte> huffmanList, byte[] lineY) {
        int lineYit = 0;
        for (int it = 0; it < huffmanList.size(); ++it) { //RLE lossless decompression for Luminance
            int howManyZeroes = huffmanList.get(it++);
            int size = huffmanList.get(it++);
            if (howManyZeroes != 0 || size != 0) {
                byte value = huffmanList.get(it);
                for (int z = 0; z < howManyZeroes; ++z) lineY[lineYit++] = 0;
                lineY[lineYit++] = value;
            } else {
                while (lineYit < 64) lineY[lineYit++] = 0;
                it = huffmanList.size();
            }
        }
    }

    /**
     * Pasa los valores de la luminancia a un bloque 8x8 y hace su cuantización inversa.
     * @param Y Componente de luminancia de JPEG.
     * @param x Posición en el eje x en Y.
     * @param topi Posición límite del bloque en el eje x en Y (es x + 8).
     * @param y Posición en el eje y en Y.
     * @param buffY Buffer temporal del bloque 8x8 donde se guardan los cálculos de DCT antes de escribir los definitivos en Y.
     * @param topj Posición límite del bloque en el eje y en Y (es y + 8).
     * @param lineY Valores de la luminancia a pasar al bloque 8x8.
     */
    private void zigZagQuantizeY(int[][] Y, int x, int topi, int y, double[][] buffY, int topj, byte[] lineY) {
        int lineYit = 0;
        boolean up = true;
        int k = x, l = y;
        while (k < topi && l < topj) { //zig-zag reading Luminance and inverse downsampling values
            buffY[k%8][l%8] = (lineY[lineYit++]) * (LuminanceQuantizationTable[k%8][l%8] * calidad); //(byte) because reads [0,255] but it's been stored as [-128,127]
            Y[k][l] = (int)Math.round(buffY[k%8][l%8]);
            if (k == x && l != topj - 1 && up) {
                ++l;
                up = false;
            }
            else if (l == y && k != topi - 1 && !up) {
                ++k;
                up = true;
            }
            else if (k == topi - 1 && l != topj- 1 && !up) {
                ++l;
                up = true;
            }
            else if (l == topj - 1 && k != topi - 1 && up) {
                ++k;
                up = false;
            }
            else if (k == topi - 1 && l == topj - 1) {
                ++k;
                ++l;
            }
            else if (up) {
                --k;
                ++l;
            }
            else {
                ++k;
                --l;
            }
        }
    }

    /**
     * Escribe el bloque 8x8 procesado en Y.
     * @param Y Componente de luminancia de JPEG.
     * @param x Posición en el eje x en Y.
     * @param topi Posición límite del bloque en el eje x en Y (es x + 8).
     * @param y Posición en el eje y en Y.
     * @param buffY Buffer temporal del bloque 8x8 donde se guardan los cálculos de DCT antes de escribir los definitivos en Y.
     * @param topj Posición límite del bloque en el eje y en Y (es y + 8).
     */
    private void writeY(int[][] Y, int x, int topi, int y, double[][] buffY, int topj) {
        for (int i = x; i < topi; ++i) {
            for (int j = y; j < topj; ++j) {
                Y[i][j] = (int)Math.round(buffY[i%8][j%8]);
            }
        }
    }

    /**
     * Aplica el algoritmo DCT-III, conocido como Inverse DCT, a la luminancia en descompresión.
     * @param Y Componente de luminancia de JPEG.
     * @param x Posición en el eje x en Y.
     * @param topi Posición límite del bloque en el eje x en Y (es x + 8).
     * @param y Posición en el eje y en Y.
     * @param buffY Buffer temporal del bloque 8x8 donde se guardan los cálculos de DCT antes de escribir los definitivos en Y.
     * @param topj Posición límite del bloque en el eje y en Y (es y + 8).
     */
    private void DCT_III_Y(int[][] Y, int x, int topi, int y, double[][] buffY, int topj) {
        IntStream.range(x, topi).parallel().forEach(i -> {
            double alphau, alphav, cosu, cosv;
            for (int j = y; j < topj; ++j) { //for each luminance pixel of the 8x8 square, the DCT-III calculation is applied
                buffY[i%8][j%8] = 0;
                for (int u = x; u < topi; ++u) {
                    if (u % 8 == 0) alphau = 1 / Math.sqrt(2);
                    else alphau = 1;
                    cosu = Math.cos(((2 * (i % 8) + 1) * (u % 8) * Math.PI) / 16.0);
                    for (int v = y; v < topj; ++v) {
                        if (v % 8 == 0) alphav = 1 / Math.sqrt(2);
                        else alphav = 1;
                        cosv = Math.cos(((2 * (j % 8) + 1) * (v % 8) * Math.PI) / 16.0);
                        buffY[i%8][j%8] += alphau * alphav * (double) Y[u][v] * cosu * cosv;
                    }
                }
                buffY[i%8][j%8] *= 0.25;
            }
        });
    }

    /**
     * Obtiene un objeto Image a partir de una imagen .ppm provista.
     * @param datosInput byte array de los datos de la imagen a leer.
     * @return Objeto Image de la imagen leída.
     */
    public Image getImage(byte[] datosInput) throws FormatoErroneoException {
        int pos = 0, width, height;
        List<Byte> l = new ArrayList<>();
        DatosHeaderComp datosHeaderComp = new DatosHeaderComp(datosInput, l, pos).readHeader();
        pos = datosHeaderComp.getPos();
        width = datosHeaderComp.getWidth();
        height = datosHeaderComp.getHeight();
        //pixelmap reading
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                int color = new Color(((int)datosInput[pos++] & 0xFF), ((int)datosInput[pos++] & 0xFF), ((int)datosInput[pos++] & 0xFF)).getRGB();
                bufferedImage.setRGB(j, i, color);
            }
        }
        //end of pixelmap reading
        double scale = (double) 250 / height;
        return bufferedImage.getScaledInstance((int) (width * scale), (int) (height * scale),  Image.SCALE_SMOOTH);
    }

    /**
     * Clase encargada de contener un par de datos de tipo byte para la tabla de Huffman de descompresión
     */
    static class Pair {
        /**
         * Primer byte, el cual, según RLE, contiene el valor de Runlength
         */
        byte key;
        /**
         * Segundo byte, el cual, según RLE, contiene el valor del size del valor distinto de cero
         */
        byte value;

        /**
         * Constructora de la clase
         * @param key Valor de Runlength
         * @param value Valor del size del valor distinto de cero
         */
        Pair(byte key, byte value) {
            this.key = key;
            this.value = value;
        }

        /**
         * Obtenedora del key del pair
         * @return El key del pair
         */
        byte getKey() {
            return this.key;
        }

        /**
         * Obtenedora del value del pair
         * @return El value del pair
         */
        byte getValue() {
            return this.value;
        }
    }

    /**
     * Clase encargada de leer el header de archivos comprimidos.
     */
    private class DatosHeaderDesc {
        /**
         * Datos de la imagen comprimida.
         */
        private byte[] datosInput;
        /**
         * Datos de la imagen descomprimida.
         */
        private List<Byte> result;
        /**
         * Offset de lectura de la imagen comprimida.
         */
        private int pos;
        /**
         * Ancho y alto de la imagen.
         */
        private String[] widthHeight;
        /**
         * Valor de RGB máximo de la imagen.
         */
        private String rgbMVal;

        /**
         * Constructora de la clase.
         * @param datosInput Datos de la imagen comprimida.
         * @param result Datos de la imagen descomprimida.
         * @param pos Offset de lectura de la imagen comprimida.
         */
        public DatosHeaderDesc(byte[] datosInput, List<Byte> result, int pos) {
            this.datosInput = datosInput;
            this.result = result;
            this.pos = pos;
        }

        /**
         * Obtiene el offset de lectura de la imagen comprimida.
         * @return El offset de lectura de la imagen comprimida.
         */
        public int getPos() {
            return pos;
        }

        /**
         * Obtiene el ancho y alto de la imagen.
         * @return El ancho y alto de la imagen.
         */
        public String[] getWidthHeight() {
            return widthHeight;
        }

        /**
         * Obtiene el valor RGB máximo de la imagen.
         * @return El valor RGB máximo de la imagen.
         */
        public String getRgbMVal() {
            return rgbMVal;
        }

        /**
         * Lector del header de la imagen.
         * @return DatosHeaderDesc resultantes de la lectura.
         * @throws FormatoErroneoException Si la imagen no cumple con el formato .imgc, se activa una excepción de formato erróneo.
         */
        public DatosHeaderDesc readHeader() throws FormatoErroneoException {
            if (datosInput.length < 16) throw new FormatoErroneoException("El formato de .imgc no es correcto!");
            StringBuilder buff = new StringBuilder();
            //start of header reading
            while (pos < datosInput.length - 1 && (char)datosInput[pos] != '\n') {
                buff.append((char) datosInput[pos]);
                result.add(datosInput[pos]);
                ++pos;
            }
            result.add((byte)'\n');
            ++pos;
            String magicNumber = buff.toString(); //read .ppm magicNumber, which is P6 (pixels are codified in binary)
            if(!magicNumber.equals("P6")) throw new FormatoErroneoException("El formato de .imgc no es correcto!");
            buff = new StringBuilder();
            while (pos < datosInput.length - 1 && (char)datosInput[pos] != '\n') {
                buff.append((char) datosInput[pos]);
                result.add(datosInput[pos]);
                ++pos;
            }
            result.add((byte)'\n');
            ++pos;
            widthHeight = buff.toString().split(" ");
            if (widthHeight.length > 2 || Integer.parseInt(widthHeight[0]) < 1 || Integer.parseInt(widthHeight[1]) < 1) throw new FormatoErroneoException("El formato de .imgc no es correcto!");
            buff = new StringBuilder();
            while (pos < datosInput.length - 1 && (char)datosInput[pos] != '\n') {
                buff.append((char) datosInput[pos]);
                result.add(datosInput[pos]);
                ++pos;
            }
            result.add((byte)'\n');
            ++pos;
            rgbMVal = buff.toString();
            if (!rgbMVal.equals("255")) throw new FormatoErroneoException("El formato de .imgc no es correcto!");
            buff = new StringBuilder();
            while (pos < datosInput.length - 1 && (char)datosInput[pos] != '\n') {
                buff.append((char) datosInput[pos]);
                ++pos;
            }
            ++pos;
            String quality = buff.toString();
            setCalidad(Integer.parseInt(quality));
            if(quality.length() > 1) throw new FormatoErroneoException("El formato de .imgc no es correcto!");
            //end of header reading
            return this;
        }
    }

    /**
     * Clase encargada de leer el header de archivos descomprimidos.
     */
    private class DatosHeaderComp {
        /**
         * Datos de la imagen descomprimida.
         */
        private byte[] datosInput;
        /**
         * Datos de la imagen descomprimida.
         */
        private List<Byte> result;
        /**
         * Offset de lectura de la imagen descomprimida.
         */
        private int pos;
        /**
         * Ancho de la imagen.
         */
        private int width;
        /**
         * Alto de la imagen.
         */
        private int height;

        /**
         * Constructora de la clase.
         * @param datosInput Datos de la imagen descomprimida.
         * @param result Datos de la imagen comprimida.
         * @param pos Offset de lectura de la imagen descomprimida.
         */
        public DatosHeaderComp(byte[] datosInput, List<Byte> result, int pos) {
            this.datosInput = datosInput;
            this.result = result;
            this.pos = pos;
        }

        /**
         * Obtiene el offset de lectura de la imagen descomprimida.
         * @return El offset de lectura de la imagen descomprimida.
         */
        public int getPos() {
            return pos;
        }

        /**
         * Obtiene el ancho de la imagen.
         * @return El ancho de la imagen.
         */
        public int getWidth() {
            return width;
        }

        /**
         * Obtiene el alto de la imagen.
         * @return El alto de la imagen.
         */
        public int getHeight() {
            return height;
        }

        /**
         * Lector del header de la imagen.
         * @return DatosHeaderComp resultantes de la lectura.
         * @throws FormatoErroneoException Si la imagen no cumple con el formato .ppm P6, se activa una excepción de formato erróneo.
         */
        public DatosHeaderComp readHeader() throws FormatoErroneoException {
            //start of header reading
            if (datosInput.length < 14) throw new FormatoErroneoException("El formato de .ppm no es correcto!");
            StringBuilder buff = new StringBuilder();
            while (pos < datosInput.length - 1 && (char)datosInput[pos] != '\n') {
                buff.append((char) datosInput[pos]);
                result.add(datosInput[pos]);
                ++pos;
            }
            result.add((byte)'\n');
            ++pos;
            String magicNumber = buff.toString(); //read .ppm magicNumber, which is P6 (pixels are codified in binary)
            if(!magicNumber.equals("P6")) throw new FormatoErroneoException("El formato de .ppm no es correcto!");
            while (pos < datosInput.length - 1 && (char)datosInput[pos] == '#') {
                while ((char)datosInput[pos] != '\n') { //avoiding comments between parameters...
                    ++pos;
                }
                ++pos;
            }
            String[] widthHeight;
            buff = new StringBuilder();
            while (pos < datosInput.length - 1 && (char)datosInput[pos] != '\n') {
                buff.append((char) datosInput[pos]);
                result.add(datosInput[pos]);
                ++pos;
            }
            result.add((byte)'\n');
            ++pos;
            widthHeight = buff.toString().split(" ");  //read and split dimensions into two (one for each value)
            if (widthHeight.length > 2 || Integer.parseInt(widthHeight[0]) < 1 || Integer.parseInt(widthHeight[1]) < 1) throw new FormatoErroneoException("El formato de .ppm no es correcto!");
            while (pos < datosInput.length - 1 && (char)datosInput[pos] == '#') {
                while ((char)datosInput[pos] != '\n') { //avoiding comments between parameters...
                    ++pos;
                }
                ++pos;
            }
            width = Integer.parseInt(widthHeight[0]);  //string to int of image width
            height = Integer.parseInt(widthHeight[1]); //string to int of image height
            buff = new StringBuilder();
            while (pos < datosInput.length - 1 && (char)datosInput[pos] != '\n') {
                buff.append((char) datosInput[pos]);
                result.add(datosInput[pos]);
                ++pos;
            }
            result.add((byte)'\n');
            ++pos;
            String rgbMVal = buff.toString(); //string of rgb maximum value per pixel (8 bits)
            if (!rgbMVal.equals("255")) throw new FormatoErroneoException("El formato de .ppm no es correcto!");
            String qualityPercent = Integer.toString(calidadHeader); //writting in header the compression quality
            char [] qualityPercentArray = qualityPercent.toCharArray(); //.imgc extension determines that its header will be the same one than the one of the .ppm image, but adding the quality compression in it
            for (char c : qualityPercentArray) result.add((byte)c);
            result.add((byte)'\n');
            //end of header reading
            return this;
        }
    }
}
