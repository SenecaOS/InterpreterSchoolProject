package parser;

import lexer.Token;
import lexer.TokenType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class ParserTest {

    private Token rightParens() {
        return new Token(TokenType.LEFT_PARENS, "(", null, 0);
    }

    Token leftParens() {
        return new Token(TokenType.RIGHT_PARENS, ")", null, 0);
    }

    Token print() {
        return new Token(TokenType.PRINT, "", null, 0);
    }

    Token stringToken(String value) {
        return new Token(TokenType.STRING, "\"" + value + "\"" , value, 0);
    }

    @Test
    void GivenValidPrint_ReturnPrintStatement() {
        List<Token> tokens = new ArrayList<>();

        tokens.add(print());
        tokens.add(leftParens());
        tokens.add(stringToken("Hello world"));
        tokens.add(rightParens());

        Parser parser = new Parser(tokens);
        List<Statement> statements = parser.parse();
        Assertions.assertEquals(1, statements.size());
    }

}