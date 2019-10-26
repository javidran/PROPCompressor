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

    private int calidad;
    private double[][] DCT = new double[8][8];
    private int[][] LuminanceQuantizationTable = new int[8][8];   //50% compression
    private int[][] ChrominanceQuantizationTable = new int[8][8]; //50% compression

    public static JPEG getInstance()
    {
        if (instance == null)
            instance = new JPEG();

        return instance;
    }

    public JPEG() {
        calidad=50;
        DCT = new double[][] {
                {.3536,  .3536,  .3536,  .3536,  .3536,  .3536,  .3536,  .3536},
                {.4904,  .4157,  .2778,  .0975, -.0975, -.2778, -.4157, -.4904},
                {.4619,  .1913, -.1913, -.4619, -.4619, -.1913,  .1913,  .4619},
                {.4157, -.0975, -.4904, -.2778,  .2778,  .4904,  .0975, -.4157},
                {.3536, -.3536, -.3536,  .3536,  .3536, -.3536, -.3536,  .3536},
                {.2778, -.4904,  .0975,  .4157, -.4157, -.0975,  .4904, -.2778},
                {.1913, -.4619,  .4619, -.1913, -.1913,  .4619, -.4619,  .1913},
                {.0975, -.2778,  .4157, -.4904,  .4904, -.4157,  .2778, -.0975}
        };
		/*DCT = new double[][] {
			{ 6.1917, -0.3411,  1.2418,  0.1492,  0.1583,  0.2742, -0.0724,  0.0561},
			{ 0.2205,  0.0214,  0.4503,  0.3947, -0.7846, -0.4391,  0.1001, -0.2554},
			{ 1.0423,  0.2214, -1.0017, -0.2720,  0.0789, -0.1952,  0.2801,  0.4713},
			{-0.2340, -0.0392, -0.2617, -0.2866,  0.6351,  0.3501, -0.1433,  0.3550},
			{ 0.2750,  0.0226,  0.1229,  0.2183, -0.2583, -0.0742, -0.2042, -0.5906},
			{ 0.0653,  0.0428, -0.4721, -0.2905,  0.4745,  0.2875, -0.0284, -0.1311},
			{ 0.3169,  0.0541, -0.1033, -0.0225, -0.0056,  0.1017, -0.1650, -0.1500},
			{-0.2970, -0.0627,  0.1960,  0.0644, -0.1136, -0.1031,  0.1887,  0.1444}
		};*/
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
        //ADD PRIVATE FUNCTIONS WITH COMMON CODE
        //ADD SINGLETON METHODS
        //WRITE AS JFIF
    }

    public OutputAlgoritmo comprimir(File fileIn) { //pre: fileIn is .ppm P6, each of the three header parameters (magicNumber, dimensions, maxRGBValue) is in a different line, comments are between parameters
        long startTime = System.nanoTime(); //starting time
        File fileOut = new File(fileIn.getAbsolutePath().replace(".ppm", "_comp.imgc")); //custom output format THIS WILL BE TAKEN BY CONTROLLER
        try {
            BufferedReader originalImage = new BufferedReader (new FileReader(fileIn)); //creation of buffered reader to read header
            int fileOffset = 0;
            String buff = originalImage.readLine();
            String magicNumber = buff; //read .ppm magicNumber, which is P6 (pixels are codified in binary)
            buff = originalImage.readLine();
            while (buff.contains("#")) { //avoiding comments between parameters...
                fileOffset += buff.length() + 1;
                buff = originalImage.readLine();
            }
            String[] widthHeight = buff.split(" ");  //read and split dimensions into two (one for each value)
            buff = originalImage.readLine();
            while (buff.contains("#")) { //avoiding comments...
                fileOffset += buff.length() + 1;
                buff = originalImage.readLine();
            }
            String rgbMVal = buff; //string of rgb maximum value per pixel (from 1 to 255, 8 bits)
            buff = originalImage.readLine();
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

			/*int topi = 0, topj = 0;
			double[][] buffY = new double[8][8];
			double[][] buffCb = new double[8][8];
			double[][] buffCr = new double[8][8];
			for (int x = 0; x < height; x += 8) { //image DCT and quantization (done in pixel squares of 8x8)
				if (x + 7 < height) topi = x + 8;
				else topi = height;
				for (int y = 0; y < width; y += 8) {
					for (int i = x; i < topi; ++i) {
						for (int j = y; j < topj; ++j) {
							for (int k = x; k < topi; ++k) {
								buffY[i%8][j%8] += DCT[i%8][k%8] * Y[k][j];
								buffCb[i%8][j%8] += DCT[i%8][k%8] * Cb[k][j];
								buffCr[i%8][j%8] += DCT[i%8][k%8] * Cr[k][j];
							}
							Y[i][j] = buffY[i%8][j%8];
							Cb[i][j] = buffCb[i%8][j%8];
							Cr[i][j] = buffCb[i%8][j%8];
							System.out.println(buffY[i%8][j%8]+" "+buffCb[i%8][j%8]+" "+buffCr[i%8][j%8]);
						}
					}
					for (int i = x; i < topi; ++i) {
						for (int j = y; j < topj; ++j) {
							for (int k = x; k < topi; ++k) {
								buffY[i%8][j%8] += Y[i][k] * DCT[j%8][k%8];
								buffCb[i%8][j%8] += Cb[i][k] * DCT[j%8][k%8];
								buffCr[i%8][j%8] += Cr[i][k] * DCT[j%8][k%8];
							}
							Y[i][j] = buffY[i%8][j%8] / LuminanceQuantizationTable[i%8][j%8];
							Cb[i][j] = buffCb[i%8][j%8] / ChrominanceQuantizationTable[i%8][j%8];
							Cr[i][j] = buffCb[i%8][j%8] / ChrominanceQuantizationTable[i%8][j%8];
						}
					}
				}
			}*/

            BufferedWriter compressedImage = new BufferedWriter(new FileWriter(fileOut));
            compressedImage.write(magicNumber+"\n", 0, magicNumber.length() + 1); //writing same header as .ppm fileIn
            compressedImage.write(widthHeight[0]+" "+widthHeight[1]+"\n", 0, widthHeight[0].length() + widthHeight[1].length() + 2);
            compressedImage.write(rgbMVal + "\n", 0, rgbMVal.length() + 1);
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
        //System.out.println("Execution time (compression): "+totalTime);
        OutputAlgoritmo outAlg = new OutputAlgoritmo((int)totalTime, fileOut);
        return outAlg;
    }

    public OutputAlgoritmo descomprimir(File fileIn) { //pre: file is .imgc and it is a .ppm P6 compressed file
        long startTime = System.nanoTime(); //starting time
        File fileOut = new File(fileIn.getAbsolutePath().replace("_comp.imgc", "_out.ppm")); //custom output format THIS WILL BE TAKEN BY CONTROLLER
        try {
            BufferedReader originalImage = new BufferedReader (new FileReader(fileIn)); //creation of buffered reader
            String magicNumber = originalImage.readLine(); //read ppm magicNumber
            String[] widthHeight = originalImage.readLine().split(" ");  //read and split dimensions into two (one for each value)
            String rgbMVal = originalImage.readLine();
            int width = Integer.parseInt(widthHeight[0]);  //string to int
            int height = Integer.parseInt(widthHeight[1]); //string to int
            int rgbMaxVal = Integer.parseInt(rgbMVal); //string to int of rgb maximum value per pixel
            String space = " ", eol = "\n";
            int fileOffset = magicNumber.getBytes().length + widthHeight[0].getBytes().length + space.getBytes().length + widthHeight[1].getBytes().length + rgbMVal.getBytes().length + eol.getBytes().length * 3; //skipping already read header...
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
        //System.out.println("Execution time (decompression): "+totalTime);
        OutputAlgoritmo outAlg = new OutputAlgoritmo((int)totalTime, fileOut);
        return outAlg;
    }
}
