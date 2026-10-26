package parser;

import lexer.Token;
import lexer.TokenType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static lexer.TokenType.MINUS;
import static lexer.TokenType.PLUS;

class ExprTest {

    Expr binary(double left, TokenType operator, double right) {
        return new Expr.Binary(literal(left), createToken(operator), literal(right));
    }


    Expr binary(Expr left, TokenType operator, Expr right) {
        return new Expr.Binary(left, createToken(operator), right);
    }

    Expr literal(Object val) {
        return new Expr.Literal(val);
    }

    Token createToken(TokenType type) {
        return new Token(type, null, null, 0);
    }

    @Test
    void givenInvalidExpressionComparison_ReturnFalse() {
        Expr expression = new Expr.Variable("bar");
        Expr expression2 = new Expr.Literal(3.0);
        Assertions.assertAll(
                () -> Assertions.assertNotEquals(new Expr.Call("a", List.of()), expression),
                () -> Assertions.assertNotEquals(new Expr.Grouping(literal(2.0)), expression),
                () -> Assertions.assertNotEquals(new Expr.Unary(createToken(MINUS), literal(3.0)), expression),
                () -> Assertions.assertNotEquals(binary(1.0, PLUS, 2.0), expression),
                () -> Assertions.assertNotEquals(literal(2.0), expression),
                () -> Assertions.assertNotEquals(new Expr.Variable("bar"), expression2)
        );
    }

    @Test
    void givenWrongArity_returnFalse() {
        Expr expression = new Expr.Call("a", List.of());
        Assertions.assertNotEquals(new Expr.Call("a", List.of(literal(3.0))), expression);
    }

    @Test
    void givenDifferentBinary_returnFalse() {
        Expr expression = binary(2.0, MINUS, 3.0);
        Expr expression2 = binary(literal(null), MINUS, literal(3.0));
        Expr expression3 = binary(2.0, PLUS, 3.0);
        Assertions.assertAll(
                () ->  Assertions.assertNotEquals(expression, expression2),
                () ->  Assertions.assertNotEquals(expression, expression3)
        );
    }

    @Test
    void givenDifferentCall_returnFalse() {
        Expr expression = new Expr.Call("a", List.of());
        Expr expression2 = new Expr.Call("b", List.of());
        Assertions.assertNotEquals(expression, expression2);
    }

}