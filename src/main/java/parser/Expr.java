package parser;

import lexer.Token;

public class Expr {

    static class Binary extends Expr {

        final Expr left;
        final Token operator;
        final Expr right;

        Binary(Expr left, Token operator, Expr right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }
    }

    static class Literal extends Expr {

        final Object value;

        Literal(Object value) {
            this.value = value;
        }
    }

    static class Unary extends Expr {

        final Expr expr;
        final Token operator;

        Unary(Token Operator, Expr expr) {
            this.operator = Operator;
            this.expr = expr;
        }
    }

    static class Grouping extends Expr {

        final Expr expr;

        Grouping(Expr expr) {
            this.expr = expr;
        }
    }

}
