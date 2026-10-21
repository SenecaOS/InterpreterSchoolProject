package lexer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import exceptions.lexer.LexerException;
import exceptions.lexer.LexerExceptionHandler;
import interpreter.RuntimeError;
import org.junit.jupiter.api.Test;


//TODO add test for smth keyword

public class ScannerTest {


	@Test
	void testNothKeyword() {

		Scanner scanner = new Scanner("noth");
		scanner.scanTokens();
		assertEquals("NOTH(\"noth\") EOF(\"\") ", scanner.toString());

		scanner = new Scanner("noth;");
		scanner.scanTokens();
		assertEquals("NOTH(\"noth\") SEMICOLON(\";\") EOF(\"\") ", scanner.toString());

		scanner = new Scanner("notha;");
		scanner.scanTokens();
		assertEquals("IDENTIFIER(\"notha\") SEMICOLON(\";\") EOF(\"\") ", scanner.toString());


		scanner = new Scanner("noth\t");
		scanner.scanTokens();
		assertEquals("NOTH(\"noth\") EOF(\"\") ", scanner.toString());

		scanner = new Scanner("noth \\ 5");
		scanner.scanTokens();
		assertEquals("NOTH(\"noth\") VALUE(\"5\") EOF(\"\") ", scanner.toString());
	}

	@Test
	void testErrorHandler_PrintsOutCorrectErrors() {
		LexerExceptionHandler exceptionHandler = new LexerExceptionHandler();
		Scanner scanner = new Scanner("55afwa\n\n\n\n\"unterminated string", exceptionHandler);
		try {
			scanner.scanTokens();
		}catch (RuntimeException e) {
			assertEquals("Error at 1 with token type: VALUE\nError at 5 with token type: STRING\n", exceptionHandler.printExceptions());
		}
	}

	@Test
	void testNoErrorHandler_ThrowsError(){

		assertThrows(RuntimeException.class, () -> {
			Scanner scanner = new Scanner("55afwa\n\n\n\n");
			scanner.scanTokens();
		});
		assertThrows(RuntimeException.class, () -> {
			Scanner scanner = new Scanner("\"a");
			scanner.scanTokens();
		});
	}


	@Test
	void testFalseKeyword() {

		Scanner scanner = new Scanner("false");
		scanner.scanTokens();
		assertEquals("FALSE(\"false\") EOF(\"\") ", scanner.toString());

		scanner = new Scanner("false;");
		scanner.scanTokens();
		assertEquals("FALSE(\"false\") SEMICOLON(\";\") EOF(\"\") ", scanner.toString());

		scanner = new Scanner("falsea;");
		scanner.scanTokens();
		assertEquals("IDENTIFIER(\"falsea\") SEMICOLON(\";\") EOF(\"\") ", scanner.toString());


	}

	@Test
	void testReturnKeyword() {

		Scanner scanner = new Scanner("return");
		scanner.scanTokens();
		assertEquals("RETURN(\"return\") EOF(\"\") ", scanner.toString());

		scanner = new Scanner("return;");
		scanner.scanTokens();
		assertEquals("RETURN(\"return\") SEMICOLON(\";\") EOF(\"\") ", scanner.toString());

		scanner = new Scanner("returna;");
		scanner.scanTokens();
		assertEquals("IDENTIFIER(\"returna\") SEMICOLON(\";\") EOF(\"\") ", scanner.toString());


	}


	@Test
	void testSEMICOLON() {

		Scanner scanner = new Scanner(";");
		scanner.scanTokens();
		assertEquals("SEMICOLON(\";\") EOF(\"\") ", scanner.toString());

		scanner = new Scanner("!;");
		scanner.scanTokens();
		assertEquals("NOT(\"!\") SEMICOLON(\";\") EOF(\"\") ", scanner.toString());

		scanner = new Scanner("elsea;");
		scanner.scanTokens();
		assertEquals("IDENTIFIER(\"elsea\") SEMICOLON(\";\") EOF(\"\") ", scanner.toString());


	}


	@Test
	void testElseKeyword() {

		Scanner scanner = new Scanner("else");
		scanner.scanTokens();
		assertEquals("ELSE(\"else\") EOF(\"\") ", scanner.toString());

		scanner = new Scanner("!else");
		scanner.scanTokens();
		assertEquals("NOT(\"!\") ELSE(\"else\") EOF(\"\") ", scanner.toString());

		scanner = new Scanner("elsea");
		scanner.scanTokens();
		assertEquals("IDENTIFIER(\"elsea\") EOF(\"\") ", scanner.toString());


	}


