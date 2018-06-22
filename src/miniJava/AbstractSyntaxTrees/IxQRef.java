package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.SourcePosition;

public class IxQRef extends QualifiedRef {
	
	public IxQRef(QualifiedRef ref, Expression exp, SourcePosition posn){
		super(posn);
		this.ref = ref;
		this.id  = ref.id;
		this.ixExpr = exp;
	}

	@Override
	public <A, R> R visit(Visitor<A, R> v, A o) {
		return v.visitIxQRef(this, o);
	}

	public Reference ref;
	//public Identifier id;
	public Expression ixExpr;

}
