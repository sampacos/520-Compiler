package miniJava.ContextualAnalyzer;
import java.util.HashMap;
import java.util.Iterator;

import miniJava.AbstractSyntaxTrees.*;
import miniJava.SyntacticAnalyzer.Token;

public class Identification implements Visitor<IDTable, Void> {
	ClassDecl currclass = null;
	MethodDecl currmethod = null;
	IDTable idtable;
	HashMap<String, HashMap<String, Declaration>> classesFields = new HashMap<String, HashMap<String, Declaration>>();
	HashMap<String, Declaration> justClasses = new HashMap<String, Declaration>();
	
	
	public IDTable makeTable(AST ast){
		IDTable table = new IDTable();
		ast.visit(this, table);
		idtable = table;
		return table;
	}
	
	@Override
	public Void visitPackage(miniJava.AbstractSyntaxTrees.Package pack, IDTable table){
		ClassDecl sysdecl = (ClassDecl) table.get(IDTable.SYSTEM);
		classesFields.put(IDTable.SYSTEM, new HashMap<String, Declaration>());
		justClasses.put(IDTable.SYSTEM, table.get(IDTable.SYSTEM));
		ClassDecl printdecl = (ClassDecl) table.get(IDTable.PRINTSTREAM);
		classesFields.put(IDTable.PRINTSTREAM, new HashMap<String, Declaration>());
		justClasses.put(IDTable.PRINTSTREAM, table.get(IDTable.PRINTSTREAM));
		justClasses.put("String", IDTable.STRING_DECL);
		
		for(FieldDecl fd : sysdecl.fieldDeclList){
			classesFields.get(IDTable.SYSTEM).put(fd.name, fd);
		}
		for(MethodDecl md : sysdecl.methodDeclList){
			classesFields.get(IDTable.SYSTEM).put(md.name, md);
		}
		
		for(FieldDecl fd : printdecl.fieldDeclList){
			classesFields.get(IDTable.PRINTSTREAM).put(fd.name, fd);
		}
		for(MethodDecl md : printdecl.methodDeclList){
			classesFields.get(IDTable.PRINTSTREAM).put(md.name, md);
		}
		
		for(ClassDecl classdecl : pack.classDeclList){
			Helpers.addDecl(table, classdecl);
			classesFields.put(classdecl.name, new HashMap<String, Declaration>());
			justClasses.put(classdecl.name, classdecl);
			
			for(FieldDecl f : classdecl.fieldDeclList){
				classesFields.get(classdecl.name).put(f.name, f);
			}
			for(MethodDecl m : classdecl.methodDeclList){
				classesFields.get(classdecl.name).put(m.name, m);
			}
			
		}
		
		for(ClassDecl classdecl : pack.classDeclList){
			classdecl.visit(this, table);
		}
		
		return null;
	}
	
	
	@Override
	public Void visitClassDecl(ClassDecl classdecl, IDTable table){
		//classdecl.id.decl = classdecl;
		currclass = classdecl;
		
		table.openScope();
		
		for(FieldDecl f : classdecl.fieldDeclList){
			f.visit(this, table);
		}
		
		for(FieldDecl f : classdecl.fieldDeclList){
			f.type.visit(this, table);
		}
		
		Iterator<MethodDecl> method_iter = classdecl.methodDeclList.iterator();
		while (method_iter.hasNext()){
			MethodDecl m = method_iter.next();
			m.visit(this, table);
		}
		//table.display();
		table.closeScope();
		
		return null;
	}
	
	@Override
	public Void visitFieldDecl(FieldDecl fielddecl, IDTable table){
		//fielddecl.id.decl = fielddecl;
		
		if(fielddecl.type.typeKind == TypeKind.VOID){
			Helpers.reportError("Void is not a valid type for a field", fielddecl.posn);
		}
		else{
			Helpers.addDecl(table, fielddecl);
		}
		
		return null;
	}
	
	@Override
	public Void visitMethodDecl(MethodDecl methoddecl, IDTable table){
		//methoddecl.id.decl = methoddecl;
		currmethod = methoddecl;
		methoddecl.type.visit(this, table);
		Helpers.addDecl(table, methoddecl);
		
		table.openScope(); //parameters
		
		
		for(ParameterDecl param : methoddecl.parameterDeclList){
			param.visit(this, table);
		}
		
		for(ParameterDecl param : methoddecl.parameterDeclList){
			param.type.visit(this, table);
		}
		
		table.openScope(); //method statement scope
		
		
		Iterator<Statement> iter = methoddecl.statementList.iterator();
		
		while(iter.hasNext()){
			Statement stmt = iter.next();
			stmt.visit(this, table);
		}
		
		//table.display();
		table.closeScope(); //parameter scope
		table.closeScope(); //back to class members scope
		
		return null;
			
	}
	
