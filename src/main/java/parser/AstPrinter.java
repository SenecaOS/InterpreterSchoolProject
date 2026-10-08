package parser;

import java.util.List;

public class AstPrinter implements Expr.Visitor<String>, Statement.Visitor<String> {

    public String print(Statement statement) {
        return statement.accept(this);
    }

    private String toPrintableList(List<Expr> exprs) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < exprs.size(); i++) {
            if (i != 0) sb.append(", ");
            sb.append(exprs.get(i).accept(this));
        }
        sb.append("]");

        return sb.toString();
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
    public String visitCall(Expr.Call expr) {
        return "call: " + expr.name + "(" + toPrintableList(expr.args) + ")";
    }

    @Override
    public String visitVariable(Expr.Variable expr) {
        return "var: " + expr.name;
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
        sb.append("{\n");
        for (Statement statement : block.statements) {
            sb.append(print(statement)).append("\n");
        }
        sb.append("}\n");
        return sb.toString();
    }

    @Override
    public String visitLoop(Statement.Loop loop) {
    return "loop(" + loop.condition.accept(this) + ")\n" +
                loop.block.accept(this);
    }

    @Override
    public String visitIf(Statement.If iff) {
        StringBuilder sb =  new StringBuilder();
        if (iff.condition != null) {
            sb.append("if(").append(iff.condition.accept(this)).append(")\n");
        }
        sb.append(iff.block.accept(this));
        if (iff.statement != null) {
            sb.append("else ").append(iff.statement.accept(this));
        }

        return sb.toString();
    }

    @Override
    public String visitReturn(Statement.Return ret) {
        return "return" + (ret.value != null ? " " + ret.value.accept(this) : "");
    }

    @Override
    public String visitFunction(Statement.Function function) {
        return "function " + function.name + "(" + function.params + ")\n" + function.block.accept(this);
    }
}
