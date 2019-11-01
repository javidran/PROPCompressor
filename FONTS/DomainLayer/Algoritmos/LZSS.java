// Creado por Jan Escorza Fuertes
package DomainLayer.Algoritmos;

import java.io.*;
import java.io.FileOutputStream;
import java.lang.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class LZSS implements CompresorDecompresor {
    private static LZSS instance = null;



    // static method to create instance of Singleton class
    public static LZSS getInstance()
    {
        if (instance == null)
            instance = new LZSS();

        return instance;
    }

    public static int fromByteArray(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getInt();
    }

    // private constructor restricted to this class itself
    public LZSS() {

    }

    @Override
    public OutputAlgoritmo comprimir(File fileIn) {
        //gestiono el pasar de file a string
        long startTime = System.nanoTime();
        File fileOut = new File(fileIn.getAbsolutePath().replace(".txt", ".lzss")); //custom output format

        //try {
            // READING the file into a byte array
            BufferedInputStream srcfile = null;
            List<Byte> srclist = new ArrayList<Byte>();// List to store bytes
            try {
                FileInputStream fin = new FileInputStream(fileIn); // create FileInputStream object
                srcfile = new BufferedInputStream(fin); // create object of BufferedInputStream
                while (srcfile.available() > 0) {
                    byte symbol = (byte) srcfile.read();
                    srclist.add(symbol);
                }
            } catch (FileNotFoundException e) {
                System.out.println("File not found" + e);
            } catch (IOException ioe) {
                System.out.println("Exception while reading the file " + ioe);
            } finally {
                try {
                    if (srcfile != null)
                        srcfile.close();// close the BufferedInputStream using close method
                } catch (IOException ioe) {
                    System.out.println("Error while closing the stream : " + ioe);
                }
            }
            // we have a list of all the bytes
            byte[] data = new byte[srclist.size()];
            int i = 0;
            for (Byte b : srclist) {
                data[i] = b;
                ++i;
            }
            // END of reading the file into a byte array

            // We have the array of bytes
            List<Byte> result = new ArrayList<Byte>();// List to store the resulting bytes (coded)
            // BITSET
            BitSet match = new BitSet();// Lo iniciamos vacío y lo iremos llenando en cada momento en el que meteriamos
            // un flag
            int bitsetpos = 0;
            // BITSET end

            int search_buffer_size = 255;// Because we want to keep it in a byte
            int look_ahead_buffer_size = 255;//Now the can be 255 and not 127

            // Gonna look through the whole array of bytes
            int n = data.length;
            for (int act = 0; act < n; act++) {// for every position of the array
                int sbufsize = Math.min(search_buffer_size, act);// we can at most go from the 0 to actual posotion in the
                // array
                int maxlength = 0;
                int offset = 0; // ini vars for new position
                for (int offsetact = 0; offsetact < sbufsize; offsetact++) {// for every pos on searchbuffer
                    // I will now search for a match but it can't:
                    // 1. can't go to the right more than insearchbuf positions
                    // 2. be greater that our search buffer size which will determine the greatest
                    // match
                    int bytestoend = n - 1 - act;// maybe el -1 sobra
                    int lahbufsize = Math.min(bytestoend, look_ahead_buffer_size);// we can't have a match longer than what
                    // we have remaining
                    int lengthact;
                    for (lengthact = 0; lengthact < offsetact + 1 && lengthact < lahbufsize; lengthact++) {
                        // j<i+1 pq no queremos reusar los del lookahead para match asi que si nos
                        // alejamos 2 pos hacia la izq del position, con la j solo podemos ir hacia la
                        // derecha dos posiciones y no seguir más de la posicions actual
                        byte sbufrelement = data[act - offsetact + lengthact - 1];
                        byte lahbufelement = data[act + lengthact];
                        // if we are on a match we keep checking else we stop
                        if (sbufrelement != lahbufelement) {
                            break;
                        }
                    } // Now we have a match(maybe length 0) for this position in the search buffer
                    if (lengthact > maxlength) { // if the match in this position of the searchbuffer is greater
                        offset = offsetact; // set new offset to encode
                        maxlength = lengthact; // set new length of best match to encode
                    }
                } // We have checked all the dictionary for the longest match
                if (maxlength >= 3) {// if there is a match of a certain length, big enough to compensate the bytes
                    // of match
                    match.set(bitsetpos);// BITSET
                    //result.add((byte) '1');// FLAG
                    int offsetnew = offset + 1;

                    result.add((byte) offsetnew);
                    result.add((byte) maxlength);// Length of match
                    act = act + maxlength - 1; // me muevo a pasado el match para la siguiente vuelta

                } else {
                    match.set(bitsetpos, false);// BITSET
                    //result.add((byte) '0');// FLAG
                    result.add(data[act]);
                }
                ++bitsetpos;// BITSET
            }
            // BISET

            //checking the bitset
            //System.out.println("bitset valueOf bytes: "+match);

            byte[] bitsetinbytes = match.toByteArray();

            int sizeofbitset = bitsetinbytes.length; //num of bytes of the bitset
            byte[] sizeofbitset_inarray = ByteBuffer.allocate(4).putInt(sizeofbitset).array();//array of 4 positions containing int

            // Method which write the bytes into a file
            BufferedOutputStream endfile = null;
            try {
                FileOutputStream fout = new FileOutputStream(fileOut); // create FileInputStream object
                endfile = new BufferedOutputStream(fout); // create object of BufferedInputStream
                //Añado el tamaño del bitset (4 Bytes)
                for(int j=0; j<sizeofbitset_inarray.length; ++j){
                    endfile.write( (byte) sizeofbitset_inarray[j]);
                }
                // //Añado el bitset en bytes
                for(int k=0; k<bitsetinbytes.length; ++k){
                    endfile.write(bitsetinbytes[k]);
                }
                //Añado lo comprimido
                for (int l = 0; l < result.size(); l++) {
                    endfile.write(result.get(l));
                }
                endfile.close();
            } catch (Exception e) {
                System.out.println("File not found" + e);
            }
        //}
        /*catch(IOException e) {
            System.err.println("Error: "+e);
        }*/

        //return result.toString(); // lo convertimos a stirng en si
        long endTime = System.nanoTime();
        long total_time = endTime -startTime;

        OutputAlgoritmo outAlg = new OutputAlgoritmo(total_time, fileOut);
        return outAlg;
    }

    @Override
    public OutputAlgoritmo descomprimir(File fileIn) {
        long startTime = System.nanoTime();
        File fileOut = new File(fileIn.getAbsolutePath().replace(".lzss", "_out.txt")); //custom output format

        //try {
            // READING the file into a byte array
            BufferedInputStream srcfile = null;
            List<Byte> srclist = new ArrayList<Byte>();// List to store bytes
            try {
                FileInputStream fin = new FileInputStream(fileIn); // create FileInputStream object
                srcfile = new BufferedInputStream(fin); // create object of BufferedInputStream

                while (srcfile.available() > 0) {
                    byte symbol = (byte) srcfile.read();
                    srclist.add(symbol);
                    // System.out.print((char) symbol);
                }
            } catch (FileNotFoundException e) {
                System.out.println("File not found" + e);
            } catch (IOException ioe) {
                System.out.println("Exception while reading the file " + ioe);
            } finally {
                // close the BufferedInputStream using close method
                try {
                    if (srcfile != null)
                        srcfile.close();// close the BufferedInputStream using close method
                } catch (IOException ioe) {
                    System.out.println("Error while closing the stream : " + ioe);
                }
            }
            // we have a list of all the bytes
            byte[] data = new byte[srclist.size()];
            int k = 0;
            for (Byte b : srclist) {
                data[k] = b;
                ++k;
            }
            // END of reading the file into a byte array

            //BITSET
            //cojo los bytes que hacen el integer
            byte[] sizeofbitset_inarray = new byte[4];
            //coloco byte a byte
            for(int i=0; i<4; ++i) sizeofbitset_inarray[i] = data[i];
            //Tengo el array listo
            int sizeofbitset = 0;
            sizeofbitset = fromByteArray(sizeofbitset_inarray);
            //System.out.println("\nel numero de bytes que ocupa el bitset es:"+sizeofbitset);
            //Voy a leer los "sizeofbitset" bytes y dejarlos en un array para pasarlo a un bitset de nuevo
            byte[] bitsetinbytes = new byte[sizeofbitset];
            //leo desde el 4o byte el numero de bytes que ocupa el bitset
            int z = 0;
            for(int i=4; i<(sizeofbitset+4); ++i){//la i és del data i la k del nou array
                bitsetinbytes[z] = data[i];
                ++z;
            }
            BitSet match = new BitSet();//No defino tamaño :o

            match = BitSet.valueOf(bitsetinbytes); //de aqui sale un bitset estupendo
            //BITSET END

            ByteArrayOutputStream result = new ByteArrayOutputStream();


            int n = data.length;//int tamaño bitse + bytes del bitset + bytes que van al resultado + pares de offset y length
            int inicio_real_data = 4 + sizeofbitset;
            int pos_bitset = 0;//empiezo al inicio del bitset donde tendre los flags
            //empiezo a recorrer donde empiezan mis datos (bytes que van al resultado + pares de offset y length)
            for (int i = inicio_real_data; i < n; i++) {// for every compressed data
                // System.out.print("Lo que hay en data[" + i + "] es: " + ((char) data[i]) +
                // "\n");
                if (! match.get(pos_bitset)) {// if it's not compressed
                    result.write(data[i]);// just add the following char to the output
                    //No hago ++ porque ya se hace a la siguiente vuelta del loop
                } else /*if (match.get(pos_bitset))*/ {// if there is a mcatch, get the length and offset
                    Byte actual = data[i];//la posicion i es la correcta del
                    int offset = byteToIntOffset(actual);
                    // OLD offset: int offset = actual.intValue();// offset is first
                    ++i;
                    Byte actual2 = data[i];
                    int matchlength = actual2.intValue();
                    // Now I will append the match that i get from the result itself
                    int sizeofbufnow = result.size();
                    int start = sizeofbufnow - offset; // start of the chars I have to copy for the match
                    int length = start + matchlength; // The size of the part to copy

                    for (; start < length; start++) { // for every spot
                        byte[] dataprevious = result.toByteArray();
                        result.write(dataprevious[start]);
                    }
                }
                ++pos_bitset;//me muevo una en el bitset
                if(pos_bitset>match.size()){//Si nos salimos de la medida *64 del bitset
                    System.out.print("OJO MANOLO QUE NOS SALIMOS DEL BITSET " + i + "\n");
                }
            }


            // Method which writes the bytes into a file
            byte[] result_final = result.toByteArray();
            BufferedOutputStream endfile = null;
            try {
                FileOutputStream fout = new FileOutputStream(fileOut); // create FileInputStream object
                endfile = new BufferedOutputStream(fout); // create object of BufferedInputStream
                //Añado lo comprimido
                for (int l = 0; l < result_final.length; l++) {
                    endfile.write(result_final[l]);
                }
                endfile.close();
            } catch (Exception e) {
                System.out.println("File not found" + e);
            }
        /*}
        catch(IOException e) {
            System.err.println("Error: "+e);
        }*/
        long endTime = System.nanoTime();
        long total_time = endTime -startTime;
        OutputAlgoritmo outAlg = new OutputAlgoritmo(total_time, fileOut);
        return outAlg;
    }

    //Making the offset int
    public static int byteToIntOffset(byte b)
    {
        return   b & 0xFF |
                (0x00) << 8 |
                (0x00) << 16 |
                (0x00) << 24;
    }


}