	@Override
	public Void visitParameterDecl(ParameterDecl paramdecl, IDTable table){
		paramdecl.id.decl = paramdecl;
		
		if(paramdecl.type.typeKind == TypeKind.VOID){
			Helpers.reportError("Void is not a valid type for a parameter", paramdecl.posn);
		}
		
		/*if (paramdecl.type.typeKind == TypeKind.CLASS){
			if(!classesFields.containsKey(paramdecl.id.spelling)){
				Helpers.reportError("Class type of parameter not defined", paramdecl.posn);
			}
		}*/
		
		Helpers.addDecl(table, paramdecl);
		
		return null;
	}
	
	@Override
	public Void visitVarDecl(VarDecl vardecl, IDTable table){
		vardecl.id.decl = vardecl;
		if(vardecl.type instanceof ArrayType){
			Identifier id = new Identifier(new Token("length", vardecl.posn));
			QRef length = new QRef(new IdRef(vardecl.id, vardecl.posn), id, vardecl.posn);
			FieldDecl len = new FieldDecl(false, false, (TypeDenoter) BaseType.INT_TYPE, id, vardecl.posn);
			id.decl = len;
			length.id = id;
			len.id = id;
			id.decl.type = BaseType.INT_TYPE;
			length.decl = len;
			length.decl.type = BaseType.INT_TYPE;
			length.id.decl = len;
			length.id.decl.type = BaseType.INT_TYPE;
			
			length.ref.decl = vardecl;
			length.decl.name = length.id.spelling;
			((ArrayType)vardecl.type).lengthField.add(len);
			//System.out.println(length.id.decl);
		}
		return null;
	}
	
	@Override
	public Void visitBaseType(BaseType base, IDTable table){
		return null;
	}
	
	@Override
	public Void visitClassType(ClassType ct, IDTable table){
		Declaration decl = justClasses.get(ct.className.spelling);
		
		if(!(decl instanceof ClassDecl)){
			Helpers.reportError(ct.className.spelling + " cannot be resolved to a type", ct.posn);
			Helpers.exitIfError();
			return null;
		}
		
		ct.declaration = (ClassDecl) decl;
		return null;
	}
	
	
	@Override
	public Void visitArrayType(ArrayType at, IDTable table){
		at.eltType.visit(this, table);
		return null;
	}
	
	
	@Override
	public Void visitErrorType(ErrorType error, IDTable tabel){
		return null;
	}
	
	@Override
	public Void visitUnsupportedType(UnsupportedType unsup, IDTable table){
		return null;
	}

	@Override
	public Void visitBlockStmt(BlockStmt stmt, IDTable table) {
		table.openScope();
		Iterator<Statement> iter = stmt.sl.iterator();
		while(iter.hasNext()){
			iter.next().visit(this, table);
		}
		table.closeScope();
		
		return null;
	}

