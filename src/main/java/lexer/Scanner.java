package lexer;

import java.util.ArrayList;
import java.util.Arrays;
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
		case ' ':
		case '\r':
		case '\t':
			// we ignore white space
			break;
		case ';':
			addToken(TokenType.TERMINATOR);
			break;
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
			// We read comment until either end of line or end of file
			if (isNext('/')) {
				while (peek() != '\n' && !isAtEnd())
					advance();
			} else {
				addToken(TokenType.SLASH);
			}
			break;
		case '"':
			lexString();
			break;
		case '|':
			addToken(TokenType.OR);
			break;
		case '&':
			addToken(TokenType.AND);
			break;
		case 'i':
			if (!isAnumAfterWord("f")  && isNext('f')) {
				addToken(TokenType.IF);
				//Reset c to prevent fall through behavior
				c = '\0';
			}
			break;
		case 'r':
			if (inputMatchesWord("eturn")) {
				addToken(TokenType.RETURN);
				c = '\0';
			}
			break;
		case 't':
		case 'e':
			String word = c == 't' ? "rue" : "lse";
			if (inputMatchesWord(word)) {
				addToken(c == 't' ? TokenType.TRUE : TokenType.ELSE);
				c = '\0';
			}
			break;
		case 'l':
		case 's':
			word = c == 's' ? "mth" : "oop";
			if (inputMatchesWord(word)) {
				addToken(c == 's' ? TokenType.SMTH : TokenType.LOOP);
				//Reset c to prevent fall through behavior
				c = '\0';
			}
			break;
		case '=':
			addToken(isNext('=') ? TokenType.EQUALS : TokenType.ASSIGNMENT);
			break;
		case '!':
			addToken(isNext('=') ? TokenType.NOT_EQUALS : TokenType.NOT);
			break;
		case '<':
			addToken(isNext('=') ? TokenType.LESS_EQUALS : TokenType.LESSER);
			break;
		case '>':
			addToken(isNext('=') ? TokenType.GREATER_EQUALS : TokenType.GREATER);
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

	boolean isNext(char c) {

		if (isAtEnd())
			return false;
		if (c == peek()) {
			++current;
			return true;
		}
		return false;
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
			if (!isNum(currChar))
				break;
		}
		// e.g. if 24A is read
		if (!isNum(currChar) && !isAtEnd()) {
			throw new IllegalArgumentException("Incorrect value format on line " + nline);
		}
		addToken(TokenType.VALUE, Double.parseDouble(source.substring(start, current)));
	}

	private boolean inputMatchesWord(String word) {
	
		if(isAnumAfterWord(word) || current + word.length() > source.length()) return false;
		int isMatching = word.compareTo(source.substring(current, current + word.length()));
	
		if(isMatching == 0){
			consumeRemainingLetters(word);
			return true;
		}	
		return false;
	}
	
	private void consumeRemainingLetters(String word) {
		
		for (int i = 0; i < word.length(); i++) {
			advance();
		}
		
	}
	
	private boolean isAnumAfterWord(String word) {
		
		//e.g. we want to inspect 'a' in loopa. We are already 1 index deep into the word from our switch statement 
		int inspectedIndex = current + word.length(); //4
		return inspectedIndex <= source.length() - 1 && isAnum(source.charAt(inspectedIndex));
		
	}
	

	private void lexString() {
		while (!isAtEnd() && peek() != '"') {
			if (peek() == '\n')
				++nline;
			advance();
		}

		// If we reach EOF without reading the terminating '"'
		if (peek() != '"') {
			throw new IllegalArgumentException("Unterminated string on line " + nline);
		}

		// consume '"'
		advance();

		// Strip '"' at start and end of substring
		addToken(TokenType.STRING, source.substring(start + 1, current - 1));
	}

	private void lexIdentifier() {

		while (!isAtEnd() && isAnum(peek())) {
			advance();
		}

		if (!isAtEnd() && !isBlank(source.charAt(current - 1)) && !isAnum(source.charAt(current - 1))) {
			throw new IllegalArgumentException("Identifier assumes incorrect format on line " + nline);

		}
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

	public List<Token> getTokens() {

		return List.copyOf(tokens);

	}



	public int getNline() {
		return nline;

	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Token tok : tokens) {
			sb.append(tok + " ");
		}
		return sb.toString();
	}

}
