package miniJava.SyntacticAnalyzer;

import java.io.InputStream;
import java.util.Stack;

import miniJava.AbstractSyntaxTrees.*;

public class Parser {
	private Scanner scanner;
	private Token currtoken = null;
	private boolean isVoid;
	
	public Parser(InputStream input){
		scanner = new Scanner(input);
	}
	
	
	/**
	 * Takes the next token, if there isn't anything there, or its not valid,
	 * the scanner will throw an error, and a SyntaxErrorException is thrown
	 */
	private void takeIt() throws ParsingErrorException{
		try{
			currtoken = scanner.scanNextToken();
		}catch(ScanningErrorException a){
			throw new ParsingErrorException("Parser: " + a.getMessage());
		}
	}
	
	private void expect(TokenKind kind) throws ParsingErrorException{
		if(currtoken.kind != kind){
			if(currtoken.tokenstring != null){
				throw new ParsingErrorException("I expected" + kind.tokenstring + " but found: ", currtoken);
			}else{
				throw new ParsingErrorException("I expected" + kind.tokenstring + " but did not find it");
			}
		}
		takeIt();
	}
	
	/**
	 * starts the parsing of a program. calls the parsing of a ClassDeclaration
	 * until EOT is found
	 * this method is public so Compiler.java can call it, but the rest of the
	 * parsing methods are private
	 */
	public miniJava.AbstractSyntaxTrees.Package parseProgram() throws ParsingErrorException{
		takeIt();
		SourcePosition posn = currtoken.position;
		ClassDeclList classlist = new ClassDeclList();
		while(currtoken.kind != TokenKind.EOT){
			classlist.add(parseClassDeclaration());
		}
		return new miniJava.AbstractSyntaxTrees.Package(classlist, posn);
	}
	
	private ClassDecl parseClassDeclaration() throws ParsingErrorException{
		SourcePosition classposn = currtoken.position;
		expect(TokenKind.CLASS);
		Identifier classid = new Identifier(currtoken);
		expect(TokenKind.ID);
		expect(TokenKind.LBRACE);
		FieldDeclList fieldList = new FieldDeclList();
		MethodDeclList methodList = new MethodDeclList();
		/*
		 * Below I'm combining the two types of declarations (partially), which both start with the
		 * same thing (Visibility Access), because this is a choice point and combining simplifies.
		 * Parsing the DeclarationStart then delegates to parse the Type, if there is one. If it's void, then
		 * we know it's a method, otherwise we have to keep looking 
		 */
		while(currtoken.kind != TokenKind.RBRACE){
			MemberDecl memberDecl = parseDeclarationStart();
			expect(TokenKind.ID);
			
			if(currtoken.kind == TokenKind.LPAREN){ //method decl
				takeIt();
				ParameterDeclList params = new ParameterDeclList();
				if(currtoken.kind != TokenKind.RPAREN){
					params = parseParameterList();
				}
				expect(TokenKind.RPAREN);
				
				expect(TokenKind.LBRACE);
				StatementList methodStatements = new StatementList();
		
				while(currtoken.kind != TokenKind.RBRACE){
					methodStatements.add(parseStatement());
					}
				expect(TokenKind.RBRACE);
				MethodDecl method = new MethodDecl(memberDecl, params, methodStatements, null, memberDecl.posn);
				methodList.add(method);
				isVoid = false;
			}
			else{
				if(isVoid){
					isVoid = false;
					throw new ParsingErrorException(currtoken);
				}
				FieldDecl field = new FieldDecl(memberDecl, memberDecl.posn);
				fieldList.add(field);
				expect(TokenKind.SEMICOLON);
			}
			
			
		}
		expect(TokenKind.RBRACE); // end of class
		return new ClassDecl(classid, fieldList, methodList, classposn);
	}
	
	
	
