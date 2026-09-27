package lexer;

public class Token {
	
	private TokenType tokenType;
	private String val;
	private int nline;
	private Object literal;
	
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



	
	public String toString(){
		
		return tokenType.toString() + "(\"" + val + "\")";
	}
	
}
