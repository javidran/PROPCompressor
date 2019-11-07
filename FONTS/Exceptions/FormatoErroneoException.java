package Exceptions;

import java.io.IOException;

public class FormatoErroneoException extends IOException {
    public FormatoErroneoException(String message) {
        super(message);
    }
}
