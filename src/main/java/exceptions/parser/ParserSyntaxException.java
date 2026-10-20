package exceptions.parser;

import lexer.Token;

public class ParserSyntaxException extends ParserException {

    public ParserSyntaxException(Token token, String expected) {
        super(expected, token);
    }

    @Override
    String errorMessage() {
        return "Expected '" + getMessage() + "' at line " + token.getLine() + ", but found '" + token.getVal() + "'.";
    }
}
