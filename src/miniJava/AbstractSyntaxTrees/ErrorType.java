package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.SourcePosition;

public class ErrorType extends TypeDenoter{
	public ErrorType(SourcePosition posn){
		super(TypeKind.ERROR, "Error", posn);
	}
	
	
	@Override
	public <A, R> R visit(Visitor<A, R> v, A o){
		return v.visitErrorType(this, o);
	}
}
