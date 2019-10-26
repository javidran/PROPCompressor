package domainLayer.algoritmos;

import java.io.File;

public class LZSS implements CompresorDecompresor {
    private static LZSS instance = null;

    // private constructor restricted to this class itself
    private LZSS() {
    }

    // static method to create instance of Singleton class
    public static LZSS getInstance()
    {
        if (instance == null)
            instance = new LZSS();

        return instance;
    }

    @Override
    public int comprimir(File input, File output) {
        //gestiono el pasar de file a string


        long startTime = System.nanoTime();

        StringBuilder result = new StringBuilder();// we will append the output here

        CharBuffer src = CharBuffer.wrap(data).asReadOnlyBuffer();
        // CharBuffer src2 = CharBuffer.wrap(data).asReadOnlyBuffer();

        // result.append(data);
        int search_buffer_size = 32768;
        int look_ahead_buffer_size = 255;
        // int yesmatch = 1, nomatch = 0;
        while (src.hasRemaining()) {// començem a la primera position fins al final del buffer
            // ini vars for new position
            int maxlength = 0;
            int offset = 0;
            int sbufsize = Math.min(search_buffer_size, src.position());// we can at most go from the 0 to start
            // position
            for (int offsetact = 0; offsetact < sbufsize; offsetact++) {// for every pos on searchbuffer
                // I will now search for a match but it can't:
                // 1. we can't go to the right more than insearchbuf positions
                // 2. be greater that our search buffer size which will determine the greatest
                // match
                int lahbufsize = Math.min(src.remaining(), look_ahead_buffer_size);// we can't have a match longer than
                // what we have remaining
                int lengthact;
                // boolean inamatch = true; //USING BOOLEAN
                // for (lengthact = 0; inamatch && lengthact < lahbufsize && lengthact <=
                // offsetact ;lengthact++){ //USING BOOLEAN
                for (lengthact = 0; lengthact < offsetact + 1 && lengthact < lahbufsize; lengthact++) {
                    // Now some advanced magic to get the element on the search buffer
                    char sbufrelement = src.get(src.position() - offsetact + lengthact - 1);// FUCKING -1
                    // And from the actual position we get the element in the lookahead buffer
                    char lahbufelement = src.get(src.position() + lengthact);
                    // if we are on a match we keep checking else we stop
                    if (sbufrelement != lahbufelement) {
                        // inamatch = false; //USING BOOLEAN
                        break;
                    }

                }
                if (lengthact > maxlength) { // if the match in this position of the searchbuffer is greater
                    offset = offsetact; // set new offset to encode
                    maxlength = lengthact; // set new length of best match to encode
                }
            }
            // Now we have a match or if not maxlength = 0
            if (maxlength >= 3) {// if there is a match of a certain length, big enough to compensate the
                // result.append(offset + 1).append(maxlength);
                int offsetnew = offset + 1;
                // WRONG NUMS:
                // result.append('1').append(offsetnew + '0').append(maxlength + '0');

                // result.append('1').append(offset + 1).append(maxlength);
                // Here I have to add +1 so it doesn't go short on the offset

                // WHITE BOXES:
                result.append('1').append((char) offsetnew).append((char) maxlength);
                src.position(src.position() + maxlength);// we jump to where the match ends
            } else {// If there is no match //It works :D
                result.append('0').append(src.get());// add the char at this position
            }

        }
        //return result.toString(); // lo convertimos a stirng en si
        long endTime = System.nanoTime();
        long total_time = endTime -startTime;
        return (int)total_time;
    }

    @Override
    public int descomprimir(File input, File output) {
        return 0;
    }


}
