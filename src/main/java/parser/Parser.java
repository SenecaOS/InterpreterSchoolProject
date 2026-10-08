package parser;

import lexer.Token;
import lexer.TokenType;

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

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public List<Statement> parse() {
        List<Statement> statements = new ArrayList<>();
        while (!isAtEnd()) {
            statements.add(getNext());
        }

        return statements;
    }

    private Statement getNext() {

        if (match(FUNCTION)) {
            return functionStatement();
        }

        if (match(LET)) {
            return variableStatement();
        }

        if (match(RETURN)) {
            return returnStatement();
        }

        if (match(IF)) {
            return ifStatement();
        }

        if  (match(LOOP)) {
            return loopStatement();
        }

        if (match(PRINT)) {
            return printStatement();
        }

        return expressionStatement();
    }

    private Statement.Function functionStatement() {
        String name = consume(IDENTIFIER, "Expected identifier").getVal();
        consume(LEFT_PARENS, "Expect '(' after identifier.");

        List<String> params = new ArrayList<>();

        if (match(IDENTIFIER)) {

            params.add(prev().getVal());

            while(match(COMMA)) {
                String paramName =  consume(IDENTIFIER, "Expected identifier").getVal();
                if (params.contains(paramName)) throw new IllegalArgumentException("Duplicate parameter: " + paramName);
                params.add(paramName);

                if (params.size() > MAX_ARITY) throw new IllegalArgumentException("Too many parameters for function: " + name);
            }
        }
        
        consume(RIGHT_PARENS, "Expect ')' after expression.");
        Statement.Block block = blockStatement();
        return new Statement.Function(name, block, params);
    }

    private Statement.Variable variableStatement() {
        Token token = consume(IDENTIFIER, "Expected identifier");
        consume(ASSIGNMENT, "Expected '=' after variable name");
        Expr expression = expression();
        return new Statement.Variable(token.getVal(), expression);
    }

    private Statement.Return returnStatement() {
        Expr expression = null;
        try {
            expression = expression();
        } catch (IllegalArgumentException ignored) {}
        return new Statement.Return(expression);
    }

    private Statement.Loop loopStatement() {
        consume(LEFT_PARENS, "Expect '(' after loop.");
        Expr expression = expression();
        consume(RIGHT_PARENS, "Expect ')' after expression.");
        Statement.Block block = blockStatement();
        return  new Statement.Loop(block, expression);

    }

    private Statement.If ifStatement() {
        consume(LEFT_PARENS, "Expect '(' after loop.");
        Expr expression = expression();
        consume(RIGHT_PARENS, "Expect ')' after expression.");
        Statement.Block block = blockStatement();
        Statement elseStatement = null;

        if (match(ELSE)) {
            if (match(IF)) {
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
        consume(LEFT_SQBRACKET, "Expected '{' at the start of block statement");
        List<Statement> statements = new ArrayList<>();
        while(!check(RIGHT_SQBRACKET)) {
            statements.add(getNext());
        }
        consume(RIGHT_SQBRACKET, "Expected '}'at the end of block statement");
        depth--;
        return new Statement.Block(statements);
    }

    private Statement.Print printStatement() {
        consume(LEFT_PARENS, "Expect '(' after print.");
        Expr expression = expression();
        consume(RIGHT_PARENS, "Expect ')' after expression.");
        return new Statement.Print(expression);
    }

    private Statement.Expression expressionStatement() {
        Expr expression = expression();
        return new Statement.Expression(expression);
    }

    private Expr expression() {
        return equality();
    }

    private Expr equality() {
        Expr left = comparison();

        while(match(NOT_EQUAL, IS_EQUAL)) {
            Token operator = prev();
            Expr right = comparison();
            left = new Expr.Binary(left, operator, right);
        }

        return left;
    }

    private Expr comparison() {
        Expr left = term();

        while(match(GREATER, LESS, GREATER_EQUAL, LESS_EQUAL)) {
            Token operator = prev();
            Expr right = term();
            left = new Expr.Binary(left, operator, right);
        }

        return left;
    }

    private Expr term() {
        Expr left = factor();

        while(match(PLUS, MINUS)) {
            Token operator = prev();
            Expr right = factor();
            left = new Expr.Binary(left, operator, right);
        }

        return left;
    }

    private Expr factor() {
        Expr left = unary();

        while(match(STAR, SLASH)) {
            Token operator = prev();
            Expr right = unary();
            left = new Expr.Binary(left, operator, right);
        }

        return left;
    }

    private Expr unary() {
        if (match(NOT, MINUS)) {
            Token operator = prev();
            Expr expr = unary();
            return new Expr.Unary(operator, expr);
        } else {
            return primary();
        }
    }

    private Expr primary() {
        if (match(VALUE, STRING)) {
            Token token = prev();
            return new Expr.Literal(token.getLiteral());
        }

        if (match(TRUE)) return TRUE_EXPR;
        if (match(FALSE)) return FALSE_EXPR;
        if (match(NULL)) return NULL_EXPR;

        if (match(LEFT_PARENS)) {
            Expr expression = expression();
            consume(RIGHT_PARENS, "Expect ')' after expression.");
            return new Expr.Grouping(expression);
        }

        if (match(IDENTIFIER)) {
            return call();
        }

        Token latest = peek();
        throw new IllegalArgumentException("Cant parse expression at: " + latest);
    }

    private Expr call() {
        String name = prev().getVal();

        if (!check(LEFT_PARENS)) {
            return new Expr.Variable(name);
        }

        List<Expr> arguments = new ArrayList<>();
        consume(LEFT_PARENS, "Expect '(' after identifier.");

        if (!check(RIGHT_PARENS)) {

            do {
                arguments.add(expression());
            } while (match(COMMA));
        }

        consume(RIGHT_PARENS, "Expect ')' after expression.");

        return new Expr.Call(name, arguments);
    }

    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                current++;
                return true;
            }
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

    private Token consume(TokenType type, String errorMessage) {
        if (check(type)) {
            current++;
            return prev();
        }

        throw new IllegalArgumentException(errorMessage + ", but got: " + peek());
    }
}