	@Override
	public Void visitVardeclStmt(VarDeclStmt stmt, IDTable table) {
		if(stmt.varDecl.type.typeKind == TypeKind.VOID){
			Helpers.reportError("Void is not a valid variable type", stmt.varDecl.posn);
		}
		//stmt.varDecl.type.visit(this, table);
		Helpers.addDecl(table, stmt.varDecl);
		stmt.varDecl.visit(this, table);
		stmt.initExp.visit(this, table);
		stmt.varDecl.initialized = true;
		
		if(stmt.initExp instanceof RefExpr){ //varDecl expr is ref Expr
			RefExpr expr = (RefExpr) stmt.initExp;
			if(expr.ref.decl != null){
				//System.out.println(expr.ref.decl.posn.line);
				//System.out.println(stmt.varDecl.posn.line);
				if(expr.ref.decl.posn.line == stmt.varDecl.posn.line){
					Helpers.reportError("Cannot use variable in its own declaration ", expr.posn);
				}
			}
			if(classesFields.containsKey(expr.ref.id.spelling)){
				Helpers.reportError("Cannot assign a class name to a variable", expr.posn);
			}
			
		}else if(stmt.initExp instanceof BinaryExpr){ // varDecl expr is binexpr
			BinaryExpr expr = (BinaryExpr) stmt.initExp;
			if(expr.left instanceof RefExpr){
				RefExpr expr1 = (RefExpr) expr.left;
				if(expr1.ref.decl.posn.line == stmt.varDecl.posn.line){
					Helpers.reportError("Cannot use variable in its own declaration ", expr.posn);
				}
			}if(expr.right instanceof RefExpr){
				RefExpr expr1 = (RefExpr) expr.right;
				if(expr1.ref.decl.posn.line == stmt.varDecl.posn.line){
					Helpers.reportError("Cannot use variable in its own declaration ", expr.posn);
				}
			}if((expr.left instanceof BinaryExpr || expr.right instanceof BinaryExpr) || //the binexpr above is actually a complex expr
					(expr.left instanceof UnaryExpr || expr.right instanceof UnaryExpr)){
				if(expr.left instanceof BinaryExpr){
					BinaryExpr expr2 = (BinaryExpr) expr.left;
					if(expr2.left instanceof RefExpr){
						RefExpr expr1 = (RefExpr) expr2.left;
						if(expr1.ref.decl.posn.line == stmt.varDecl.posn.line){
							Helpers.reportError("Cannot use variable in its own declaration ", expr.posn);
						}
					}else if(expr2.right instanceof RefExpr){
						RefExpr expr1 = (RefExpr) expr2.right;
						if(expr1.ref.decl.posn.line == stmt.varDecl.posn.line){
							Helpers.reportError("Cannot use variable in its own declaration ", expr.posn);
						}
					}
				}if(expr.right instanceof BinaryExpr){
					BinaryExpr expr2 = (BinaryExpr) expr.right;
					if(expr2.left instanceof RefExpr){
						RefExpr expr1 = (RefExpr) expr2.left;
						if(expr1.ref.decl.posn.line == stmt.varDecl.posn.line){
							Helpers.reportError("Cannot use variable in its own declaration ", expr.posn);
						}
					}else if(expr2.right instanceof RefExpr){
						RefExpr expr1 = (RefExpr) expr2.right;
						if(expr1.ref.decl.posn.line == stmt.varDecl.posn.line){
							Helpers.reportError("Cannot use variable in its own declaration ", expr.posn);
						}
					}
				}if(expr.left instanceof UnaryExpr){
					UnaryExpr expr3 = (UnaryExpr) expr.left;
					if(expr3.expr instanceof RefExpr){
						RefExpr expr1 = (RefExpr) expr3.expr;
						if(expr1.ref.decl.posn.line == stmt.varDecl.posn.line){
							Helpers.reportError("Cannot use variable in its own declaration ", expr.posn);
						}
					}
				}if(expr.right instanceof UnaryExpr){
					UnaryExpr expr3 = (UnaryExpr) expr.right;
					if(expr3.expr instanceof RefExpr){
						RefExpr expr1 = (RefExpr) expr3.expr;
						if(expr1.ref.decl.posn.line == stmt.varDecl.posn.line){
							Helpers.reportError("Cannot use variable in its own declaration ", expr.posn);
						}
					}
				}
			}
		}else if(stmt.initExp instanceof UnaryExpr){
			UnaryExpr expr4 = (UnaryExpr) stmt.initExp;
			if(expr4.expr instanceof RefExpr){
				RefExpr expr1 = (RefExpr) expr4.expr;
				if(expr1.ref.decl.posn.line == stmt.varDecl.posn.line){
					Helpers.reportError("Cannot use variable in its own declaration ", expr4.posn);
				}
			}
			else if(expr4.expr instanceof BinaryExpr){
				BinaryExpr expr = (BinaryExpr) expr4.expr;
				if(expr.left instanceof RefExpr){
					RefExpr expr1 = (RefExpr) expr.left;
					if(expr1.ref.decl.posn.line == stmt.varDecl.posn.line){
						Helpers.reportError("Cannot use variable in its own declaration ", expr.posn);
					}
				}if(expr.right instanceof RefExpr){
					RefExpr expr1 = (RefExpr) expr.right;
					if(expr1.ref.decl.posn.line == stmt.varDecl.posn.line){
						Helpers.reportError("Cannot use variable in its own declaration ", expr.posn);
					}
				}if((expr.left instanceof BinaryExpr || expr.right instanceof BinaryExpr) ||
						(expr.left instanceof UnaryExpr || expr.right instanceof UnaryExpr)){
					if(expr.left instanceof BinaryExpr){
						BinaryExpr expr2 = (BinaryExpr) expr.left;
						if(expr2.left instanceof RefExpr){
							RefExpr expr1 = (RefExpr) expr2.left;
							if(expr1.ref.decl.posn.line == stmt.varDecl.posn.line){
								Helpers.reportError("Cannot use variable in its own declaration ", expr.posn);
							}
						}if(expr2.right instanceof RefExpr){
							RefExpr expr1 = (RefExpr) expr2.right;
							if(expr1.ref.decl.posn.line == stmt.varDecl.posn.line){
								Helpers.reportError("Cannot use variable in its own declaration ", expr.posn);
							}
						}
					}if(expr.right instanceof BinaryExpr){
						BinaryExpr expr2 = (BinaryExpr) expr.right;
						if(expr2.left instanceof RefExpr){
							RefExpr expr1 = (RefExpr) expr2.left;
							if(expr1.ref.decl.posn.line == stmt.varDecl.posn.line){
								Helpers.reportError("Cannot use variable in its own declaration ", expr.posn);
							}
						}if(expr2.right instanceof RefExpr){
							RefExpr expr1 = (RefExpr) expr2.right;
							if(expr1.ref.decl.posn.line == stmt.varDecl.posn.line){
								Helpers.reportError("Cannot use variable in its own declaration ", expr.posn);
							}
						}
					}if(expr.left instanceof UnaryExpr){
						UnaryExpr expr3 = (UnaryExpr) expr.left;
						if(expr3.expr instanceof RefExpr){
							RefExpr expr1 = (RefExpr) expr3.expr;
							if(expr1.ref.decl.posn.line == stmt.varDecl.posn.line){
								Helpers.reportError("Cannot use variable in its own declaration ", expr.posn);
							}
						}
					}if(expr.right instanceof UnaryExpr){
						UnaryExpr expr3 = (UnaryExpr) expr.right;
						if(expr3.expr instanceof RefExpr){
							RefExpr expr1 = (RefExpr) expr3.expr;
							if(expr1.ref.decl.posn.line == stmt.varDecl.posn.line){
								Helpers.reportError("Cannot use variable in its own declaration ", expr.posn);
							}
						}
					}
				}
			}if(expr4.expr instanceof UnaryExpr){
				UnaryExpr expr5 = (UnaryExpr) expr4.expr;
				if(expr5.expr instanceof RefExpr){
					RefExpr expr1 = (RefExpr) expr5.expr;
					if(expr1.ref.decl.posn.line == stmt.varDecl.posn.line){
						Helpers.reportError("Cannot use variable in its own declaration ", expr4.posn);
					}
				}
				else if(expr5.expr instanceof BinaryExpr){
					BinaryExpr expr = (BinaryExpr) expr5.expr;
					if(expr.left instanceof RefExpr){
						RefExpr expr1 = (RefExpr) expr.left;
						if(expr1.ref.decl.posn.line == stmt.varDecl.posn.line){
							Helpers.reportError("Cannot use variable in its own declaration ", expr.posn);
						}
					}if(expr.right instanceof RefExpr){
						RefExpr expr1 = (RefExpr) expr.right;
						if(expr1.ref.decl.posn.line == stmt.varDecl.posn.line){
							Helpers.reportError("Cannot use variable in its own declaration ", expr.posn);
						}
					}if((expr.left instanceof BinaryExpr || expr.right instanceof BinaryExpr) ||
							(expr.left instanceof UnaryExpr || expr.right instanceof UnaryExpr)){
						if(expr.left instanceof BinaryExpr){
							BinaryExpr expr2 = (BinaryExpr) expr.left;
							if(expr2.left instanceof RefExpr){
								RefExpr expr1 = (RefExpr) expr2.left;
								if(expr1.ref.decl.posn.line == stmt.varDecl.posn.line){
									Helpers.reportError("Cannot use variable in its own declaration ", expr.posn);
								}
							}if(expr2.right instanceof RefExpr){
								RefExpr expr1 = (RefExpr) expr2.right;
								if(expr1.ref.decl.posn.line == stmt.varDecl.posn.line){
									Helpers.reportError("Cannot use variable in its own declaration ", expr.posn);
								}
							}
						}if(expr.right instanceof BinaryExpr){
							BinaryExpr expr2 = (BinaryExpr) expr.right;
							if(expr2.left instanceof RefExpr){
								RefExpr expr1 = (RefExpr) expr2.left;
								if(expr1.ref.decl.posn.line == stmt.varDecl.posn.line){
									Helpers.reportError("Cannot use variable in its own declaration ", expr.posn);
								}
							}if(expr2.right instanceof RefExpr){
								RefExpr expr1 = (RefExpr) expr2.right;
								if(expr1.ref.decl.posn.line == stmt.varDecl.posn.line){
									Helpers.reportError("Cannot use variable in its own declaration ", expr.posn);
								}
							}
						}if(expr.left instanceof UnaryExpr){
							UnaryExpr expr3 = (UnaryExpr) expr.left;
							if(expr3.expr instanceof RefExpr){
								RefExpr expr1 = (RefExpr) expr3.expr;
								if(expr1.ref.decl.posn.line == stmt.varDecl.posn.line){
									Helpers.reportError("Cannot use variable in its own declaration ", expr.posn);
								}
							}
						}if(expr.right instanceof UnaryExpr){
							UnaryExpr expr3 = (UnaryExpr) expr.right;
							if(expr3.expr instanceof RefExpr){
								RefExpr expr1 = (RefExpr) expr3.expr;
								if(expr1.ref.decl.posn.line == stmt.varDecl.posn.line){
									Helpers.reportError("Cannot use variable in its own declaration ", expr.posn);
								}
							}
						}
					}
				}
				else if(expr5.expr instanceof UnaryExpr){
					UnaryExpr expr6 = (UnaryExpr) expr5.expr;
					if(expr6.expr instanceof RefExpr){
						RefExpr expr1 = (RefExpr) expr6.expr;
						if(expr1.ref.decl.posn.line == stmt.varDecl.posn.line){
							Helpers.reportError("Cannot use variable in its own declaration ", expr4.posn);
						}
					}
				}
			}
		}
		
		//if(stmt.varDecl.id == stmt.initExp)
		
		
		return null;
	}

