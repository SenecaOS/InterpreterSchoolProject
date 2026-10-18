package parser.exceptions;

import lexer.Token;

public class ParserElseException extends ParserException {

    public ParserElseException(Token token) {
        super("", token);
    }

    @Override
    public String toString() {
        return "Found else without if at line " + token.getLine();
    }
}
