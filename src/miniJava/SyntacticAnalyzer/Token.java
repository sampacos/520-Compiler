package miniJava.SyntacticAnalyzer;

import miniJava.SyntacticAnalyzer.SourcePosition;

public class Token {
	public TokenKind kind;
	public String tokenstring;
	public SourcePosition position;
	
	public Token(TokenKind kind, String tokenstring, SourcePosition poition){
		this.kind = kind;
		this.tokenstring = tokenstring;
		if(position == null){
			this.position = new SourcePosition(1, 0);
		}else{
		this.position = new SourcePosition(position.line, position.character);
		}
	}
	
	
	/*
	 * token constructor that takes a string and sees if it matches a kind
	 * if not, this method makes an identifier
	 */
	
	
	public Token(String s, SourcePosition p){
		boolean found = false;
		
		for (TokenKind a : TokenKind.values()){
			if(a.tokenstring != null && a.tokenstring.compareTo(s) == 0){
				this.kind = a;
				found = true;
				break;
			}
		}
		
		if(!found){
			kind = TokenKind.ID;
		}
		this.tokenstring = s;
		this.position = new SourcePosition(p.line, p.character);
	}
	public Token(String s){
		this.tokenstring = s;
	}
	
	
	
	public String toString(){
		return kind + ": " + tokenstring + "position: " + position;
	}
	
}
