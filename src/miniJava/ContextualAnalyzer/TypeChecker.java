package miniJava.ContextualAnalyzer;
import miniJava.AbstractSyntaxTrees.*;
import miniJava.AbstractSyntaxTrees.Package;
import miniJava.SyntacticAnalyzer.SourcePosition;
import miniJava.SyntacticAnalyzer.Token;
import miniJava.SyntacticAnalyzer.TokenKind;

public class TypeChecker implements Visitor<Object, TypeDenoter>{
	ClassDecl currclass;
	MethodDecl currmethod;
	IDTable table;
	public MethodDecl mainMethod;
	
	public TypeChecker(IDTable table){
		this.table = table;
	}
	
	@Override
	public TypeDenoter visitPackage(Package prog, Object arg) {
		MethodDecl main = null;
		int maincount = 0;
	
		if(prog.classDeclList.size() != 0){
			for(ClassDecl cd : prog.classDeclList){
				currclass = cd;
				cd.visit(this, null);
				for(MethodDecl md : cd.methodDeclList){
					if(md.type.typeKind == TypeKind.VOID && md.isStatic && !md.isPrivate){
						if(md.name.equals("main")){
							if(md.parameterDeclList.size() == 1){
								ParameterDecl p = md.parameterDeclList.get(0);
								if(p.name.equals("args") && (p.type.spelling.equals("String[]") || (p.type.spelling.equals("String") && p.type.typeKind == TypeKind.ARRAY))){
									maincount++;
									main = md;
									mainMethod = md;
								}
							}
						}
					}
				}
			}
		}
		if(maincount > 1){
			Helpers.reportError("Cannot have more than one main method", main.posn);
		}
		else if(main == null){
			Helpers.reportError("No main method declared. Exiting program.", prog.posn);
		}
		
		return null;
	}

	@Override
	public TypeDenoter visitClassDecl(ClassDecl cd, Object arg) {
		if(cd.fieldDeclList.size() != 0){
			for(FieldDecl f : cd.fieldDeclList){
				f.visit(this, null);
			}
		}
		
		if(cd.methodDeclList.size() != 0){
			for(MethodDecl m : cd.methodDeclList){
				m.visit(this, null);
			}
		}
		
		return null;
	}

	@Override
	public TypeDenoter visitFieldDecl(FieldDecl fd, Object arg) {
		return Helpers.handleUnsupported(fd.type, table);
	}

	@Override
	public TypeDenoter visitMethodDecl(MethodDecl md, Object arg) {
		currmethod = md;
		if(md.parameterDeclList.size() != 0){
			for(ParameterDecl pd : md.parameterDeclList){
				pd.visit(this, null);
			}
		}
		if(md.statementList.size() != 0){
			for(Statement st : md.statementList){
				st.visit(this, null);
			}
		}
		TypeDenoter returntype = md.returnExpr == null ? BaseType.VOID_TYPE : md.returnExpr.visit(this, null);
		if(!Helpers.getTypeEquality(returntype, md.type)){
			if(md.type.typeKind == TypeKind.VOID){
				Helpers.reportError("Method \"" + md.name + "\" is void, and therefore cannot return a value", md.posn);
			}
			else{
				Helpers.reportError("Method \"" + md.name + "\" must return type " + md.type.spelling, md.posn);
			}
		}
		
		Statement last;
		if(md.statementList.size() == 0){
			if(md.type.typeKind == TypeKind.VOID){//if method is void and no return stmt exists, we add one.					
				md.statementList.add(new ReturnStmt(null, new SourcePosition(md.posn.line + 1, 0)));
			}else{
				Helpers.reportError("Method " + md.name + " must end with a return statement of type " + 
						md.type.spelling, md.posn);		// if method is not void and no return stmt exists, we exit program
				Helpers.exitIfError();
			}
		}
		else{
			last = md.statementList.get(md.statementList.size() - 1);
			if(!(last instanceof ReturnStmt)){		// checking for return statement on last statement of method
				if(md.type.typeKind == TypeKind.VOID){//if method is void and no return stmt exists, we add one.					
					md.statementList.add(new ReturnStmt(null, new SourcePosition(last.posn.line + 1, 0)));
				}else{
					Helpers.reportError("Method " + md.name + " must end with a return statement of type " + 
							md.type.spelling, md.posn);		// if method is not void and no return stmt exists, we exit program
					Helpers.exitIfError();
				}
			}
		}
		
		
		
		return null;
	}

	@Override
	public TypeDenoter visitParameterDecl(ParameterDecl pd, Object arg) {
		return null;
	}

	@Override
	public TypeDenoter visitVarDecl(VarDecl decl, Object arg) {
		return null;
	}

