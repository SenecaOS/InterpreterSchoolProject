package parser.exceptions;

import lexer.Token;

public abstract class ParserException extends RuntimeException {

    final Token token;

    public ParserException(String message, Token token) {
        super(message);
        this.token = token;
    }
}