	@Override
	public Void visitAssignStmt(AssignStmt stmt, IDTable table) {
		stmt.ref.visit(this, table);
		stmt.val.visit(this, table);
		if(stmt.ref instanceof ThisRef){
			Helpers.reportError("Cannot assign a value to nonqualified ThisRef", stmt.ref.posn);
		}
		
		if(stmt.ref instanceof QRef){
			if(((QRef)stmt.ref).ref.decl.type instanceof ArrayType && stmt.ref.id.spelling.equals("length")){
				Helpers.reportError("Array length is immutable", stmt.ref.posn);
			}
		}
		
		
		
		
		//System.out.println(stmt.ref.toString());
		
		
		return null;
	}

	@Override
	public Void visitCallStmt(CallStmt stmt, IDTable table) {
		stmt.methodRef.visit(this, table);
		for(Expression e : stmt.argList){
			e.visit(this, table);
		}
		return null;
	}

	@Override
	public Void visitReturnStmt(ReturnStmt stmt, IDTable table) {
		if(stmt.returnExpr == null){
			return null;
		}
		stmt.returnExpr.visit(this, table);
		return null;
	}

	@Override
	public Void visitIfStmt(IfStmt stmt, IDTable table) {
		stmt.cond.visit(this, table);
		if(stmt.thenStmt instanceof VarDeclStmt){
			Helpers.reportError("Variable declaration cannot be sole statement in branch", stmt.posn);
		}
		else{
			stmt.thenStmt.visit(this, table);
		}
		
		if(stmt.elseStmt != null){
			if(stmt.elseStmt instanceof VarDeclStmt){
				Helpers.reportError("Variable declaration cannot be sole statement in branch", stmt.posn);
			}
			else{
				stmt.elseStmt.visit(this, table);
			}
		}
		
		if(stmt.thenStmt instanceof BlockStmt){
			BlockStmt bs = (BlockStmt) stmt.thenStmt;
			if(bs.sl.size() == 1 && bs.sl.get(0) instanceof VarDeclStmt){
				Helpers.reportError("Variable declaration cannot be sole statement in branch", bs.sl.get(0).posn);
			}
		}
		if(stmt.elseStmt instanceof BlockStmt){
			BlockStmt bs = (BlockStmt) stmt.elseStmt;
			if(bs.sl.size() == 1 && bs.sl.get(0) instanceof VarDeclStmt){
				Helpers.reportError("Variable declaration cannot be sole statement in branch", bs.sl.get(0).posn);
			}
		}
		
		
		return null;
	}

