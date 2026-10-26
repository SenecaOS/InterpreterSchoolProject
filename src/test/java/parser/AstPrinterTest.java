package parser;

import lexer.Token;
import lexer.TokenType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class AstPrinterTest {

    Expr literal(Object val) {
        return new Expr.Literal(val);
    }

    Token createToken(TokenType type) {
        return new Token(type, null, null, 0);
    }

    @Test
    void givenExpressionList_returnValidString() {
        List<Expr> list = List.of(new Expr.Literal("a"),
                new Expr.Unary(createToken(TokenType.MINUS),new Expr.Literal(2.0)),
                new Expr.Variable("x"));
        Assertions.assertEquals("[a, (null2.0), var: x]", AstPrinter.INSTANCE.toPrintableList(list));
    }

    @Test
    void givenLoop_returnValidString() {
        Statement statement = new Statement.If(literal(3.0), new Statement.Block(List.of()), null);
        Assertions.assertEquals("""
                if(3.0)
                {
                }
                """, statement.accept(AstPrinter.INSTANCE));
    }

    @Test
    void givenReturn_returnValidString() {
        Statement statement = new Statement.Return(literal(3.0));
        Assertions.assertEquals("return 3.0", statement.accept(AstPrinter.INSTANCE));
    }

    @Test
    void givenReturnWithNoArg_returnValidString() {
        Statement statement = new Statement.Return(null);
        Assertions.assertEquals("return", statement.accept(AstPrinter.INSTANCE));
    }
}