package miniJava.SyntacticAnalyzer;

public class ParsingErrorException extends Exception{
	private static final long serialVersionUID = 1L;
	
	public Token token = null;
	
	public ParsingErrorException(String errormessage, Token t){
		super("Parsing Error: " + t.position + " " + errormessage + " " + t.tokenstring);
		this.token = t;
	}
	
	public ParsingErrorException(Token t){
		super("Parsing Error: " + t.position + " " + "unexpected token " + t.tokenstring);
		this.token = t;
	}
	
	public ParsingErrorException(String errormessage){
		super("Parsing Error: " + errormessage);
	}
}