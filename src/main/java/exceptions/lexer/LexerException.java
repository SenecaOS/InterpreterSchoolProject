package exceptions.lexer;

import lexer.Token;

import java.util.ArrayList;

public class LexerException extends RuntimeException{
    private final Token t;

    public LexerException(Token t) {
        this.t = t;

    }
    public String toString(){
        return "Error at " + t.getLine() + " with token type: " + t.getTokenType().toString();

    }



}
