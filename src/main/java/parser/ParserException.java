package parser;

import lexer.Token;

public class ParserException extends Exception {

    final Token token;

    public ParserException(Token token, String message) {
        super(message);
        this.token = token;
    }
}
