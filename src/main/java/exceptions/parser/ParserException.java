package exceptions.parser;

import lexer.Token;
import lexer.TokenType;

public abstract class ParserException extends RuntimeException {

    final Token token;

    public ParserException(String message, Token token) {
        super(message);
        this.token = token;
    }

    abstract String errorMessage();

    @Override
    public String toString() {
        if (token.getTokenType() == TokenType.EOF) {
            return "Reached end of file while parsing.";
        } else {
            return errorMessage();
        }
    }
}
