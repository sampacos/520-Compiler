package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.SourcePosition;

public class NullType extends ClassType{

	public NullType(Identifier cn, SourcePosition posn) {
		super(cn, posn);
		this.typeKind = TypeKind.NULL;
		// TODO Auto-generated constructor stub
	}

	public <A,R> R visit(Visitor<A,R> v, A o) {
        return v.visitNullType(this, o);
    }
}
