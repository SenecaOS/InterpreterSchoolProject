import interpreter.Interpreter;
import lexer.Scanner;
import lexer.Token;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {
	private static final Interpreter interpreter = new Interpreter();

	private static boolean isValidFormat(String arg) {
		return arg.matches(".*\\.smth$");
		
	}
	
	private static void runFile() throws IOException {
		byte[] bytes = Files.readAllBytes(Paths.get("src/main/java/large-file.smth"));
		run(new String(bytes, Charset.defaultCharset()));


	}
		

	
	
	private static void run(String source) {
		Scanner scanner = new Scanner(source);
		List<Token> tokens = scanner.scanTokens();
		/*
		ParserExceptionHandler parserExceptionHandler = new ParserExceptionHandler();
		Parser parser = new Parser(tokens, parserExceptionHandler);
		List<Statement> statements = parser.parse();
		if (parserExceptionHandler.hasExceptions()) {
			System.out.println(parserExceptionHandler);
			return;
		}
		parser = new Parser(tokens);
		Expr expression = parser.parseTest();
		interpreter.interpret(expression);*/
	}

	public static void main(String [] args) throws IOException {
		if (args.length < 1) {
			System.out.println("No arguments passed in, program will now exit");
			return;
		}
		/*if (isValidFormat(args[0])) {
			runFile(args[0]);
		}*/
		//runFile("src/main/java/test.smth");
		Double test = Double.MAX_VALUE;
		System.out.println(test);
		runFile();
	}
}