	@Override
	public Void visitWhileStmt(WhileStmt stmt, IDTable table) {
		stmt.cond.visit(this, table);
		
		if(stmt.body instanceof VarDeclStmt){
			Helpers.reportError("Variable declaration cannot be sole statement in branch", stmt.posn);
		}
		else{
			stmt.body.visit(this, table);
		}
		return null;
	}

	@Override
	public Void visitUnaryExpr(UnaryExpr expr, IDTable table) {
		expr.operator.visit(this, table);
		expr.expr.visit(this, table);
		return null;
	}

	@Override
	public Void visitBinaryExpr(BinaryExpr expr, IDTable table) {
		expr.operator.visit(this, table);
		expr.left.visit(this, table);
		expr.right.visit(this, table);
		return null;
	}

	@Override
	public Void visitRefExpr(RefExpr expr, IDTable table) {
		expr.ref.visit(this, table);
		
		if(table.getScope(expr.ref.id.spelling) == -1 || table.getScope(expr.ref.id.spelling) == IDTable.CLASS_SCOPE){
			if(classesFields.containsKey(expr.ref.id.spelling)){
				Helpers.reportError("Cannot assign a class name to a variable", expr.posn);
			}
		}
		if(expr.ref.decl instanceof MethodDecl){
			Helpers.reportError("Identifier referenced is a method, not a value", expr.posn);
		}

		
		
		return null;
	}

