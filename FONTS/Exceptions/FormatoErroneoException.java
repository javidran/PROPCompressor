package Exceptions;

import java.io.IOException;

/**
 * Excepción utilizada para indicar si el formato y/o contenido del archivo no es adecuado para el proceso de compresión o descompresión.
 */
public class FormatoErroneoException extends IOException {
    /**
     * Constructura de la excepción.
     * @param message Mensaje personalizado que acompaña a la excepción.
     */
    public FormatoErroneoException(String message) {
        super(message);
    }
}
