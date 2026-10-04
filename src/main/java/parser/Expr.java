package parser;

import lexer.Token;

public abstract class Expr  {

    public interface Visitor<R> {
        R visitBinary(Binary expr);
        R visitLiteral(Literal expr);
        R visitUnary(Unary expr);
        R visitGrouping(Grouping expr);
    }

    abstract <R> R accept(Visitor<R> visitor);

    public static class Binary extends Expr {

        final Expr left;
        final Token operator;
        final Expr right;

        Binary(Expr left, Token operator, Expr right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitBinary(this);
        }
    }

    public static class Literal extends Expr {

        final Object value;

        Literal(Object value) {
            this.value = value;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitLiteral(this);
        }
    }

    public static class Unary extends Expr {

        final Expr expr;
        final Token operator;

        Unary(Token Operator, Expr expr) {
            this.operator = Operator;
            this.expr = expr;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitUnary(this);
        }
    }

    public static class Grouping extends Expr {

        final Expr expr;

        Grouping(Expr expr) {
            this.expr = expr;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitGrouping(this);
        }
    }

}