	@Test
	void testSmthKeyword() {

		Scanner scanner = new Scanner("smth");
		scanner.scanTokens();
		assertEquals("SMTH(\"smth\") EOF(\"\") ", scanner.toString());

		scanner = new Scanner("!smth");
		scanner.scanTokens();
		assertEquals("NOT(\"!\") SMTH(\"smth\") EOF(\"\") ", scanner.toString());

		scanner = new Scanner("smtha");
		scanner.scanTokens();
		assertEquals("IDENTIFIER(\"smtha\") EOF(\"\") ", scanner.toString());


	}

	@Test
	void testNot() {

		Scanner scanner = new Scanner("!");
		scanner.scanTokens();
		assertEquals("NOT(\"!\") EOF(\"\") ", scanner.toString());

		scanner = new Scanner("!!");
		scanner.scanTokens();
		assertEquals("NOT(\"!\") NOT(\"!\") EOF(\"\") ", scanner.toString());

		scanner = new Scanner("!!true");
		scanner.scanTokens();
		assertEquals("NOT(\"!\") NOT(\"!\") TRUE(\"true\") EOF(\"\") ", scanner.toString());


	}


	@Test
	void testComments() {
		Scanner scanner = new Scanner("//test");
		scanner.scanTokens();
		assertEquals("EOF(\"\") ", scanner.toString());

		scanner = new Scanner("//test\nId");
		scanner.scanTokens();
		assertEquals("IDENTIFIER(\"Id\") EOF(\"\") ", scanner.toString());

		scanner = new Scanner("//test\nId//test\nId");
		scanner.scanTokens();
		assertEquals("IDENTIFIER(\"Id\") IDENTIFIER(\"Id\") EOF(\"\") ", scanner.toString());

	}


	@Test
	void testAssignment() {
		Scanner scanner = new Scanner("=");
		scanner.scanTokens();

		assertEquals("ASSIGNMENT(\"=\") EOF(\"\") ", scanner.toString());

		scanner = new Scanner("\"test\" = test");
		scanner.scanTokens();

		assertEquals("STRING(\"\"test\"\") ASSIGNMENT(\"=\") IDENTIFIER(\"test\") EOF(\"\") ", scanner.toString());


	}


	@Test
	void testExpressions() {

		Scanner scanner = new Scanner("5+3+8*5=20");
		scanner.scanTokens();

		assertEquals("VALUE(\"5\") PLUS(\"+\") VALUE(\"3\") PLUS(\"+\") VALUE(\"8\") STAR(\"*\") VALUE(\"5\") ASSIGNMENT(\"=\") VALUE(\"20\") EOF(\"\") ", scanner.toString());

		scanner = new Scanner("5/8/5*=20");
		scanner.scanTokens();

		assertEquals("VALUE(\"5\") SLASH(\"/\") VALUE(\"8\") SLASH(\"/\") VALUE(\"5\") STAR(\"*\") ASSIGNMENT(\"=\") VALUE(\"20\") EOF(\"\") ", scanner.toString());

		scanner = new Scanner("5=8+5/=20");
		scanner.scanTokens();

		assertEquals("VALUE(\"5\") ASSIGNMENT(\"=\") VALUE(\"8\") PLUS(\"+\") VALUE(\"5\") SLASH(\"/\") ASSIGNMENT(\"=\") VALUE(\"20\") EOF(\"\") ", scanner.toString());

	}

	@Test
	void testEquals() {
		Scanner scanner = new Scanner("==");
		scanner.scanTokens();

		assertEquals("EQUALS(\"==\") EOF(\"\") ", scanner.toString());

		scanner = new Scanner("\"test\" == test");
		scanner.scanTokens();

		assertEquals("STRING(\"\"test\"\") EQUALS(\"==\") IDENTIFIER(\"test\") EOF(\"\") ", scanner.toString());

		scanner = new Scanner("===");
		scanner.scanTokens();

		assertEquals("EQUALS(\"==\") ASSIGNMENT(\"=\") EOF(\"\") ", scanner.toString());
	}


