package parser;

import lexer.Token;
import lexer.TokenType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ParserTest {

    Token leftParens() {
        return new Token(TokenType.LEFT_PARENS, "(", null, 0);
    }

    Token rightParens() {
        return new Token(TokenType.RIGHT_PARENS, ")", null, 0);
    }

    Token print() {
        return new Token(TokenType.PRINT, "", null, 0);
    }

    Token stringToken(String value) {
        return new Token(TokenType.STRING, "\"" + value + "\"", value, 0);
    }

    Token valueToken(Double value) {
        return new Token(TokenType.VALUE, value.toString(), value, 0);
    }

    Token createToken(TokenType type, Object literal) {
        return new Token(type, literal.toString(), literal, 0);
    }

    Token end() {
        return new Token(TokenType.EOF, "", null, 0);
    }

    @Test
    void GivenValidPrint_ReturnPrintStatement() {
        List<Token> tokens = new ArrayList<>();

        tokens.add(print());
        tokens.add(leftParens());
        tokens.add(stringToken("Hello world"));
        tokens.add(rightParens());
        tokens.add(end());

        Parser parser = new Parser(tokens);
        List<Statement> statements = parser.parse();
        Assertions.assertEquals(1, statements.size());
    }

    @Test
    void givenInvalidPrint_ThrowError() {
        List<Token> tokens = new ArrayList<>();
        tokens.add(print());
        tokens.add(leftParens());
        tokens.add(stringToken("Hello world"));
        //tokens.add(rightParens());
        tokens.add(new Token(TokenType.EOF, "", null, 0));

        Parser parser = new Parser(tokens);
        assertThrows(IllegalArgumentException.class, parser::parse);
    }

    @Test
    void givenValidExpression_ReturnExpressionStatement() {
        List<Token> tokens = new ArrayList<>();
        tokens.add(valueToken(2.0));
        tokens.add(createToken(TokenType.PLUS, "+"));
        tokens.add(valueToken(3.0));
        tokens.add(createToken(TokenType.MINUS, "-"));
        tokens.add(valueToken(4.0));
        tokens.add(end());

        Parser parser = new Parser(tokens);
        List<Statement> statements = parser.parse();
        Assertions.assertEquals(1, statements.size());
    }

}