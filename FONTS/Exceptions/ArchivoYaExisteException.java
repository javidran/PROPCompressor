package Exceptions;

import java.io.IOException;

public class ArchivoYaExisteException extends IOException {
    public ArchivoYaExisteException(String message) {
        super(message);
    }
}
