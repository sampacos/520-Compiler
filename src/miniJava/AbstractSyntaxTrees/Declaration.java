/**
 * miniJava Abstract Syntax Tree classes
 * @author prins
 * @version COMP 520 (v2.2)
 */
package miniJava.AbstractSyntaxTrees;

import miniJava.CodeGenerator.RuntimeEntityDescriptor;
import miniJava.SyntacticAnalyzer.SourcePosition;

public abstract class Declaration extends AST {
	
	public Declaration(Identifier id, TypeDenoter type, SourcePosition posn) {
		super(posn);
		this.id = id;
		this.name = id.spelling;
		this.type = type;
	}
	
	public String name;
	public TypeDenoter type;
	public Identifier id;
	public RuntimeEntityDescriptor RED;
}
