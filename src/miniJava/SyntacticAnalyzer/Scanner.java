package miniJava.SyntacticAnalyzer;

import java.io.IOException;
import java.io.InputStream;

public class Scanner {
	private InputStream input;
	private char currchar;
	private SourcePosition position;
	private StringBuilder currString;
	
	
	//this is the unicode character for "null", which signifies the end of file here
	final static char EOT = '\u0000';
	
	public Scanner(InputStream input){
		this.input = input;
		this.currchar = ' ';
		this.position = new SourcePosition(1, 0);
		this.currString = new StringBuilder(); //used to build ids and numbers
	}
	
	
	/**
	 * reads next char, if null, it makes the next char the EOF.
	 * otherwise, checks for newline or carriage returns and acts accordingly
	 */
	private char next() throws IOException{
		int a = input.read();
		
		if(a == -1){
			currchar = EOT;
		}else{
			currchar = (char) a;
			if(currchar == '\n'){
				position.line++;
				position.character = 0;
			}else if(currchar == '\r'){
				return next();
			}else{
				position.character++;
			}
		}
		return currchar;
	}
	
	/**
	 * Compares the expected char to the current one
	 * if not the same, throws an error.
	 * This is a scanning error, rather than parsing error, because it is not
	 * an incorrect token, but an incorrect char being read in.
	 * however the exception will most likely be passed to a ParsingErrorException
	 */
	private void expect(char expchar) throws IOException, ScanningErrorException{
		if(currchar != expchar){
			throw new ScanningErrorException(position, Character.toString(currchar), Character.toString(expchar));
		}
		next();
	}
	
	private void nextLine() throws IOException{
		while(!charchecks.isNewline(currchar) && currchar != EOT){
			next();
		}
	}
	
	private void skipWhitespace() throws IOException{
		while(charchecks.isWhitespace(currchar)){
			next();
		}
	}
	
	/**
	 * ONLY CALL AFTER THE START OF LONG COMMENT
	 * Skips multiline comments by making sure currchar is not EOF or *.
	 * if EOF, then exception, because the comment needs to be closed.
	 * if *, then it checks for /, signifying the end of comment.
	 * skips until these cases
	 */
	private void skipLongComment() throws IOException, ScanningErrorException{
		while(true){
			while(currchar != EOT && currchar != '*'){
				next();
			}
			if(currchar == '*' && next() == '/'){
				next();
				break;
			}else if(currchar == EOT){
				throw new ScanningErrorException(position, Character.toString(currchar), "*/ to close multiline comment");
			}
		}
	}
	
	
	public Token scanNextToken() throws ScanningErrorException{
		try{
			skipWhitespace();
			
			switch(currchar){
			
			case '+':
			case '*':
			case '(':
			case ')':
			case '[':
			case ']':
			case '{':
			case '}':
			case '.':
			case ';':
			case ',':
				Token t = new Token(Character.toString(currchar), position);
				next();
				return t;
			
			case '|':
				next();
				expect('|');
				return new Token("||", position);
				
			case '&':
				next();
				expect('&');
				return new Token("&&", position);
				
			case '=':
			case '!':
			case '<':
			case '>':
				String operator = Character.toString(currchar);
				next();
				if(currchar == '='){
					operator = operator + currchar;
					next();
				}
				return new Token(operator, position);
				
			case '/':
				next();
				if(currchar == '/'){
					nextLine();
					return scanNextToken();
				}else if(currchar == '*'){
					next();
					skipLongComment();
					return scanNextToken();
				}
				return new Token(TokenKind.SLASH, "/", position);
			
			case '-':
				next();
				if (currchar == '-'){
					return new Token(TokenKind.MINUSMINUS, "--", position);
				}
				else{
					return new Token(TokenKind.MINUS, "-", position);
				}
				
			case EOT:
				return new Token(TokenKind.EOT, "", position);
			
			default:
				currString.setLength(0);
				currString.append(currchar);
				
				if(charchecks.isAlpha(currchar)){
					//thus is an id
					next();
					while(charchecks.isAlnum(currchar) || currchar == '_'){
						currString.append(currchar);
						next();
					}
					return new Token(currString.toString(), position);
				}else if(charchecks.isDigit(currchar)){
					//thus is a number, since ids must start with a letter
					next();
					while (charchecks.isDigit(currchar)){
						currString.append(currchar);
						next();
					}
					return new Token(TokenKind.NUMBER, currString.toString(), position);
				}
				
				throw new ScanningErrorException(position, Character.toString(currchar));
				//character is not recognized
			}
			
		}catch(IOException a){
			//IOException happens in the case that there is nothing to read, so EOF
			return new Token(TokenKind.EOT, "", position);
		}
	}
	
	
	
	
	
	
	
}
/**
 * this little class is here to check if chars are special chars,
 * such as whitespace or digits and such
 */

class charchecks{
	public static boolean isNewline(char c) {
		return (c == '\r' || c == '\n');
	}

	public static boolean isWhitespace(char c) {
		return (c == ' ' || c == '\t' || isNewline(c));
	}
	
	public static boolean isAlpha(char c) {
		return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
	}

	public static boolean isDigit(char c) {
		return (c >= '0' && c <= '9');
	}

	public static boolean isAlnum(char c) {
		return isAlpha(c) || isDigit(c);
	}

}