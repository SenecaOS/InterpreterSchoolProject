package exceptions.lexer;

import exceptions.ExceptionHandler;

public class LexerExceptionHandler extends ExceptionHandler<LexerException> {
    @Override
    protected String printException(LexerException exception) {
        return exception.toString();
    }

}
