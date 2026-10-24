package parser;

import lexer.Token;
import lexer.TokenType;

import java.util.List;

public class TokenHandler {

    private final List<Token> tokens;

    private int current = 0;
    private Token currentToken, prevToken;

    public TokenHandler(List<Token> tokens) {
        if (tokens == null) throw new IllegalArgumentException("tokens cannot be null");
        this.tokens = tokens;
        currentToken = tokens.get(0);
    }

    public boolean isAtEnd() {
        return peek().getTokenType() == TokenType.EOF;
    }

    public Token peek() {
        return currentToken;
    }

    public Token prev() {
        return prevToken;
    }


    public void increment() {
        if (current + 1 >= tokens.size()) throw new IndexOutOfBoundsException("no more tokens to read");
        prevToken = currentToken;
        current++;
        currentToken = tokens.get(current);
    }
}
