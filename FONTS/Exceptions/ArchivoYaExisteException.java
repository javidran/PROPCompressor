package Exceptions;

import java.io.IOException;

/**
 * Excepción utilizada para indicar que el archivo buscado ya existe.
 */
public class ArchivoYaExisteException extends IOException {
    /**
     * Constructura de la excepción.
     * @param message Mensaje personalizado que acompaña a la excepción.
     */
    public ArchivoYaExisteException(String message) {
        super(message);
    }
}
