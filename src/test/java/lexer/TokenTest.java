package lexer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import exceptions.lexer.LexerExceptionHandler;
import org.junit.jupiter.api.Test;


public class TokenTest{;


 @Test
void testReturnsCorrectTokenType() {
Scanner scanner = new Scanner("loop");
scanner.scanTokens();
TokenType type = TokenType.LOOP;
assertEquals(type, scanner.getTokens().get(0).getTokenType());
assertEquals("loop", scanner.getTokens().get(0).getVal());


scanner = new Scanner("if");
scanner.scanTokens();
type = TokenType.IF;
assertEquals(type, scanner.getTokens().get(0).getTokenType());
assertEquals("if", scanner.getTokens().get(0).getVal());


scanner = new Scanner("variable");
scanner.scanTokens();
type = TokenType.IDENTIFIER;
assertEquals(type, scanner.getTokens().get(0).getTokenType());
assertEquals("variable", scanner.getTokens().get(0).getVal());


scanner = new Scanner("123");
scanner.scanTokens();
type = TokenType.VALUE;
assertEquals(type, scanner.getTokens().get(0).getTokenType());
assertEquals("123", scanner.getTokens().get(0).getVal());


scanner = new Scanner("+");
scanner.scanTokens();
type = TokenType.PLUS;
assertEquals(type, scanner.getTokens().get(0).getTokenType());
assertEquals("+", scanner.getTokens().get(0).getVal());


scanner = new Scanner("-");
scanner.scanTokens();
type = TokenType.MINUS;
assertEquals(type, scanner.getTokens().get(0).getTokenType());
assertEquals("-", scanner.getTokens().get(0).getVal());


scanner = new Scanner("(");
scanner.scanTokens();
type = TokenType.LEFT_PARENS;
assertEquals(type, scanner.getTokens().get(0).getTokenType());
assertEquals("(", scanner.getTokens().get(0).getVal());


scanner = new Scanner(")");
scanner.scanTokens();
type = TokenType.RIGHT_PARENS;
assertEquals(type, scanner.getTokens().get(0).getTokenType());
assertEquals(")", scanner.getTokens().get(0).getVal());


scanner = new Scanner("[");
scanner.scanTokens();
type = TokenType.LEFT_SQBRACKET;
assertEquals(type, scanner.getTokens().get(0).getTokenType());
assertEquals("[", scanner.getTokens().get(0).getVal());


scanner = new Scanner("]");
scanner.scanTokens();
type = TokenType.RIGHT_SQBRACKET;
assertEquals(type, scanner.getTokens().get(0).getTokenType());
assertEquals("]", scanner.getTokens().get(0).getVal());


scanner = new Scanner("{");
scanner.scanTokens();
type = TokenType.LEFT_CURLYBRAC;
assertEquals(type, scanner.getTokens().get(0).getTokenType());
assertEquals("{", scanner.getTokens().get(0).getVal());


scanner = new Scanner("}");
scanner.scanTokens();
type = TokenType.RIGHT_CURLYBRAC;
assertEquals(type, scanner.getTokens().get(0).getTokenType());
assertEquals("}", scanner.getTokens().get(0).getVal());


scanner = new Scanner("*");
scanner.scanTokens();
type = TokenType.STAR;
assertEquals(type, scanner.getTokens().get(0).getTokenType());
assertEquals("*", scanner.getTokens().get(0).getVal());


scanner = new Scanner(";");
scanner.scanTokens();
type = TokenType.SEMICOLON;
assertEquals(type, scanner.getTokens().get(0).getTokenType());
assertEquals(";", scanner.getTokens().get(0).getVal());


scanner = new Scanner("/");
scanner.scanTokens();
type = TokenType.SLASH;
assertEquals(type, scanner.getTokens().get(0).getTokenType());
assertEquals("/", scanner.getTokens().get(0).getVal());


scanner = new Scanner("");
scanner.scanTokens();
type = TokenType.EOF;
assertEquals(type, scanner.getTokens().get(0).getTokenType());
assertEquals("", scanner.getTokens().get(0).getVal());


scanner = new Scanner("\"hello\"");
scanner.scanTokens();
type = TokenType.STRING;
assertEquals(type, scanner.getTokens().get(0).getTokenType());
assertEquals("\"hello\"", scanner.getTokens().get(0).getVal());


scanner = new Scanner("!");
scanner.scanTokens();
type = TokenType.NOT;
assertEquals(type, scanner.getTokens().get(0).getTokenType());
assertEquals("!", scanner.getTokens().get(0).getVal());


scanner = new Scanner("!=");
scanner.scanTokens();
type = TokenType.NOT_EQUALS;
assertEquals(type, scanner.getTokens().get(0).getTokenType());
assertEquals("!=", scanner.getTokens().get(0).getVal());


scanner = new Scanner("|");
scanner.scanTokens();
type = TokenType.OR;
assertEquals(type, scanner.getTokens().get(0).getTokenType());
assertEquals("|", scanner.getTokens().get(0).getVal());


scanner = new Scanner("&");
scanner.scanTokens();
type = TokenType.AND;
assertEquals(type, scanner.getTokens().get(0).getTokenType());
assertEquals("&", scanner.getTokens().get(0).getVal());


scanner = new Scanner("<");
scanner.scanTokens();
type = TokenType.LESSER;
assertEquals(type, scanner.getTokens().get(0).getTokenType());
assertEquals("<", scanner.getTokens().get(0).getVal());


scanner = new Scanner(">");
scanner.scanTokens();
type = TokenType.GREATER;
assertEquals(type, scanner.getTokens().get(0).getTokenType());
assertEquals(">", scanner.getTokens().get(0).getVal());


scanner = new Scanner(">=");
scanner.scanTokens();
type = TokenType.GREATER_EQUALS;
assertEquals(type, scanner.getTokens().get(0).getTokenType());
assertEquals(">=", scanner.getTokens().get(0).getVal());


scanner = new Scanner("<=");
scanner.scanTokens();
type = TokenType.LESS_EQUALS;
assertEquals(type, scanner.getTokens().get(0).getTokenType());
assertEquals("<=", scanner.getTokens().get(0).getVal());


scanner = new Scanner("smth");
scanner.scanTokens();
type = TokenType.SMTH;
assertEquals(type, scanner.getTokens().get(0).getTokenType());
assertEquals("smth", scanner.getTokens().get(0).getVal());


scanner = new Scanner("true");
scanner.scanTokens();
type = TokenType.TRUE;
assertEquals(type, scanner.getTokens().get(0).getTokenType());
assertEquals("true", scanner.getTokens().get(0).getVal());


scanner = new Scanner("=");
scanner.scanTokens();
type = TokenType.ASSIGNMENT;
assertEquals(type, scanner.getTokens().get(0).getTokenType());
assertEquals("=", scanner.getTokens().get(0).getVal());


scanner = new Scanner("==");
scanner.scanTokens();
type = TokenType.EQUALS;
assertEquals(type, scanner.getTokens().get(0).getTokenType());
assertEquals("==", scanner.getTokens().get(0).getVal());


scanner = new Scanner("else");
scanner.scanTokens();
type = TokenType.ELSE;
assertEquals(type, scanner.getTokens().get(0).getTokenType());
assertEquals("else", scanner.getTokens().get(0).getVal());


scanner = new Scanner("return");
scanner.scanTokens();
type = TokenType.RETURN;
assertEquals(type, scanner.getTokens().get(0).getTokenType());
assertEquals("return", scanner.getTokens().get(0).getVal());


scanner = new Scanner("noth");
scanner.scanTokens();
type = TokenType.NOTH;
assertEquals(type, scanner.getTokens().get(0).getTokenType());
assertEquals("noth", scanner.getTokens().get(0).getVal());


scanner = new Scanner("false");
scanner.scanTokens();
type = TokenType.FALSE;
assertEquals(type, scanner.getTokens().get(0).getTokenType());
assertEquals("false", scanner.getTokens().get(0).getVal());


scanner = new Scanner(",");
scanner.scanTokens();
type = TokenType.COMMA;
assertEquals(type, scanner.getTokens().get(0).getTokenType());
assertEquals(",", scanner.getTokens().get(0).getVal());


scanner = new Scanner("print");
scanner.scanTokens();
type = TokenType.PRINT;
assertEquals(type, scanner.getTokens().get(0).getTokenType());
assertEquals("print", scanner.getTokens().get(0).getVal());


scanner = new Scanner("function");
scanner.scanTokens();
type = TokenType.FUNCTION;
assertEquals(type, scanner.getTokens().get(0).getTokenType());
assertEquals("function", scanner.getTokens().get(0).getVal());


scanner = new Scanner("interrupt");
scanner.scanTokens();
type = TokenType.INTERRUPT;
assertEquals(type, scanner.getTokens().get(0).getTokenType());
assertEquals("interrupt", scanner.getTokens().get(0).getVal());


	}
    @Test
    void testTokenNLineTest(){
        Scanner scanner = new Scanner("\n\n\ninterrupt");
        scanner.scanTokens();
        assertEquals(4, scanner.getTokens().get(0).getLine());
        scanner = new Scanner("\n\n\ninterrupt\n\n\n\ntaaaaa");
        scanner.scanTokens();
        assertEquals(8, scanner.getTokens().get(1).getLine());
        scanner = new Scanner("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\ntaaaaa");
        scanner.scanTokens();
        assertEquals(17, scanner.getTokens().get(0).getLine());
    }


