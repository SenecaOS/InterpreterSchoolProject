package parser;

import java.util.List;

public abstract class Statement {

    public interface Visitor<R> {
        R visitExpression(Expression expr);
        R visitPrint(Print print);
        R visitVariable(Variable variable);
        R visitBlock(Block block);
        R visitLoop(Loop loop);
        R visitIf(If iff);
        R visitReturn(Return ret);
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

    public static class Variable extends Statement {

        final String name;
        final Expr value;

        Variable(String name, Expr value) {
            this.name = name;
            this.value = value;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitVariable(this);
        }
    }

    public static class Block extends Statement {

        final List<Statement> statements;

        Block(List<Statement> statements) {
            this.statements = statements;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitBlock(this);
        }
    }

    public static class Loop extends Statement {

        final Block block;
        final Expr condition;


        public Loop(Block block, Expr condition) {
            this.block = block;
            this.condition = condition;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitLoop(this);
        }
    }

    public static class If extends Statement {

        final Expr condition;
        final Block block;
        final Statement statement;

        public If(Expr condition, Block block, Statement statement) {
            this.condition = condition;
            this.block = block;
            this.statement = statement;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitIf(this);
        }
    }

    public static class Return extends Statement {

        final Expr value;

        public Return(Expr value) {
            this.value = value;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitReturn(this);
        }
    }
}
