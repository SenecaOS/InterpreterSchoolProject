package lexer;

public class Token {
	public enum TokenType{
		LET,
		FOR,
		WHILE,
		DO,
		IF,
		IDENTIFIER,
		VALUE,
		PLUS,
		MINUS,
		LEFT_PARENS,
		RIGHT_PARENS,
		LEFT_SQBRACKET,
		RIGHT_SQBRACKET,
		LEFT_CURLYBRAC,
		RIGHT_CURLYBRAC,
		OPERATOR,
	}
	
	private TokenType tokenType;
	private String val;
	
	public Token(String val) {
		this.val = val;
		this.tokenType = parseTokenType();
		
	}
	public TokenType getTokenType() {
		return tokenType;
	}
	public String getVal() {
		
		return val;
	}
	
	private TokenType parseTokenType() {
		switch(this.val) {
		case "let":
			return TokenType.LET;
		case "for":
			return TokenType.FOR;
		case "while":
			return TokenType.WHILE;
		case "if":
			return TokenType.IF;
		case "do":
			return TokenType.DO;
		case "(":
		case ")":
			return val.equals("(") ? TokenType.LEFT_PARENS : TokenType.RIGHT_PARENS;
		case "[":
		case "]":
			return val.equals("[") ? TokenType.LEFT_SQBRACKET : TokenType.RIGHT_SQBRACKET;
		case "{":
		case "}":	
			return val.equals("{") ? TokenType.LEFT_CURLYBRAC : TokenType.RIGHT_CURLYBRAC;
		case "+":
		case "-":
		case "*":
		case "/":
		case "=":
		case "==":
			return TokenType.OPERATOR;
		default:
			return parseAnum();
		}
	}


	private TokenType parseAnum() {
		
	
			if(!Character.isDigit(val.charAt(0))){
				return TokenType.IDENTIFIER;
				
			}
			if (val.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
		        return TokenType.IDENTIFIER;
		    }
		
		    throw new IllegalArgumentException("Invalid token: " + val);
	}
	
}
