package exceptions;

public class BadException
extends RuntimeException {
    public BadException(String info) {
        super(info);
    }

    public BadException(String info, Throwable e) {
        super(info, e);
    }

    public BadException() {
    }

    public BadException(Throwable e) {
        super(e);
    }
}