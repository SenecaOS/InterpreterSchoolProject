package parser.exceptions;

import lexer.Token;

public class ParserElseException extends ParserException {

    public ParserElseException(Token token) {
        super("", token);
    }

    @Override
    String errorMessage() {
        return "Found else without if at line " + token.getLine() + ".";
    }
}
