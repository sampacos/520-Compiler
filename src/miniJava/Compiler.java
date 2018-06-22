package miniJava;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import miniJava.SyntacticAnalyzer.Parser;
import miniJava.SyntacticAnalyzer.ParsingErrorException;
import miniJava.AbstractSyntaxTrees.*;
import miniJava.ContextualAnalyzer.*;
import miniJava.CodeGenerator.*;
import mJAM.*;

public class Compiler {
	private static AST ast;
	private static IDTable table;
	private static boolean debug = true;
	
	
	
	
	public static void codeGenerateExecute(AST ast, String filename, MethodDecl main){
		String file = filename.substring(0, filename.lastIndexOf('.'));
		
		CodeGeneration codegenerator = new CodeGeneration();
		
		codegenerator.visitPackage((miniJava.AbstractSyntaxTrees.Package) ast, main);
		
		String codefilename = file + ".mJAM";
		ObjectFile of = new ObjectFile(codefilename);
		
		if (debug) {
			System.out.print("Writing object code file " + codefilename + " ... ");
		}
		if (of.write()) {
			if (debug)
				System.out.println("FAILED!");
			return;
		} else {
			if (debug)
				System.out.println("SUCCEEDED");
		}
/*
		String asmCodeFileName = codefilename.replace(".mJAM",".asm");
        System.out.print("Writing assembly file " + asmCodeFileName + " ... ");
        Disassembler d2 = new Disassembler(codefilename);
        if (d2.disassemble()) {
                System.out.println("FAILED!");
                return;
        }
        else
                System.out.println("SUCCEEDED");

/* 
 * run code using debugger
 * 

        System.out.println("Running code in debugger ... ");
        Interpreter.debug(codefilename, asmCodeFileName);

        System.out.println("*** mJAM execution completed");
*/

		
		
		if(debug){
			System.out.println("Writing assembly file");
			Disassembler d = new Disassembler(codefilename);
			if(d.disassemble()){
				System.out.println("Failure");
				return;
			}
			else{
				System.out.println("Success");
			}
			
			System.out.println("Running code...");
			Interpreter.interpret(codefilename);
			System.out.println("Execution complete.");
			
			
			
		
		}
		
	}
	
	
	
	
	
	public static void main(String[] args) {
		InputStream inputstream = null;
		String filename = null;
		try{
			filename = args[0];
			inputstream = new FileInputStream(filename);
		}catch(FileNotFoundException a){
			System.out.println("Input file " + args[0] + " not found");
			System.exit(1);
		}
		
		Parser parser = new Parser(inputstream);
		MethodDecl main = null;
		System.out.println("Syntactic and contextual analysis...");
		try {
			ast = parser.parseProgram();
		} catch (ParsingErrorException a) {
			System.out.println(a.getMessage());
			System.out.println("Invalid miniJava program");
			a.printStackTrace();
			System.exit(4);
		}
		
		try{
			Identification ident = new Identification();
			IDTable table = ident.makeTable(ast);
			//ASTDisplay display = new ASTDisplay();
			//display.showTree(ast);
			//table.display();
			if(Helpers.hasErrors()){
				System.out.println("Identification error: Contextual analysis halted.");
			}
			Helpers.exitIfError();
			
			TypeChecker checker = new TypeChecker(table);
			checker.visitPackage((miniJava.AbstractSyntaxTrees.Package) ast, null);
			main = checker.mainMethod;
			if(Helpers.hasErrors()){
				System.out.println("Type checking error: Contextual analysis completed with erorrs.");
			}
			Helpers.exitIfError();
		}
		catch(Exception e){
			e.printStackTrace();
			Helpers.exitIfError();
		}
		
		try{
			codeGenerateExecute(ast, filename, main);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		System.out.println("Valid miniJava program");
		//ASTDisplay display = new ASTDisplay();
		//display.showTree(ast);
		System.exit(0);
		
	}

}
