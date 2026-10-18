import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import lexer.Scanner;
import lexer.Token;
import parser.Parser;
import parser.Statement;
import parser.exceptions.ExceptionHandler;

public class Main {

	
	
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
		ExceptionHandler  exceptionHandler = new ExceptionHandler();
		Parser parser = new Parser(tokens, exceptionHandler);
		List<Statement> statements = parser.parse();
		if (exceptionHandler.hasExceptions()) {
			System.out.println(exceptionHandler);
			return;
		}
	}

	public static void main(String [] args) throws IOException {
		if(args.length < 1) {
			System.out.println("No arguments passed in, program will now exit");
			return;
		}
		
		if(isValidFormat(args[0])) {
			runFile(args[0]);	
		}
		
		//runFile("src/main/java/test.smth");
		//runFile("src/main/java/large-file.smth");
	}
	
	
}
