package parser;

import lexer.Token;
import lexer.TokenType;
import exceptions.parser.ParserExceptionHandler;
import exceptions.parser.ParserElseException;
import exceptions.parser.ParserException;
import exceptions.parser.ParserSyntaxException;

import java.util.ArrayList;
import java.util.List;

import static lexer.TokenType.*;

public class Parser {

    private static final int MAX_DEPTH = 20;
    private static final int MAX_ARITY = 20;

    private static final Expr TRUE_EXPR = new Expr.Literal(true);
    private static final Expr FALSE_EXPR = new Expr.Literal(false);
    private static final Expr NULL_EXPR = new Expr.Literal(null);

    private int depth = 0;
    private int current = 0;
    private final List<Token> tokens;

    private final ParserExceptionHandler parserExceptionHandler;

    public Parser(List<Token> tokens, ParserExceptionHandler parserExceptionHandler) {
        this.tokens = tokens;
        this.parserExceptionHandler = parserExceptionHandler;
    }

    public Parser(List<Token> tokens) {
        this(tokens, null);
    }

    public List<Statement> parse() {
        List<Statement> statements = new ArrayList<>();

        while (!isAtEnd()) {
            try {
                statements.add(getNext());
            } catch (ParserException error) {
                handleError(error);
            }
        }

        return statements;
    }

    private Statement getNext() {

        if (matchAndIncrement(FUNCTION)) {
            return functionStatement();
        }

        if (matchAndIncrement(SMTH)) {
            return variableStatement();
        }

        if (matchAndIncrement(RETURN)) {
            return returnStatement();
        }

        if (matchAndIncrement(IF)) {
            return ifStatement();
        }

        if (matchAndIncrement(LOOP)) {
            return loopStatement();
        }

        if (matchAndIncrement(PRINT)) {
            return printStatement();
        }

        if (matchAndIncrement(INTERRUPT)) {
            return InterruptStatement();
        }

        return expressionStatement();
    }

    private Statement.Function functionStatement() {
        String name = consume(IDENTIFIER, "identifier").getVal();
        consume(LEFT_PARENS, "(");

        List<String> params = new ArrayList<>();

        if (matchAndIncrement(IDENTIFIER)) {

            params.add(prev().getVal());

            while (matchAndIncrement(COMMA)) {
                String paramName = consume(IDENTIFIER, "identifier").getVal();
                if (params.contains(paramName)) throw new IllegalArgumentException("Duplicate parameter: " + paramName);
                params.add(paramName);

                if (params.size() > MAX_ARITY)
                    throw new IllegalArgumentException("Too many parameters for function: " + name);
            }
        }

        consume(RIGHT_PARENS, ")");
        Statement.Block block = blockStatement();
        return new Statement.Function(name, block, params);
    }

    private Statement.Variable variableStatement() {
        Token token = consume(IDENTIFIER, "identifier");
        consume(ASSIGNMENT, "=");
        Expr expression = expression();
        consume(TERMINATOR, ";");
        return new Statement.Variable(token.getVal(), expression);
    }

    private Statement.Return returnStatement() {
        Expr expression = null;
        try {
            expression = expression();
        } catch (ParserSyntaxException ignored) {
        }
        consume(TERMINATOR, ";");
        return new Statement.Return(expression);
    }

    private Statement.Loop loopStatement() {
        consume(LEFT_PARENS, "(");
        Expr expression = expression();
        consume(RIGHT_PARENS, ")");
        Statement.Block block = blockStatement();
        return new Statement.Loop(block, expression);

    }

    private Statement.If ifStatement() {
        consume(LEFT_PARENS, "(");
        Expr expression = expression();
        consume(RIGHT_PARENS, ")");
        Statement.Block block = blockStatement();
        Statement elseStatement = null;

        if (matchAndIncrement(ELSE)) {
            if (matchAndIncrement(IF)) {
                elseStatement = ifStatement();
            } else {
                elseStatement = blockStatement();
            }

        }

        return new Statement.If(expression, block, elseStatement);
    }

    private Statement.Block blockStatement() {
        depth++;
        if (depth >= MAX_DEPTH) throw new IllegalArgumentException("Reached max depth, consider refactoring your code");
        consume(LEFT_CURLYBRAC, "{");
        List<Statement> statements = new ArrayList<>();
        while (!check(RIGHT_CURLYBRAC) && !isAtEnd()) {
            try {
                statements.add(getNext());
            } catch (ParserException error) {
                handleError(error);
            }
        }
        consume(RIGHT_CURLYBRAC, "}");
        depth--;
        return new Statement.Block(statements);
    }

