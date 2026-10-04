package parser;

public abstract class Statement {

    public interface Visitor<R> {
        R visitExpression(Expression expr);
        R visitPrint(Print print);
    }

    abstract <R> R accept(Visitor<R> visitor);

    public static class Expression extends Statement {

        final Expr expression;

        Expression(Expr expression) {
            this.expression = expression;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitExpression(this);
        }
    }

    public static class Print extends Statement {

        final Expr expr;

        Print(Expr expr) {
            this.expr = expr;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitPrint(this);
        }
    }

}
