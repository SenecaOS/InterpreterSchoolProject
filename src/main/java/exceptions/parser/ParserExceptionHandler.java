package exceptions.parser;

import exceptions.ExceptionHandler;

public class ParserExceptionHandler extends ExceptionHandler<ParserException> {

    @Override
    public String printException(ParserException exception) {
        return exception.toString();
    }
}