    private Statement.Print printStatement() {
        consume(LEFT_PARENS, "(");
        Expr expression = expression();
        consume(RIGHT_PARENS, ")");
        consume(TERMINATOR, ";");
        return new Statement.Print(expression);
    }

    private Statement.Interrupt InterruptStatement() {
        consume(TERMINATOR, ";");
        return new Statement.Interrupt();
    }

    private Statement.Expression expressionStatement() {
        Expr expression = expression();
        consume(TERMINATOR, ";");
        return new Statement.Expression(expression);
    }

    private Expr expression() {
        return logical();
    }

    private Expr logical() {
        Expr left = equality();

        while (matchAndIncrement(AND, OR)) {
            Token operator = prev();
            Expr right = equality();
            left = new Expr.Binary(left, operator, right);
        }

        return left;
    }

    private Expr equality() {
        Expr left = comparison();

        while (matchAndIncrement(NOT_EQUALS, EQUALS)) {
            Token operator = prev();
            Expr right = comparison();
            left = new Expr.Binary(left, operator, right);
        }

        return left;
    }

    private Expr comparison() {
        Expr left = term();

        while (matchAndIncrement(GREATER, LESSER, GREATER_EQUALS, LESS_EQUALS)) {
            Token operator = prev();
            Expr right = term();
            left = new Expr.Binary(left, operator, right);
        }

        return left;
    }

    private Expr term() {
        Expr left = factor();

        while (matchAndIncrement(PLUS, MINUS)) {
            Token operator = prev();
            Expr right = factor();
            left = new Expr.Binary(left, operator, right);
        }

        return left;
    }

    private Expr factor() {
        Expr left = unary();

        while (matchAndIncrement(STAR, SLASH)) {
            Token operator = prev();
            Expr right = unary();
            left = new Expr.Binary(left, operator, right);
        }

        return left;
    }

    private Expr unary() {
        if (matchAndIncrement(NOT, MINUS)) {
            Token operator = prev();
            Expr expr = unary();
            return new Expr.Unary(operator, expr);
        } else {
            return primary();
        }
    }

    private Expr primary() {
        if (matchAndIncrement(VALUE, STRING)) {
            Token token = prev();
            return new Expr.Literal(token.getLiteral());
        }

        if (matchAndIncrement(TRUE)) return TRUE_EXPR;
        if (matchAndIncrement(FALSE)) return FALSE_EXPR;
        if (matchAndIncrement(NOTH)) return NULL_EXPR;

        if (matchAndIncrement(LEFT_PARENS)) {
            Expr expression = expression();
            consume(RIGHT_PARENS, ")");
            return new Expr.Grouping(expression);
        }

        if (matchAndIncrement(IDENTIFIER)) {
            return call();
        }

        throw new ParserSyntaxException(peek(), "expression");
    }

    private Expr call() {
        String name = prev().getVal();

        if (!check(LEFT_PARENS)) {
            return new Expr.Variable(name);
        }

        List<Expr> arguments = new ArrayList<>();
        consume(LEFT_PARENS, "(");

        if (!check(RIGHT_PARENS)) {

            do {
                arguments.add(expression());
            } while (matchAndIncrement(COMMA));
        }

        consume(RIGHT_PARENS, ")");

        return new Expr.Call(name, arguments);
    }

    private boolean matchAndIncrement(TokenType... types) {
        if (match(types)) {
            current++;
            return true;
        }
        return false;
    }

    private boolean match(TokenType... types) {
        if (isAtEnd()) return false;
        TokenType needs = peek().getTokenType();
        for (TokenType type : types) {
            if (type == needs) return true;
        }
        return false;
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().getTokenType() == type;
    }

    private boolean isAtEnd() {
        return peek().getTokenType() == TokenType.EOF;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token prev() {
        return tokens.get(current - 1);
    }

    private Token consume(TokenType type, String expected) {
        if (matchAndIncrement(type)) {
            return prev();
        }

        throw new ParserSyntaxException(peek(), expected);
    }

    private void findNextStatement() {
        while (!isAtEnd() && !match(FUNCTION, SMTH, RETURN, IF, LOOP, PRINT)) {
            current++;

            if (matchAndIncrement(ELSE)) {
                parserExceptionHandler.addException(new ParserElseException(prev()));
            }
        }
    }

    private void handleError(ParserException error) {
        if (parserExceptionHandler != null) {
            parserExceptionHandler.addException(error);
            findNextStatement();
        } else {
            throw error;
        }
    }
}
