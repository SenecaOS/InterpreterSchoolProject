package parser;

import lexer.Scanner;
import lexer.Token;
import lexer.TokenType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ParserTest {

    String toPrint(List<Statement> statements) {
        AstPrinter printer = new AstPrinter();
        StringBuilder sb = new StringBuilder();
        for (Statement statement : statements) {
            sb.append(printer.print(statement));
        }

        return sb.toString();
    }

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

    @ParameterizedTest
    @CsvSource({
            "2 + 3 - 4, ((2.0 + 3.0) - 4.0)",
            "3 + 5 * 6, (3.0 + (5.0 * 6.0))",
            "2 / ( - 3 + 2 ), (2.0 / (((-3.0) + 2.0)))",
            "3 + \"string\", (3.0 + string)"
    })
    void givenValidExpression_ReturnExpressionStatement(String input, String expected) {
        Scanner scanner = new Scanner(input);
        List<Token> tokens = scanner.scanTokens();
        Parser parser = new Parser(tokens);
        List<Statement> statements = parser.parse();
        Assertions.assertEquals(expected, toPrint(statements));
    }

    @ParameterizedTest
    @CsvSource({
            "2 + ( 3",
            "* 4 5",
            "- ",

    })
    void givenInvalidExpression_ThrowError(String input) {
        Scanner scanner = new Scanner(input);
        List<Token> tokens = scanner.scanTokens();
        Parser parser = new Parser(tokens);
        assertThrows(IllegalArgumentException.class, parser::parse);
    }

}