	@Override
	public TypeDenoter visitBaseType(BaseType type, Object arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TypeDenoter visitClassType(ClassType type, Object arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TypeDenoter visitArrayType(ArrayType type, Object arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TypeDenoter visitUnsupportedType(UnsupportedType type, Object arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TypeDenoter visitErrorType(ErrorType type, Object arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TypeDenoter visitBlockStmt(BlockStmt stmt, Object arg) {
		if(stmt.sl.size() != 0){
			for(Statement st : stmt.sl){
				st.visit(this, null);
			}
		}
		return null;
	}

	@Override
	public TypeDenoter visitVardeclStmt(VarDeclStmt stmt, Object arg) {
		//System.out.println(stmt.initExp);
		TypeDenoter stmttype = Helpers.handleUnsupported(stmt.varDecl.type, table);
		TypeDenoter exprType = stmt.initExp.visit(this, null);
		//System.out.println(exprType);
		
		
		/*if(exprType instanceof ArrayType){
			exprType = ((ArrayType) exprType).eltType;
		}*/
		Helpers.checkTypeEquality(stmttype, exprType, stmt.posn);
		return null;
	}

	@Override
	public TypeDenoter visitAssignStmt(AssignStmt stmt, Object arg) {
		TypeDenoter type = stmt.ref.visit(this, null);
		Declaration decl = stmt.ref.decl;
		if(!(decl instanceof FieldDecl || decl instanceof LocalDecl)){
			Helpers.reportError(stmt.ref.id.spelling + " cannot be resolved to a variable", stmt.posn);
		}
		
		
		
		
		TypeDenoter type1 = stmt.val.visit(this, null);
		
		
		Helpers.checkTypeEquality(type, type1, stmt.posn);
		
		return null;
	}

	@Override
	public TypeDenoter visitCallStmt(CallStmt stmt, Object arg) {
		return Helpers.checkMethodCall(stmt.methodRef, stmt.argList, this, table);
	}

	@Override
	public TypeDenoter visitReturnStmt(ReturnStmt stmt, Object arg) {
		currmethod.returnExpr = stmt.returnExpr;
		/*if (currmethod.type.typeKind == TypeKind.VOID && stmt.returnExpr == null){
			Helpers.reportError("Cannot return a value in a void method", stmt.posn);
		}*/
		if(stmt.returnExpr == null){
			return null;
		}
		return stmt.returnExpr.visit(this, null);
	}

	@Override
	public TypeDenoter visitIfStmt(IfStmt stmt, Object arg) {
		TypeDenoter type = stmt.cond.visit(this, null);
		if(!Helpers.getTypeEquality(type, BaseType.BOOLEAN_TYPE)){
			Helpers.reportError("Incompatible types: Condition expression of if statement must be of type boolean", stmt.cond.posn);
			return new ErrorType(stmt.cond.posn);
		}
		stmt.thenStmt.visit(this, null);
		if(stmt.elseStmt != null){
			stmt.elseStmt.visit(this, null);
		}
		
		return null;
	}

	@Override
	public TypeDenoter visitWhileStmt(WhileStmt stmt, Object arg) {
		if(stmt.cond.visit(this, null).typeKind != TypeKind.BOOLEAN){
			Helpers.reportError("Incompatible types: Condition expression of if statement must be of type boolean ", stmt.cond.posn);
			return new ErrorType(stmt.cond.posn);
		}
		stmt.body.visit(this, null);
		
		return null;
	}

	@Override
	public TypeDenoter visitUnaryExpr(UnaryExpr expr, Object arg) {
		return expr.expr.visit(this, null);
	}

	@Override
	public TypeDenoter visitBinaryExpr(BinaryExpr expr, Object arg) {
		TypeDenoter left = expr.left.visit(this, null);
		TypeDenoter right = expr.right.visit(this, null);
		TypeDenoter result = null;
		
		switch(expr.operator.kind){
		case BOOLEQUAL:
		case NOT_EQUAL:
			if(!Helpers.checkTypeEquality(left, right, expr.posn)){
				return new ErrorType(expr.posn);
			}
			result = new BaseType(TypeKind.BOOLEAN, "boolean", expr.posn);
			break;
		case BARX2:
		case AMPERSANDX2:
			if(!Helpers.checkTypeEquality(BaseType.BOOLEAN_TYPE, right, expr.right.posn)){
				return new ErrorType(expr.right.posn);
			}
			if(!Helpers.checkTypeEquality(BaseType.BOOLEAN_TYPE, left, expr.left.posn)){
				return new ErrorType(expr.left.posn);
			}
			result = new BaseType(TypeKind.BOOLEAN, "boolean", expr.posn);
			break;
			
		case LESSTHAN:
		case GREATERTHAN:
		case LESSEQUAL:
		case GREATEREQUAL:
			if(!Helpers.checkTypeEquality(BaseType.INT_TYPE, right, expr.right.posn)){
				return new ErrorType(expr.right.posn);
			}
			if(!Helpers.checkTypeEquality(BaseType.INT_TYPE, left, expr.left.posn)){
				return new ErrorType(expr.left.posn);
			}
			result = new BaseType(TypeKind.BOOLEAN, "boolean", expr.posn);
			break;
			
		case PLUS:
		case MINUS:
		case ASTERISK:
		case SLASH:
			if(!Helpers.checkTypeEquality(BaseType.INT_TYPE, right, expr.right.posn)){
				return new ErrorType(expr.right.posn);
			}
			if(!Helpers.checkTypeEquality(BaseType.INT_TYPE, left, expr.left.posn)){
				return new ErrorType(expr.left.posn);
			}
			result = new BaseType(TypeKind.INT, "int", expr.posn);
			break;
		
		default:
			return null;
		
		}
		
		Helpers.exitIfError();
		return result;
	}

	@Override
	public TypeDenoter visitRefExpr(RefExpr expr, Object arg) {
		return Helpers.handleUnsupported(expr.ref.visit(this, null), table);
	}

	@Override
	public TypeDenoter visitCallExpr(CallExpr expr, Object arg) {
		TypeDenoter t = Helpers.checkMethodCall(expr.functionRef, expr.argList, this, table);
		if(t instanceof ErrorType || t instanceof UnsupportedType){
			return t;
		}
		else{
			return expr.functionRef.visit(this, null);
		}
	}

	@Override
	public TypeDenoter visitLiteralExpr(LiteralExpr expr, Object arg) {
		return expr.lit.visit(this, null);
	}

	@Override
	public TypeDenoter visitNewObjectExpr(NewObjectExpr expr, Object arg) {
		return Helpers.handleUnsupported(expr.classtype, table);
	}

	@Override
	public TypeDenoter visitNewArrayExpr(NewArrayExpr expr, Object arg) {
		TypeDenoter type = expr.sizeExpr.visit(this, null);
		
		if(type.typeKind != TypeKind.INT){
			Helpers.reportError("Array indexing expression must be of type int ", expr.sizeExpr.posn);
			return new ErrorType(expr.sizeExpr.posn);
		}
		return new ArrayType(expr.eltType, expr.eltType.posn);
	}

	@Override
	public TypeDenoter visitThisRef(ThisRef ref, Object arg) {
		return Helpers.handleUnsupported(currclass.type, table);
	}

	@Override
	public TypeDenoter visitIdRef(IdRef ref, Object arg) {
		return Helpers.handleUnsupported(ref.decl.type, table);
	}

	@Override
	public TypeDenoter visitIxIdRef(IxIdRef ref, Object arg) {
		TypeDenoter type = ref.indexExpr.visit(this, null);
		if(type.typeKind != TypeKind.INT){
			Helpers.reportError("Indexing expression must be of type int ", ref.indexExpr.posn);
			return new ErrorType(ref.indexExpr.posn);
		}
	
		
		TypeDenoter type1 = ref.decl.type;
		//System.out.println(type1);
		if(!(type1 instanceof ArrayType)){
			Helpers.reportError("Indexed reference must be ArrayType ", ref.posn);
			return new ErrorType(ref.posn);
		}
		return Helpers.handleUnsupported(((ArrayType) type1).eltType, table);
	}

	@Override
	public TypeDenoter visitQRef(QRef ref, Object arg) {
		TypeDenoter reftype = ref.id.visit(this, null);
		return Helpers.handleUnsupported(reftype, table);
	}

	@Override
	public TypeDenoter visitIxQRef(IxQRef ref, Object arg) {
		TypeDenoter ixtype = ref.ixExpr.visit(this, null);
		if(ixtype.typeKind != TypeKind.INT){
			Helpers.reportError("Indexing expression must be of type int ", ref.ixExpr.posn);
			return new ErrorType(ref.ixExpr.posn);
		}
		TypeDenoter eltType = ref.ref.visit(this, null);
		//System.out.println(eltType);
		if(!(eltType instanceof ArrayType)){
			Helpers.reportError("Indexed reference must be ArrayType ", ref.posn);
			return new ErrorType(ref.posn);
		}
		ArrayType at = (ArrayType) eltType;
		eltType = at.eltType;
		return Helpers.handleUnsupported(eltType, table);
	}

	@Override
	public TypeDenoter visitIdentifier(Identifier id, Object arg) {
		TypeDenoter t = id.decl.type;
		return Helpers.handleUnsupported(t, table);
	}

	@Override
	public TypeDenoter visitOperator(Operator op, Object arg) {
		return null;
	}

	@Override
	public TypeDenoter visitIntLiteral(IntLiteral num, Object arg) {
		// TODO Auto-generated method stub
		return new BaseType(TypeKind.INT, "int", num.posn);
	}

	@Override
	public TypeDenoter visitBooleanLiteral(BooleanLiteral bool, Object arg) {
		// TODO Auto-generated method stub
		return new BaseType(TypeKind.BOOLEAN, "boolean", bool.posn);
	}

	@Override
	public TypeDenoter visitNullLiteral(NullLiteral nul, Object arg) {
		// TODO Auto-generated method stub
		NullType nulltype = new NullType(new Identifier(new Token(TokenKind.NULL, "null", nul.posn)), nul.posn);
		return nulltype;
	}

	@Override
	public TypeDenoter visitNullType(NullType type, Object arg) {
		// TODO Auto-generated method stub
		return null;
	}

}
