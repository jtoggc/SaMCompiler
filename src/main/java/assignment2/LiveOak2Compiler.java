package main.java.assignment2;

import edu.utexas.cs.sam.io.SamTokenizer;
import edu.utexas.cs.sam.io.Tokenizer;
import edu.utexas.cs.sam.io.Tokenizer.TokenType;

import java.io.IOException;
import java.util.HashMap;

public class LiveOak2Compiler
{
	public static void main(String[] args) throws IOException {
		String fileName = args[0];
		String pgm = compiler(fileName);
		//TODO: write out the pgm to the args[1] outfile (or throw except if compiler fails)
	}

	// setting up the global symbol table for LO-0 and LO-1
	// needs to be handled per method in LO-2
	public static HashMap<String, Integer> symbolTable = new HashMap<String, Integer>();
	public static HashMap<String, String> symbolTypeTable = new HashMap<String, String>();
	public static int symbolCount = 100;
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
				//pgm+= getMethod(f);
				pgm += getExp(f); // this is for LO-0 and LO-1
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
		 case CHARACTER:
		 {
			 return getChar(f);
		 }
		 case WORD:
		 {
			 // the beginning of variable assignment
			 return getIdentifier(f);
		 }
		 default:   return "ERROR\n";
	  }
	}

	//this will be used to grab params to methods in LO-2
	static String getFormals(SamTokenizer f){
			return null;
	}

	static String getIdentifier(SamTokenizer f) {
		// TODO: validate the word for proper identifier formatting
		String type = f.getWord(); // Need type error checking for LO-0
		f.skipToken(); // consume type id
		if (f.peekAtKind() == TokenType.WORD) // this is the case of identifier with no init
		{
			String id = f.getWord();
			// add to the symbol table as a variable or check it exists
			if (!symbolTable.containsKey(id)) {
				int tmpCnt = symbolCount += 1;
				symbolTable.put(id, tmpCnt);
				symbolTypeTable.put(id, type);
			}
		}
			return null;
	}

	static String getChar(SamTokenizer f) {
		char ch = f.getCharacter();
		switch (ch) {
			case '(':
			case '{':
				f.check(ch); // this should consume paren
				if (f.peekAtKind() == TokenType.OPERATOR) {
					// this is the unop case, need to get op
					return getOperator(f);
				}
				return getExp(f);
			case ')':
				f.check(ch); // consume ending paren
				return "";
			case '}':
				f.check(ch);
				return "STOP"; // should be ending with } brace
			case '?':
				f.check(ch);
				return getTernaryOp(f);
			default: return "ERROR\n";
		}
	}

	static String getAssignment(SamTokenizer f) {
		return null;
	}

	static String getTernaryOp(SamTokenizer f) {
		return null;
	}

	static String getOperator(SamTokenizer f) {
		char op = f.getOp();
		switch (op) {
			case '+':
				f.check(op); // consume
				 // get next expression to add
				return getExp(f) + "ADD\n";

			case '-':
				f.check(op); // consume
				// get next expression to add
				return getExp(f) + "SUB\n";

			case '*':
				f.check(op); // consume
				// get next expression to add
				return getExp(f) + "TIMES\n";

			case '/':
				f.check(op); // consume
				// get next expression to add
				return getExp(f) + "DIV\n";

			case '%':
				f.check(op); // consume
				// get next expression to add
				return getExp(f) + "MOD\n";

			case '=':
				f.check(op); // consume
				// TODO: handle var assignment
				return getAssignment(f);

			case '!':
				f.check(op); //consume
				return getExp(f) + "NOT\n"; // TODO: this might be bad way to handle NOT operator

			case '~':
				f.check(op);
				return getExp(f); // TODO: handle string reversal
			default: return "ERROR\n";
		}
	}
}