	private FieldDecl parseDeclarationStart() throws ParsingErrorException{
		boolean isPrivate = false;
		boolean isStatic = false;
		SourcePosition declposn = currtoken.position;
		
		if(currtoken.kind == TokenKind.PRIVATE || (currtoken.kind == TokenKind.PUBLIC)){
			isPrivate = (currtoken.kind == TokenKind.PRIVATE);
			takeIt();
		}
		if(currtoken.kind == TokenKind.STATIC){
			isStatic = true;
			takeIt();
		}
		TypeDenoter memberType = parseType();
		Identifier memberId = new Identifier(currtoken);
		return new FieldDecl(isPrivate, isStatic, memberType, memberId, declposn);
	}
	
	
	/**
	 * parses Type for field decl., (Type | void) for method decl.
	 */
	private TypeDenoter parseType() throws ParsingErrorException{
		TypeDenoter type;
	
		switch(currtoken.kind){
		case ID:
		case INT:
			SourcePosition posn = currtoken.position;
			switch(currtoken.kind){
			case ID:
				Identifier id = new Identifier(currtoken);
				type = new ClassType(id, currtoken.position);
				break;
				
			case INT:
				type = new BaseType(TypeKind.INT, "int", currtoken.position);
				break;
			
			default:
				throw new ParsingErrorException(currtoken);
			}
		takeIt();
		if(currtoken.kind == TokenKind.LBRACKET){ //array
			takeIt();
			expect(TokenKind.RBRACKET);
			type = new ArrayType(type, posn);
		}
		break;
		
		case BOOLEAN:
		case VOID:
			switch(currtoken.kind){
			case BOOLEAN:
				type = new BaseType(TypeKind.BOOLEAN, "boolean", currtoken.position);
				break;
			
			case VOID:
				type = new BaseType(TypeKind.VOID, "void", currtoken.position);
				isVoid = true;
				break;
			
			default:
				throw new ParsingErrorException(currtoken);
			}
		takeIt();
		break;
		
		default:
			throw new ParsingErrorException("parseType Error: Expected a Type, found a", currtoken);
		}
		return type;
	}
	
	
	private ParameterDeclList parseParameterList() throws ParsingErrorException{
		ParameterDeclList paramList = new ParameterDeclList();
		isVoid = false;
		TypeDenoter type = parseType();
		if(isVoid){
			throw new ParsingErrorException(currtoken);
		}
		isVoid = false;
		Identifier id = new Identifier(currtoken);
		paramList.add(new ParameterDecl(type, id, type.posn));
		
		expect(TokenKind.ID);
		while(currtoken.kind == TokenKind.COMMA){
			takeIt();
			isVoid = false;
			type = parseType();
			if(isVoid){
				throw new ParsingErrorException(currtoken);
			}
			isVoid = false;
			id = new Identifier(currtoken);
			paramList.add(new ParameterDecl(type, id, type.posn));
			expect(TokenKind.ID);
		}
		return paramList;
	}
	
	
	
	/**
	 * parses Reference ::= (id | this) SubReference
	 */
	private Reference parseReference() throws ParsingErrorException{
		Reference ref;
		
		if(currtoken.kind == TokenKind.ID || currtoken.kind == TokenKind.THIS){
			if(currtoken.kind == TokenKind.ID){
				Identifier refid = new Identifier(currtoken);
				ref = new IdRef(refid, refid.posn);
				takeIt();
				if(currtoken.kind == TokenKind.LBRACKET){
					takeIt();
					Expression a = parseExpression();
					IxIdRef ixidref = new IxIdRef(refid, a, refid.posn);
					expect(TokenKind.RBRACKET);
					ref = ixidref;
				}
				
				while (currtoken.kind == TokenKind.DOT){
					takeIt();
					Identifier refid2 = new Identifier(currtoken);
					expect(TokenKind.ID);
					QualifiedRef qref = new QRef(ref, refid2, refid2.posn);
					if(currtoken.kind == TokenKind.LBRACKET){
						takeIt();
						Expression a = parseExpression();
						//IxQRef ixqref = new IxQRef(ref, refid2, a, refid2.posn); // trying out the new IxQRef
						IxQRef ixqref = new IxQRef(qref, a, refid2.posn);
						expect(TokenKind.RBRACKET);
						qref = ixqref;
					}
					ref = qref;
				}
			}
			else{
				ref = new ThisRef(currtoken.position);
				takeIt();
				while(currtoken.kind == TokenKind.DOT){
					takeIt();
					Identifier refid3 = new Identifier(currtoken);
					expect(TokenKind.ID);
					QualifiedRef qref = new QRef(ref, refid3, refid3.posn);
					if(currtoken.kind == TokenKind.LBRACKET){
						takeIt();
						Expression a = parseExpression();
						//IxQRef ixqref = new IxQRef(ref, refid3, a, refid3.posn);
						IxQRef ixqref = new IxQRef(qref, a, refid3.posn);
						expect(TokenKind.RBRACKET);
						qref = ixqref;
					}
					ref = qref;
				}
			}
		
		}
		else{
			throw new ParsingErrorException(currtoken);
		}
		
		return ref;
		
	}
	
