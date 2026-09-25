package lexer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lexer.Lexer;

import static org.junit.jupiter.api.Assertions.*;

public class LexerTest {
	
	private Lexer lexer;
	
	@BeforeEach
	void setUp() {
		lexer = new Lexer("let x = 10");
	}
	
	@Test
	void testTokenisation() {
		String result = String.join(" ", lexer.getTokensList());
		
		assertEquals("x = 10", result);
		
	}
	
	
	
}
