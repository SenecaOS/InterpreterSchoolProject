package parser.exceptions;

import java.util.ArrayList;

public class ExceptionHandler {

    private final ArrayList<ParserException> exceptions;

    public ExceptionHandler() {
        exceptions = new ArrayList<>();
    }

    public void addException(ParserException e) {
        exceptions.add(e);
    }

    public boolean hasExceptions() {
        return !exceptions.isEmpty();
    }

    public String printExceptions() {
        StringBuilder sb = new StringBuilder();
        for (Exception e : exceptions) {
            sb.append(e.toString()).append("\n");
        }
        return sb.toString();
    }
}
