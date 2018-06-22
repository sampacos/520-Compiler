package miniJava.CodeGenerator;

import miniJava.AbstractSyntaxTrees.ArrayType;
import miniJava.AbstractSyntaxTrees.AssignStmt;
import miniJava.AbstractSyntaxTrees.BaseType;
import miniJava.AbstractSyntaxTrees.BinaryExpr;
import miniJava.AbstractSyntaxTrees.BlockStmt;
import miniJava.AbstractSyntaxTrees.BooleanLiteral;
import miniJava.AbstractSyntaxTrees.CallExpr;
import miniJava.AbstractSyntaxTrees.CallStmt;
import miniJava.AbstractSyntaxTrees.ClassDecl;
import miniJava.AbstractSyntaxTrees.ClassType;
import miniJava.AbstractSyntaxTrees.Declaration;
import miniJava.AbstractSyntaxTrees.ErrorType;
import miniJava.AbstractSyntaxTrees.Expression;
import miniJava.AbstractSyntaxTrees.FieldDecl;
import miniJava.AbstractSyntaxTrees.IdRef;
import miniJava.AbstractSyntaxTrees.Identifier;
import miniJava.AbstractSyntaxTrees.IfStmt;
import miniJava.AbstractSyntaxTrees.IntLiteral;
import miniJava.AbstractSyntaxTrees.IxIdRef;
import miniJava.AbstractSyntaxTrees.IxQRef;
import miniJava.AbstractSyntaxTrees.LiteralExpr;
import miniJava.AbstractSyntaxTrees.LocalDecl;
import miniJava.AbstractSyntaxTrees.MethodDecl;
import miniJava.AbstractSyntaxTrees.NewArrayExpr;
import miniJava.AbstractSyntaxTrees.NewObjectExpr;
import miniJava.AbstractSyntaxTrees.NullLiteral;
import miniJava.AbstractSyntaxTrees.NullType;
import miniJava.AbstractSyntaxTrees.Operator;
import miniJava.AbstractSyntaxTrees.Package;
import miniJava.AbstractSyntaxTrees.ParameterDecl;
import miniJava.AbstractSyntaxTrees.QRef;
import miniJava.AbstractSyntaxTrees.RefExpr;
import miniJava.AbstractSyntaxTrees.Reference;
import miniJava.AbstractSyntaxTrees.ReturnStmt;
import miniJava.AbstractSyntaxTrees.Statement;
import miniJava.AbstractSyntaxTrees.StatementList;
import miniJava.AbstractSyntaxTrees.ThisRef;
import miniJava.AbstractSyntaxTrees.TypeKind;
import miniJava.AbstractSyntaxTrees.UnaryExpr;
import miniJava.AbstractSyntaxTrees.UnsupportedType;
import miniJava.AbstractSyntaxTrees.VarDecl;
import miniJava.AbstractSyntaxTrees.VarDeclStmt;
import miniJava.AbstractSyntaxTrees.Visitor;
import miniJava.AbstractSyntaxTrees.WhileStmt;


import mJAM.*;
import mJAM.Machine.Op;
import mJAM.Machine.Prim;
import mJAM.Machine.Reg;
import miniJava.ContextualAnalyzer.*;
import miniJava.SyntacticAnalyzer.TokenKind;

public class CodeGeneration implements Visitor<Object, RuntimeEntityDescriptor>{

	ClassDecl currclass;
	Package pack;
	int currdisp; //local variable displacement on LB
	int objdisp; //object displacement on SB
	
	enum CallType{
		ADDRESS, METHOD, VALUE;
	}
	
	@Override
	public RuntimeEntityDescriptor visitPackage(Package prog, Object arg) {
		pack = prog;
		
		Machine.initCodeGen();
		
		objdisp = 0;
		for(ClassDecl cd : prog.classDeclList){
			setObjectREDs(cd);
		}
		
		Machine.emit(Op.LOADL,0);            // array length 0
		Machine.emit(Prim.newarr);           // empty String array argument
		int patchAddr_Call_main = Machine.nextInstrAddr();  // record instr addr where main is called                // "main" is called
		Machine.emit(Op.CALL,Reg.CB, -1);     // static call main (address to be patched)
		Machine.emit(Op.HALT,0,0,0);         // end execution
		

		
		for(ClassDecl cd : prog.classDeclList){
			currclass = cd;
			cd.visit(this, null);
		}
		
		int codeAddr_main = Machine.nextInstrAddr();
		((MethodDecl) arg).visit(this, null);
		
		Machine.patch(patchAddr_Call_main, codeAddr_main);
		
		return null;
	}

