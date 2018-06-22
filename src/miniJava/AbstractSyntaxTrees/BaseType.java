/**
 * miniJava Abstract Syntax Tree classes
 * @author prins
 * @version COMP 520 (v2.2)
 */
package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.SourcePosition;

public class BaseType extends TypeDenoter{
	
	public static final BaseType INT_TYPE = new BaseType(TypeKind.INT, "int", null);
	public static final BaseType BOOLEAN_TYPE = new BaseType(TypeKind.BOOLEAN, "boolean", null);
	public static final BaseType VOID_TYPE = new BaseType(TypeKind.VOID, "void", null);
	public static final BaseType UNSUPPORTED_TYPE = new BaseType(TypeKind.UNSUPPORTED, "", null);
	public static final BaseType STRING_TYPE = new BaseType(TypeKind.STRING, "String", null);
	public static final BaseType NULL_TYPE = new BaseType(TypeKind.NULL, "null", null);
	
	
    public BaseType(TypeKind t, String sp, SourcePosition posn){
        super(t, sp, posn);
    }
    
    public <A,R> R visit(Visitor<A,R> v, A o) {
        return v.visitBaseType(this, o);
    }
}