    @Test
    void testValidTokenNum(){
     //EC Värden högre än som ryms i double
        Double temp = 1.7976931348623157E308 * 2;
        Token token = new Token(TokenType.VALUE, temp.toString(), temp, 1);

        assertEquals(Double.POSITIVE_INFINITY, token.getLiteral());

        //EC Värden lägre än som ryms i double

        temp = -1.7976931348623157E308 * 2;
        token = new Token(TokenType.VALUE, temp.toString(), temp, 1);

        assertEquals(Double.NEGATIVE_INFINITY, token.getLiteral());

        //EC Nummer med decimalvärde
        Scanner scanner = new Scanner("1.5");
        scanner.scanTokens();
        assertEquals(1.5, scanner.getTokens().get(0).getLiteral());
        //EC Värdet 0
        scanner = new Scanner("0");
        scanner.scanTokens();
        assertEquals(0.0, scanner.getTokens().get(0).getLiteral());
        //EC Värde med decimalvärde
        scanner = new Scanner("1.5");
        scanner.scanTokens();
        assertEquals(1.5, scanner.getTokens().get(0).getLiteral());


        //från EC värde med semikolon i slutet
        scanner = new Scanner("25;");
        scanner.scanTokens();
        assertEquals(25.0, scanner.getTokens().get(0).getLiteral());


        //från EC värde med operator i slutet
        scanner = new Scanner("25*");
        scanner.scanTokens();
        assertEquals(25.0, scanner.getTokens().get(0).getLiteral());



    }

}
