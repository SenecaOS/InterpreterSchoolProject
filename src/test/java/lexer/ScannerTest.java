package lexer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class ScannerTest {

	@Test
	void testReadTokens() {
				
		Scanner scanner = new Scanner("abc 4 ([])");
		scanner.scanTokens();
		
		assertEquals("IDENTIFIER(\"abc\") VALUE(\"4\") LEFT_PARENS(\"(\") LEFT_SQBRACKET(\"[\") RIGHT_SQBRACKET(\"]\") RIGHT_PARENS(\")\") EOF(\"\") ", scanner.toString());
		
		
	}
	@Test
	void testTokensInvalidInput_throwsException() {
		
		Scanner scanner = new Scanner("abc 4a ([])");
		
		assertThrows(IllegalArgumentException.class, () -> {
			scanner.scanTokens();
			
		});
		
	}
	
	
}
