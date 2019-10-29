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
    private static int calidadPorcentaje;
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
        if (calidad < 0) calidad = 0;
        else if (calidad > 100) calidad = 100;
        if (calidad > 50) this.calidad = (100 - calidad) / calidad;
        else this.calidad = 50 / calidad;
        calidadPorcentaje = calidad;
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
            if (widthHeight.length > 2) throw new Exception("El formato de .ppm no es correcto!");
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

            FileInputStream fin = new FileInputStream(fileIn);
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
                    rgb[0] = (double)in.read();
                    rgb[1] = (double)in.read();
                    rgb[2] = (double)in.read();
                    Y[x][y] = 0.257 * rgb[0] + 0.504 * rgb[1] + 0.098 * rgb[2] + 16.0 - 128.0;
                    Cb[x][y] = - 0.148 * rgb[0] - 0.291 * rgb[1] + 0.439 * rgb[2];
                    Cr[x][y] = 0.439 * rgb[0] - 0.368 * rgb[1] - 0.071 * rgb[2];
                }
            }
            in.close();

			int topi = 0, topj = 0;
			double quantizationQuality;
			double[][] buffY = new double[8][8];
			double[][] buffCb = new double[8][8];
			double[][] buffCr = new double[8][8];
			for (int x = 0; x < height; x += 8) { //image DCT and quantization (done in pixel squares of 8x8)
				if (x + 7 < height) topi = x + 8;
				else topi = height;
				for (int y = 0; y < width; y += 8) {
					for (int i = x; i < topi; ++i) {
						for (int j = y; j < topj; ++j) {
						    double alphau, alphav;
						    if (i % 8 == 0) alphau = 1 / Math.sqrt(2);
						    else alphau = 1;
						    if (j % 8 == 0) alphav = 1 / Math.sqrt(2);
						    else alphav = 1;
                            buffY[i%8][j%8] = 0;
                            buffCb[i%8][j%8] = 0;
                            buffCr[i%8][j%8] = 0;
							for (int k = x; k < topi; ++k) {
							    for (int l = y; l < topj; ++l) {
                                    double cosa = Math.cos(((2 * k + 1) * (i % 8) * Math.PI) / 16);
                                    double cosv = Math.cos(((2 * l + 1) * (j % 8) * Math.PI) / 16);
                                    buffY[i%8][j%8] += Y[k][l] * cosa * cosv;
                                    buffCb[i%8][j%8] += Cb[k][l] * cosa * cosv;
                                    buffCr[i%8][j%8] += Cr[k][l] * cosa * cosv;
                                }
							}
                            buffY[i%8][j%8] *= (alphau * alphav * 0.25);
                            buffCb[i%8][j%8] *= (alphau * alphav * 0.25);
                            buffCr[i%8][j%8] *= (alphau * alphav * 0.25);
						}
					}
                    for (int i = x; i < topi; ++i) {
                        for (int j = y; j < topj; ++j) {
                            Y[i][j] = buffY[i%8][j%8] / (LuminanceQuantizationTable[i%8][j%8] * calidad);
                            Cb[i][j] = buffCb[i%8][j%8] / (ChrominanceQuantizationTable[i%8][j%8] * calidad);
                            Cr[i][j] = buffCb[i%8][j%8] / (ChrominanceQuantizationTable[i%8][j%8] * calidad);
                            System.out.println((int)Math.round(Y[i][j]));
                        }
                    }
				}
			}
            String qualityPercent = Integer.toString(calidadPorcentaje);
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
                    //System.out.println((int)Math.round(Y[x][y]));
                }
            }
            for (int x = 0; x < height; ++x) { //TEST: writing data into file (then Chrominance DownSampled to 25%, compressed image will weight 50% less than original)
                for (int y = 0; y < width; ++y) {
                    if (x%2 == 0 && y%2 == 0) {
                        if (x < height - 1 && y < width - 1) {
                            out.write((int)Math.round((Cb[x][y] + Cb[x][y+1] + Cb[x+1][y] + Cb[x+1][y+1]) / 4));
                            out.write((int)Math.round((Cr[x][y] + Cr[x][y+1] + Cr[x+1][y] + Cr[x+1][y+1]) / 4));
                        }
                        else if (x < height - 1 && y == width - 1) {
                            out.write((int)Math.round((Cb[x][y] + Cb[x+1][y]) / 2));
                            out.write((int)Math.round((Cr[x][y] + Cr[x+1][y]) / 2));
                        }
                        else if (x == height - 1 && y < width - 1) {
                            out.write((int)Math.round((Cb[x][y] + Cb[x][y+1]) / 2));
                            out.write((int)Math.round((Cr[x][y] + Cr[x][y+1]) / 2));
                        }
                        else {
                            out.write((int)Math.round(Cb[x][y]));
                            out.write((int)Math.round(Cr[x][y]));
                            //System.out.println((int)Math.round(Cb[x][y]));
                            //System.out.println((int)Math.round(Cr[x][y]));
                        }
                    }
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

            FileInputStream fin = new FileInputStream(fileIn);
            BufferedInputStream in = new BufferedInputStream(fin);
            int[][] R = new int[height][width];
            int[][] G = new int[height][width];
            int[][] B = new int[height][width];
            int[][] Y = new int[height][width];
            int[][] Cb = new int[height][width];//chrominance blue
            int[][] Cr = new int[height][width];//chrominance red
            in.skip(fileOffset);
            for (int x = 0; x < height; ++x) {//image luminace reading and [0,255] range reapplied
                for (int y = 0; y < width; ++y) {
                    Y[x][y] = (byte)in.read() + 128; //(byte) because reads [0,255] but it's been stored in [-128,127]
                }
            }
            for (int x = 0; x < height; x += 2) {//image chrominance reading and [0,255] range reapplied
                for (int y = 0; y < width; y += 2) {
                    Cb[x][y] = (byte)in.read() + 128; //(byte) because reads [0,255] but it's been stored in [-128,127]
                    Cr[x][y] = (byte)in.read() + 128; //(byte) because reads [0,255] but it's been stored in [-128,127]
                    if (x < height -1 && y < width -1) {
                        Cb[x][y+1] = Cb[x][y];
                        Cb[x+1][y] = Cb[x][y];
                        Cb[x+1][y+1] = (Cb[x][y] + Cb[x+1][y] + Cb[x][y+1]) / 3;
                        Cr[x][y+1] = Cr[x][y];
                        Cr[x+1][y] = Cr[x][y];
                        Cr[x+1][y+1] = (Cr[x][y] + Cr[x+1][y] + Cr[x][y+1]) / 3;
                    }
                    else if (x == height - 1 && y < width - 1) {
                        Cb[x][y+1] = Cb[x][y];
                        Cr[x][y+1] = Cr[x][y];
                    }
                    else if (y == width - 1 && x < height - 1) {
                        Cb[x+1][y] = Cb[x][y];
                        Cr[x+1][y] = Cr[x][y];
                    }
                }
            }
            in.close();

            for (int x = 0; x < height; ++x) {//image color recomposition from YCbCr to RGB
                for (int y = 0; y < width; ++y) {
                    R[x][y] = (int)Math.round(1.164 * (double)(Y[x][y] - 16) + 1.596 * (double)(Cr[x][y] - 128));
                    G[x][y] = (int)Math.round(1.164 * (double)(Y[x][y] - 16) - 0.391 * (double)(Cb[x][y] - 128) - 0.813 * (double)(Cr[x][y] - 128));
                    B[x][y] = (int)Math.round(1.164 * (double)(Y[x][y] - 16) + 2.018 * (double)(Cb[x][y] - 128));
                    if (R[x][y] > rgbMaxVal) { //controlling and correcting any value that is out of range (0 to rgbMaxVal)
                        R[x][y] = rgbMaxVal;
                    }
                    else if (R[x][y] < 0) {
                        R[x][y] = 0;
                    }
                    if (G[x][y] > rgbMaxVal) {
                        G[x][y] = rgbMaxVal;
                    }
                    else if (G[x][y] < 0) {
                        G[x][y] = 0;
                    }
                    if (B[x][y] > rgbMaxVal) {
                        B[x][y] = rgbMaxVal;
                    }
                    else if (B[x][y] < 0) {
                        B[x][y] = 0;
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
            for (int x = 0; x < height; ++x) { //TEST: writing data into file (RGB)
                for (int y = 0; y < width; ++y) {
                    out.write((byte)R[x][y]);
                    out.write((byte)G[x][y]);
                    out.write((byte)B[x][y]);
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