	@Override
	public Void visitCallExpr(CallExpr expr, IDTable table) {
		expr.functionRef.visit(this, table);
		
		if(expr.functionRef.decl instanceof FieldDecl || expr.functionRef.decl instanceof VarDecl){
			Helpers.reportError("Identifier referenced in CallExpr is declared as a field or variable", expr.posn);
		}
		
		for(Expression e : expr.argList){
			e.visit(this, table);
		}
		
		return null;
	}

	@Override
	public Void visitLiteralExpr(LiteralExpr expr, IDTable table) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitNewObjectExpr(NewObjectExpr expr, IDTable table) {
		expr.classtype.visit(this, table);
		return null;
	}

	@Override
	public Void visitNewArrayExpr(NewArrayExpr expr, IDTable table) {
		expr.eltType.visit(this, table);
		expr.sizeExpr.visit(this, table);
		return null;
	}

	@Override
	public Void visitThisRef(ThisRef ref, IDTable table) {
		ref.decl = currclass;
		if(currmethod.isStatic){
			Helpers.reportError("Cannot use \"this\" keyword in static context", ref.posn);
		}
		return null;
	}

	@Override
	public Void visitIdRef(IdRef ref, IDTable table) {
		ref.decl = table.get(ref.id.spelling);
		int scope = table.getScope(ref.id.spelling);
		
		
		
		if(ref.decl instanceof FieldDecl){
			FieldDecl fd = (FieldDecl) ref.decl;
			if(!fd.isStatic && currmethod.isStatic){
				Helpers.reportError("Cannot make non-static reference in static context", ref.posn);
			}
		}
		
		if(scope == 0){
			return null;
		}
		else if(scope == -1){
			if(ref.decl == null){
				ref.decl = classesFields.get(currclass.id.spelling).get(ref.id.spelling);
			}
			if(ref.decl == null){
				Helpers.reportError("IdRef has no declaration", ref.posn);
			}
		}
		
		return null;
	}

	@Override
	public Void visitIxIdRef(IxIdRef ref, IDTable table) {
		ref.decl = table.get(ref.id.spelling);
		ref.indexExpr.visit(this, table);
		ref.id.visit(this, table);
		int scope = table.getScope(ref.id.spelling);
		if(scope < 1){
			Helpers.reportError("IdRef has no declaration", ref.posn);
		}
		return null;
	}