	@Test
	void testNotEquals() {

		Scanner scanner = new Scanner("!=");
		scanner.scanTokens();

		assertEquals("NOT_EQUALS(\"!=\") EOF(\"\") ", scanner.toString());

		scanner = new Scanner("\"test\" != test");
		scanner.scanTokens();

		assertEquals("STRING(\"\"test\"\") NOT_EQUALS(\"!=\") IDENTIFIER(\"test\") EOF(\"\") ", scanner.toString());

	}


	@Test
	void testValidTokenInput() {


		//Testing all tokens (continue here, unfinished)
		Scanner scanner = new Scanner("abc (4) ([])-+ \"test\" loop {} <=>=&|/ smth! if ifa >=>< true ===");
		scanner.scanTokens();

		assertEquals("IDENTIFIER(\"abc\") LEFT_PARENS(\"(\") VALUE(\"4\") RIGHT_PARENS(\")\") LEFT_PARENS(\"(\") LEFT_SQBRACKET(\"[\") RIGHT_SQBRACKET(\"]\") RIGHT_PARENS(\")\") MINUS(\"-\") PLUS(\"+\") STRING(\"\"test\"\") LOOP(\"loop\") LEFT_CURLYBRAC(\"{\") RIGHT_CURLYBRAC(\"}\") LESS_EQUALS(\"<=\") GREATER_EQUALS(\">=\") AND(\"&\") OR(\"|\") SLASH(\"/\") IDENTIFIER(\"smth!\") IF(\"if\") IDENTIFIER(\"ifa\") GREATER_EQUALS(\">=\") GREATER(\">\") LESSER(\"<\") TRUE(\"true\") EQUALS(\"==\") ASSIGNMENT(\"=\") EOF(\"\") ", scanner.toString());


		//Testing empty input
		scanner = new Scanner("");
		scanner.scanTokens();

		assertEquals("EOF(\"\") ", scanner.toString());


	}


	@Test
	void testTrueKeyword() {

		Scanner scanner = new Scanner("true");
		scanner.scanTokens();
		assertEquals("TRUE(\"true\") EOF(\"\") ", scanner.toString());

		//Ensuring that you can start identifiers with loop keyword
		scanner = new Scanner("truea");
		scanner.scanTokens();
		assertEquals("IDENTIFIER(\"truea\") EOF(\"\") ", scanner.toString());


		scanner = new Scanner("true hello (");
		scanner.scanTokens();
		assertEquals("TRUE(\"true\") IDENTIFIER(\"hello\") LEFT_PARENS(\"(\") EOF(\"\") ", scanner.toString());

		scanner = new Scanner("true if truea");
		scanner.scanTokens();
		assertEquals("TRUE(\"true\") IF(\"if\") IDENTIFIER(\"truea\") EOF(\"\") ", scanner.toString());


	}


	@Test
	void testLoopKeyword() {

		Scanner scanner = new Scanner("loop");
		scanner.scanTokens();
		assertEquals("LOOP(\"loop\") EOF(\"\") ", scanner.toString());

		//Ensuring that you can start identifiers with loop keyword
		scanner = new Scanner("loopa");
		scanner.scanTokens();
		assertEquals("IDENTIFIER(\"loopa\") EOF(\"\") ", scanner.toString());


		scanner = new Scanner("loop hello (");
		scanner.scanTokens();
		assertEquals("LOOP(\"loop\") IDENTIFIER(\"hello\") LEFT_PARENS(\"(\") EOF(\"\") ", scanner.toString());

		scanner = new Scanner("loop if loopa");
		scanner.scanTokens();
		assertEquals("LOOP(\"loop\") IF(\"if\") IDENTIFIER(\"loopa\") EOF(\"\") ", scanner.toString());

	}


	@Test
	void testIfToken() {

		//Test if with multiple tokens
		Scanner scanner = new Scanner("if hello )");
		scanner.scanTokens();
		assertEquals("IF(\"if\") IDENTIFIER(\"hello\") RIGHT_PARENS(\")\") EOF(\"\") ", scanner.toString());

		//Ensuring that you can start identifiers with if keywords

		scanner = new Scanner("ifhello");
		scanner.scanTokens();
		assertEquals("IDENTIFIER(\"ifhello\") EOF(\"\") ", scanner.toString());


		//test with single if
		scanner = new Scanner("if");
		scanner.scanTokens();
		assertEquals("IF(\"if\") EOF(\"\") ", scanner.toString());
	}


