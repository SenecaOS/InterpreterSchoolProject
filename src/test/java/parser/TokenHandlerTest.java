package parser;

import lexer.Token;
import lexer.TokenType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static lexer.TokenType.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TokenHandlerTest {

    Token createToken(TokenType type) {
        return new Token(type, null, null, 0);
    }

    List<Token> getTokens() {
        return List.of(createToken(PLUS), createToken(MINUS), createToken(IDENTIFIER), createToken(SEMICOLON), createToken(EOF));
    }

    @Test
    void whenIncrementing_incrementToken() {
        List<Token> tokens = getTokens();
        TokenHandler tokenHandler = new TokenHandler(tokens);
        tokenHandler.increment();
        Assertions.assertEquals(MINUS, tokenHandler.peek().getTokenType());
    }

    @Test
    void whenIncrementingTooFar_throwError() {
        List<Token> tokens = getTokens();
        TokenHandler tokenHandler = new TokenHandler(tokens);
        for (int i = 0; i < tokens.size() - 1; i++) {
            tokenHandler.increment();
            System.out.println(tokenHandler.peek().getTokenType());
        }
        assertThrows(IndexOutOfBoundsException.class, tokenHandler::increment);

    }
}