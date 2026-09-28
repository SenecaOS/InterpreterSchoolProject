package lexer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Scanner {
	private String source;
	private final List<Token> tokens = new ArrayList<>();
	private int start = 0;
	private int current = 0;
	private int nline = 1;

	public Scanner(String source) {
		this.source = source;

	}

	public List<Token> scanTokens() {
		while (!isAtEnd()) {
			start = current;
			scanToken();
		}

		tokens.add(new Token(TokenType.EOF, "", null, nline));
		return tokens;

	}
	
	private char advance() {
		current++;
		return source.charAt(current - 1);
	}

	private char peek() {
		if (isAtEnd())
			return '\0';
		return source.charAt(current);

	}

	private boolean isAtEnd() {
		// TODO Auto-generated method stub
		return current >= source.length();
	}

	private void scanToken() {
		char c = advance();
		switch (c) {

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
			addToken(TokenType.LEFT_CURLYBRAC);
			break;
		case '}':
			addToken(TokenType.RIGHT_CURLYBRAC);
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
		case '"':
			lexString();
			break;
		case '\n':
			++nline;
			break;
		}

		if (isAlpha(c)) {
			lexIdentifier();
		}

		else if (isNum(c)) {
			lexNum();
		}
	}

	private void addToken(TokenType type) {
		addToken(type, null);
	}

	private void addToken(TokenType type, Object literal) {
		// TODO Auto-generated method stub
		String text = source.substring(start, current);
		tokens.add(new Token(type, text, literal, nline));
	}

	private void lexNum() {
		int currChar = source.charAt(current - 1);
		while (peek() != '\0' && !isBlank(peek())) {
			currChar = advance();
			if(!isNum(currChar)) break;
		}
		//e.g. if 24A is read
		if (!isNum(currChar) && !isAtEnd()) {
			throw new IllegalArgumentException("Incorrect value format on line " + nline);
		}
		addToken(TokenType.VALUE, Double.parseDouble(source.substring(start, current)));
	}

	private void lexString() {
		while (!isAtEnd() && peek() != '"') {
			if (peek() == '\n') ++nline;
			advance();
		}

		// If we reach EOF without reading the terminating '"'
		if (peek() != '"') {
			throw new IllegalArgumentException("Unterminated string on line " + nline);
		}
		
		//consume '"'
		advance();

		// Strip '"' at start and end of substring
		addToken(TokenType.STRING, source.substring(start + 1, current - 1));
	}

	private void lexIdentifier() {
		
		while (!isAtEnd() && isAnum(peek())) {
			advance();		
		}
/*
		if(!isAtEnd() && !isBlank(source.charAt(current)) && !isAnum(source.charAt(current))) {
			throw new IllegalArgumentException("Identifier assumes incorrect format on " + nline);
			
		}*/
		addToken(TokenType.IDENTIFIER);
	}

	private boolean isAnum(int c) {
		return isNum(c) || isAlpha(c);

	}

	private boolean isNum(int c) {
		return c >= '0' && c <= '9';

	}

	private boolean isAlpha(int c) {
		return c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c == '_';

	}

	private boolean isBlank(int c) {
		return c == ' ' || c == '\t' || c == '\n';
	}

	public List<Token> getTokens(){
		
		return List.copyOf(tokens);
		
	}
	public void resetScanner(String source) {
		start = 0;
		current = 0;
		nline = 1;
		this.source = source;
		tokens.clear();		
	}
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Token tok : tokens) {
			sb.append(tok + " ");
		}
		return sb.toString();
	}

}
