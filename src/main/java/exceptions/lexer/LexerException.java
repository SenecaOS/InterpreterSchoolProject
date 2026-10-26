package exceptions.lexer;

import lexer.Token;

public class LexerException extends RuntimeException{
    private final Token t;

    public LexerException(Token t) {
        this.t = t;

    }
    public String toString(){
        return "Error at line " + t.getLine() + " with token type: " + t.getTokenType().toString();

    }



}
