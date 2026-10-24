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

        R visitFunction(Function function);

        R visitInterrupt(Interrupt interrupt);
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

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Expression expr) {
                return this.expression.equals(expr.expression);
            }
            return false;
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

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Print print) {
                return this.expr.equals(print.expr);
            }
            return false;
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


        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Variable variable) {
                return this.name.equals(variable.name) && this.value.equals(variable.value);
            }
            return false;
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

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Block block) {
                return this.statements.equals(block.statements);
            }
            return false;
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

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Loop loop) {
                return this.block.equals(loop.block) && this.condition.equals(loop.condition);
            }
            return false;
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

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof If iff) {
                if (this.condition.equals(iff.condition) && this.block.equals(iff.block)) {
                    if (this.statement == null || iff.statement == null) {
                        return this.statement == null && iff.statement == null;
                    } else {
                        return this.statement.equals(iff.statement);
                    }
                }
            }
            return false;
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

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Return ret) {
                return this.value.equals(ret.value);
            }
            return false;
        }
    }

    public static class Function extends Statement {

        final String name;
        final Block block;
        final List<String> params;

        public Function(String name, Block block, List<String> params) {
            this.name = name;
            this.block = block;
            this.params = params;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitFunction(this);
        }

        @Override
        public boolean equals(Object obj) {
            if  (obj instanceof Function function) {
                return this.name.equals(function.name) && this.params.equals(function.params) && this.block.equals(function.block);
            }

            return false;
        }
    }

    public static class Interrupt extends Statement {

        public Interrupt() {
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitInterrupt(this);
        }

        @Override
        public boolean equals(Object obj) {
            return  obj instanceof Interrupt interrupt;
        }
    }
}