	private ExprList parseArgumentList() throws ParsingErrorException{
		ExprList exprList = new ExprList();
		Expression expr;
		expr = parseExpression();
		exprList.add(expr);
		while(currtoken.kind == TokenKind.COMMA){
			takeIt();
			expr = parseExpression();
			exprList.add(expr);
		}
		return exprList;
	}
	
	/**
	 * Parses Statments
	 * Statement ::= 
	 * 				{Statement*}
	 *				| Type id = Expression;
	 *				| Reference ([Expression])? = Expression;
	 *				| Reference (ArgumentList?);
	 *				| if (Expression) Statement (else Statement)?
	 *				| while (Expression) Statement
	 */
	private Statement parseStatement() throws ParsingErrorException{
		Statement stmt;
		SourcePosition stmtposn = currtoken.position;
		
		switch(currtoken.kind){
		case LBRACE:
			takeIt();
			StatementList stmtList = new StatementList();
			while(currtoken.kind != TokenKind.RBRACE){
				stmtList.add(parseStatement());
			}
			expect(TokenKind.RBRACE);
			stmt = new BlockStmt(stmtList, stmtposn);
			break;
		
		case INT:
			TypeDenoter vartype1 = new BaseType(TypeKind.INT, "int", currtoken.position);
			takeIt();
			if(currtoken.kind == TokenKind.LBRACKET){ //int array decl
				takeIt();
				vartype1 = new ArrayType(vartype1, vartype1.posn);
				expect(TokenKind.RBRACKET);
			}
			Identifier varid1 = new Identifier(currtoken);
			expect(TokenKind.ID);
			expect(TokenKind.EQUAL);
			Expression varExpr1 = parseExpression();
			expect(TokenKind.SEMICOLON);
			VarDecl vardecl1 = new VarDecl(vartype1, varid1, stmtposn);
			stmt = new VarDeclStmt(vardecl1, varExpr1, stmtposn);
			break;	
		
		case BOOLEAN:
		//case VOID:
			takeIt();
			TypeDenoter vartype2 = new BaseType(TypeKind.BOOLEAN, "boolean", currtoken.position);
			Identifier varid2 =  new Identifier(currtoken);
			expect(TokenKind.ID);
			expect(TokenKind.EQUAL);
			Expression varExpr2 = parseExpression();
			expect(TokenKind.SEMICOLON);
			VarDecl vardecl2 = new VarDecl(vartype2, varid2, stmtposn);
			stmt = new VarDeclStmt(vardecl2, varExpr2, stmtposn);
			break;
			
		case ID:
			Identifier id1 = new Identifier(currtoken);
			Reference idRef1 = new IdRef(id1, id1.posn);
			takeIt();
			switch(currtoken.kind){
			case EQUAL: //id = expr
				takeIt();
				Expression idexpr1 = parseExpression();
				expect(TokenKind.SEMICOLON);
				stmt = new AssignStmt(idRef1, idexpr1, stmtposn);
				break;
			
			case LBRACKET:
				takeIt();
				if(currtoken.kind == TokenKind.RBRACKET){ //id[] id = expr
					TypeDenoter idtype = new ClassType(id1, id1.posn);
					TypeDenoter idArray = new ArrayType(idtype, idtype.posn);
					takeIt();
					Identifier id2 = new Identifier(currtoken);
					expect(TokenKind.ID);
					VarDecl idvardecl = new VarDecl(idArray, id2, stmtposn);
					expect(TokenKind.EQUAL);
					Expression idexpr2 = parseExpression();
					expect(TokenKind.SEMICOLON);
					stmt = new VarDeclStmt(idvardecl, idexpr2, stmtposn);
				}else{ //id[expr] = expr
					Expression idexpr3 = parseExpression();
					expect(TokenKind.RBRACKET);
					IxIdRef idRef2 = new IxIdRef(id1, idexpr3, id1.posn);
					Reference qref = idRef2;
					while (currtoken.kind == TokenKind.DOT){
						takeIt();
						Identifier refid2 = new Identifier(currtoken);
						expect(TokenKind.ID);
						if(currtoken.kind == TokenKind.LBRACKET){
							takeIt();
							Expression a = parseExpression();
							//qref = new IxQRef(qref, refid2, a, refid2.posn);
							QRef qref1 = new QRef(qref, refid2, refid2.posn);
							qref = new IxQRef(qref1, a, refid2.posn);
							expect(TokenKind.RBRACKET);
						}
						else{
							qref = new QRef(qref, refid2, refid2.posn);
						}
					}
					if(currtoken.kind == TokenKind.LPAREN){
						takeIt();
						ExprList idExprList = new ExprList();
						if(currtoken.kind != TokenKind.RPAREN){
							idExprList = parseArgumentList();
						}
						expect(TokenKind.RPAREN);
						expect(TokenKind.SEMICOLON);
						stmt = new CallStmt(qref, idExprList, stmtposn);
					}
					else{expect(TokenKind.EQUAL);
					Expression idexpr2 = parseExpression();
					expect(TokenKind.SEMICOLON);
					stmt = new AssignStmt(qref, idexpr2, stmtposn);
					}
				}
				break;
			
				
			case LPAREN: // id(ArgumentList?);
				takeIt();
				ExprList idExprList = new ExprList();
				if(currtoken.kind != TokenKind.RPAREN){
					idExprList = parseArgumentList();
				}
				expect(TokenKind.RPAREN);
				expect(TokenKind.SEMICOLON);
				stmt = new CallStmt(idRef1, idExprList, stmtposn);
				break;
				
			
			case ID: // id id = expr
				Identifier id2 = new Identifier(currtoken);
				TypeDenoter id1class = new ClassType(id1, id1.posn);
				Expression idExpr;
				takeIt();
				expect(TokenKind.EQUAL);
				idExpr = parseExpression();
				expect(TokenKind.SEMICOLON);
				VarDecl idVarDecl = new VarDecl(id1class, id2, stmtposn);
				stmt = new VarDeclStmt(idVarDecl, idExpr, stmtposn);
				break;
				
			case DOT:
				Reference qref = idRef1;
				Expression idRefExpr1;
				while (currtoken.kind == TokenKind.DOT){
					takeIt();
					Identifier refid2 = new Identifier(currtoken);
					expect(TokenKind.ID);
					qref = new QRef(qref, refid2, refid2.posn);
					if(currtoken.kind == TokenKind.LBRACKET){
						takeIt();
						Expression a = parseExpression();
						//IxQRef ixqref = new IxQRef(qref, refid2, a, refid2.posn);
						IxQRef ixqref = new IxQRef((QRef) qref, a, refid2.posn);
						expect(TokenKind.RBRACKET);
						qref = ixqref;
					}
				}
				
				switch(currtoken.kind){
				case EQUAL: // id(.id)* = expr
					takeIt();
					idRefExpr1 = parseExpression();
					expect(TokenKind.SEMICOLON);
					stmt = new AssignStmt(qref, idRefExpr1, stmtposn);
					break;
					
				case LPAREN: // id(.id)*(ArgumentList?);
					takeIt();
					ExprList idRefExprList = new ExprList();
					if(currtoken.kind != TokenKind.RPAREN){
						idRefExprList = parseArgumentList();
					}
					expect(TokenKind.RPAREN);
					expect(TokenKind.SEMICOLON);
					stmt = new CallStmt(qref, idRefExprList, stmtposn);
					break;
				
				default:
					throw new ParsingErrorException(currtoken);
			
				}
				break;
				
			default:
				throw new ParsingErrorException(currtoken);
				
			}
			break;
		
			
			
		case THIS:
			Reference thisref = parseReference();
			
			switch(currtoken.kind){
			case EQUAL:
				takeIt();
				Expression thisexpr1 = parseExpression();
				expect(TokenKind.SEMICOLON);
				stmt = new AssignStmt(thisref, thisexpr1, stmtposn);
				break;
				
			case LPAREN:
				takeIt();
				ExprList thisExprList = new ExprList();
				if(currtoken.kind != TokenKind.RPAREN){
					thisExprList = parseArgumentList();
				}
				expect(TokenKind.RPAREN);
				expect(TokenKind.SEMICOLON);
				stmt = new CallStmt(thisref, thisExprList, stmtposn);
				break;
				
			default:
				throw new ParsingErrorException(currtoken);
			}
			break;
			
		case IF:
			takeIt();
			expect(TokenKind.LPAREN);
			Expression ifExpr = parseExpression();
			expect(TokenKind.RPAREN);
			Statement ifblock = parseStatement();
			if(currtoken.kind == TokenKind.ELSE){
				takeIt();
				stmt = new IfStmt(ifExpr, ifblock, parseStatement(), stmtposn);
			}
			else{
				stmt = new IfStmt(ifExpr, ifblock, stmtposn);
			}
			break;
			
		case WHILE:
			takeIt();
			expect(TokenKind.LPAREN);
			Expression whileExpr = parseExpression();
			expect(TokenKind.RPAREN);
			stmt = new WhileStmt(whileExpr, parseStatement(), stmtposn);
			break;
			
		case RETURN:
			takeIt();
			if(currtoken.kind == TokenKind.SEMICOLON){
				takeIt();
				stmt = new ReturnStmt(null, stmtposn);
			}
			else{
				Expression retExpr = parseExpression();
				stmt = new ReturnStmt(retExpr, stmtposn);
				expect(TokenKind.SEMICOLON);
			}
			break;
			
		default:
			throw new ParsingErrorException(currtoken);
			
		}
		return stmt;
	}
	
