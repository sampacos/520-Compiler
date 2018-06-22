/**
 * miniJava Abstract Syntax Tree classes
 * @author prins
 * @version COMP 520 (v2.2)
 */
package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.SourcePosition;

abstract public class TypeDenoter extends AST {
    
    public TypeDenoter(TypeKind type, String sp, SourcePosition posn){
        super(posn);
        typeKind = type;
        spelling = sp;
    }
    
    public TypeKind typeKind;
    public String spelling;
}

        