// Creado por Joan Gamez Rodriguez
package DomainLayer.Algoritmos;

import Exceptions.FormatoErroneoException;
import java.util.ArrayList;
import java.util.List;

public class JPEG implements CompresorDecompresor {
    private static JPEG instance = null;

    private double calidad;
    private static int calidadHeader;
    private static int[][] LuminanceQuantizationTable = new int[8][8];   //50% compression
    private static int[][] ChrominanceQuantizationTable = new int[8][8]; //50% compression

    public static JPEG getInstance()
    {
        if (instance == null)
            instance = new JPEG();

        return instance;
    }

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
    }

    public void setCalidad(int calidad) {
        if (calidad < 10) calidad = 10; //narrowing out-of-bounds quality preset to nearest value
        else if (calidad > 70) calidad = 70;
        if (calidad > 50) this.calidad = (100.0 - (double)calidad) / 50.0; //calculating new quality scalar and setting it
        else this.calidad = 50.0 / (double)calidad;
        calidadHeader = calidad; //setting new quality percentage (the one passed as parameter)
    }

    @Override
    public OutputAlgoritmo comprimir(byte[] datosInput) throws FormatoErroneoException {
        long startTime = System.nanoTime(); //starting time
        List<Byte> result = new ArrayList<>();
        int pos = 0;
        String buff = "";
        while ((char)datosInput[pos] != '\n') {
            buff += (char)datosInput[pos];
            result.add(datosInput[pos]);
            ++pos;
        }
        result.add((byte)'\n');
        ++pos;
        String magicNumber = buff; //read .ppm magicNumber, which is P6 (pixels are codified in binary)
        if(!magicNumber.equals("P6")) throw new FormatoErroneoException("El formato de .ppm no es correcto!");
        while ((char)datosInput[pos] == '#') {
            while ((char)datosInput[pos] != '\n') { //avoiding comments between parameters...
                ++pos;
            }
            ++pos;
        }
        buff = "";
        while ((char)datosInput[pos] != '\n') {
            buff += (char)datosInput[pos];
            result.add(datosInput[pos]);
            ++pos;
        }
        result.add((byte)'\n');
        ++pos;
        String[] widthHeight = buff.split(" ");  //read and split dimensions into two (one for each value)
        if (widthHeight.length > 2 || Integer.parseInt(widthHeight[0]) < 16 || Integer.parseInt(widthHeight[1]) < 16) throw new FormatoErroneoException("El formato de .ppm no es correcto!");
        while ((char)datosInput[pos] == '#') {
            while ((char)datosInput[pos] != '\n') { //avoiding comments between parameters...
                ++pos;
            }
            ++pos;
        }
        buff = "";
        while ((char)datosInput[pos] != '\n') {
            buff += (char)datosInput[pos];
            result.add(datosInput[pos]);
            ++pos;
        }
        result.add((byte)'\n');
        ++pos;
        String rgbMVal = buff; //string of rgb maximum value per pixel (8 bits)
        if (!rgbMVal.equals("255")) throw new FormatoErroneoException("El formato de .ppm no es correcto!");

        int width = Integer.parseInt(widthHeight[0]);  //string to int of image width
        int height = Integer.parseInt(widthHeight[1]); //string to int of image height
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

        int downSampledPaddedHeight, downSampledPaddedWidth;
        if ((paddedHeight/2) % 8 == 0) downSampledPaddedHeight = paddedHeight/2;
        else downSampledPaddedHeight = (paddedHeight/2) + 4;
        if ((paddedWidth/2) % 8 == 0) downSampledPaddedWidth = paddedWidth/2;
        else downSampledPaddedWidth = (paddedWidth/2) + 4;
        double[][] downSampledCb = new double[downSampledPaddedHeight][downSampledPaddedWidth];
        double[][] downSampledCr = new double[downSampledPaddedHeight][downSampledPaddedWidth];
        for (int x = 0; x < paddedHeight; x += 2) { //Chrominance DownSampled to 25% each colour channel. Compressed image will be 50% less large than original thanks to this
            for (int y = 0; y < paddedWidth; y += 2) {
                if (x < paddedHeight - 2 && y < paddedWidth - 2) {
                    downSampledCb[x/2][y/2] = (Cb[x][y] + Cb[x][y+1] + Cb[x+1][y] + Cb[x+1][y+1]) / 4;
                    downSampledCr[x/2][y/2] = (Cr[x][y] + Cr[x][y+1] + Cr[x+1][y] + Cr[x+1][y+1]) / 4;
                }
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

        String qualityPercent = Integer.toString(calidadHeader);
        char [] qualityPercentArray = qualityPercent.toCharArray();
        for (char c : qualityPercentArray) result.add((byte)c);
        result.add((byte)'\n');

        int topu, topv;
        double alphau, alphav, cosu, cosv;
        double[][] buffY = new double[8][8];
        for (int x = 0; x < paddedHeight; x += 8) { //image DCT-II and quantization (done in pixel squares of 8x8) for luminance
            topu = x + 8;
            for (int y = 0; y < paddedWidth; y += 8) {
                topv = y + 8;
                for (int u = x; u < topu; ++u) {
                    if (u % 8 == 0) alphau = 1 / Math.sqrt(2);
                    else alphau = 1;
                    for (int v = y; v < topv; ++v) {
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
                int i = x, j = y;
                while (i < topu && j < topv) { //TEST: zig-zag writting Luminance (Huffman yet to be applied)
                    result.add((byte)Math.round(buffY[i%8][j%8]));
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
        }
        double[][] buffCb = new double[8][8];
        double[][] buffCr = new double[8][8];
        for (int x = 0; x < downSampledPaddedHeight; x += 8) { //image DCT-II and quantization (done in pixel squares of 8x8) for chrominance
            topu = x + 8;
            for (int y = 0; y < downSampledPaddedWidth; y += 8) {
                topv = y + 8;
                for (int u = x; u < topu; ++u) {
                    if (u % 8 == 0) alphau = 1 / Math.sqrt(2);
                    else alphau = 1;
                    for (int v = y; v < topv; ++v) {
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
                int i = x, j = y;
                while (i < topu && j < topv) { //TEST: zig-zag writting Chrominance (Huffman yet to be applied)
                    result.add((byte)Math.round(buffCb[i%8][j%8]));
                    result.add((byte)Math.round(buffCr[i%8][j%8]));
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
        }
        byte[] datosOutput = new byte[result.size()];
        for (int i = 0; i < result.size(); ++i) {
            datosOutput[i] = result.get(i);
        }
        long endTime = System.nanoTime(), totalTime = endTime - startTime; //ending time and total execution time
        return new OutputAlgoritmo(totalTime, datosOutput);
    }

    @Override
    public OutputAlgoritmo descomprimir(byte[] datosInput) throws Exception{
        long startTime = System.nanoTime(); //starting time
        List<Byte> result = new ArrayList<>();
        int pos = 0;
        String buff = "";
        while ((char)datosInput[pos] != '\n') {
            buff += (char)datosInput[pos];
            result.add(datosInput[pos]);
            ++pos;
        }
        result.add((byte)'\n');
        ++pos;
        String magicNumber = buff; //read .ppm magicNumber, which is P6 (pixels are codified in binary)
        if(!magicNumber.equals("P6")) throw new FormatoErroneoException("El formato de .ppm no es correcto!");
        buff = "";
        while ((char)datosInput[pos] != '\n') {
            buff += (char)datosInput[pos];
            result.add(datosInput[pos]);
            ++pos;
        }
        result.add((byte)'\n');
        ++pos;
        String[] widthHeight = buff.split(" ");  //read and split dimensions into two (one for each value)
        if (widthHeight.length > 2 || Integer.parseInt(widthHeight[0]) < 16 || Integer.parseInt(widthHeight[1]) < 16) throw new FormatoErroneoException("El formato de .ppm no es correcto!");
        buff = "";
        while ((char)datosInput[pos] != '\n') {
            buff += (char)datosInput[pos];
            result.add(datosInput[pos]);
            ++pos;
        }
        result.add((byte)'\n');
        ++pos;
        String rgbMVal = buff; //string of rgb maximum value per pixel (8 bits)
        if (!rgbMVal.equals("255")) throw new FormatoErroneoException("El formato de .ppm no es correcto!");
        buff = "";
        while ((char)datosInput[pos] != '\n') {
            buff += (char)datosInput[pos];
            ++pos;
        }
        ++pos;
        String quality = buff;
        if(quality.length() > 2) throw new Exception("El formato de .ppm no es correcto!");
        int width = Integer.parseInt(widthHeight[0]);  //string to int
        int height = Integer.parseInt(widthHeight[1]); //string to int
        int rgbMaxVal = Integer.parseInt(rgbMVal); //string to int of rgb maximum value per pixel

        int paddedWidth, paddedHeight;
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
            topi = x + 8;
            for (int y = 0; y < paddedWidth; y += 8) {
                topj = y + 8;
                boolean up = true;
                int k = x, l = y;
                while (k < topi && l < topj) { //TEST: zig-zag reading Luminance (Huffman yet to be applied)
                    buffY[k%8][l%8] = (datosInput[pos++]) * (LuminanceQuantizationTable[k%8][l%8] * calidad); //(byte) because reads [0,255] but it's been stored as [-128,127]
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
                    for (int j = y; j < topj; ++j) {
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
            topi = x + 8;
            for (int y = 0; y < downSampledPaddedWidth; y += 8) {
                topj = y + 8;
                boolean up = true;
                int k = x, l = y;
                while (k < topi && l < topj) { //TEST: zig-zag reading Chrominance (Huffman yet to be applied)
                    buffCb[k%8][l%8] = (datosInput[pos++]) * (ChrominanceQuantizationTable[k%8][l%8] * calidad); //(byte) because reads [0,255] but it's been stored as [-128,127]
                    buffCr[k%8][l%8] = (datosInput[pos++]) * (ChrominanceQuantizationTable[k%8][l%8] * calidad); //(byte) because reads [0,255] but it's been stored as [-128,127]
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
                    for (int j = y; j < topj; ++j) {
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
        for (int x = 0; x < height; ++x) { //writing data into file (RGB)
            for (int y = 0; y < width; ++y) {
                rgb[0] = (int)Math.round(1.164 * (double)(Y[x][y] - 16 + 128) + 1.596 * (double)(Cr[x/2][y/2]));
                rgb[1] = (int)Math.round(1.164 * (double)(Y[x][y] - 16 + 128) - 0.391 * (double)(Cb[x/2][y/2]) - 0.813 * (double)(Cr[x/2][y/2]));
                rgb[2] = (int)Math.round(1.164 * (double)(Y[x][y] - 16 + 128) + 2.018 * (double)(Cb[x/2][y/2]));
                result.add((byte)Math.max(0, Math.min(rgb[0], rgbMaxVal))); //controlling and correcting any value that is out of range (0 to rgbMaxVal)
                result.add((byte)Math.max(0, Math.min(rgb[1], rgbMaxVal)));
                result.add((byte)Math.max(0, Math.min(rgb[2], rgbMaxVal)));
            }
        }
        byte[] datosOutput = new byte[result.size()];
        for (int i = 0; i < result.size(); ++i) {
            datosOutput[i] = result.get(i);
        }
        long endTime = System.nanoTime(), totalTime = endTime - startTime; //ending time and total execution time
        return new OutputAlgoritmo(totalTime, datosOutput);
    }
}