	private Expression parseExpression() throws ParsingErrorException{
		/*if(currtoken.kind == TokenKind.NULL){
			NullLiteral t = new NullLiteral(currtoken);
			Expression nullexpr = new LiteralExpr(t, t.posn);
			takeIt();
			return nullexpr;
		}*/
		Expression expr = parseConjunction();
		while(currtoken.kind == TokenKind.BARX2){
			Operator oper = new Operator(currtoken);
			takeIt();
			expr = new BinaryExpr(oper, expr, parseConjunction(), currtoken.position);
		}
		return expr;
	}
	
	private Expression parseConjunction() throws ParsingErrorException{
		Expression expr = parseEquality();
		while(currtoken.kind == TokenKind.AMPERSANDX2){
			Operator oper = new Operator(currtoken);
			takeIt();
			expr = new BinaryExpr(oper, expr, parseEquality(), currtoken.position);
		}
		return expr;
	}
	
	
	private Expression parseEquality() throws ParsingErrorException{
		Expression expr = parseRelational();
		while(currtoken.kind == TokenKind.BOOLEQUAL || currtoken.kind == TokenKind.NOT_EQUAL){
			Operator oper = new Operator(currtoken);
			takeIt();
			expr = new BinaryExpr(oper, expr, parseRelational(), currtoken.position);
		}
		return expr;
	}
	