	@Override
	public Void visitQRef(QRef ref, IDTable table) {
		ref.ref.visit(this, table);
		if(ref.id.spelling.equals("length") && ref.ref.decl.type.typeKind == TypeKind.ARRAY){
			//do nothing
		}else{
			ref.id.visit(this, table);
		}
		Declaration controllingdecl = ref.ref.decl;
		Declaration decl = null;
		
		if(ref.ref instanceof ThisRef){
			if(currmethod.isStatic){
				//Helpers.reportError("Cannot use this in static context", ref.posn);
			}
			if(!(classesFields.get(currclass.name).containsKey(ref.id.spelling))){
				Helpers.reportError("Current class referenced by \"this\" keyword does not have member mentioned" , ref.posn);
			}
			//Helpers.exitIfError();
			
			decl = classesFields.get(currclass.name).get(ref.id.spelling);
			ref.decl = decl;
			return null;
		}
		//System.out.println(ref.ref.id.spelling + ", " + ref.id.spelling + ", " + ref.ref.decl); print for testing
		
		if(ref.ref instanceof IxIdRef){
			IxIdRef idref = (IxIdRef) ref.ref;
			ArrayType t = (ArrayType) idref.decl.type;
			if(t.eltType.typeKind == TypeKind.CLASS){
				controllingdecl = justClasses.get(t.eltType.spelling);				
			}
		}
		if(ref.ref instanceof IxQRef){
			IxQRef idref = (IxQRef) ref.ref;
			ArrayType t = (ArrayType) idref.decl.type;
			if(t.eltType.typeKind == TypeKind.CLASS){
				controllingdecl = justClasses.get(t.eltType.spelling);				
			}
		}
		
		
		
		if(controllingdecl instanceof ClassDecl || (controllingdecl instanceof VarDecl && controllingdecl.type.typeKind == TypeKind.CLASS)){
			if(controllingdecl instanceof VarDecl){
				if(table.getScope(controllingdecl.name) >= 2){
					controllingdecl = (VarDecl) controllingdecl;
					ClassType ct = (ClassType) controllingdecl.type;
					if(classesFields.containsKey(ct.className.spelling)){
						if(classesFields.get(ct.className.spelling).containsKey(ref.id.spelling)){
							decl = classesFields.get(ct.className.spelling).get(ref.id.spelling);
						}else{
							Helpers.reportError("Referenced instance does not have member " + ref.id.spelling, ref.posn);
						}
					}else{
						Helpers.reportError("Undefined class", ref.posn);
					}
				}
				else{
					Helpers.reportError("Qualified reference invalid scope. Parent of QRef is VarDecl but not a class member", ref.posn);
					Helpers.exitIfError();
				}
			}else{
				if(classesFields.containsKey(controllingdecl.name) && classesFields.get(controllingdecl.name).containsKey(ref.id.spelling)){
					decl = classesFields.get(controllingdecl.name).get(ref.id.spelling);
				}
				else{
					decl = table.get(ref.id.spelling);
				}
				
			}
		}else{
			if(controllingdecl instanceof MemberDecl){
				if(!(controllingdecl.type instanceof ClassType)){
					Helpers.reportError("Referenced object does not have a field " + ref.id.spelling, ref.posn);
					Helpers.exitIfError();
				}
				ClassType ct = (ClassType) controllingdecl.type;
				decl = classesFields.get(ct.className.spelling).get(ref.id.spelling);
				if(classesFields.get(ct.className.spelling).containsKey(ref.id.spelling)){
					decl = classesFields.get(ct.className.spelling).get(ref.id.spelling);
				}
				else if (table.getScope(ref.id.spelling) != -1){
					decl = table.get(ref.id.spelling);
				}
				else{
					if((ref.id.spelling.equals("println") || ref.id.spelling.equals("print"))
							&& ref.ref.decl == classesFields.get(IDTable.SYSTEM).get(IDTable.SYSTEM_OUT)){
						decl = classesFields.get(IDTable.PRINTSTREAM).get(ref.id.spelling);
						if(decl instanceof MethodDecl){
							if(((MethodDecl) decl).parameterDeclList.get(0).type.typeKind == TypeKind.INT){
								decl = IDTable.PRINTLN_DECL_INT;
							}else if(((MethodDecl) decl).parameterDeclList.get(0).type.typeKind == IDTable.STRING_DECL.type.typeKind){
								//decl = IDTable.PRINTLN_DECL_STR;
							}
						}
					}else{
					Helpers.reportError("Referenced instance does not have member " + ref.id.spelling, ref.posn);
					}
				}
			}
			Helpers.exitIfError();
		}
		
		if(ref.id.spelling.equals(IDTable.SYSTEM_OUT) && ref.ref.id.spelling.equals(IDTable.SYSTEM)){
			decl = classesFields.get(IDTable.SYSTEM).get(IDTable.SYSTEM_OUT);
		}
		
		
		if(ref.ref.decl.type.typeKind == TypeKind.ARRAY && ref.id.spelling.equals("length")){
			Declaration temp = table.get(ref.ref.id.spelling);
			if(((ArrayType)temp.type).lengthField.size() != 0){
				decl = ((ArrayType)temp.type).lengthField.get(0);
				ref.id.decl = decl;
			}
			else{
				decl = null;
			}
			ref.decl = decl;
			return null;
		}
		
		if(decl == null){
			
			Helpers.reportError("Reference has no declaration", ref.posn);
			Helpers.exitIfError();
		}
		
		ref.decl = decl;
		
		if(table.getScope(ref.ref.id.spelling) == IDTable.CLASS_SCOPE){
			MemberDecl member = (MemberDecl) classesFields.get(ref.ref.id.spelling).get(ref.id.spelling);
			if(ref.ref instanceof QualifiedRef){
				Helpers.reportError("Parent of QualifiedRef cannot be QualifiedRef in this context", ref.posn);
			}
			if(!member.isStatic){
				Helpers.reportError("Member being referenced must be static in this context", ref.posn);
			}
			if(!currclass.id.spelling.equals(ref.ref.id.spelling)){
				if(member.isPrivate){
					Helpers.reportError("Cannot access private member outside of class", ref.posn);
				}
			}
		}else if(table.getScope(ref.ref.id.spelling) > IDTable.CLASS_SCOPE && !(ref.ref.decl.type instanceof ArrayType)){
			ClassType ct = (ClassType) controllingdecl.type;
			MemberDecl member = (MemberDecl) classesFields.get(ct.className.spelling).get(ref.id.spelling);
			/*if(member.isStatic && !currmethod.isStatic){
				Helpers.reportError("Cannot make nonstatic reference to static member", ref.posn);
			}*/
			if(ref.ref.decl != currclass && !ref.ref.decl.type.spelling.equals(currclass.id.spelling)){
				if(member.isPrivate){
					Helpers.reportError("Cannot access private member outside of class", ref.posn);
				}
			}
		}else if(table.getScope(ref.ref.id.spelling) == IDTable.PREDEFINED_SCOPE ||
				decl == classesFields.get(IDTable.PRINTSTREAM).get(ref.id.spelling)){
			// do nothing
		}
		else{
			if(ref.ref.decl instanceof FieldDecl){
				FieldDecl fd = (FieldDecl) ref.ref.decl;
				if(ref.ref.decl != currclass && !ref.ref.decl.type.spelling.equals(currclass.id.spelling)){
					if(fd.isPrivate){
						Helpers.reportError("Cannot access private member outside of class", ref.posn);
					}
				}
			}
			else if(ref.ref.decl instanceof MethodDecl){
				Helpers.reportError("Cannot qualify a method call", ref.ref.posn);
			}
			else if(ref.ref.decl.type instanceof ArrayType){
				//do nothing
			}
			else{
				Helpers.reportError("Not a valid reference, or maybe it has not been initialized", ref.posn);
			}
		}
		
		Helpers.exitIfError();
		
		return null;
		
	}

	@Override
	public Void visitIxQRef(IxQRef ref, IDTable table) {
		ref.ixExpr.visit(this, table);
		if(ref.id.spelling.equals("length") && ref.ref.decl.type.typeKind == TypeKind.ARRAY){
			//do nothing
		}else{
			ref.id.visit(this, table);
		}
		ref.ref.visit(this, table);
		return null;
	}

	@Override
	public Void visitIdentifier(Identifier id, IDTable table) {
		id.decl = table.get(id.spelling);
		if(id.decl == null){
			Iterator<HashMap<String, Declaration>> iter = classesFields.values().iterator();
			
			while(iter.hasNext()){
				HashMap<String, Declaration> a = (HashMap<String, Declaration>) iter.next();
				if(a.containsKey(id.spelling)){
					id.decl = a.get(id.spelling);
				}
			}
		}
		return null;
	}

	@Override
	public Void visitOperator(Operator op, IDTable table) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitIntLiteral(IntLiteral num, IDTable table) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitBooleanLiteral(BooleanLiteral bool, IDTable table) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Void visitNullLiteral(NullLiteral nul, IDTable table){
		return null;
	}

	@Override
	public Void visitNullType(NullType type, IDTable arg) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
