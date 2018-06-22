/**
 * miniJava Abstract Syntax Tree classes
 * @author prins
 * @version COMP 520 (v2.2)
 */
package miniJava.AbstractSyntaxTrees;

import miniJava.CodeGenerator.RuntimeEntityDescriptor;
import miniJava.SyntacticAnalyzer.SourcePosition;

public class VarDecl extends LocalDecl {
	
	public boolean initialized;

	public VarDecl(TypeDenoter t, Identifier id, SourcePosition posn) {
		super(id, t, posn);
		this.RED = new RuntimeEntityDescriptor(1);
	}
	
	public <A,R> R visit(Visitor<A,R> v, A o) {
		return v.visitVarDecl(this, o);
	}
	
}