	@Override
	public RuntimeEntityDescriptor visitClassDecl(ClassDecl cd, Object arg) {
		for(FieldDecl fd : cd.fieldDeclList){
			fd.visit(this, null);
		}
		
		for(MethodDecl md : cd.methodDeclList){
			md.visit(this, null);
		}
		
		
		return null;
	}

	@Override
	public RuntimeEntityDescriptor visitFieldDecl(FieldDecl fd, Object arg) {
		Machine.emit(Op.LOADL, fd.RED.sizeDisp);
		return null;
	}

	@Override
	public RuntimeEntityDescriptor visitMethodDecl(MethodDecl md, Object arg) {
		currdisp = Machine.linkDataSize;
		md.RED.sizeDisp = Machine.nextInstrAddr();
		
		int pdisp = -md.parameterDeclList.size();
		for(ParameterDecl pd : md.parameterDeclList){
			pd.visit(this, null);
			pd.RED.sizeDisp = pdisp++;
		}
		
		int localspace = getLocalSpace(md.statementList);
		for(Statement st : md.statementList){
			st.visit(this, null);
		}
		
		currdisp -= localspace;
		
		if(md.returnExpr != null){
			md.returnExpr.visit(this, null);
			Machine.emit(Op.RETURN, 1, 0, md.parameterDeclList.size());
		}
		else{
			Machine.emit(Op.RETURN, 0, 0, md.parameterDeclList.size());
		}
		
		
		return null;
	}

