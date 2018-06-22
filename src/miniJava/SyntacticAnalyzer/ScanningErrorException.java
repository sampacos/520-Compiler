package miniJava.SyntacticAnalyzer;

public class ScanningErrorException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	
	public SourcePosition position = null;
	public String error = null;
	
	
	public ScanningErrorException(){
		super("Scanning Error");
	}
	
	public ScanningErrorException(SourcePosition p){
		super("Scanning Error at " + p);
		this.position = p;
	}
	
	
	public ScanningErrorException(SourcePosition p, String error){
		super("Scanning Error: " + p + ", " + error + " is not a recognized token");
		this.position = p;
		this.error = error;
	}
	
	public ScanningErrorException(SourcePosition p, String error, String expected){
		super("Scanning Error at " + p + " on " + error + ", Expected: " + expected);
		this.position = p;
		this.error = error;
	}
	
	
}
