package exceptions;

import java.util.ArrayList;

public abstract class ExceptionHandler<T extends Exception> {

    private final ArrayList<T> exceptions;

    public ExceptionHandler() {
        exceptions = new ArrayList<>();
    }

    public void addException(T e) {
        exceptions.add(e);
    }

    public boolean hasExceptions() {
        return !exceptions.isEmpty();
    }

    public String printExceptions() {
        StringBuilder sb = new StringBuilder();
        for (T exception : exceptions) {
            sb.append(printException(exception)).append("\n");
        }
        return sb.toString();
    }

    protected abstract String printException(T exception);

}
