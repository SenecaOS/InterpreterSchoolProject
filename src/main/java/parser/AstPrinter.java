package parser;

public class AstPrinter implements Expr.Visitor<String>, Statement.Visitor<String> {

    public String print(Statement statement) {
        return statement.accept(this);
    }

    @Override
    public String visitBinary(Expr.Binary expr) {
        return "(" + expr.left.accept(this) + " " + expr.operator.getVal() + " " + expr.right.accept(this) + ")";
    }

    @Override
    public String visitLiteral(Expr.Literal expr) {
        Object value = expr.value;
        return value == null ? "null" : value.toString();
    }

    @Override
    public String visitUnary(Expr.Unary expr) {
        return "(" + expr.operator.getVal() + expr.expr.accept(this) + ")";
    }

    @Override
    public String visitGrouping(Expr.Grouping expr) {
        return "(" + expr.expr.accept(this) + ")";
    }

    @Override
    public String visitExpression(Statement.Expression expr) {
        return expr.expression.accept(this);
    }

    @Override
    public String visitPrint(Statement.Print print) {
        return print.expr.accept(this);
    }
}
