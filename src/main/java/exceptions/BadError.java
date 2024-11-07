package exceptions;

public class BadError
extends Error {
    public BadError(String info) {
        super(info);
    }

    public BadError(String info, Throwable e) {
        super(info, e);
    }

    public BadError() {
    }

    public BadError(Throwable e) {
        super(e);
    }
}