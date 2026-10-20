package parser;

import lexer.Token;

import java.util.List;

public abstract class Expr  {

    public interface Visitor<R> {
        R visitBinary(Binary expr);
        R visitLiteral(Literal expr);
        R visitUnary(Unary expr);
        R visitGrouping(Grouping expr);
        R visitCall(Call expr);
        R visitVariable(Variable expr);
    }

    public abstract <R> R accept(Visitor<R> visitor);

    public static class Binary extends Expr {

        public final Expr left;
        public final Token operator;
        public final Expr right;

        Binary(Expr left, Token operator, Expr right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitBinary(this);
        }
    }

    public static class Literal extends Expr {

        public final Object value;

        Literal(Object value) {
            this.value = value;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitLiteral(this);
        }
    }

    public static class Unary extends Expr {

        public final Expr expr;
        public final Token operator;

        Unary(Token Operator, Expr expr) {
            this.operator = Operator;
            this.expr = expr;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitUnary(this);
        }
    }

    public static class Grouping extends Expr {

        public final Expr expr;

        Grouping(Expr expr) {
            this.expr = expr;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitGrouping(this);
        }
    }

    public static class Call extends Expr {

        final String name;
        final List<Expr> args;
        Call(String name, List<Expr> args) {
            this.name = name;
            this.args = args;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitCall(this);
        }
    }

    public static class Variable extends Expr {

        final String name;

        public Variable(String name) {
            this.name = name;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitVariable(this);
        }
    }

}
