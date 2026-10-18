package parser.exceptions;

import lexer.Token;

public class ParserSyntaxException extends ParserException {

    public ParserSyntaxException(Token token, String expected) {
        super(expected, token);
    }

    @Override
    public String toString() {
        return "Expected " + getMessage() + " at line " + token.getLine() + ", found " + token.getVal();
    }

}