	private Expression parseRelational() throws ParsingErrorException{
		Expression expr = parseAdditive();
		while(currtoken.kind == TokenKind.LESSEQUAL || currtoken.kind == TokenKind.LESSTHAN
				|| currtoken.kind == TokenKind.GREATEREQUAL || currtoken.kind == TokenKind.GREATERTHAN){
			Operator oper = new Operator(currtoken);
			takeIt();
			expr = new BinaryExpr(oper, expr, parseAdditive(), currtoken.position);
		}
		return expr;
	}
	
	
	private Expression parseAdditive() throws ParsingErrorException{
		Expression expr = parseMultiplicative();
		while(currtoken.kind == TokenKind.PLUS || currtoken.kind == TokenKind.MINUS){
			Operator oper = new Operator(currtoken);
			takeIt();
			expr = new BinaryExpr(oper, expr, parseMultiplicative(), currtoken.position);
		}
		return expr;
	}
	
	
	private Expression parseMultiplicative() throws ParsingErrorException{
		Expression expr = parseUnary();
		while(currtoken.kind == TokenKind.ASTERISK || currtoken.kind == TokenKind.SLASH){
			Operator oper = new Operator(currtoken);
			takeIt();
			expr = new BinaryExpr(oper, expr, parseUnary(), currtoken.position);
		}
		return expr;
	}
	
