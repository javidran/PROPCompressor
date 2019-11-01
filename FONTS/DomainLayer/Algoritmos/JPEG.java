// Creado por Joan Gamez Rodriguez
package DomainLayer.Algoritmos;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;

public class JPEG implements CompresorDecompresor {
    private static JPEG instance = null;

    private static double calidad;
    private static int calidadHeader;
    private static int[][] LuminanceQuantizationTable = new int[8][8];   //50% compression
    private static int[][] ChrominanceQuantizationTable = new int[8][8]; //50% compression

    public static JPEG getInstance()
    {
        if (instance == null)
            instance = new JPEG();

        return instance;
    }

    public JPEG() {
        calidad = 1.0;
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
        else if (calidad > 80) calidad = 80;
        if (calidad > 50) this.calidad = (100.0 - (double)calidad) / 50.0; //calculating new quality scalar and setting it
        else this.calidad = 50.0 / (double)calidad;
        calidadHeader = calidad; //setting new quality percentage (the one passed as parameter)
    }

    @Override
    public OutputAlgoritmo comprimir(File fileIn) throws Exception {
        long startTime = System.nanoTime(); //starting time
        File fileOut = new File(fileIn.getAbsolutePath().replace(".ppm", ".imgc")); //custom output format
        try {
            BufferedReader originalImage = new BufferedReader (new FileReader(fileIn)); //creation of buffered reader to read header
            int fileOffset = 0;
            String buff = originalImage.readLine();
            String magicNumber = buff; //read .ppm magicNumber, which is P6 (pixels are codified in binary)
            if(!magicNumber.equals("P6")) throw new Exception("El formato de .ppm no es correcto!");
            buff = originalImage.readLine();
            while (buff.contains("#")) { //avoiding comments between parameters...
                fileOffset += buff.length() + 1;
                buff = originalImage.readLine();
            }
            if (buff.endsWith(" ")) throw new Exception("El formato de .ppm no es correcto!");
            String[] widthHeight = buff.split(" ");  //read and split dimensions into two (one for each value)
            if (widthHeight.length > 2 || Integer.parseInt(widthHeight[0]) < 3 || Integer.parseInt(widthHeight[1]) < 3) throw new Exception("El formato de .ppm no es correcto!");
            buff = originalImage.readLine();
            while (buff.contains("#")) { //avoiding comments...
                fileOffset += buff.length() + 1;
                buff = originalImage.readLine();
            }
            String rgbMVal = buff; //string of rgb maximum value per pixel (8 bits)
            if (!rgbMVal.equals("255")) throw new Exception("El formato de .ppm no es correcto!");
            String space = " ", eol = "\n";
            fileOffset += magicNumber.getBytes().length + widthHeight[0].getBytes().length + space.getBytes().length + widthHeight[1].getBytes().length + rgbMVal.getBytes().length + eol.getBytes().length * 3; //skipping already read header...
            originalImage.close();

            FileInputStream fin = new FileInputStream(fileIn); //creation of buffered input stream to read pixel map
            BufferedInputStream in = new BufferedInputStream(fin);
            int width = Integer.parseInt(widthHeight[0]);  //string to int of image width
            int height = Integer.parseInt(widthHeight[1]); //string to int of image height
            double[][] Y = new double[height][width];
            double[][] Cb = new double[height][width];
            double[][] Cr = new double[height][width];
            double[] rgb = new double[3];//red green blue
            in.skip(fileOffset);
            for (int x = 0; x < height; ++x) {//image color decomposition in YCbCr and centering values to 0 (range [-128,127])
                for (int y = 0; y < width; ++y) {
                    rgb[0] = in.read();
                    rgb[1] = in.read();
                    rgb[2] = in.read();
                    Y[x][y] = 0.257 * rgb[0] + 0.504 * rgb[1] + 0.098 * rgb[2] + 16.0 - 128.0;
                    Cb[x][y] = - 0.148 * rgb[0] - 0.291 * rgb[1] + 0.439 * rgb[2];
                    Cr[x][y] = 0.439 * rgb[0] - 0.368 * rgb[1] - 0.071 * rgb[2];
                }
            }
            in.close();

            double[][] downSampledCb = new double[height/2][width/2];
            double[][] downSampledCr = new double[height/2][width/2];
            for (int x = 0; x < height; ++x) { //Chrominance DownSampled to 25% each colour channel. Compressed image will be 50% less large than original thanks to this
                for (int y = 0; y < width; ++y) {
                    if (x%2 == 0 && y%2 == 0) {
                        if (x < height - 1 && y < width - 1) {
                            downSampledCb[x/2][y/2] = (int)Math.round((Cb[x][y] + Cb[x][y+1] + Cb[x+1][y] + Cb[x+1][y+1]) / 4);
                            downSampledCr[x/2][y/2] = (int)Math.round((Cr[x][y] + Cr[x][y+1] + Cr[x+1][y] + Cr[x+1][y+1]) / 4);
                        }
                        else if (x < height - 1 && y == width - 1) {
                            if (y%2 != 0) {
                                downSampledCb[x/2][y/2] = (int)Math.round((Cb[x][y] + Cb[x+1][y]) / 2);
                                downSampledCr[x/2][y/2] = (int)Math.round((Cr[x][y] + Cr[x+1][y]) / 2);
                            }
                        }
                        else if (x == height - 1 && y < width - 1) {
                            if (x%2 != 0) {
                                downSampledCb[x/2][y/2] = (int)Math.round((Cb[x][y] + Cb[x][y+1]) / 2);
                                downSampledCr[x/2][y/2] = (int)Math.round((Cr[x][y] + Cr[x][y+1]) / 2);
                            }
                        }
                        else {
                            if (x%2 != 0 && y%2 != 0) {
                                downSampledCb[x/2][y/2] = (int)Math.round(Cb[x][y]);
                                downSampledCr[x/2][y/2] = (int)Math.round(Cr[x][y]);
                            }
                        }
                    }
                }
            }

			int topu = 0, topv = 0;
            double alphau, alphav, cosu, cosv;
            double[][] buffY = new double[8][8];
            for (int x = 0; x < height; x += 8) { //image DCT-II and quantization (done in pixel squares of 8x8) for luminance
                if (x + 7 < height) topu = x + 8;
                else topu = height;
                for (int y = 0; y < width; y += 8) {
                    if (y + 7 < width) topv = y + 8;
                    else topv = width;
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
                        }
                    }
                    for (int i = x; i < topu; ++i) {
                        for (int j = y; j < topv; ++j) {
                            Y[i][j] = buffY[i%8][j%8] / (LuminanceQuantizationTable[i%8][j%8] * calidad);
                        }
                    }
                }
            }
            double[][] buffCb = new double[8][8];
            double[][] buffCr = new double[8][8];
            for (int x = 0; x < height/2; x += 8) { //image DCT-II and quantization (done in pixel squares of 8x8) for chrominance
                if (x + 7 < height/2) topu = x + 8;
                else topu = height/2;
                for (int y = 0; y < width/2; y += 8) {
                    if (y + 7 < width/2) topv = y + 8;
                    else topv = width/2;
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
                            buffCr[u%8][v%8] *= (alphau * alphav * 0.25);
                        }
                    }
                    for (int i = x; i < topu; ++i) {
                        for (int j = y; j < topv; ++j) {
                            downSampledCb[i][j] = buffCb[i%8][j%8] / (ChrominanceQuantizationTable[i%8][j%8] * calidad);
                            downSampledCr[i][j] = buffCr[i%8][j%8] / (ChrominanceQuantizationTable[i%8][j%8] * calidad);
                        }
                    }
                }
            }

            String qualityPercent = Integer.toString(calidadHeader);
            BufferedWriter compressedImage = new BufferedWriter(new FileWriter(fileOut));
            compressedImage.write(magicNumber+"\n", 0, magicNumber.length() + 1); //writing same header as .ppm fileIn + jpeg quality
            compressedImage.write(widthHeight[0]+" "+widthHeight[1]+"\n", 0, widthHeight[0].length() + widthHeight[1].length() + 2);
            compressedImage.write(rgbMVal + "\n", 0, rgbMVal.length() + 1);
            compressedImage.write(qualityPercent+"\n", 0, qualityPercent.length() + 1);
            compressedImage.close();

            FileOutputStream fout = new FileOutputStream(fileOut, true);
            BufferedOutputStream  out= new BufferedOutputStream(fout);
            for (int x = 0; x < height; ++x) { //TEST: writing data into file (first Luminance)
                for (int y = 0; y < width; ++y) {
                    out.write((int)Math.round(Y[x][y]));
                }
            }
            for (int x = 0; x < height/2; ++x) { //TEST: writing data into file (then Chrominance DownSampled to 25%, compressed image will weight 50% less than original)
                for (int y = 0; y < width/2; ++y) {
                        out.write((int)Math.round(downSampledCb[x][y]));
                        out.write((int)Math.round(downSampledCr[x][y]));
                }
            }
            out.close();
        }
        catch(IOException e) {
            System.err.println("Error: "+e);
        }
        long endTime = System.nanoTime(), totalTime = endTime - startTime; //ending time and total execution time
        OutputAlgoritmo outAlg = new OutputAlgoritmo(totalTime, fileOut);
        return outAlg;
    }

    @Override
    public OutputAlgoritmo descomprimir(File fileIn) throws Exception{
        long startTime = System.nanoTime(); //starting time
        File fileOut = new File(fileIn.getAbsolutePath().replace(".imgc", "_out.ppm")); //custom output format
        try {
            BufferedReader originalImage = new BufferedReader (new FileReader(fileIn)); //creation of buffered reader
            String magicNumber = originalImage.readLine(); //read ppm magicNumber
            if(!magicNumber.equals("P6")) throw new Exception("El formato de .ppm no es correcto!");
            String[] widthHeight = originalImage.readLine().split(" ");  //read and split dimensions into two (one for each value)
            if (widthHeight.length > 2) throw new Exception("El formato de .ppm no es correcto!");
            String rgbMVal = originalImage.readLine();
            if(!rgbMVal.equals("255")) throw new Exception("El formato de .ppm no es correcto!");
            String quality = originalImage.readLine();
            if(quality.length() > 3) throw new Exception("El formato de .ppm no es correcto!");
            int width = Integer.parseInt(widthHeight[0]);  //string to int
            int height = Integer.parseInt(widthHeight[1]); //string to int
            int rgbMaxVal = Integer.parseInt(rgbMVal); //string to int of rgb maximum value per pixel
            int qualityPercent = Integer.parseInt(quality);
            String space = " ", eol = "\n";
            int fileOffset = magicNumber.getBytes().length + widthHeight[0].getBytes().length + space.getBytes().length + widthHeight[1].getBytes().length + rgbMVal.getBytes().length + quality.getBytes().length + eol.getBytes().length * 4; //skipping already read header...
            originalImage.close();

            FileInputStream fin = new FileInputStream(fileIn); //creation of buffered input stream to read pixel map
            BufferedInputStream in = new BufferedInputStream(fin);
            int[][] Y = new int[height][width];//luminance
            int[][] Cb = new int[height/2][width/2];//chrominance blue
            int[][] Cr = new int[height/2][width/2];//chrominance red
            in.skip(fileOffset);
            for (int x = 0; x < height; ++x) {//image luminace reading
                for (int y = 0; y < width; ++y) {
                    Y[x][y] = (byte)in.read(); //(byte) because reads [0,255] but it's been stored as [-128,127]
                }
            }
            for (int x = 0; x < height/2; ++x) {//image chrominance reading
                for (int y = 0; y < width/2; ++y) {
                    Cb[x][y] = (byte)in.read(); //(byte) because reads [0,255] but it's been stored as [-128,127]
                    Cr[x][y] = (byte)in.read(); //(byte) because reads [0,255] but it's been stored as [-128,127]
                }
            }
            in.close();

            int topi = 0, topj = 0;
            double[][] buffY = new double[8][8];
            double alphau, alphav, cosu, cosv;
            for (int x = 0; x < height; x += 8) { //image inverse quantization and DCT-III (aka inverse DCT) (done in pixel squares of 8x8) (luminance part)
                if (x + 7 < height) topi = x + 8;
                else topi = height;
                for (int y = 0; y < width; y += 8) {
                    if (y + 7 < width) topj = y + 8;
                    else topj = width;
                    for (int i = x; i < topi; ++i) {
                        for (int j = y; j < topj; ++j) {
                            Y[i][j] *= (LuminanceQuantizationTable[i%8][j%8] * calidad);
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
            for (int x = 0; x < height/2; x += 8) { //image inverse quantization and DCT-III (aka inverse DCT) (done in pixel squares of 8x8) (chrominance part)
                if (x + 7 < height/2) topi = x + 8;
                else topi = height/2;
                for (int y = 0; y < width/2; y += 8) {
                    if (y + 7 < width/2) topj = y + 8;
                    else topj = width/2;
                    for (int i = x; i < topi; ++i) {
                        for (int j = y; j < topj; ++j) {
                            Cb[i][j] *= (ChrominanceQuantizationTable[i%8][j%8] * calidad);
                            Cr[i][j] *= (ChrominanceQuantizationTable[i%8][j%8] * calidad);
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

            BufferedWriter decompressedImage = new BufferedWriter(new FileWriter(fileOut));
            decompressedImage.write(magicNumber+"\n", 0, magicNumber.length() + 1); //writing same header as .ppm fileIn
            decompressedImage.write(widthHeight[0]+" "+widthHeight[1]+"\n", 0, widthHeight[0].length() + widthHeight[1].length() + 2);
            decompressedImage.write(rgbMVal + "\n", 0, rgbMVal.length() + 1);
            decompressedImage.close();

            FileOutputStream fout = new FileOutputStream(fileOut, true);
            BufferedOutputStream out = new BufferedOutputStream(fout);
            int[] rgb = new int[3];
            int cb = Cb[0][0], cr = Cr[0][0];
            for (int x = 0; x < height; ++x) { //writing data into file (RGB)
                for (int y = 0; y < width; ++y) {
                    if (x < height - 1 && y < width - 1) {
                        cb = Cb[x/2][y/2];
                        cr = Cr[x/2][y/2];
                    }
                    else if (x < height - 1 && y == width - 1) {
                        if (y%2 == 0) {
                            cb = Cb[x/2][(y/2)-1];
                            cr = Cr[x/2][(y/2)-1];
                        }
                        else {
                            cb = Cb[x/2][(y/2)-2];
                            cr = Cr[x/2][(y/2)-2];
                        }
                    }
                    else if (x == height - 1 && y < width - 1) {
                        if (x%2 == 0) {
                            cb = Cb[(x/2)-1][y/2];
                            cr = Cr[(x/2)-1][y/2];
                        }
                        else {
                            cb = Cb[(x/2)-2][y/2];
                            cr = Cr[(x/2)-2][y/2];
                        }
                    }
                    else {
                        if (x%2 == 0 && y%2 == 0) {
                            cb = Cb[(x/2)-1][(y/2)-1];
                            cr = Cr[(x/2)-1][(y/2)-1];
                        }
                        else {
                            cb = Cb[(x/2)-2][(y/2)-2];
                            cr = Cr[(x/2)-2][(y/2)-2];
                        }
                    }
                    rgb[0] = (int)Math.round(1.164 * (double)(Y[x][y] - 16 + 128) + 1.596 * (double)(cr));
                    rgb[1] = (int)Math.round(1.164 * (double)(Y[x][y] - 16 + 128) - 0.391 * (double)(cb) - 0.813 * (double)(cr));
                    rgb[2] = (int)Math.round(1.164 * (double)(Y[x][y] - 16 + 128) + 2.018 * (double)(cb));
                    if (rgb[0] > rgbMaxVal) { //controlling and correcting any value that is out of range (0 to rgbMaxVal)
                        rgb[0] = rgbMaxVal;
                    }
                    else if (rgb[0] < 0) {
                        rgb[0] = 0;
                    }
                    if (rgb[1] > rgbMaxVal) {
                        rgb[1] = rgbMaxVal;
                    }
                    else if (rgb[1] < 0) {
                        rgb[1] = 0;
                    }
                    if (rgb[2] > rgbMaxVal) {
                        rgb[2] = rgbMaxVal;
                    }
                    else if (rgb[2] < 0) {
                        rgb[2] = 0;
                    }
                    out.write((byte)rgb[0]);
                    out.write((byte)rgb[1]);
                    out.write((byte)rgb[2]);
                }
            }
            out.close();
        }
        catch(IOException e) {
            System.err.println("Error: "+e);
        }
        long endTime = System.nanoTime(), totalTime = endTime - startTime; //ending time and total execution time
        OutputAlgoritmo outAlg = new OutputAlgoritmo(totalTime, fileOut);
        return outAlg;
    }
}
