package by.smertex.server.exception;

public class ClientSocketCloseException extends RuntimeException {
    public ClientSocketCloseException(String message) {
        super(message);
    }
}