	@Override
	public RuntimeEntityDescriptor visitParameterDecl(ParameterDecl pd, Object arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RuntimeEntityDescriptor visitVarDecl(VarDecl decl, Object arg) {
		// TODO Auto-generated method stub
		if(decl.type instanceof ArrayType){
			//Machine.emit(Op.LOADA, Reg.HB, decl.RED.sizeDisp);
			//do nothing
		}
		return null;
	}

	@Override
	public RuntimeEntityDescriptor visitBaseType(BaseType type, Object arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RuntimeEntityDescriptor visitClassType(ClassType type, Object arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RuntimeEntityDescriptor visitArrayType(ArrayType type, Object arg) {
		return null;
	}

	@Override
	public RuntimeEntityDescriptor visitUnsupportedType(UnsupportedType type, Object arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RuntimeEntityDescriptor visitErrorType(ErrorType type, Object arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RuntimeEntityDescriptor visitNullType(NullType type, Object arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RuntimeEntityDescriptor visitBlockStmt(BlockStmt stmt, Object arg) {
		int localspace = getLocalSpace(stmt.sl);
		for(Statement st : stmt.sl){
			st.visit(this, null);
		}
		currdisp -= localspace;
		Machine.emit(Op.POP, localspace);
		return null;
	}

	@Override
	public RuntimeEntityDescriptor visitVardeclStmt(VarDeclStmt stmt, Object arg) {
		stmt.initExp.visit(this, CallType.VALUE);
		if(stmt.varDecl.type instanceof ArrayType){
			//stmt.varDecl.RED.sizeDisp = mJAM.Interpreter.content(5) - 1;
			//do nothing
		}
		
		if(stmt.varDecl.RED.sizeDisp != currdisp - 1){
			Machine.emit(Op.STORE, Reg.LB, stmt.varDecl.RED.sizeDisp);
			currdisp--;
		}
		
		stmt.varDecl.visit(this, null);
		return null;
	}

	@Override
	public RuntimeEntityDescriptor visitAssignStmt(AssignStmt stmt, Object arg) {
		
		if(stmt.ref.decl instanceof LocalDecl && !(stmt.ref.decl.type instanceof ArrayType)){
			stmt.val.visit(this, null);
			Machine.emit(Op.STORE, Reg.LB, getVarDisplacement(stmt.ref));
			currdisp--;
		}
		else{
			stmt.ref.visit(this, CallType.ADDRESS);
			stmt.val.visit(this, null);
			
			if(stmt.ref instanceof IxIdRef || stmt.ref instanceof IxQRef){
				Machine.emit(Prim.arrayupd);
			}
			else{
				Machine.emit(Prim.fieldupd);
			}
		}
		
		return null;
	}

	@Override
	public RuntimeEntityDescriptor visitCallStmt(CallStmt stmt, Object arg) {
		for(Expression expr : stmt.argList){// getting arguments
			expr.visit(this, null);
		}
		
		MethodDecl md = (MethodDecl) stmt.methodRef.decl;
		if(md == IDTable.PRINTLN_DECL_INT){
			Machine.emit(Prim.putintnl);
			return null;							//these are println cases
		}
		
		if(currclass.methodDeclList != null){
			for(MethodDecl method : currclass.methodDeclList){
				if(method.equals(stmt.methodRef.decl)){
					Machine.emit(Op.LOADA, Reg.OB, 0); //if this method is in current class, load currclass instance onto stack
				}
			}
		}
		stmt.methodRef.visit(this, CallType.METHOD);
		Machine.emit(Op.CALLI, Reg.CB, md.RED.sizeDisp);
		
		
		if(md.type.typeKind != TypeKind.VOID){
			Machine.emit(Op.POP, 1);
		}
		
		return null;
	}

	@Override
	public RuntimeEntityDescriptor visitReturnStmt(ReturnStmt stmt, Object arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RuntimeEntityDescriptor visitIfStmt(IfStmt stmt, Object arg) {
		stmt.cond.visit(this, null);
		
		int jump = Machine.nextInstrAddr(); // address of else statement, or next statement if none
		
		Machine.emit(Op.JUMPIF, 0, Reg.CB, 0);
		stmt.thenStmt.visit(this, null);
		
		if(stmt.elseStmt != null){
			int jump2 = Machine.nextInstrAddr();
			Machine.emit(Op.JUMP, Reg.CB, 0);
			Machine.patch(jump, Machine.nextInstrAddr());
			stmt.elseStmt.visit(this, null);
			Machine.patch(jump2, Machine.nextInstrAddr());
		}
		else{
			Machine.patch(jump, Machine.nextInstrAddr());
		}
		
		return null;
	}

	@Override
	public RuntimeEntityDescriptor visitWhileStmt(WhileStmt stmt, Object arg) {
		int jump_start = Machine.nextInstrAddr();
		Machine.emit(Op.JUMP, 0, Reg.CB, 0);
		
		int loop = Machine.nextInstrAddr();
		stmt.body.visit(this, null);
		
		int test = Machine.nextInstrAddr();
		stmt.cond.visit(this, null);
		Machine.emit(Op.JUMPIF, 1, Reg.CB, loop);
		Machine.patch(jump_start, test);
		
		return null;
	}

	@Override
	public RuntimeEntityDescriptor visitUnaryExpr(UnaryExpr expr, Object arg) {
		expr.expr.visit(this, null);
		if(expr.operator.kind == TokenKind.MINUS){
			Machine.emit(Prim.neg);
		}
		else if(expr.operator.kind == TokenKind.EXCLAM){
			Machine.emit(Prim.not);
		}
		else{
			throw new RuntimeException("Unexpected operator on unary expression at " + expr.operator.posn.toString());
		}
		
		return null;
	}

	@Override
	public RuntimeEntityDescriptor visitBinaryExpr(BinaryExpr expr, Object arg) {
		expr.left.visit(this, null);
		expr.right.visit(this, null);
		
		switch(expr.operator.kind){
		case PLUS:
			Machine.emit(Prim.add);
			break;
		case MINUS:
			Machine.emit(Prim.sub);
			break;
		case ASTERISK:
			Machine.emit(Prim.mult);
			break;
		case SLASH:
			Machine.emit(Prim.div);
			break;
		case LESSTHAN:
			Machine.emit(Prim.lt);
			break;
		case GREATERTHAN:
			Machine.emit(Prim.gt);
			break;
		case LESSEQUAL:
			Machine.emit(Prim.le);
			break;
		case GREATEREQUAL:
			Machine.emit(Prim.ge);
			break;
		case BOOLEQUAL:
			Machine.emit(Prim.eq);
			break;
		case NOT_EQUAL:
			Machine.emit(Prim.ne);
			break;
		case BARX2:
			Machine.emit(Prim.or);
			break;
		case AMPERSANDX2:
			Machine.emit(Prim.and);
			break;
		default:
			throw new RuntimeException("Unexcpeted binary operator at " + expr.operator.posn.toString());
		}
		
		return null;
	}

	@Override
	public RuntimeEntityDescriptor visitRefExpr(RefExpr expr, Object arg) {
		expr.ref.visit(this, CallType.VALUE);
		if(expr.ref.decl instanceof FieldDecl && (!expr.ref.id.spelling.equals("length") && !(expr.ref instanceof QRef))){
			Machine.emit(Prim.fieldref);
		}
		return null;
	}

	@Override
	public RuntimeEntityDescriptor visitCallExpr(CallExpr expr, Object arg) {
		for(Expression ex : expr.argList){
			ex.visit(this, null);
		}
		
		for(MethodDecl md : currclass.methodDeclList){
			if(md.equals(expr.functionRef.decl)){
				Machine.emit(Op.LOADA, Reg.OB, 0); //if this method is in current class, load currclass instance onto stack
			}
		}
		
		expr.functionRef.visit(this, CallType.METHOD);
		
		return null;
	}

	@Override
	public RuntimeEntityDescriptor visitLiteralExpr(LiteralExpr expr, Object arg) {
		expr.lit.visit(this, null);
		return null;
	}

	@Override
	public RuntimeEntityDescriptor visitNewObjectExpr(NewObjectExpr expr, Object arg) {
		ClassDecl cd = expr.classtype.declaration;
		
		Machine.emit(Op.LOADL, cd.classRED.classDisplacement);
		Machine.emit(Op.LOADL, cd.classRED.sizeDisp);
		Machine.emit(Prim.newobj);
		currdisp++;
		
		return null;
	}

	@Override
	public RuntimeEntityDescriptor visitNewArrayExpr(NewArrayExpr expr, Object arg) {
		expr.sizeExpr.visit(this, null);
		Machine.emit(Prim.newarr);
		return null;
	}

	@Override
	public RuntimeEntityDescriptor visitThisRef(ThisRef ref, Object arg) {
		Machine.emit(Op.LOADA, Reg.OB, 0); // object base, since this is referencing current object
		return null;
	}

	@Override
	public RuntimeEntityDescriptor visitIdRef(IdRef ref, Object arg) {
		ref.id.visit(this, null);
		if(ref.decl instanceof LocalDecl){
			Machine.emit(Op.LOAD, Reg.LB, ref.decl.RED.sizeDisp);
			currdisp++;
		}
		else if(ref.decl instanceof FieldDecl){
			for(ClassDecl cd : pack.classDeclList){
				for(FieldDecl fd : cd.fieldDeclList){
					if(fd.equals(ref.decl)){
						Machine.emit(Op.LOADA, Reg.OB, 0);
						ref.decl.visit(this, null);
						if(arg == CallType.VALUE){
							Machine.emit(Prim.fieldref);
						}
						
					}
						
				}
			}
		}
		else if(ref.decl instanceof MethodDecl){
			for(ClassDecl cd : pack.classDeclList){
				for(MethodDecl md : cd.methodDeclList){
					if(md.equals(ref.decl)){}
						//ref.decl.visit(this, null);
						//do nothing
				}
			}
		}
		else if(ref.decl instanceof ClassDecl){
			Machine.emit(Op.LOADA, Reg.HB, ((ClassDecl)ref.decl).classRED.classDisplacement + ((ClassDecl)ref.decl).classRED.sizeDisp);
			currdisp++;
		}
		
		return null;
	}

	@Override
	public RuntimeEntityDescriptor visitIxIdRef(IxIdRef ref, Object arg) {
		switch((CallType) arg){
		case ADDRESS:
			ref.id.visit(this, CallType.VALUE);
			ref.indexExpr.visit(this, null);
			break;
		case VALUE:
			ref.id.visit(this, CallType.VALUE);
			ref.indexExpr.visit(this, null);
			Machine.emit(Prim.arrayref);
			break;
		default:
			throw new RuntimeException("Improper call type for IxIdRef at " + ref.posn.toString());
		}
		return null;
	}

	@Override
	public RuntimeEntityDescriptor visitQRef(QRef ref, Object arg) {
		ref.ref.visit(this, CallType.VALUE);
		if(ref.id.spelling.equals("length") && ref.ref.decl.type.typeKind == TypeKind.ARRAY){
			Machine.emit(Prim.arraylen);
			return null;
		}
		else if(ref.ref instanceof QRef){
			if(ref.decl instanceof FieldDecl){
				//ref.decl.visit(this, null);
				//Machine.emit(Prim.fieldref);
				//ref.decl.visit(this, null);
				//do nothing
			}
			else if(ref.decl instanceof MethodDecl){
				
			}
			else if(ref.decl instanceof LocalDecl){
				Machine.emit(Op.LOAD, Reg.LB, getVarDisplacement(ref));
				currdisp++;
			}
		}
		if(!(ref.decl instanceof MethodDecl) && arg != CallType.VALUE)
			ref.decl.visit(this, CallType.VALUE);
		//
		if(arg == CallType.VALUE && ref.decl instanceof FieldDecl){
			ref.decl.visit(this, null);
			Machine.emit(Prim.fieldref);
		}
		
		return null;
	}

	@Override
	public RuntimeEntityDescriptor visitIxQRef(IxQRef ref, Object arg) {
		ref.ref.visit(this, null);
		ref.ixExpr.visit(this, null);
		ref.decl.visit(this, null);
		return null;
	}

	@Override
	public RuntimeEntityDescriptor visitIdentifier(Identifier id, Object arg) {
		if(id.decl instanceof LocalDecl){
			Machine.emit(Op.LOAD, Reg.LB, id.decl.RED.sizeDisp);
			currdisp++;
		}
		else if(id.decl instanceof FieldDecl){
			for(ClassDecl cd : pack.classDeclList){
				for(FieldDecl fd : cd.fieldDeclList){
					if(fd.equals(id.decl)){
						//id.decl.visit(this, null);
						//do nothing
					}
						
				}
			}
		}
		else if(id.decl instanceof MethodDecl){
			for(ClassDecl cd : pack.classDeclList){
				for(MethodDecl md : cd.methodDeclList){
					if(md.equals(id.decl))
						id.decl.visit(this, null);
				}
			}
		}
		else if(id.decl instanceof ClassDecl){
			Machine.emit(Op.LOADA, Reg.HB, ((ClassDecl)id.decl).classRED.classDisplacement + ((ClassDecl)id.decl).classRED.sizeDisp);
			currdisp++;
		}
		
		return null;
	}

	@Override
	public RuntimeEntityDescriptor visitOperator(Operator op, Object arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RuntimeEntityDescriptor visitIntLiteral(IntLiteral num, Object arg) {
		Machine.emit(Op.LOADL, Integer.parseInt(num.spelling));
		currdisp++;
		return null;
	}

	@Override
	public RuntimeEntityDescriptor visitBooleanLiteral(BooleanLiteral bool, Object arg) {
		if(bool.spelling.equals("true")){
			Machine.emit(Op.LOADL, 1);
		}
		else{
			Machine.emit(Op.LOADL, 0);
		}
		currdisp++;
		return null;
	}

	@Override
	public RuntimeEntityDescriptor visitNullLiteral(NullLiteral nul, Object arg) {
		Machine.emit(Op.LOADL, 0);
		currdisp++;
		return null;
	}
	
	
	private int getLocalSpace(StatementList sl){
		int localspace = 0;
		for(Statement st : sl){
			if(st instanceof VarDeclStmt){
				VarDecl vd = ((VarDeclStmt) st).varDecl;
				vd.RED.sizeDisp = currdisp + localspace;
				localspace ++;
			}
		}
		
		return localspace;
	}
	
	private int getVarDisplacement(Reference ref){
		Declaration decl = ref.decl;
		if(decl instanceof VarDecl){
			return ((VarDecl) decl).RED.sizeDisp;
		}
		else if(decl instanceof ParameterDecl){
			return ((ParameterDecl) decl).RED.sizeDisp;
		}
		else{
			throw new RuntimeException("Error in finding local variable / parameter");
		}
		
	}

	
	private void setObjectREDs(ClassDecl cd){
		if(cd.classRED.sizeDisp == -1){
			cd.classRED.sizeDisp = 0;
			
			for(FieldDecl fd : cd.fieldDeclList){
				if(fd.RED == null){
					fd.RED = new RuntimeEntityDescriptor(0);
				}
				if(cd.classRED == null){
					//error
				}
				fd.RED.sizeDisp = cd.classRED.sizeDisp;
				cd.classRED.sizeDisp++;
			}
		}
	}
}
