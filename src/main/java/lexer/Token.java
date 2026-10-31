package lexer;

public class Token {
	
	final private TokenType tokenType;
	final private String val;
	final private int nline;
	final private Object literal;
	
	public Token(TokenType type, String val, Object literal, int nline) {
		this.val = val;
		this.tokenType = type;
		this.nline = nline;
		this.literal = literal;
	}

	public TokenType getTokenType() {
		return tokenType;
	}
	public String getVal() {
		return val;
	}

	public int getLine() {return nline;}

	public Object getLiteral() {
		return literal;
	}

	public String toString(){
		return tokenType.toString() + "(\"" + val + "\")";
	}
	
}
