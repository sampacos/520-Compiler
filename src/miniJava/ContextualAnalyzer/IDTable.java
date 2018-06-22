package miniJava.ContextualAnalyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import miniJava.AbstractSyntaxTrees.*;
import miniJava.SyntacticAnalyzer.*;

public class IDTable {
	public static final int PREDEFINED_SCOPE = 0, CLASS_SCOPE = 1, MEMBER_SCOPE = 2, PARAMETER_SCOPE = 3,
			LOCAL_SCOPE = 4, INVALID_SCOPE = -1;
	
	public static final String PRINTSTREAM = "_PrintStream";
	public static final String PRINTSTREAM_PRINT = "print";
	public static final String PRINTSTREAM_PRINTLN = "println";
	public static final String SYSTEM = "System";
	public static final String SYSTEM_OUT = "out";
	
	public static final ClassDecl STRING_DECL = new ClassDecl(new Identifier(new Token("String")), new FieldDeclList(),
			new MethodDeclList(), null);
	
	public static MethodDecl PRINTLN_DECL_INT;// PRINTLN_DECL_STR;
	
	
	List<HashMap<String, Declaration>> scopetable = new ArrayList<HashMap<String, Declaration>>();
	
	public IDTable(){
		openScope();
		
		MethodDeclList printStreamMethods = new MethodDeclList();
		ParameterDeclList params = new ParameterDeclList();
		params.add(new ParameterDecl(new BaseType(TypeKind.INT, "int", null), new Identifier(new Token("int")), null));
		
		MemberDecl print = new FieldDecl(false, false, new BaseType(TypeKind.VOID, "void", null), 
				new Identifier(new Token(PRINTSTREAM_PRINT)), null);
		print = new MethodDecl(print, params, null, null, null);
		printStreamMethods.add((MethodDecl) print);
		
		MemberDecl println = new FieldDecl(false, false, new BaseType(TypeKind.VOID, "void", null), 
				new Identifier(new Token(PRINTSTREAM_PRINTLN)), null);
		PRINTLN_DECL_INT = new MethodDecl(println, params, null, null, null);
		printStreamMethods.add(PRINTLN_DECL_INT);
		
		//ParameterDeclList paramsStr = new ParameterDeclList();
		//paramsStr.add(new ParameterDecl(STRING_DECL.type, new Identifier(new Token("str")), null));
		//PRINTLN_DECL_STR = new MethodDecl(println, paramsStr, null, null, null);
		//printStreamMethods.add(PRINTLN_DECL_STR);
		
		
		
		ClassDecl printStreamDecl = new ClassDecl(new Identifier(new Token(PRINTSTREAM)), 
				new FieldDeclList(), printStreamMethods, null);
		
		try{
			set(printStreamDecl);
		}catch (ParsingErrorException e){
			// parsing should've already passed at this point, so this won't occur
			System.out.println("what shouldn't happen did");
		}
		
		Token printStreamToken = new Token(PRINTSTREAM);
		FieldDeclList systemFields = new FieldDeclList();
		
		FieldDecl out = new FieldDecl(false, true, new ClassType(new Identifier(printStreamToken), null),
				new Identifier(new Token(SYSTEM_OUT)), null);
		systemFields.add(out);
		
		ClassDecl systemDecl = new ClassDecl(new Identifier(new Token(SYSTEM)), systemFields, new MethodDeclList(), null);
		
		try{
			set(systemDecl);
			set(STRING_DECL);
		}catch(ParsingErrorException e){
			//again, this should not happen, but we'll catch anyways
			System.out.println("what shouldn't happen did, 2");
		}
		
		
		openScope(); //class scope
		
		
		if(this.get(PRINTSTREAM) == null){
			System.out.println("ewwww");
		}
		
	}
	
	
	
	/**
	 * Returns the declaration of an ID
	 * @param name: id whose decl will be fetched
	 * @return declaration of the id
	 */
	
	public Declaration get(String name){
		Declaration decl = null;
		for(int i = scopetable.size() - 1; i >= 0; i--){
			decl = scopetable.get(i).get(name);
			if(decl != null){
				break;
			}
		}
		return decl;
	}
	
	
	/**
	 * gets the scope level of an id
	 * -1 if not found
	 * @param name of id
	 * @return scope
	 */
	
	public int getScope(String name){
		for(int i = scopetable.size() - 1; i >= 0; i--){
			if(scopetable.get(i).containsKey(name)){
				return i;
			}
		}
		return INVALID_SCOPE;
	}
	
	
	public void set(Declaration declaration) throws ParsingErrorException{
		String name = declaration.name;
		
		
		//checking deeper scopes for duplicate declaration
		for(int i = PARAMETER_SCOPE; i < scopetable.size(); i++){
			Declaration declar = scopetable.get(i).get(name);
			if(declar != null){
				throw new ParsingErrorException("Duplicate declaration: " + name + " was already declared at ");// + declar.posn);
			}
		}
		
		//checking current scope for duplicate
		HashMap<String, Declaration> scope = scopetable.get(scopetable.size() - 1);
		Declaration declar = scope.get(name);
		if(declar != null){
			throw new ParsingErrorException("Duplicate declaration: " + name + " was already declared at ");// + declar.posn);
		}
		
		scope.put(name, declaration); //not previously declared, so putting the id in current scoped table
		declaration.id.decl = declaration;
		
	}
	
	
	public void openScope(){
		scopetable.add(new HashMap<String, Declaration>());
	}
	
	public void closeScope(){
		if (scopetable.size() <= 1){
			throw new RuntimeException("IDTable nonexistent. closeScope() called too many times");
		}
		scopetable.remove(scopetable.size() - 1);
	}
	
	
	public int linkDecl(Identifier id){
		Declaration decl;
		for(int i = scopetable.size() - 1; i >= 0; i--){
			decl = scopetable.get(i).get(id.spelling);
			if(decl != null){
				id.decl = decl;
				if(i >= LOCAL_SCOPE){
					return LOCAL_SCOPE;
				}
				return i;
			}
			
		}
		return INVALID_SCOPE;
	}
	
	
	public HashMap<String, Declaration> getClasses(){
		if(scopetable.size() - 1 >= IDTable.CLASS_SCOPE){
			return scopetable.get(IDTable.CLASS_SCOPE);
		}
		return null;
	}
	
	
	public HashMap<String, Declaration> getClassMembers(){
		if(scopetable.size() - 1 >= IDTable.MEMBER_SCOPE){
			return scopetable.get(IDTable.MEMBER_SCOPE);
		}
		return null;
	}
	
	
	public void display(){
		Iterator<HashMap<String, Declaration>> iter = scopetable.iterator();
		String indent = "";
		
		while(iter.hasNext()){
			HashMap<String, Declaration> scope = iter.next();
			
			Iterator<String> iter2 = scope.keySet().iterator();
			while (iter2.hasNext()){
				String id = iter2.next();
				System.out.println(indent + "\"" + id + "\": " + scope.get(id) + " " + scope.get(id).posn);
			}
			indent += "  ";
		}
	}
	
	
}
