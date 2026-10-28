package parser;

import exceptions.parser.ParserExceptionHandler;
import exceptions.parser.ParserSyntaxException;
import lexer.Token;
import lexer.TokenType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static lexer.TokenType.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ParserTest {

    String toPrint(List<Statement> statements) {
        AstPrinter printer = AstPrinter.INSTANCE;
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

    Token leftCurlyBracket() {
        return new Token(LEFT_CURLYBRAC, "{", null, 0);
    }

    Token rightCurlyBracket() {
        return new Token(RIGHT_CURLYBRAC, "}", null, 0);
    }

    Token print() {
        return new Token(PRINT, "", null, 0);
    }

    Token stringToken(String value) {
        return new Token(STRING, "\"" + value + "\"", value, 0);
    }

    Token valueToken(Double value) {
        return new Token(VALUE, value.toString(), value, 0);
    }

    Token createToken(TokenType type, String val) {
        return new Token(type, val, null, 0);
    }

    Token createToken(TokenType type) {
        return new Token(type, null, null, 0);
    }

    Token end() {
        return new Token(TokenType.EOF, "", null, 0);
    }

    Token identifier(String val) {
        return new Token(IDENTIFIER, val, val, 0);
    }

    Token terminator() {
        return new Token(SEMICOLON, ";", null, 0);
    }

    Statement printStatement(String string) {
        return new Statement.Print(new Expr.Literal(string));
    }

    Statement expression(Expr expr) {
        return new Statement.Expression(expr);
    }

    Expr literal(Object val) {
        return new Expr.Literal(val);
    }

    Expr binary(double left, TokenType operator, double right) {
        return new Expr.Binary(literal(left), createToken(operator), literal(right));
    }

    Expr binary(Expr left, TokenType operator, Expr right) {
        return new Expr.Binary(left, createToken(operator), right);
    }

    Expr grouping(Expr expr) {
        return new Expr.Grouping(expr);
    }

    Expr unary(TokenType operator, Expr expr) {
        return new Expr.Unary(createToken(operator), expr);
    }

    @Test
    void GivenValidPrint_ReturnPrintStatement() {
        List<Token> tokens = List.of(print(), leftParens(), stringToken("Hello world"), rightParens(), terminator(), end());

        Parser parser = new Parser(tokens);
        List<Statement> statements = parser.parse();
        List<Statement> expected = List.of(printStatement("Hello world"));
        Assertions.assertEquals(expected, statements);
    }

    @Test
    void givenInvalidPrint_ThrowError() {
        List<Token> tokens = List.of(print(), leftParens(), stringToken("Hello world"), terminator(), end());
        Parser parser = new Parser(tokens);
        assertThrows(ParserSyntaxException.class, parser::parse);
    }

    @Test
    void givenArithmeticExpression_ReturnExpressionStatement() {
        List<Token> tokens = List.of(valueToken(2.0), createToken(PLUS, "+"), valueToken(3.0), createToken(MINUS, "-"), valueToken(4.0), terminator(), end());
        Parser parser = new Parser(tokens);
        List<Statement> statements = parser.parse();
        List<Statement> expected = List.of(expression(binary(binary(2.0, PLUS, 3.0), MINUS, literal(4.0))));
        Assertions.assertEquals(expected, statements);
    }

    @Test
    void givenFactorExpression_ReturnExpressionStatement() {
        List<Token> tokens = List.of(valueToken(3.0), createToken(PLUS, "+"), valueToken(5.0), createToken(STAR, "*"), valueToken(6.0), terminator(), end());
        Parser parser = new Parser(tokens);
        List<Statement> statements = parser.parse();
        List<Statement> expected = List.of(expression(binary(literal(3.0), PLUS, binary(5.0, STAR, 6.0))));
        Assertions.assertEquals(expected, statements);
    }

    @Test
    void givenParensExpression_ReturnExpressionStatement() {
        List<Token> tokens = List.of(valueToken(2.0), createToken(SLASH, "/"), leftParens(), createToken(MINUS, "-"), valueToken(3.0), createToken(PLUS, "+"), valueToken(2.0), rightParens(), terminator(), end());
        Parser parser = new Parser(tokens);
        List<Statement> statements = parser.parse();
        List<Statement> expected = List.of(expression(binary(literal(2.0), SLASH, grouping(binary(unary(MINUS, literal(3.0)), PLUS, literal(2.0))))));
        Assertions.assertEquals(expected, statements);
    }

    @Test
    void givenStringExpression_ReturnExpressionStatement() {
        List<Token> tokens = List.of(valueToken(3.0), createToken(PLUS, "+"), stringToken("string"), terminator(), end());
        Parser parser = new Parser(tokens);
        List<Statement> statements = parser.parse();
        List<Statement> expected = List.of(expression(binary(literal(3.0), PLUS, literal("string"))));
        Assertions.assertEquals(expected, statements);
    }

    @Test
    void givenComparisonExpression_ReturnExpressionStatement() {
        List<Token> tokens = List.of(valueToken(2.0), createToken(NOT_EQUALS, "!="), valueToken(3.0), terminator(), end());
        Parser parser = new Parser(tokens);
        List<Statement> statements = parser.parse();
        List<Statement> expected = List.of(expression(binary(2.0, NOT_EQUALS, 3.0)));
        Assertions.assertEquals(expected, statements);
    }

    @Test
    void givenLogicalExpression_ReturnExpressionStatement() {
        List<Token> tokens = List.of(createToken(TRUE, "true"), createToken(AND, "&"), valueToken(5.0), createToken(EQUALS, "=="), identifier("a"), createToken(PLUS, "+"), identifier("b"), createToken(OR, "|"), valueToken(2.0), terminator(), end());
        Parser parser = new Parser(tokens);
        List<Statement> statements = parser.parse();
        List<Statement> expected = List.of(expression(
                binary(
                        binary(literal(true), AND,
                                binary(literal(5.0), EQUALS,
                                        binary(new Expr.Variable("a"), PLUS, new Expr.Variable("b"))
                                )), OR, literal(2.0)
                )
        ));
        Assertions.assertEquals(expected, statements);
    }

    @Test
    void givenNotFinishedParens_throwError() {
        List<Token> tokens = List.of(valueToken(2.0), createToken(PLUS), leftParens(), valueToken(3.0), end());
        Parser parser = new Parser(tokens);
        assertThrows(ParserSyntaxException.class, parser::parse);
    }

    @Test
    void givenIncorrectFactorPattern_throwError() {
        List<Token> tokens = List.of(createToken(STAR), valueToken(4.0), valueToken(5.0), end());
        Parser parser = new Parser(tokens);
        assertThrows(ParserSyntaxException.class, parser::parse);
    }

    @Test
    void givenOnlyMinus_throwError() {
        List<Token> tokens = List.of(createToken(MINUS), end());
        Parser parser = new Parser(tokens);
        assertThrows(ParserSyntaxException.class, parser::parse);
    }


    @Test
    void givenValidVariable_ReturnVariableStatement() {
        List<Token> tokens = List.of(createToken(SMTH), createToken(IDENTIFIER, "newVar"), createToken(ASSIGNMENT), valueToken(5.0), terminator(), end());
        Parser parser = new Parser(tokens);
        List<Statement> statements = parser.parse();
        List<Statement> expected = List.of(new Statement.Variable("newVar", literal(5.0)));
        Assertions.assertEquals(expected, statements);
    }

    @Test
    void givenInValidVariable_ReturnVariableStatement() {
        List<Token> tokens = List.of(createToken(SMTH), valueToken(3.0), createToken(ASSIGNMENT), stringToken("newVar"), terminator(), end());
        Parser parser = new Parser(tokens);
        assertThrows(ParserSyntaxException.class, parser::parse);
    }

    @Test
    void GivenValidLoop_ReturnLoopStatement() {
        List<Token> tokens = List.of(createToken(LOOP), leftParens(), valueToken(3.0), rightParens(), leftCurlyBracket(),
                createToken(SMTH), createToken(IDENTIFIER, "newVar"), createToken(ASSIGNMENT), valueToken(5.0), terminator(),
                print(), leftParens(), stringToken("Hello world"), rightParens(), terminator(),
                rightCurlyBracket(), end());

        Parser parser = new Parser(tokens);
        List<Statement> statements = parser.parse();
        List<Statement> expected = List.of(new Statement.Loop(
                new Statement.Block(List.of(
                        new Statement.Variable("newVar", literal(5.0)),
                        new Statement.Print(literal("Hello world")))
                ), literal(3.0)));
        Assertions.assertEquals(expected, statements);
    }

    @Test
    void givenOverMaxSyntaxBlockDepth_throwError() {
        List<Token> loopStart = List.of(createToken(LOOP), leftParens(), valueToken(3.0), rightParens(), leftCurlyBracket());
        List<Token> tokens = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            tokens.addAll(loopStart);
        }

        for (int i = 0; i < 30; i++) {
            tokens.add(rightCurlyBracket());
        }

        tokens.add(end());
        Parser parser = new Parser(tokens);
        assertThrows(IllegalArgumentException.class, parser::parse);
    }

    @Test
    void givenValidIfStatement_ReturnIfStatement() {
        List<Token> tokens = List.of(
                createToken(IF), leftParens(), createToken(FALSE), rightParens(), leftCurlyBracket(),
                print(), leftParens(), stringToken("Hello world"), rightParens(), terminator(),
                rightCurlyBracket()
                , end()
        );

        Parser parser = new Parser(tokens);
        List<Statement> statements = parser.parse();
        List<Statement> expected = List.of(new Statement.If(literal(false), new Statement.Block(List.of(printStatement("Hello world"))), null));
        Assertions.assertEquals(expected, statements);
    }

    @Test
    void givenValidIfAndElseIfStatement_ReturnIfStatementWithElseIf() {
        List<Token> tokens = List.of(createToken(IF), leftParens(), createToken(NOTH), rightParens(), leftCurlyBracket(),
                rightCurlyBracket(), createToken(ELSE), createToken(IF), leftParens(), createToken(TRUE), rightParens(), leftCurlyBracket(),
                rightCurlyBracket()
                , end()
        );
        Parser parser = new Parser(tokens);
        List<Statement> statements = parser.parse();
        List<Statement> expected = List.of(new Statement.If(literal(null), new Statement.Block(List.of()), new Statement.If(literal(true), new Statement.Block(List.of()), null)));
        Assertions.assertEquals(expected, statements);
    }

    @Test
    void givenValidIfAndElseStatement_ReturnIfStatementWithElse() {
        List<Token> tokens = List.of(
                createToken(IF), leftParens(), createToken(TRUE), rightParens(), leftCurlyBracket(),
                print(), leftParens(), stringToken("Hello world"), rightParens(), terminator(),
                rightCurlyBracket(), createToken(ELSE), leftCurlyBracket(),
                print(), leftParens(), stringToken("Hello world"), rightParens(), terminator(),
                rightCurlyBracket()
                , end()
        );

        Parser parser = new Parser(tokens);
        List<Statement> statements = parser.parse();
        List<Statement> expected = List.of(new Statement.If(literal(true), new Statement.Block(List.of(printStatement("Hello world"))),
                new Statement.Block(List.of(printStatement("Hello world")))));
        Assertions.assertEquals(expected, statements);
    }

    @Test
    void givenValidElseIfStatement_ReturnIfStatementWithElseIf() {
        List<Token> tokens = List.of(
                createToken(IF), leftParens(), createToken(TRUE), rightParens(), leftCurlyBracket(),
                print(), leftParens(), stringToken("Hello world"), rightParens(), terminator(),
                rightCurlyBracket(), createToken(ELSE), createToken(IF), leftParens(), createToken(TRUE), rightParens(), leftCurlyBracket(),
                print(), leftParens(), stringToken("Hello world"), rightParens(), terminator(),
                rightCurlyBracket(), createToken(ELSE), leftCurlyBracket(),
                print(), leftParens(), stringToken("Hello world"), rightParens(), terminator(),
                rightCurlyBracket()
                , end()
        );

        Parser parser = new Parser(tokens);
        String expected = """
                if(true)
                {
                print(Hello world)
                }
                else if(true)
                {
                print(Hello world)
                }
                else {
                print(Hello world)
                }
                """;
        Assertions.assertEquals(expected, toPrint(parser.parse()));
    }

    @Test
    void givenElseInsteadOfIf_throwError() {
        List<Token> tokens = List.of(createToken(ELSE), leftParens(), createToken(TRUE), rightParens(), leftCurlyBracket(),
                print(), leftParens(), stringToken("Hello world"), rightParens(), terminator(),
                rightCurlyBracket()
        );

        Parser parser = new Parser(tokens);
        Assertions.assertThrows(ParserSyntaxException.class, parser::parse);
    }

    @Test
    void givenIfWithNoCondition_throwError() {
        List<Token> tokens = List.of(
                createToken(IF), leftParens(), rightParens(), leftCurlyBracket(),
                print(), leftParens(), stringToken("Hello world"), rightParens(), terminator(),
                rightCurlyBracket()
                , end()
        );

        Parser parser = new Parser(tokens);
        Assertions.assertThrows(ParserSyntaxException.class, parser::parse);
    }

    @Test
    void givenReturnWithExpression_returnReturnStatement() {
        List<Token> tokens = List.of(createToken(RETURN), valueToken(3.0), terminator(), end());
        Parser parser = new Parser(tokens);
        Assertions.assertEquals(parser.parse(), List.of(new Statement.Return(literal(3.0))));
    }

    @Test
    void givenReturnWithNoExpression_returnReturnStatementWithNull() {
        List<Token> tokens = List.of(
                createToken(IF), leftParens(), createToken(TRUE), rightParens(), leftCurlyBracket(),
                print(), leftParens(), stringToken("Hello world"), rightParens(), terminator(),
                createToken(RETURN), terminator(),
                rightCurlyBracket()
                , end());
        Parser parser = new Parser(tokens);
        List<Statement> statements = parser.parse();
        List<Statement> expected = List.of(new Statement.If(literal(true), new Statement.Block(List.of(
                printStatement("Hello world"),
                new Statement.Return(null)
        )), null));
        Assertions.assertEquals(expected, statements);
    }

    @Test
    void givenValidEmptyFunction_returnFunction() {
        List<Token> tokens = List.of(createToken(FUNCTION), identifier("foo"), leftParens(), rightParens(), leftCurlyBracket(), rightCurlyBracket(), end());
        Parser parser = new Parser(tokens);
        List<Statement> statements = parser.parse();
        List<Statement> expected = List.of(new Statement.Function("foo", new Statement.Block(List.of()), List.of()));
        Assertions.assertEquals(expected, statements);
    }


    @Test
    void givenValidFunctionWithArity1_returnFunctionWithArity1() {
        List<Token> tokens = List.of(createToken(FUNCTION), identifier("bar"), leftParens(), identifier("val"), rightParens(), leftCurlyBracket(), rightCurlyBracket(), end());
        Parser parser = new Parser(tokens);
        List<Statement> statements = parser.parse();
        List<Statement> expected = List.of(new Statement.Function("bar", new Statement.Block(List.of()), List.of("val")));
        Assertions.assertEquals(expected, statements);
    }

    @Test
    void givenValidFunctionWithArity3_returnFunctionWithArity3() {
        List<Token> tokens = List.of(createToken(FUNCTION), identifier("bar"), leftParens(), identifier("val"), createToken(COMMA), identifier("val1"), createToken(COMMA), identifier("val2"), rightParens(), leftCurlyBracket(), rightCurlyBracket(), end());
        Parser parser = new Parser(tokens);
        List<Statement> statements = parser.parse();
        ArrayList<String> params = new ArrayList<>(List.of("val", "val1", "val2"));
        List<Statement> expected = List.of(new Statement.Function("bar",
                new Statement.Block(List.of()), params));
        Assertions.assertEquals(expected, statements);
    }

    @Test
    void givenFunctionWithNoName_throwError() {
        List<Token> tokens = List.of(createToken(FUNCTION), leftParens(), identifier("val"), rightParens(), leftCurlyBracket(), rightCurlyBracket(), end());
        Parser parser = new Parser(tokens);
        Assertions.assertThrows(ParserSyntaxException.class, parser::parse);
    }

    @Test
    void givenValidFunctionWithCommaBefore_throwError() {
        List<Token> tokens = new ArrayList<>(List.of(createToken(FUNCTION), identifier("bar"), leftParens(), createToken(COMMA), identifier("val"), rightParens(), leftCurlyBracket(), rightCurlyBracket(), end()));

        Parser parser = new Parser(tokens);
        Assertions.assertThrows(ParserSyntaxException.class, parser::parse);
    }

    @Test
    void givenFunctionWithArityOverMax_throwError() {
        List<Token> tokens = new ArrayList<>(List.of(createToken(FUNCTION), identifier("bar"), leftParens(), identifier("val")));


        for (int i = 0; i < 50; i++) {
            tokens.addAll(List.of(createToken(COMMA), identifier("val" + i)));
        }

        tokens.addAll(List.of(rightParens(), leftCurlyBracket(), rightCurlyBracket(), end()));
        Parser parser = new Parser(tokens);
        Assertions.assertThrows(IllegalArgumentException.class, parser::parse);
    }

    @Test
    void givenFunctionWithDuplicateVariableName_throwError() {
        List<Token> tokens = List.of(createToken(FUNCTION), identifier("bar"), leftParens(), identifier("val"), createToken(COMMA), identifier("val"), createToken(COMMA), identifier("val"), rightParens(), leftCurlyBracket(), rightCurlyBracket(), end());
        Parser parser = new Parser(tokens);
        Assertions.assertThrows(IllegalArgumentException.class, parser::parse);
    }

    @Test
    void givenFunctionWithNoBody_throwError() {
        List<Token> tokens = List.of(createToken(FUNCTION), identifier("bar"), leftParens(), identifier("val"), rightParens(), end());
        Parser parser = new Parser(tokens);
        Assertions.assertThrows(ParserSyntaxException.class, parser::parse);
    }

    @Test
    void givenValidEmptyCall_returnCallExpression() {
        List<Token> tokens = List.of(identifier("bar"), leftParens(), rightParens(), terminator(), end());
        Parser parser = new Parser(tokens);
        List<Statement> statements = parser.parse();
        List<Statement> expected = List.of(expression(new Expr.Call("bar", List.of())));
        Assertions.assertEquals(expected, statements);
    }

    @Test
    void givenValidArity1Call_returnCallExpressionWithArity1() {
        List<Token> tokens = List.of(identifier("bar"), leftParens(), valueToken(1.0), createToken(PLUS, "+"), valueToken(2.0), rightParens(), terminator(), end());
        Parser parser = new Parser(tokens);
        List<Statement> statements = parser.parse();
        List<Statement> expected = List.of(expression(new Expr.Call("bar", List.of(binary(1.0, PLUS, 2.0)))));
        Assertions.assertEquals(expected, statements);
    }

    @Test
    void givenValidArity4Call_returnCallExpressionWithArity4() {
        List<Token> tokens = List.of(identifier("bar"), leftParens(), createToken(TRUE), createToken(COMMA),
                createToken(MINUS, "-"), valueToken(5.0), createToken(COMMA),
                identifier("foo"), leftParens(), identifier("num"), rightParens(), createToken(COMMA),
                leftParens(), valueToken(4.0), rightParens(), rightParens(), terminator(), end());
        Parser parser = new Parser(tokens);
        List<Statement> statements = parser.parse();
        List<Statement> expected = List.of(new Statement.Expression(new Expr.Call("bar", List.of(
                literal(true),
                unary(MINUS, literal(5.0)),
                new Expr.Call("foo", List.of(new Expr.Variable("num"))),
                new Expr.Grouping(literal(4.0))
        ))));
        Assertions.assertEquals(expected, statements);
    }

    @Test
    void givenInvalidArgument_throwError() {
        List<Token> tokens = List.of(identifier("bar"), leftParens(), valueToken(1.0), createToken(COMMA), rightParens(), terminator(), end());
        Parser parser = new Parser(tokens);
        Assertions.assertThrows(ParserSyntaxException.class, parser::parse);
    }

    @Test
    void givenInvalidNextArgument_throwError() {
        List<Token> tokens = List.of(identifier("bar"), leftParens(), createToken(COMMA), valueToken(2.0), rightParens(), terminator(), end());
        Parser parser = new Parser(tokens);
        Assertions.assertThrows(ParserSyntaxException.class, parser::parse);
    }

    @Test
    void givenValidVariable_returnVariableExpression() {
        List<Token> tokens = List.of(identifier("bar"), terminator(), end());
        ParserExceptionHandler exceptionHandler = new ParserExceptionHandler();
        Parser parser = new Parser(tokens, exceptionHandler);
        List<Statement> statements = parser.parse();
        List<Statement> expected = List.of(expression(new Expr.Variable("bar")));
        Assertions.assertEquals(expected, statements);
    }


    @Test
    void givenMultipleInValidSyntax_printOutTheCorrectErrors() {
        List<Token> tokens = List.of(
                createToken(SMTH), identifier("a"), createToken(ASSIGNMENT), terminator(),
                createToken(LOOP), leftParens(), valueToken(2.0), leftCurlyBracket(),
                createToken(IF), leftParens(), identifier("a"), createToken(GREATER), valueToken(5.0), rightParens(), leftCurlyBracket(),
                print(), leftParens(), stringToken("bigger"), terminator(),
                createToken(ELSE), leftCurlyBracket(),
                print(), leftParens(), stringToken("smaller"), rightParens(),
                rightCurlyBracket(),
                rightCurlyBracket(), end());
        ParserExceptionHandler parserExceptionHandler = new ParserExceptionHandler();
        Parser parser = new Parser(tokens, parserExceptionHandler);

        try {
            parser.parse();
        } catch (RuntimeException e) {
        }
        Assertions.assertTrue(parserExceptionHandler.hasExceptions());
        Assertions.assertEquals("""
                Expected 'expression' at line 0, but found ';'.
                Expected ')' at line 0, but found '{'.
                Expected ')' at line 0, but found ';'.
                Found else without if at line 0.
                Expected ';' at line 0, but found '}'.
                Reached end of file while parsing.
                """, parserExceptionHandler.printExceptions());
    }

    @Test
    void givenValidInterrupt_returnInterruptStatement() {
        List<Token> tokens = List.of(createToken(INTERRUPT), terminator(), end());
        Parser parser = new Parser(tokens);
        List<Statement> statements = parser.parse();
        List<Statement> expected = List.of(new Statement.Interrupt());
        Assertions.assertEquals(expected, statements);
    }

    @Test
    void givenIfWithElseAndPrintAndReturn_returnValidList() {
        List<Token> tokens = List.of(
                createToken(IF), leftParens(), createToken(TRUE), rightParens(),
                leftCurlyBracket(), rightCurlyBracket(), createToken(ELSE),
                leftCurlyBracket(), rightCurlyBracket(),
                print(), leftParens(), stringToken("test"), rightParens(), terminator(),
                createToken(RETURN), terminator(), end());

        Parser parser = new Parser(tokens);
        List<Statement> statements = parser.parse();
        List<Statement> expected = List.of(
                new Statement.If(literal(true), new Statement.Block(List.of()), new Statement.Block(List.of())),
                printStatement("test"),
                new Statement.Return(null)
        );
        Assertions.assertEquals(expected, statements);
    }

    @Test
    void givenIfWithIfElseAndReturnWithValue_returnValidList() {
        List<Token> tokens = List.of(
                createToken(IF), leftParens(), createToken(TRUE), rightParens(),
                leftCurlyBracket(), rightCurlyBracket(), createToken(ELSE),
                createToken(IF), leftParens(), createToken(TRUE), rightParens(),
                leftCurlyBracket(), rightCurlyBracket(),
                createToken(RETURN), stringToken("Hello world"), terminator(), end());

        Parser parser = new Parser(tokens);
        List<Statement> statements = parser.parse();
        List<Statement> expected = List.of(
                new Statement.If(literal(true), new Statement.Block(List.of()),
                        new Statement.If(literal(true), new Statement.Block(List.of()), null)),
                new Statement.Return(literal("Hello world"))
        );
        Assertions.assertEquals(expected, statements);
    }

    @Test
    void givenLoopVariableAnd0ArityFunction_returnValidList() {
        List<Token> tokens = List.of(
                createToken(LOOP), leftParens(), createToken(TRUE), rightParens(),
                leftCurlyBracket(), rightCurlyBracket(),
                createToken(SMTH), identifier("str"), createToken(ASSIGNMENT), stringToken("Value"), terminator(),
                createToken(FUNCTION), identifier("foo"), leftParens(), rightParens(),
                leftCurlyBracket(), rightCurlyBracket(), end()
        );

        Parser parser = new Parser(tokens);
        List<Statement> statements = parser.parse();
        List<Statement> expected = List.of(
                new Statement.Loop(new Statement.Block(List.of()), literal(true)),
                new Statement.Variable("str", literal("Value")),
                new Statement.Function("foo", new Statement.Block(List.of()), List.of())
        );
        Assertions.assertEquals(expected, statements);
    }

    @Test
    void givenArity2FunctionAndInterrupt_returnValidList() {
        List<Token> tokens = List.of(
                createToken(FUNCTION), identifier("foo"), leftParens(),
                identifier("val1"), createToken(COMMA), identifier("val2"),
                rightParens(), leftCurlyBracket(), rightCurlyBracket(),
                createToken(INTERRUPT), terminator(), end()
        );

        Parser parser = new Parser(tokens);
        List<Statement> statements = parser.parse();
        List<Statement> expected = List.of(
                new Statement.Function("foo", new Statement.Block(List.of()), List.of("val1", "val2")),
                new Statement.Interrupt()
        );
        Assertions.assertEquals(expected, statements);
    }
}