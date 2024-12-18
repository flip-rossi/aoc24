package aoc;

public class NoSolutionException extends RuntimeException {
    public NoSolutionException() {
        super();
    }

    public NoSolutionException(String message) {
        super(message);
    }

    public NoSolutionException(Throwable cause) {
        super(cause);
    }

    public NoSolutionException(String message, Throwable cause) {
        super(message, cause);
    }
}
