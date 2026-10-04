package parser;

public abstract class Statement {

    static class Expression extends Statement {

        final Expr expression;

        Expression(Expr expression) {
            this.expression = expression;
        }

    }

    static class Print extends Statement {

        final Expr expr;

        Print(Expr expr) {
            this.expr = expr;
        }

    }

}
