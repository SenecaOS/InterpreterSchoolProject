package lexer;


import exceptions.lexer.LexerException;
import exceptions.lexer.LexerExceptionHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Scanner {
	private final String source;
	private final List<Token> tokens = new ArrayList<>();
	private int start = 0;
	private int current = 0;
	private int nline = 1;
	private static final HashMap<String, TokenType> reservedWords;
	private LexerExceptionHandler exceptionHandler;
	
	
	static {
        reservedWords = new HashMap<>();
        reservedWords.put("return", TokenType.RETURN);
        reservedWords.put("loop", TokenType.LOOP);
        reservedWords.put("print", TokenType.PRINT);
        reservedWords.put("smth", TokenType.SMTH);
        reservedWords.put("interrupt", TokenType.INTERRUPT);
        reservedWords.put("true", TokenType.TRUE);
        reservedWords.put("false", TokenType.FALSE);
        reservedWords.put("noth", TokenType.NOTH);
        reservedWords.put("else", TokenType.ELSE);
        reservedWords.put("if", TokenType.IF);
		reservedWords.put("function", TokenType.FUNCTION);
    }
	

	public Scanner(String source) {
		this.source = source;
	}

	public Scanner(String source, LexerExceptionHandler exceptionHandler){
		this.source = source;
		this.exceptionHandler = exceptionHandler;

	}


	public List<Token> scanTokens() {
		while (!isAtEnd()) {
			start = current;
			scanToken();
		}
		tokens.add(new Token(TokenType.EOF, "", null, nline));
		if(exceptionHandler != null && exceptionHandler.hasExceptions()){
			System.err.println(exceptionHandler.printExceptions());
			throw new RuntimeException();
		}
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
		boolean notKnown = false;
		char c = advance();
		switch (c) {
		case ' ':
		case '\r':
		case '\t':
		case '\\':
			// we ignore white space
			break;
			case ',':
				addToken(TokenType.COMMA);
				break;
		case ';':
			addToken(TokenType.SEMICOLON);
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
			default:
				notKnown = true;
		}

		if (isAlpha(c)) {
			lexIdentifier();
			notKnown = false;
		}

		else if (isNum(c)) {
			lexNum();
			notKnown = false;
		}
		if(notKnown){
				handleError(new LexerException(new Token(TokenType.UNKNOWN, null, null, nline)));
		}

	}

	private boolean isNext(char c) {

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
		while (!isAtEnd() && isNum(peek())) {
			advance();
		}
		if(peek() == '.'){
			do{
				advance();
			}while (!isAtEnd() && isNum(peek()));
		}
		if(!isAcceptableNumEnding(peek()) || source.charAt(current - 1) == '.'){
			++current;
			handleError(new LexerException(new Token(TokenType.VALUE, null, null, nline)));
		}
		else addToken(TokenType.VALUE, Double.parseDouble(source.substring(start, current)));
	}

	private boolean isAcceptableNumEnding(int c) {
		return isOperator(c) || isBlank(c) || c == '\0' || c == ';' || c == ')';
	}

	private boolean isOperator(int c) {
		return c == '*' || c == '+' || c == '-' || c == '/' || c == '=';
	}

	private void lexString() {

		while (!isAtEnd() && peek() != '"' ) {
			if (peek() == '\n')
				++nline;
			advance();
		}

		// If we reach EOF without reading the terminating '"'
		if (peek() != '"') {
			handleError(new LexerException(new Token(TokenType.STRING, null, null, nline)));
			return;
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
		char c;
		if(!isAcceptableIdEnding()){
			while (!isAtEnd() && !isBlank((c = peek())) && !isValidDelimiter(c)) {
				advance();
			}
			handleError(new LexerException(new Token(TokenType.IDENTIFIER, null, null, nline)));
			return;
		}

        TokenType type;
        if((type = reservedWords.get(source.substring(start, current))) != null) {
        	addToken(type, source.substring(start, current));
        	return;
        }
        addToken(TokenType.IDENTIFIER, source.substring(start, current));
    }

	private boolean isValidDelimiter(char c) {
		return c == ';' || c == ')' || c == ',' || c == '+' || c == '=' || c == '\n';
	}

	private boolean isAcceptableIdEnding() {
		if(isAtEnd()) return true;
		char c = peek();
		ArrayList<Character> invalidAfterIdentifier = new ArrayList<>(Arrays.asList(
				'@', '#', '$', '^', '!',
				'~', '`', '{', ':', '\'', '"', '.',
				'\\'
		));
		return Character.isWhitespace(c) || (!invalidAfterIdentifier.contains(c) && c <= 127);
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
		return c == ' ' || c == '\t';
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
			sb.append(tok).append(" ");
		}
		return sb.toString();
	}

	private void handleError(LexerException error){
		if(exceptionHandler != null){
			exceptionHandler.addException(error);
		}
		else throw error;
	}

}
