package parser;

import lexer.Token;
import lexer.TokenType;

import java.util.ArrayList;
import java.util.List;

import static lexer.TokenType.*;

public class Parser {

    private static final Expr TRUE_EXPR = new Expr.Literal(true);
    private static final Expr FALSE_EXPR = new Expr.Literal(false);
    private static final Expr NULL_EXPR = new Expr.Literal(null);

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
        if (match(PRINT)) {
            return printStatement();
        }

        return expressionStatement();
    }

    private Statement printStatement() {
        consume(LEFT_PARENS, "Expect '(' after print.");
        Expr expression = expression();
        consume(RIGHT_PARENS, "Expect ')' after expression.");
        return new Statement.Print(expression);
    }

    private Statement expressionStatement() {
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
        Expr left = unary();

        while(match(PLUS, MINUS)) {
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

        Token latest = peek();
        throw new IllegalArgumentException("Cant parse expression at: " + latest);
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

    private void consume(TokenType type, String errorMessage) {
        if (check(type)) {
            current++;
            return;
        }

        throw new IllegalArgumentException(errorMessage);
    }
}
