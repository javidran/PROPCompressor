package DomainLayer.Algoritmos;

import Exceptions.FormatoErroneoException;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private static String[][] ACHuffmanTable = new String[15][11];
    /**
     * Tabla de valores estándar para la descodificación según Huffman coding de los valores obtenidos con el RLE del algoritmo JPEG. Este atributo es estático
     */
    private static Map<String, Pair<Byte, Byte>> ACInverseHuffmanTable = new HashMap<>();
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
        calidadHeader = 50;
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
        ACHuffmanTable = new String[][] {
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
        ACInverseHuffmanTable = new HashMap<String, Pair<Byte, Byte>>() {{
            put("1010", new Pair<>((byte) 0, (byte) 0)); put("00", new Pair<>((byte) 0, (byte) 1)); put("01", new Pair<>((byte) 0, (byte) 2)); put("100", new Pair<>((byte) 0, (byte) 3)); put("1011", new Pair<>((byte) 0, (byte) 4)); put("11010", new Pair<>((byte) 0, (byte) 5)); put("1111000", new Pair<>((byte) 0, (byte) 6)); put("11111000", new Pair<>((byte) 0, (byte) 7)); put("1111110110", new Pair<>((byte) 0, (byte) 8)); put("1111111110000010", new Pair<>((byte) 0, (byte) 9)); put("1111111110000011", new Pair<>((byte) 0, (byte) 10));
            put("1100", new Pair<>((byte) 1, (byte) 1)); put("11011", new Pair<>((byte) 1, (byte) 2)); put("1111001", new Pair<>((byte) 1, (byte) 3)); put("111110110", new Pair<>((byte) 1, (byte) 4)); put("11111110110", new Pair<>((byte) 1, (byte) 5)); put("1111111110000100", new Pair<>((byte) 1, (byte) 6)); put("1111111110000101", new Pair<>((byte) 1, (byte) 7)); put("1111111110000110", new Pair<>((byte) 1, (byte) 8)); put("1111111110000111", new Pair<>((byte) 1, (byte) 9)); put("1111111110001000", new Pair<>((byte) 1, (byte) 10));
            put("11100", new Pair<>((byte) 2, (byte) 1)); put("11111001", new Pair<>((byte) 2, (byte) 2)); put("1111110111", new Pair<>((byte) 2, (byte) 3)); put("111111110100", new Pair<>((byte) 2, (byte) 4)); put("1111111110001001", new Pair<>((byte) 2, (byte) 5)); put("1111111110001010", new Pair<>((byte) 2, (byte) 6)); put("1111111110001011", new Pair<>((byte) 2, (byte) 7)); put("1111111110001100", new Pair<>((byte) 2, (byte) 8)); put("1111111110001101", new Pair<>((byte) 2, (byte) 9)); put("1111111110001110", new Pair<>((byte) 2, (byte) 10));
            put("111010", new Pair<>((byte) 3, (byte) 1)); put("111110111", new Pair<>((byte) 3, (byte) 2)); put("111111110101", new Pair<>((byte) 3, (byte) 3)); put("1111111110001111", new Pair<>((byte) 3, (byte) 4)); put("1111111110010000", new Pair<>((byte) 3, (byte) 5)); put("1111111110010001", new Pair<>((byte) 3, (byte) 6)); put("1111111110010010", new Pair<>((byte) 3, (byte) 7)); put("1111111110010011", new Pair<>((byte) 3, (byte) 8)); put("1111111110010100", new Pair<>((byte) 3, (byte) 9)); put("1111111110010101", new Pair<>((byte) 3, (byte) 10));
            put("111011", new Pair<>((byte) 4, (byte) 1)); put("1111111000", new Pair<>((byte) 4, (byte) 2)); put("1111111110010110", new Pair<>((byte) 4, (byte) 3)); put("1111111110010111", new Pair<>((byte) 4, (byte) 4)); put("1111111110011000", new Pair<>((byte) 4, (byte) 5)); put("1111111110011001", new Pair<>((byte) 4, (byte) 6)); put("1111111110011010", new Pair<>((byte) 4, (byte) 7)); put("1111111110011011", new Pair<>((byte) 4, (byte) 8)); put("1111111110011100", new Pair<>((byte) 4, (byte) 9)); put("1111111110011101", new Pair<>((byte) 4, (byte) 10));
            put("1111010", new Pair<>((byte) 5, (byte) 1)); put("11111110111", new Pair<>((byte) 5, (byte) 2)); put("1111111110011110", new Pair<>((byte) 5, (byte) 3)); put("1111111110011111", new Pair<>((byte) 5, (byte) 4)); put("1111111110100000", new Pair<>((byte) 5, (byte) 5)); put("1111111110100001", new Pair<>((byte) 5, (byte) 6)); put("1111111110100010", new Pair<>((byte) 5, (byte) 7)); put("1111111110100011", new Pair<>((byte) 5, (byte) 8)); put("1111111110100100", new Pair<>((byte) 5, (byte) 9)); put("1111111110100101", new Pair<>((byte) 5, (byte) 10));
            put("1111011", new Pair<>((byte) 6, (byte) 1)); put("111111110110", new Pair<>((byte) 6, (byte) 2)); put("1111111110100110", new Pair<>((byte) 6, (byte) 3)); put("1111111110100111", new Pair<>((byte) 6, (byte) 4)); put("1111111110101000", new Pair<>((byte) 6, (byte) 5)); put("1111111110101001", new Pair<>((byte) 6, (byte) 6)); put("1111111110101010", new Pair<>((byte) 6, (byte) 7)); put("1111111110101011", new Pair<>((byte) 6, (byte) 8)); put("1111111110101100", new Pair<>((byte) 6, (byte) 9)); put("1111111110101101", new Pair<>((byte) 6, (byte) 10));
            put("11111010", new Pair<>((byte) 7, (byte) 1)); put("111111110111", new Pair<>((byte) 7, (byte) 2)); put("1111111110101110", new Pair<>((byte) 7, (byte) 3)); put("1111111110101111", new Pair<>((byte) 7, (byte) 4)); put("1111111110110000", new Pair<>((byte) 7, (byte) 5)); put("1111111110110001", new Pair<>((byte) 7, (byte) 6)); put("1111111110110010", new Pair<>((byte) 7, (byte) 7)); put("1111111110110011", new Pair<>((byte) 7, (byte) 8)); put("1111111110110100", new Pair<>((byte) 7, (byte) 9)); put("1111111110110101", new Pair<>((byte) 7, (byte) 10));
            put("111111000", new Pair<>((byte) 8, (byte) 1)); put("111111111000000", new Pair<>((byte) 8, (byte) 2)); put("1111111110110110", new Pair<>((byte) 8, (byte) 3)); put("1111111110110111", new Pair<>((byte) 8, (byte) 4)); put("1111111110111000", new Pair<>((byte) 8, (byte) 5)); put("1111111110111001", new Pair<>((byte) 8, (byte) 6)); put("1111111110111010", new Pair<>((byte) 8, (byte) 7)); put("1111111110111011", new Pair<>((byte) 8, (byte) 8)); put("1111111110111100", new Pair<>((byte) 8, (byte) 9)); put("1111111110111101", new Pair<>((byte) 8, (byte) 10));
            put("111111001", new Pair<>((byte) 9, (byte) 1)); put("1111111110111110", new Pair<>((byte) 9, (byte) 2)); put("1111111110111111", new Pair<>((byte) 9, (byte) 3)); put("1111111111000000", new Pair<>((byte) 9, (byte) 4)); put("1111111111000001", new Pair<>((byte) 9, (byte) 5)); put("1111111111000010", new Pair<>((byte) 9, (byte) 6)); put("1111111111000011", new Pair<>((byte) 9, (byte) 7)); put("1111111111000100", new Pair<>((byte) 9, (byte) 8)); put("1111111111000101", new Pair<>((byte) 9, (byte) 9)); put("1111111111000110", new Pair<>((byte) 9, (byte) 10));
            put("111111010", new Pair<>((byte) 10, (byte) 1)); put("1111111111000111", new Pair<>((byte) 10, (byte) 2)); put("1111111111001000", new Pair<>((byte) 10, (byte) 3)); put("1111111111001001", new Pair<>((byte) 10, (byte) 4)); put("1111111111001010", new Pair<>((byte) 10, (byte) 5)); put("1111111111001011", new Pair<>((byte) 10, (byte) 6)); put("1111111111001100", new Pair<>((byte) 10, (byte) 7)); put("1111111111001101", new Pair<>((byte) 10, (byte) 8)); put("1111111111001110", new Pair<>((byte) 10, (byte) 9)); put("1111111111001111", new Pair<>((byte) 10, (byte) 10));
            put("1111111001", new Pair<>((byte) 11, (byte) 1)); put("1111111111010000", new Pair<>((byte) 11, (byte) 2)); put("1111111111010001", new Pair<>((byte) 11, (byte) 3)); put("1111111111010010", new Pair<>((byte) 11, (byte) 4)); put("1111111111010011", new Pair<>((byte) 11, (byte) 5)); put("1111111111010100", new Pair<>((byte) 11, (byte) 6)); put("1111111111010101", new Pair<>((byte) 11, (byte) 7)); put("1111111111010110", new Pair<>((byte) 11, (byte) 8)); put("1111111111010111", new Pair<>((byte) 11, (byte) 9)); put("1111111111011000", new Pair<>((byte) 11, (byte) 10));
            put("1111111010", new Pair<>((byte) 12, (byte) 1)); put("1111111111011001", new Pair<>((byte) 12, (byte) 2)); put("1111111111011010", new Pair<>((byte) 12, (byte) 3)); put("1111111111011011", new Pair<>((byte) 12, (byte) 4)); put("1111111111011100", new Pair<>((byte) 12, (byte) 5)); put("1111111111011101", new Pair<>((byte) 12, (byte) 6)); put("1111111111011110", new Pair<>((byte) 12, (byte) 7)); put("1111111111011111", new Pair<>((byte) 12, (byte) 8)); put("1111111111100000", new Pair<>((byte) 12, (byte) 9)); put("1111111111100001", new Pair<>((byte) 12, (byte) 10));
            put("11111111000", new Pair<>((byte) 13, (byte) 1)); put("1111111111100010", new Pair<>((byte) 13, (byte) 2)); put("1111111111100011", new Pair<>((byte) 13, (byte) 3)); put("1111111111100100", new Pair<>((byte) 13, (byte) 4)); put("1111111111100101", new Pair<>((byte) 13, (byte) 5)); put("1111111111100110", new Pair<>((byte) 13, (byte) 6)); put("1111111111100111", new Pair<>((byte) 13, (byte) 7)); put("1111111111101000", new Pair<>((byte) 13, (byte) 8)); put("1111111111101001", new Pair<>((byte) 13, (byte) 9)); put("1111111111101010", new Pair<>((byte) 13, (byte) 10));
            put("1111111111101011", new Pair<>((byte) 14, (byte) 1)); put("1111111111101100", new Pair<>((byte) 14, (byte) 2)); put("1111111111101101", new Pair<>((byte) 14, (byte) 3)); put("1111111111101110", new Pair<>((byte) 14, (byte) 4)); put("1111111111101111", new Pair<>((byte) 14, (byte) 5)); put("1111111111110000", new Pair<>((byte) 14, (byte) 6)); put("1111111111110001", new Pair<>((byte) 14, (byte) 7)); put("1111111111110010", new Pair<>((byte) 14, (byte) 8)); put("1111111111110011", new Pair<>((byte) 14, (byte) 9)); put("1111111111110100", new Pair<>((byte) 14, (byte) 10));
            put("11111111001", new Pair<>((byte) 15, (byte) 0)); put("1111111111110101", new Pair<>((byte) 15, (byte) 1)); put("1111111111110110", new Pair<>((byte) 15, (byte) 2)); put("1111111111110111", new Pair<>((byte) 15, (byte) 3)); put("1111111111111000", new Pair<>((byte) 15, (byte) 4)); put("1111111111111001", new Pair<>((byte) 15, (byte) 5)); put("1111111111111010", new Pair<>((byte) 15, (byte) 6)); put("1111111111111011", new Pair<>((byte) 15, (byte) 7)); put("1111111111111100", new Pair<>((byte) 15, (byte) 8)); put("1111111111111101", new Pair<>((byte) 15, (byte) 9)); put("1111111111111110", new Pair<>((byte) 15, (byte) 10));
        }};
    }

    /**
     * Cuenta el número de bits mínimos con los que representar un número
     * @param num El número del cual queremos saber su tamaño en bits
     * @return El número de bits mínimos con los que representar un número
     */
    private static byte bitsNumero(byte num) {
        byte bits = 0;
        int numero = 0xFF & num;
        while (numero != 0) {
            bits++;
            numero >>>= 1;
        }
        return bits;
    }

    /**
     * Convierte un binary string en un byte array
     * @param rleY El binary string en cuestión
     * @return El byte array deseado
     */
    private List<Byte> toByteList(String rleY) {
        List<Byte> l = new ArrayList<>();
        byte n = 0;
        for (int i = 0; i < rleY.length(); ++i) {
            if (rleY.charAt(i) == '1') n++;
            if (i % 8 != 7) n<<=1;
            else {
                l.add(n);
                n = 0;
            }
        }
        if (n != 0) {
            n >>>= 1;
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
     * Algoritmo de compresión JPEG
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
        if (datosInput.length < 14) throw new FormatoErroneoException("El formato de .ppm no es correcto!");
        int pos = 0;
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
        if(!magicNumber.equals("P6")) throw new FormatoErroneoException("El formato de .ppm no es correcto!");
        while (pos < datosInput.length - 1 && (char)datosInput[pos] == '#') {
            while ((char)datosInput[pos] != '\n') { //avoiding comments between parameters...
                ++pos;
            }
            ++pos;
        }
        buff = new StringBuilder();
        while (pos < datosInput.length - 1 && (char)datosInput[pos] != '\n') {
            buff.append((char) datosInput[pos]);
            result.add(datosInput[pos]);
            ++pos;
        }
        result.add((byte)'\n');
        ++pos;
        String[] widthHeight = buff.toString().split(" ");  //read and split dimensions into two (one for each value)
        if (widthHeight.length > 2 || Integer.parseInt(widthHeight[0]) < 1 || Integer.parseInt(widthHeight[1]) < 1) throw new FormatoErroneoException("El formato de .ppm no es correcto!");
        while (pos < datosInput.length - 1 && (char)datosInput[pos] == '#') {
            while ((char)datosInput[pos] != '\n') { //avoiding comments between parameters...
                ++pos;
            }
            ++pos;
        }
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
        //end of header reading

        //start of pixelmap reading
        int width = Integer.parseInt(widthHeight[0]);  //string to int of image width
        int height = Integer.parseInt(widthHeight[1]); //string to int of image height
        if (width * height > (datosInput.length - pos) / 3 || width * height < (datosInput.length - pos) / 3) throw new FormatoErroneoException("El formato de .ppm no es correcto!");
        int paddedWidth, paddedHeight;
        if (width % 8 != 0) paddedWidth = width + (8 - width % 8);
        else paddedWidth = width;
        if (height % 8 != 0) paddedHeight = height + (8 - height % 8);
        else paddedHeight = height;
        double[][] Y = new double[paddedHeight][paddedWidth];
        double[][] Cb = new double[paddedHeight][paddedWidth];
        double[][] Cr = new double[paddedHeight][paddedWidth];
        double[] rgb = new double[3];//red green blue
        for (int x = 0; x < height; ++x) {//image color decomposition in YCbCr and centering values to 0 (range [-128,127]) (padding boundaries to have 8 multiple dimensions (needed for DCT))
            for (int y = 0; y < width; ++y) {
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
        //end of pixelmap reading

        //start of image compression
        int downSampledPaddedHeight, downSampledPaddedWidth;
        if ((paddedHeight/2) % 8 == 0) downSampledPaddedHeight = paddedHeight/2;
        else downSampledPaddedHeight = (paddedHeight/2) + 4;
        if ((paddedWidth/2) % 8 == 0) downSampledPaddedWidth = paddedWidth/2;
        else downSampledPaddedWidth = (paddedWidth/2) + 4;
        double[][] downSampledCb = new double[downSampledPaddedHeight][downSampledPaddedWidth];
        double[][] downSampledCr = new double[downSampledPaddedHeight][downSampledPaddedWidth];
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

        String qualityPercent = Integer.toString(calidadHeader); //writting in header the compression quality
        char [] qualityPercentArray = qualityPercent.toCharArray(); //.imgc extension determines that its header will be the same one than the one of the .ppm image, but adding the quality compression in it
        for (char c : qualityPercentArray) result.add((byte)c);
        result.add((byte)'\n');

        int topu, topv;
        double alphau, alphav, cosu, cosv;
        double[][] buffY = new double[8][8];
        for (int x = 0; x < paddedHeight; x += 8) { //image DCT-II and quantization (done in pixel squares of 8x8) for luminance
            topu = x + 8;                           //for each luminance pixel square of 8x8 of the image, DCT-II algorithm is applied, letting calculate the image frequencies
            for (int y = 0; y < paddedWidth; y += 8) {
                topv = y + 8;
                for (int u = x; u < topu; ++u) {
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
                }
                boolean up = true;
                int i = x, j = y, it = 0;
                byte[] lineY = new byte[64]; //linear vector for zigzagged elements of Y before RLE
                while (i < topu && j < topv) { //zig-zag and RLE of Luminance 8x8 square (Huffman yet to be applied)
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
                String rleY = ""; //RLE: lossless compression of 8x8 block values
                byte howManyZeroes = 0; //how many zeroes have been ignored until a non zero value found in 8x8 block
                for (int k = 0; k < 64; ++k) {
                    if (lineY[k] == 0) {
                        howManyZeroes++;
                    }
                    else {
                        //rle refines that each time a non zero value is found, is written how many zeroes have been ignored before and the size of the value in bits
                        rleY = rleY.concat(ACHuffmanTable[howManyZeroes][bitsNumero(lineY[k])]); //substitution of those 2 values for Huffman value
                        rleY = rleY.concat(Integer.toBinaryString(lineY[k])); //then the non zero value is written
                        howManyZeroes = 0;
                    }
                }
                rleY = rleY.concat(ACHuffmanTable[0][0]); //end of block: (0,0)
                int sizeOfBlock = rleY.length() / 8;
                if (rleY.length() % 8 != 0) sizeOfBlock++;
                result.add((byte)sizeOfBlock); //size in bits of block defined before reading each block in order to know how many bytes have to be read
                //addition of block to result
                result.addAll(toByteList(rleY));
            }
        }
        double[][] buffCb = new double[8][8];
        double[][] buffCr = new double[8][8];
        for (int x = 0; x < downSampledPaddedHeight; x += 8) { //image DCT-II and quantization (done in pixel squares of 8x8) for chrominance
            topu = x + 8;                                      //for each chrominance pixel square of 8x8 of the image, DCT-II algorithm is applied, letting calculate the image frequencies
            for (int y = 0; y < downSampledPaddedWidth; y += 8) {
                topv = y + 8;
                for (int u = x; u < topu; ++u) {
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
                }
                boolean up = true;
                int i = x, j = y, it = 0;
                byte[] lineCb = new byte[64]; //linear vector for zigzagged elements of Cb before RLE
                byte[] lineCr = new byte[64]; //linear vector for zigzagged elements of Cr before RLE
                while (i < topu && j < topv) { //zig-zag and RLE of Chrominance 8x8 square (Huffman yet to be applied)
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
                List<Byte> rleCb = new ArrayList<>(); //RLE: lossless compression of 8x8 block values
                byte howManyZeroes = 0; //how many zeroes have been ignored until a non zero value found in 8x8 block
                for (int k = 0; k < 64; ++k) {
                    if (lineCb[k] == 0) {
                        howManyZeroes++;
                    }
                    else {
                        rleCb.add(howManyZeroes); //rle refines that each time a non zero value is found, is written how many zeroes have been ignored before
                        rleCb.add(bitsNumero(lineCb[k])); //size in bits of non zero value
                        rleCb.add(lineCb[k]); //then the non zero value is written
                        howManyZeroes = 0;
                    }
                }
                rleCb.add((byte)0); //end of block: (0,0)
                rleCb.add((byte)0);
                result.add((byte)rleCb.size()); //size of block defined before reading each block in order to know how many bytes have to be read
                //addition of block to result
                result.addAll(rleCb);
                howManyZeroes = 0;
                List<Byte> rleCr = new ArrayList<>(); //RLE: lossless compression of 8x8 block values
                for (int k = 0; k < 64; ++k) {
                    if (lineCr[k] == 0) {
                        howManyZeroes++;
                    }
                    else {
                        rleCr.add(howManyZeroes); //rle refines that each time a non zero value is found, is written how many zeroes have been ignored before
                        rleCr.add(bitsNumero(lineCr[k])); //size in bits of non zero value
                        rleCr.add(lineCr[k]); //then the non zero value is written
                        howManyZeroes = 0;
                    }
                }
                rleCr.add((byte)0); //end of block: (0,0)
                rleCr.add((byte)0);
                result.add((byte)rleCr.size()); //size of block defined before reading each block in order to know how many bytes have to be read
                //addition of block to result
                result.addAll(rleCr);
            }
        }
        //end of image compression
        byte[] datosOutput = new byte[result.size()]; //writting image data in output byte array
        for (int i = 0; i < result.size(); ++i) {
            datosOutput[i] = result.get(i);
        }
        long endTime = System.nanoTime(), totalTime = endTime - startTime; //ending time and total execution time
        return new OutputAlgoritmo(totalTime, datosOutput); //returning output time and data byte array
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
        if (datosInput.length < 16) throw new FormatoErroneoException("El formato de .imgc no es correcto!");
        int pos = 0;
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
        String[] widthHeight = buff.toString().split(" ");  //read and split dimensions into two (one for each value)
        if (widthHeight.length > 2 || Integer.parseInt(widthHeight[0]) < 1 || Integer.parseInt(widthHeight[1]) < 1) throw new FormatoErroneoException("El formato de .imgc no es correcto!");
        buff = new StringBuilder();
        while (pos < datosInput.length - 1 && (char)datosInput[pos] != '\n') {
            buff.append((char) datosInput[pos]);
            result.add(datosInput[pos]);
            ++pos;
        }
        result.add((byte)'\n');
        ++pos;
        String rgbMVal = buff.toString(); //string of rgb maximum value per pixel (8 bits)
        if (!rgbMVal.equals("255")) throw new FormatoErroneoException("El formato de .imgc no es correcto!");
        buff = new StringBuilder();
        while (pos < datosInput.length - 1 && (char)datosInput[pos] != '\n') {
            buff.append((char) datosInput[pos]);
            ++pos;
        }
        ++pos;
        String quality = buff.toString();
        if(quality.length() > 1) throw new FormatoErroneoException("El formato de .imgc no es correcto!");
        //end of header reading

        int width = Integer.parseInt(widthHeight[0]);  //string to int
        int height = Integer.parseInt(widthHeight[1]); //string to int
        int rgbMaxVal = Integer.parseInt(rgbMVal); //string to int of rgb maximum value per pixel

        //start of pixelmap reading and image decompression
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
        int[][] Y = new int[paddedHeight][paddedWidth];//luminance
        int[][] Cb = new int[downSampledPaddedHeight][downSampledPaddedWidth];//chrominance blue
        int[][] Cr = new int[downSampledPaddedHeight][downSampledPaddedWidth];//chrominance red
        int topi, topj;
        double[][] buffY = new double[8][8];
        double alphau, alphav, cosu, cosv;
        for (int x = 0; x < paddedHeight; x += 8) { //image inverse quantization and DCT-III (aka inverse DCT) (done in pixel squares of 8x8) for luminance
            topi = x + 8;                           //for each luminance pixel square of 8x8 of the image, inverse downsampling and DCT-III (aka inverse DCT) algorithm are applied, letting recover the image value
            for (int y = 0; y < paddedWidth; y += 8) {
                topj = y + 8;
                boolean up = true;
                int k = x, l = y;
                int rleSize = datosInput[pos++];
                String huffmanString = "";
                for (int it = 0; it < rleSize; ++it) huffmanString = huffmanString.concat(Integer.toBinaryString(datosInput[pos++]));
                List<Byte> huffmanList = new ArrayList<>();
                String huffmanBuff = "";
                for (int it = 0; it < huffmanString.length(); ++it) {
                    huffmanBuff += huffmanString.charAt(it);
                    Pair<Byte, Byte> pair = ACInverseHuffmanTable.get(huffmanBuff);
                    if (pair != null) {
                        huffmanList.add(pair.getKey());
                        Byte value = pair.getValue();
                        huffmanList.add(value);
                        StringBuilder runlength = new StringBuilder();
                        for (int n = 0; n < value; ++n) {
                            runlength.append(huffmanString.charAt(++it));
                        }
                        huffmanList.add(Byte.parseByte(runlength.toString(), 2));
                        huffmanBuff = "";
                    }
                }
                byte[] lineY = new byte[64];
                int lineYit = 0;
                for (int it = 0; it < huffmanList.size(); ++it) { //RLE and Huffman lossless decompression for Luminance
                    int howManyZeroes = huffmanList.get(it++);
                    int size = huffmanList.get(it++);
                    if (howManyZeroes != 0 || size != 0) {
                        byte value = huffmanList.get(it++);
                        for (int z = 0; z < howManyZeroes; ++z) lineY[lineYit++] = 0;
                        lineY[lineYit++] = value;
                    }
                    else {
                        while (lineYit < 64) lineY[lineYit++] = 0;
                        it = rleSize;
                    }
                }
                lineYit = 0;
                while (k < topi && l < topj) { //zig-zag reading Luminance and inverse downsampling values (Huffman yet to be applied)
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
                for (int i = x; i < topi; ++i) {
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
                                buffY[i%8][j%8] += alphau * alphav * (double)Y[u][v] * cosu * cosv;
                            }
                        }
                        buffY[i%8][j%8] *= 0.25;
                    }
                }
                for (int i = x; i < topi; ++i) {
                    for (int j = y; j < topj; ++j) {
                        Y[i][j] = (int)Math.round(buffY[i%8][j%8]);
                    }
                }
            }
        }
        double[][] buffCb = new double[8][8];
        double[][] buffCr = new double[8][8];
        for (int x = 0; x < downSampledPaddedHeight; x += 8) { //image inverse quantization and DCT-III (aka inverse DCT) (done in pixel squares of 8x8) for chrominance
            topi = x + 8;                                      //for each chrominance pixel square of 8x8 of the image, inverse downsampling and DCT-III (aka inverse DCT) algorithm are applied, letting recover the image values
            for (int y = 0; y < downSampledPaddedWidth; y += 8) {
                topj = y + 8;
                boolean up = true;
                int k = x, l = y;
                int rleSize = datosInput[pos++];
                byte[] lineCb = new byte[64];
                int lineCbit = 0;
                for (int it = 0; it < rleSize; ++it) { //RLE lossless decompression for Luminance
                    int howManyZeroes = datosInput[pos++];
                    int size = datosInput[pos++];
                    if (howManyZeroes != 0 || size != 0) {
                        byte value = datosInput[pos++];
                        for (int z = 0; z < howManyZeroes; ++z) lineCb[lineCbit++] = 0;
                        lineCb[lineCbit++] = value;
                    }
                    else {
                        while (lineCbit < 64) lineCb[lineCbit++] = 0;
                        it = rleSize;
                    }
                }
                lineCbit = 0;
                rleSize = datosInput[pos++];
                byte[] lineCr = new byte[64];
                int lineCrit = 0;
                for (int it = 0; it < rleSize; ++it) { //RLE lossless decompression for Luminance
                    int howManyZeroes = datosInput[pos++];
                    int size = datosInput[pos++];
                    if (howManyZeroes != 0 || size != 0) {
                        byte value = datosInput[pos++];
                        for (int z = 0; z < howManyZeroes; ++z) lineCr[lineCrit++] = 0;
                        lineCr[lineCrit++] = value;
                    }
                    else {
                        while (lineCrit < 64) lineCr[lineCrit++] = 0;
                        it = rleSize;
                    }
                }
                lineCrit = 0;
                while (k < topi && l < topj) { //zig-zag reading Chrominance and inverse downsampling values (Huffman yet to be applied)
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
                for (int i = x; i < topi; ++i) {
                    for (int j = y; j < topj; ++j) { //for each chrominance pixel of the 8x8 square, the DCT-III calculation is applied
                        buffY[i%8][j%8] = 0;
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
                                buffCb[i%8][j%8] += alphau * alphav * (double)Cb[u][v] * cosu * cosv;
                                buffCr[i%8][j%8] += alphau * alphav * (double)Cr[u][v] * cosu * cosv;
                            }
                        }
                        buffCb[i%8][j%8] *= 0.25;
                        buffCr[i%8][j%8] *= 0.25;
                    }
                }
                for (int i = x; i < topi; ++i) {
                    for (int j = y; j < topj; ++j) {
                        Cb[i][j] = (int)Math.round(buffCb[i%8][j%8]);
                        Cr[i][j] = (int)Math.round(buffCr[i%8][j%8]);
                    }
                }
            }
        }

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
        //end of image reading and decompression

        byte[] datosOutput = new byte[result.size()]; //writting image data in output byte array
        for (int i = 0; i < result.size(); ++i) {
            datosOutput[i] = result.get(i);
        }
        long endTime = System.nanoTime(), totalTime = endTime - startTime; //ending time and total execution time
        return new OutputAlgoritmo(totalTime, datosOutput); //returning output time and data byte array
    }
}
