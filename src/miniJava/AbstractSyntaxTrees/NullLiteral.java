package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.Token;

public class NullLiteral extends Terminal{

	public NullLiteral(Token t) {
		super(t);
		spelling = "Null";
		// TODO Auto-generated constructor stub
	}

	@Override
	public <A, R> R visit(Visitor<A, R> v, A o) {
		// TODO Auto-generated method stub
		return v.visitNullLiteral(this, o);
	}

}
