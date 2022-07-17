package main.java.assignment2;

import edu.utexas.cs.sam.io.SamTokenizer;
import edu.utexas.cs.sam.io.Tokenizer;
import edu.utexas.cs.sam.io.Tokenizer.TokenType;

import java.io.IOException;

public class LiveOak2Compiler
{
	public static void main(String[] args) throws IOException {
		String fileName = args[0];
		String pgm = compiler(fileName);
		// write out the pgm to the args[1] outfile (or throw except if compiler fails)
	}

	public static String compiler(String fileName)
	{
		//returns SaM code for program in file
		try 
		{
			SamTokenizer f = new SamTokenizer(fileName, SamTokenizer.TokenizerOptions.PROCESS_STRINGS);
			String pgm = getProgram(f);
			return pgm;
		} 
		catch (Exception e) 
		{
			System.out.println("Fatal error: could not compile program");
			return "STOP\n";
		}
	}

	static String getProgram(SamTokenizer f)
	{
		try
		{
			String pgm="";
			while(f.peekAtKind()!=TokenType.EOF)
			{
				// this will be called in LO-2 with method declaration
				// for example, each program in LO-2 starts with main method
				pgm+= getMethod(f);
			}
			return pgm;
		}
		catch(Exception e)
		{
			System.out.println("Fatal error: could not compile program");
			return "STOP\n";
		}		
	}
	static String getMethod(SamTokenizer f)
	{
		//TODO: add code to convert a method declaration to SaM code.
		//TODO: add appropriate exception handlers to generate useful error msgs.
		f.check("int"); //must match at begining
		String methodName = f.getString();
		f.check ("("); // must be an opening parenthesis
		String formals = getFormals(f);
		f.check(")");  // must be an closing parenthesis
		//You would need to read in formals if any
		//And then have calls to getDeclarations and getStatements.
		return null;
	}

	static String getExp(SamTokenizer f) 
	{
			  switch (f.peekAtKind()) {
				 case INTEGER: //E -> integer
					return "PUSHIMM " + f.getInt() + "\n";
				 case OPERATOR:  
				 {
					 // operator could be typical math operators or paren
					 return getOperator(f);
				 }
				 default:   return "ERROR\n";
			  }
	}

	static String getFormals(SamTokenizer f){
			return null;
	}

	static String getOperator(SamTokenizer f) {
		char op = f.getOp();
		if (op == '+' || op == '-' || op == '*' || op == '%' || op == '/') {
			f.check(op); // consume op token
			return String.valueOf(op);
		} else {
			// TODO: handle the invalid operator error
			System.out.println("Invalid operation");
			return null;
		}
	}
}
