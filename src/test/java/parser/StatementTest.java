package parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class StatementTest {

    Expr literal(Object val) {
        return new Expr.Literal(val);
    }

    Statement printStatement(String string) {
        return new Statement.Print(new Expr.Literal(string));
    }

    @Test
    void givenInvalidStatementComparison_ReturnFalse() {
        Statement statement = new Statement.Expression(new Expr.Variable("bar"));
        Statement statement2 = new Statement.Interrupt();
        Assertions.assertAll(
                () -> Assertions.assertNotEquals(new Statement.Function("name", new Statement.Block(List.of()), List.of()), statement),
                () -> Assertions.assertNotEquals(new Statement.Print(literal("test")), statement),
                () -> Assertions.assertNotEquals(new Statement.Variable("a", literal(null)), statement),
                () -> Assertions.assertNotEquals(new Statement.Block(List.of()), statement),
                () -> Assertions.assertNotEquals(new Statement.Loop(new Statement.Block(List.of()), literal(true)), statement),
                () -> Assertions.assertNotEquals(new Statement.Return(literal(null)), statement),
                () -> Assertions.assertNotEquals(new Statement.Interrupt(), statement),
                () -> Assertions.assertNotEquals(new Statement.Expression(new Expr.Variable("test")), statement2),
                () -> Assertions.assertNotEquals(new Statement.If(literal(false), new Statement.Block(List.of()), null), statement2)
        );
    }

    @Test
    void givenDifferentFunctionStatements_returnFalse() {
        Statement statement = new Statement.Function("bar", new Statement.Block(List.of(printStatement("hello"))), List.of());
        Assertions.assertAll(
                () -> Assertions.assertNotEquals(new Statement.Function("", new Statement.Block(List.of(printStatement("hello"))), List.of()), statement),
                () -> Assertions.assertNotEquals(new Statement.Function("bar", new Statement.Block(List.of()), List.of()), statement),
                () -> Assertions.assertNotEquals(new Statement.Function("bar", new Statement.Block(List.of()), List.of("a")), statement)
        );
    }

    @Test
    void givenDifferentReturnStatements_returnFalse() {
        Statement statement = new Statement.Return(null);
        Statement statement2 = new Statement.Return(literal(2.0));
        Assertions.assertAll(
                () -> Assertions.assertNotEquals(new Statement.Return(literal(2.0)), statement),
                () -> Assertions.assertNotEquals(new Statement.Return(null), statement2)
        );
    }

    @Test
    void givenDifferentIfStatements_returnFalse() {
        Statement statement = new Statement.If(literal(false), new Statement.Block(List.of()), null);
        Statement statement2 = new Statement.If(literal(false), new Statement.Block(List.of()), new Statement.Block(List.of()));
        Statement statement3 = new Statement.If(literal(false), new Statement.Block(List.of(printStatement("hello"))), new Statement.Block(List.of()));
        Statement statement4 = new Statement.If(literal(true), new Statement.Block(List.of(printStatement("hello"))), new Statement.Block(List.of()));
        Assertions.assertAll(
                () -> Assertions.assertNotEquals(statement2, statement),
                () -> Assertions.assertNotEquals(statement, statement2),
                () -> Assertions.assertNotEquals(statement, statement3),
                () -> Assertions.assertNotEquals(statement, statement4)
        );
    }

    @Test
    void givenDifferentLoopStatements_returnFalse() {
        Statement statement = new Statement.Loop(new Statement.Block(List.of()), literal(true));
        Statement statement2 = new Statement.Loop(new Statement.Block(List.of()), literal(false));
        Statement statement3 = new Statement.Loop(new Statement.Block(List.of(printStatement("test"))), literal(false));
        Assertions.assertAll(
                () -> Assertions.assertNotEquals(statement2, statement),
                () -> Assertions.assertNotEquals(statement, statement2),
                () -> Assertions.assertNotEquals(statement, statement3)
        );
    }

    @Test
    void givenDifferentVariableStatements_returnFalse() {
        Statement statement = new Statement.Variable("a", literal(null));
        Statement statement2 = new Statement.Variable("a", literal(2.0));
        Statement statement3 = new Statement.Variable("b", literal(2.0));
        Assertions.assertAll(
                () -> Assertions.assertNotEquals(statement2, statement),
                () -> Assertions.assertNotEquals(statement, statement2),
                () -> Assertions.assertNotEquals(statement, statement3)
        );
    }

}