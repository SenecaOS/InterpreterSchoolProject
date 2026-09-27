package lexer;

import java.util.ArrayList;
import java.util.List;

public class Scanner {
	private String source;
	private final List<Token> tokens = new ArrayList<>();
	private int start = 0;
	private int current = 0;
	private int nline = 1;
	
	public Scanner(String source){
		this.source = source;

	}

	public List<Token> scanTokens(){
		while(!isAtEnd()) {
			start = current;
			scanToken();
		}
		
		tokens.add(new Token(TokenType.EOF, "", null, nline));
		return tokens;
		
	}

	private boolean isAtEnd() {
		// TODO Auto-generated method stub
		return current >= source.length();
	}

	private void scanToken() {
		char c = advance();
		switch(c) {
	
		case '(':
			addToken(TokenType.LEFT_PARENS);
			break;
		case ')':
			addToken(TokenType.RIGHT_PARENS);
			break;
		case '[':
			addToken(TokenType.LEFT_SQBRACKET);
			break;
		case ']':
			addToken(TokenType.RIGHT_SQBRACKET);
			break;
		case '{':
			addToken(TokenType.RIGHT_CURLYBRAC);
			break;
		case '}':	
			addToken(TokenType.LEFT_CURLYBRAC);
			break;
		case '+':
			addToken(TokenType.PLUS);
			break;
		case '-':
			addToken(TokenType.MINUS);
			break;
		case '*':
			addToken(TokenType.STAR);
			break;
		case '/':
			addToken(TokenType.SLASH);
			break;
		case '\n':
			nline++;
			break;
		}
		
		//we want to keep reading until abc is parsed
		if(c >= 'a' && c <= 'z') {
			lexIdentifier();
		
		}
		
		//needs error handling, only happy path examined thusfar
		else if(c >= '0' && c <= '9') {
			lexNum();
			
		}
	}
	
	private void lexNum() {
		
		
		int nextChar = source.charAt(current);
		while(!isAtEnd() && !isBlank(nextChar) && nextChar >= '0' && nextChar <= '9') {
			advance();
			nextChar = source.charAt(current);
		}
		addToken(TokenType.VALUE);
	}

	private void lexIdentifier() {
		int nextChar = source.charAt(current);
		while(!isAtEnd() && !isBlank(nextChar)) {
			advance();
			nextChar = source.charAt(current);
		}
		addToken(TokenType.IDENTIFIER);
	}
	
	
	private boolean isBlank(int c) {
		return c == ' ' || c == '\n' || c == '\t';
	}
	
	
	private char advance() {
		current++;
		return source.charAt(current - 1);
	}
	
	
	private void addToken(TokenType type) {
		addToken(type, null);
	}

	private void addToken(TokenType type, Object literal) {
		// TODO Auto-generated method stub
		String text = source.substring(start, current);
		tokens.add(new Token(type, text, literal, nline));
		
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(Token tok : tokens) {
			sb.append(tok + " ");
		}
		return sb.toString();
	}
	
	
}
