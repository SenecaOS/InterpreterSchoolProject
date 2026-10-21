package exceptions.lexer;

import exceptions.ExceptionHandler;

import java.util.ArrayList;

public class LexerExceptionHandler extends ExceptionHandler<LexerException> {
    @Override
    protected String printException(LexerException exception) {
        return exception.toString();
    }

}
