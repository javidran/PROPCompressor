package DomainLayer.Algoritmos;

import Exceptions.FormatoErroneoException;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * Clase que implementa los métodos necesarios para comprimir y descomprimir secuencias de bytes con el algoritmo de compresión LZSS.
 */
public class LZSS implements CompresorDecompresor {
    /**
     * Instancia de LZSS para garantizar que es una clase Singleton
     */
    private static LZSS instance = null;

    /**
     * Getter de la instancia Singleton de LZSS
     * @return La instancia Singleton de LZSS
     */
    // static method to create instance of Singleton class
    public static LZSS getInstance()
    {
        if (instance == null)
            instance = new LZSS();

        return instance;
    }

    /**
     * Transforma un array de bytes en un Integer
     * <p>
     *     El array de bytes debe tener idealmente 4 bytes
     * </p>
     * @param bytes El array de bytes que se quiere pasar a Integer
     * @return Un Integer con el valor que se obtiene al pasar los 4 bytes del array a un Integer propiamente
     */
    private static int fromByteArray(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getInt();
    }

    /**
     * Constructor de la clase LZSS.
     * <p>Su visibilidad es privada para impedir que se tengan multiples instancias de la clase y satisfacer las propiedades de Singleton.</p>
     */
    private LZSS() {
    }

