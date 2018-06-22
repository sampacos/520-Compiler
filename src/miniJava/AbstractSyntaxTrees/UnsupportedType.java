package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.*;
import miniJava.ContextualAnalyzer.IDTable;

public class UnsupportedType extends ClassType{
	public static final UnsupportedType STRING_TYPE = new UnsupportedType(IDTable.STRING_DECL, null);
	
	
	public UnsupportedType(ClassDecl decl, SourcePosition posn){
		super(new Identifier(new Token(decl.name)), posn);
		super.declaration = decl;
	}
	
	@Override
	public <A, R> R visit(Visitor<A, R> v, A o){
		return v.visitUnsupportedType(this, o);
	}
	
	@Override
	public String toString(){
		return "(Unsupported) type at " + this.posn.toString();
	}
	
}
