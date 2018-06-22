package miniJava.SyntacticAnalyzer;

public class SourcePosition {
	public int line, character;
	
	public SourcePosition(){
		line = 1;
		character = 1;
	}
	
	public SourcePosition(int l, int c){
		this.line = l;
		this.character = c;
	}
	
	public SourcePosition(SourcePosition p){
		this.line = p.line;
		this.character = p.character;
	}
	
	public String toString(){
		return "line " + line + " character " + character;
	}
	
}