    /**
     * Comprime el array de bytes que se pasa por parametro aplicando el algoritmo de compersión LZSS
     * <p>
     *     El array de bytes debería idealmente ser de una longitud significativa
     * </p>
     * @param data El array de bytes que se ha obtenido del contenido del fichero
     * @return Una instancia de la clase OutputAlgoritmo que contiene el tiempo en el que se ha realizado la compresión
     * y también el byte[] con el resultado de la compresión del byte[] data.
     */
    @Override
    public OutputAlgoritmo comprimir(byte[] data) {
        //gestiono el pasar de file a string
        long startTime = System.nanoTime();
        // We have the array of bytes
        List<Byte> result = new ArrayList<>();// List to store the resulting bytes (coded)
        // BITSET
        BitSet match = new BitSet();// Lo iniciamos vacío y lo iremos llenando en cada momento en el que meteriamos
        // un flag
        int bitsetpos = 0;
        // BITSET end

        int search_buffer_size = 4095;// MAX = 4095 50% quijote.txt
        int look_ahead_buffer_size = 15;// MAX = 15


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
                match.set(bitsetpos);// BITSET
                int offsetnew = offset + 1;
                // NEW bitPlay
                int length1 = maxlength & 0xF;
                int off1 = (offsetnew << 4) & 0xF0; // OK
                int off2 = (offsetnew >> 4) & 0xFF; // OK
                byte mega_right = (byte) (length1 + off1);// OK
                byte mega_left = (byte) off2;
                result.add(mega_left);
                result.add(mega_right);
                // END NEW bitPlay
                act = act + maxlength - 1; // me muevo a pasado el match para la siguiente vuelta

            } else {
                match.set(bitsetpos, false);// BITSET
                result.add(data[act]);
            }
            ++bitsetpos;// BITSET
        }
        // BISET
        byte[] bitsetinbytes = match.toByteArray();

        int sizeofbitset = bitsetinbytes.length; //num of bytes of the bitset
        byte[] sizeofbitset_inarray = ByteBuffer.allocate(4).putInt(sizeofbitset).array();//array of 4 positions containing int
        int MegaResultArray_size = sizeofbitset_inarray.length + bitsetinbytes.length + result.size();
        byte[] MegaResultArray = new byte[MegaResultArray_size];
        int i = 0;//Recorre el MegaResultArray
        for(int j=0; j<sizeofbitset_inarray.length; ++j){
            MegaResultArray[i] = (byte) sizeofbitset_inarray[j];
            i++;
        }
        // //Añado el bitset en bytes
        for(int k=0; k<bitsetinbytes.length; ++k){
            MegaResultArray[i] = bitsetinbytes[k];
            i++;
        }
        //Añado lo comprimido
        for (int l = 0; l < result.size(); l++) {
            MegaResultArray[i] = result.get(l);
            i++;
        }
        //Ya esta rellenado el byte[] resultante con toda la info y ahora i = MegaResultArray.length
        long endTime = System.nanoTime();
        long total_time = endTime -startTime;
        OutputAlgoritmo outAlg = new OutputAlgoritmo(total_time, MegaResultArray);
        //OutputAlgoritmo outAlg = new OutputAlgoritmo(total_time, fileOut);
        return outAlg;
    }

    /**
     * Desomprime el array de bytes que se pas por parametro aplicando el algoritmo de descompersión LZSS
     * <p>
     *     El array de bytes está comprimido con el algoritmo de compresión LZSS
     * </p>
     * @param data El array de bytes que se ha obtenido del contenido del fichero .lzss o de la compresión directamente
     * @return Una instancia de la clase OutputAlgoritmo que contiene el tiempo en el que se ha realizado la descompresión
     * y también el byte[] con el resultado de la descompresión del byte[] data.
     * @throws FormatoErroneoException El formato en el que está codificado el texto no es correcto.
     */
    @Override
    public OutputAlgoritmo descomprimir(byte[] data) throws FormatoErroneoException {
        try {
            long startTime = System.nanoTime();
            //BITSET
            //cojo los bytes que hacen el integer
            byte[] sizeofbitset_inarray = new byte[4];
            //coloco byte a byte
            for (int i = 0; i < 4; ++i) sizeofbitset_inarray[i] = data[i];
            //Tengo el array listo
            int sizeofbitset = 0;
            sizeofbitset = fromByteArray(sizeofbitset_inarray);
            //System.out.println("\nel numero de bytes que ocupa el bitset es:"+sizeofbitset);
            //Voy a leer los "sizeofbitset" bytes y dejarlos en un array para pasarlo a un bitset de nuevo
            byte[] bitsetinbytes = new byte[sizeofbitset];
            //leo desde el 4o byte el numero de bytes que ocupa el bitset
            int z = 0;
            for (int i = 4; i < (sizeofbitset + 4); ++i) {//la i és del data i la k del nou array
                bitsetinbytes[z] = data[i];
                ++z;
            }
            BitSet match = new BitSet();//No defino tamaño :o

            match = BitSet.valueOf(bitsetinbytes); //de aqui sale un bitset estupendo
            //BITSET END

            //ByteArrayOutputStream result = new ByteArrayOutputStream();
            List<Byte> result = new ArrayList<>();

            int n = data.length;//int tamaño bitse + bytes del bitset + bytes que van al resultado + pares de offset y length
            int inicio_real_data = 4 + sizeofbitset;
            int pos_bitset = 0;//empiezo al inicio del bitset donde tendre los flags
            //empiezo a recorrer donde empiezan mis datos (bytes que van al resultado + pares de offset y length)
            for (int i = inicio_real_data; i < n; i++) {// for every compressed data

                if (!match.get(pos_bitset)) {// if it's not compressed
                    //result.write(data[i]);// just add the following char to the output
                    result.add(data[i]);
                    //No hago ++ porque ya se hace a la siguiente vuelta del loop
                } else /*if (match.get(pos_bitset))*/ {// if there is a mcatch, get the length and offset
                    byte mega_left = data[i];
                    ++i;
                    byte mega_right = data[i];
                    int matchlength = mega_right & 0xF;
                    int parcial1_off = (mega_right >> 4) & 0xF;
                    int parcial2_off = (mega_left << 4) & 0xFF0;
                    int offset = parcial1_off + parcial2_off;
                    // Now I will append the match that i get from the result itself
                    //int sizeofbufnow = result.size();
                    int sizeofbufnow = result.size();
                    int start = sizeofbufnow - offset; // start of the chars I have to copy for the match
                    int length = start + matchlength; // The size of the part to copy

                    for (; start < length; start++) { // for every spot
                        //byte[] dataprevious = result.toByteArray(); //THE MAIN CAUSE OF BOTTLENECK
                        //result.write(dataprevious[start]);
                        result.add(result.get(start));
                    }
                }
                ++pos_bitset;//me muevo una en el bitset
            }


            byte[] result_final = new byte[result.size()];
            int i = 0;
            for (byte x : result) {
                result_final[i] = x;
                i++;
            }
            long endTime = System.nanoTime();
            long total_time = endTime - startTime;
            OutputAlgoritmo outAlg = new OutputAlgoritmo(total_time, result_final);
            return outAlg;
        } catch (Exception e) {
            throw new FormatoErroneoException("El archivo a descomprimir está corrupto o no se ha generado adecuadamente.");
        }
    }



}
