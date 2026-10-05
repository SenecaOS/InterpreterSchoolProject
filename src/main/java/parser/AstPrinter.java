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
        return "print(" + print.expr.accept(this) + ")";
    }

    @Override
    public String visitVariable(Statement.Variable variable) {
        return variable.name + " = " + variable.value.accept(this);
    }

    @Override
    public String visitBlock(Statement.Block block) {
        StringBuilder sb = new StringBuilder();
        for (Statement statement : block.statements) {
            sb.append(print(statement)).append("\n");
        }
        return sb.toString();
    }

    @Override
    public String visitLoop(Statement.Loop loop) {
    return "loop(" + loop.condition.accept(this) + ") " +
                "{\n" + loop.block.accept(this) + "}";
    }

    @Override
    public String visitIf(Statement.If iff) {
        StringBuilder sb =  new StringBuilder();
        if (iff.condition != null) {
            sb.append("if(").append(iff.condition.accept(this)).append(")");
        }
        sb.append("{\n").append(iff.block.accept(this)).append("}");
        if (iff.elseStatement != null) {
            sb.append("else ").append(iff.elseStatement.accept(this));
        }

        return sb.toString();
    }
}
