package miniJava.ContextualAnalyzer;

import miniJava.AbstractSyntaxTrees.*;
import miniJava.SyntacticAnalyzer.*;

public abstract class Helpers {
	private static int errors = 0;
	
	public static void checkType(TypeDenoter type, IDTable table){
		if(type instanceof ArrayType){
			type = ((ArrayType) type).eltType;
		}
		
		if(type instanceof BaseType || type instanceof UnsupportedType){
			return;
		}
		int currscope = table.getScope(type.typeKind.name());
		if(currscope > IDTable.CLASS_SCOPE){
			reportError("Invalid class name: " + type, type.posn);
		}
		else if(currscope == IDTable.INVALID_SCOPE){
			reportError("Undefined class name: " + type, type.posn);
		}
	}
	
	
	public static void reportError(String error, SourcePosition posn){
		errors++;
		System.out.println("*** " + error + " at " + posn.toString());
	}
	
	public static void exitIfError(){
		if(errors > 0){
			System.out.println(errors + (errors == 1 ? " error" : " errors"));
			System.exit(4);
		}
	}
	
	
	public static void addDecl(IDTable table, Declaration decl){
		try{
			table.set(decl);
		}catch(ParsingErrorException e){
			reportError(e.getMessage(), decl.posn);
		}
	}
	
	
	public static boolean getTypeEquality(TypeDenoter type1, TypeDenoter type2){
		if(type1 instanceof UnsupportedType || type2 instanceof UnsupportedType){
			return false;
		}
		if(type1 instanceof ErrorType || type2 instanceof ErrorType){
			return true;
		}
		if(type1.typeKind == TypeKind.CLASS && type2.typeKind == TypeKind.CLASS){
			ClassType type1class = (ClassType) type1;
			ClassType type2class = (ClassType) type2;
			return type1class.className.spelling.equals(type2class.className.spelling);
		}
		if(type1.typeKind == TypeKind.NULL || type2.typeKind == TypeKind.NULL){
			if(type1.typeKind == TypeKind.NULL && type2.typeKind == TypeKind.CLASS){
				return true;
			}
			if(type1.typeKind == TypeKind.CLASS && type2.typeKind == TypeKind.NULL){
				return true;
			}
			if(type1.typeKind == TypeKind.NULL && type2.typeKind == TypeKind.ARRAY){
				return true;
			}
			if(type1.typeKind == TypeKind.ARRAY && type2.typeKind == TypeKind.NULL){
				return true;
			}
		}
		
		return type1.typeKind.equals(type2.typeKind);
	}
	
	
	public static boolean checkTypeEquality(TypeDenoter type1, TypeDenoter type2, SourcePosition posn){
		boolean equal = getTypeEquality(type1, type2);
		
		if(!equal){
			if(type1 instanceof UnsupportedType || type2 instanceof UnsupportedType){
				if(type1 instanceof UnsupportedType){
					reportError("Unsupported type " + type1.spelling, posn);
				}
				if(type2 instanceof UnsupportedType){
					reportError("Unsupported type " + type2.spelling, posn);
				}
			}
			else{
				reportError("Non-similar types: " + type1.typeKind + ", " + type2.typeKind, posn);
			}
		}
		return equal;
	}
	
	
	public static TypeDenoter handleUnsupported(TypeDenoter type, IDTable table){
		if(type instanceof ClassType && ((ClassType) type).className.spelling.equals("String")
				&& table.getScope("String") == IDTable.PREDEFINED_SCOPE){
			return new UnsupportedType(IDTable.STRING_DECL, type.posn);
		}
		return type;
	}
	
	public static TypeDenoter checkMethodCall(Reference method, ExprList args, TypeChecker checker, IDTable table){
		Declaration decl = method.decl;
		if(method.decl == IDTable.PRINTLN_DECL_INT){// || method.decl == IDTable.PRINTLN_DECL_STR){
			if(args.get(0) instanceof LiteralExpr){
					method.decl = IDTable.PRINTLN_DECL_INT;
					return BaseType.INT_TYPE;
			}
			else if(args.get(0) instanceof RefExpr){
				RefExpr ref = (RefExpr) args.get(0);
				if(ref.ref.decl.type.typeKind == TypeKind.INT){
					method.decl = IDTable.PRINTLN_DECL_INT;
					return BaseType.INT_TYPE;
				}
			}
			
		}
		else{
			return IDTable.STRING_DECL.type;
		}
		
		
		if(!(decl instanceof MethodDecl)){
			reportError("Method call cannot be resolved to method declaration ", method.posn);
			return new ErrorType(method.posn);
		}
		
		MethodDecl md = (MethodDecl) decl;
		if(md.parameterDeclList.size() != args.size()){
			reportError("Method " + method.id.spelling + " is declared as having " + md.parameterDeclList.size()
						+ " paramaters", method.posn);
			return new ErrorType(method.posn);
		}
		
		for(int i = 0; i < args.size(); i++){
			TypeDenoter type1 = args.get(i).visit(checker, null);
			TypeDenoter type2 = md.parameterDeclList.get(i).type;
			if(!Helpers.getTypeEquality(type2, type1)){
				Helpers.reportError("Parameter " + i + " in method " + method.id.spelling + " is of the incorrect type ", args.get(i).posn);
				return new ErrorType(args.get(i).posn);
			}
			
		}
		
		return Helpers.handleUnsupported(md.type, table);
		
		
		
	}
	
	
	public static boolean hasErrors(){
		if (errors > 0){
			return true;
		}
		else{
			return false;
		}
	}
	
}
