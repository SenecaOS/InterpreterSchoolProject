
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import interpreter.Interpreter;
import lexer.Scanner;
import lexer.Token;
import parser.Expr;
import parser.Parser;
import parser.Statement;
import exceptions.parser.ParserExceptionHandler;

public class Main {
	private static final Interpreter interpreter = new Interpreter();
	private static boolean hadError;


	private static boolean isValidFormat(String arg) {
		return arg.matches(".*\\.smth$");
		
	}
	
	private static void runFile(String path) throws IOException {
		byte[] bytes = Files.readAllBytes(Paths.get(path));
		run(new String(bytes, Charset.defaultCharset()));


	}
		

	
	
	private static void run(String source) {
		Scanner scanner = new Scanner(source);
		List<Token> tokens = scanner.scanTokens();
		for(Token token : tokens) {
			System.out.println(token);
		}
		ParserExceptionHandler parserExceptionHandler = new ParserExceptionHandler();
		Parser parser = new Parser(tokens, parserExceptionHandler);
		List<Statement> statements = parser.parse();
		if (parserExceptionHandler.hasExceptions()) {
			System.out.println(parserExceptionHandler);
			return;
		}
		parser = new Parser(tokens);
		Expr expression = parser.parseTest();
		interpreter.interpret(expression);
	}

	public static void main(String [] args) throws IOException {
		if (args.length < 1) {
			System.out.println("No arguments passed in, program will now exit");
			return;
		}
		if (isValidFormat(args[0])) {
			runFile(args[0]);
		}
		//runFile("src/main/java/test.smth");
		//runFile("src/main/java/large-file.smth");
	}
}
