package lexer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;


//TODO add more test cases for each method

public class ScannerTest {

	@Test
	void testValidTokenInput() {
				
		Scanner scanner = new Scanner("abc 4 ([])");
		scanner.scanTokens();
		
		assertEquals("IDENTIFIER(\"abc\") VALUE(\"4\") LEFT_PARENS(\"(\") LEFT_SQBRACKET(\"[\") RIGHT_SQBRACKET(\"]\") RIGHT_PARENS(\")\") EOF(\"\") ", scanner.toString());
		
		
	}
	
	@Test
	void testValidIdentifierInput() {
		
		Scanner scanner = new Scanner("abc 4a ([])");
		//Testing EC identifier with number at the end
		
		scanner = new Scanner("test123");
		scanner.scanTokens();	
		assertEquals("IDENTIFIER(\"test123\") EOF(\"\") ", scanner.toString());
		
	
	}
	
	
	
	
	@Test
	void testInvalidInput_throwsException() {
		
		Scanner scanner = new Scanner("abc 4a ([])");
		
		assertThrows(IllegalArgumentException.class, () -> {
			scanner.scanTokens();
			
		});
	}
	@Test
	void testValidStringInputs() {
		
		//Testing EC plain input:
		
		Scanner scanner = new Scanner("\"test\"");
		scanner.scanTokens();
		
		assertEquals("STRING(\"\"test\"\") EOF(\"\") ", scanner.toString());
		
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
		
		Scanner scanner = new Scanner("4 ([])");
		scanner.scanTokens();
		Token tok = scanner.getTokens().get(0);
		//Third arg is delta to account for rounding precision
		assertEquals((double)tok.getLiteral(), (double) 4, 0.001);
		
		
		scanner = new Scanner("3");
		scanner.scanTokens();
		tok = scanner.getTokens().get(0);
		//Third arg is delta to account for rounding precision
		assertEquals((double)tok.getLiteral(), (double) 3, 0.001);
		

		
	}
	
	
	@Test
	void testUnterminatedStringInput_throwsException() {
		
		
	
		assertThrows(IllegalArgumentException.class, () -> {
			new Scanner("abc ([]) \"test").scanTokens();
		});
		
		
		assertThrows(IllegalArgumentException.class, () -> {
			new Scanner("abc ([]) \"test").scanTokens();
		});
	}
	
}