	private Expression parseUnary() throws ParsingErrorException{
		Stack<Operator> exclams = new Stack<Operator>();
		while(currtoken.kind == TokenKind.EXCLAM || currtoken.kind == TokenKind.MINUS){
			exclams.push(new Operator(currtoken));
			takeIt();
		}
		Expression expr = parseTerm();
		while(!exclams.empty()){
			Operator op = exclams.pop();
			expr = new UnaryExpr(op, expr, op.posn);
		}
		return expr;
	}
	
	private Expression parseTerm() throws ParsingErrorException{
		Expression expr = null;
		SourcePosition exprposn = currtoken.position;
		
		switch(currtoken.kind){
		
		case NULL:
			NullLiteral t = new NullLiteral(currtoken);
			expr = new LiteralExpr(t, t.posn);
			takeIt();
			break;
		
		case MINUSMINUS:
			throw new ParsingErrorException(currtoken);
		
		case NUMBER:
			Terminal numlit = new IntLiteral(currtoken);
			expr = new LiteralExpr(numlit, exprposn);
			takeIt();
			break;
		case TRUE:
		case FALSE:
			Terminal bool = new BooleanLiteral(currtoken);
			expr = new LiteralExpr(bool, exprposn);
			takeIt();
			break;
		
		case LPAREN:
			takeIt();
			expr = parseExpression();
			expect(TokenKind.RPAREN);
			break;
			
		case THIS:
		case ID:
			Reference refexpr = parseReference();
			
			if(currtoken.kind == TokenKind.LPAREN){
				takeIt();
				ExprList args = new ExprList();
				if(currtoken.kind != TokenKind.RPAREN){
					args = parseArgumentList();
				}
				expect(TokenKind.RPAREN);
				expr = new CallExpr(refexpr, args, exprposn);
			}
			else{
				expr = new RefExpr(refexpr, exprposn);
			}
			break;
		case NEW:
			takeIt();
			TypeDenoter type;
			Expression arrayExpr;
			switch(currtoken.kind){
			case INT: // new int[expr]
				type = new BaseType(TypeKind.INT, "int", currtoken.position);
				takeIt();
				expect(TokenKind.LBRACKET);
				arrayExpr = parseExpression();
				expect(TokenKind.RBRACKET);
				expr = new NewArrayExpr(type, arrayExpr, exprposn);
				break;
				
			case ID:
				Identifier newid = new Identifier(currtoken);
				type = new ClassType(newid, currtoken.position);
				takeIt();
				switch(currtoken.kind){
				case LPAREN: // new id()
					takeIt();
					expect(TokenKind.RPAREN);
					expr = new NewObjectExpr((ClassType) type, exprposn);
					break;
					
				case LBRACKET: //new id[expr]
					takeIt();
					arrayExpr = parseExpression();
					expect(TokenKind.RBRACKET);
					expr = new NewArrayExpr(type, arrayExpr, exprposn);
					break;
					
				default:
					throw new ParsingErrorException(currtoken);

				}
				break;
				
			default:
				throw new ParsingErrorException(currtoken);
			}
			break;
			
		default:
			throw new ParsingErrorException(currtoken);
		}
		return expr;
			
	}
	
}
