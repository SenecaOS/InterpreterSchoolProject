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

import static lexer.TokenType.*;
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
        return new Token(LEFT_PARENS, "(", null, 0);
    }

    Token rightParens() {
        return new Token(RIGHT_PARENS, ")", null, 0);
    }

    Token leftsqBracket() {return new Token(LEFT_SQBRACKET, "{", null, 0);}

    Token rightsqBracket() {return new Token(RIGHT_SQBRACKET, "}", null, 0);}


    Token print() {
        return new Token(PRINT, "", null, 0);
    }

    Token stringToken(String value) {
        return new Token(STRING, "\"" + value + "\"", value, 0);
    }

    Token valueToken(Double value) {
        return new Token(VALUE, value.toString(), value, 0);
    }

    Token createToken(TokenType type) {return new Token(type, null, null, 0);}

    Token end() {
        return new Token(TokenType.EOF, "", null, 0);
    }

    @Test
    void GivenValidPrint_ReturnPrintStatement() {
        List<Token> tokens = List.of(print(), leftParens(), stringToken("Hello world"), rightParens(), end());

        Parser parser = new Parser(tokens);
        List<Statement> statements = parser.parse();
        Assertions.assertEquals(1, statements.size());
    }

    @Test
    void givenInvalidPrint_ThrowError() {
        List<Token> tokens = List.of(print(), leftParens(), stringToken("Hello world"), end());
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

    @Test
    void givenValidVariable_ReturnVariableStatement() {
        List<Token> tokens = List.of(createToken(LET), stringToken("newVariable"), createToken(ASSIGNMENT), valueToken(5.0), end());
        Parser parser = new Parser(tokens);
        Assertions.assertEquals("newVariable = 5.0", toPrint(parser.parse()));
    }

    @Test
    void givenInValidVariable_ReturnVariableStatement() {
        List<Token> tokens = List.of(createToken(LET), valueToken(3.0), createToken(ASSIGNMENT), stringToken("newVariable"), end());
        Parser parser = new Parser(tokens);
        assertThrows(IllegalArgumentException.class, parser::parse);
    }

    @Test
    void GivenValidLoop_ReturnLoopStatement() {
        List<Token> tokens = List.of(createToken(LOOP), leftParens(), valueToken(3.0), rightParens(), leftsqBracket(),
                createToken(LET), stringToken("newVariable"), createToken(ASSIGNMENT), valueToken(5.0),
                print(), leftParens(), stringToken("Hello world"), rightParens(),
                rightsqBracket(), end());

        Parser parser = new Parser(tokens);
        String expected = """
                loop(3.0) {
                newVariable = 5.0
                print(Hello world)
                }\s""";
        Assertions.assertEquals(expected, toPrint(parser.parse()));
    }

    @Test
    void givenOverMaxSyntaxBlockDepth_throwError() {
        List<Token> loopStart  = List.of(createToken(LOOP), leftParens(), valueToken(3.0), rightParens(), leftsqBracket());
        List<Token> tokens = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            tokens.addAll(loopStart);
        }

        for (int i = 0; i < 30; i++) {
            tokens.add(rightsqBracket());
        }

        tokens.add(end());
        Parser parser = new Parser(tokens);
        assertThrows(IllegalArgumentException.class, parser::parse);
    }

}