package miniJava.SyntacticAnalyzer;

public enum TokenKind {
	//keywords
	CLASS("class"),
	PUBLIC("public"),
	PRIVATE("private"),
	RETURN("return"),
	STATIC("static"),
	INT("int"),
	BOOLEAN("boolean"),
	TRUE("true"),
	FALSE("false"),
	VOID("void"),
	THIS("this"),
	IF("if"),
	ELSE("else"),
	WHILE("while"),
	NEW("new"),
	NULL("null"),
	
	//operators and groupers
	PLUS("+"),
	MINUS("-"),
	MINUSMINUS("--"),
	ASTERISK("*"),
	SLASH("/"),
	EQUAL("="),
	BOOLEQUAL("=="),
	LESSEQUAL("<="),
	GREATEREQUAL(">="),
	NOT_EQUAL("!="),
	SEMICOLON(";"),
	COMMA(","),
	LPAREN("("),
	RPAREN(")"),
	LBRACKET("["),
	RBRACKET("]"),
	LBRACE("{"),
	RBRACE("}"),
	LESSTHAN("<"),
	GREATERTHAN(">"),
	BARX2("||"),
	AMPERSANDX2("&&"),
	EXCLAM("!"),
	DOT("."),
	//others
	NUMBER,
	ID,
	EOT;
	
	
	public String tokenstring;
	
	TokenKind(){
		this.tokenstring = null;
	}
	
	TokenKind(String tokenstring){
		this.tokenstring = tokenstring;
	}
	
}