	@Test
	void testValidIdentifierInput() {
		//Testing most basic path
		Scanner scanner = new Scanner("test123");
		scanner.scanTokens();
		assertEquals("IDENTIFIER(\"test123\") EOF(\"\") ", scanner.toString());

		//Testing underscore inside identifier
		scanner = new Scanner("test_123");
		scanner.scanTokens();
		assertEquals("IDENTIFIER(\"test_123\") EOF(\"\") ", scanner.toString());


		//Testing underscore at start of Identifier

		scanner = new Scanner("_test123");
		scanner.scanTokens();
		assertEquals("IDENTIFIER(\"_test123\") EOF(\"\") ", scanner.toString());

	}

	@Test
	void testNlines() {
		Scanner scanner = new Scanner("\n\n\n");
		scanner.scanTokens();
		//since we start at nline = 1
		assertEquals(4, scanner.getNline());

		scanner = new Scanner("\n\ntest\n\n");
		scanner.scanTokens();
		assertEquals(5, scanner.getNline());

		scanner = new Scanner("\n\ntest\n\n\n\ntest\n\n\n\ntest\n\n");
		scanner.scanTokens();
		assertEquals(13, scanner.getNline());


	}


	@Test
	void testValidStringInputs() {

		//Testing EC plain input:

		Scanner scanner = new Scanner("\"test\"");
		scanner.scanTokens();

		assertEquals("STRING(\"\"test\"\") EOF(\"\") ", scanner.toString());

		//Testing empty string
		scanner = new Scanner("\"\"");
		scanner.scanTokens();

		assertEquals("STRING(\"\"\"\") EOF(\"\") ", scanner.toString());

		//Testing EC blank spaces in strings:


		scanner = new Scanner("\"te st\"");
		scanner.scanTokens();
		assertEquals("STRING(\"\"te st\"\") EOF(\"\") ", scanner.toString());


		//Testing EC strings with newline:
		scanner = new Scanner("\"te\nst\"");
		scanner.scanTokens();
		assertEquals("STRING(\"\"te\nst\"\") EOF(\"\") ", scanner.toString());

		//Testing EC strings with digits at the beginning

		scanner = new Scanner("\"123te\nst\"");
		scanner.scanTokens();
		assertEquals("STRING(\"\"123te\nst\"\") EOF(\"\") ", scanner.toString());

		//Testing EC strings with non alphanumeric characters

		scanner = new Scanner("\"@123te\nst\"");
		scanner.scanTokens();
		assertEquals("STRING(\"\"@123te\nst\"\") EOF(\"\") ", scanner.toString());


		//Testing EC input with multiple tokens and string at the end:

		scanner = new Scanner("id} 5 /id+ 10 id*\"@123te\nst\"");
		scanner.scanTokens();
		assertEquals("IDENTIFIER(\"id\") RIGHT_CURLYBRAC(\"}\") VALUE(\"5\") SLASH(\"/\") IDENTIFIER(\"id\") PLUS(\"+\") VALUE(\"10\") IDENTIFIER(\"id\") STAR(\"*\") STRING(\"\"@123te\nst\"\") EOF(\"\") ", scanner.toString());

	}


	@Test
	void testValueReturnsCorrectLiteralVal() {
		//Testing basic input
		Scanner scanner = new Scanner("4 ([])");
		scanner.scanTokens();
		Token tok = scanner.getTokens().get(0);
		//Third arg is delta to account for rounding precision
		assertEquals((double) 4, (double) tok.getLiteral(), 0.001);

		//Testing single character digit input
		scanner = new Scanner("3");
		scanner.scanTokens();
		tok = scanner.getTokens().get(0);
		assertEquals((double) 3, (double) tok.getLiteral(), 0.001);

		scanner = new Scanner("3.5*6");
		scanner.scanTokens();
		tok = scanner.getTokens().get(0);
		assertEquals((double) 3.5, (double) tok.getLiteral(), 0.001);
	}


	@Test
	void testOperators() {
		Scanner scanner = new Scanner("++6");
		scanner.scanTokens();
		assertEquals("PLUS(\"+\") PLUS(\"+\") VALUE(\"6\") EOF(\"\") ", scanner.toString());

		scanner = new Scanner("--6++**");
		scanner.scanTokens();
		assertEquals("MINUS(\"-\") MINUS(\"-\") VALUE(\"6\") PLUS(\"+\") PLUS(\"+\") STAR(\"*\") STAR(\"*\") EOF(\"\") ", scanner.toString());


	}
}
