import exceptions.parser.ParserExceptionHandler;
import interpreter.Interpreter;
import lexer.Scanner;
import lexer.Token;
import parser.Parser;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {

	private static boolean isValidFormat(String arg) {
		return arg.matches(".*\\.smth$");
		
	}
	
	private static void runFile() throws IOException {
		byte[] bytes = Files.readAllBytes(Paths.get("src/file.smth"));
		run(new String(bytes, Charset.defaultCharset()));
	}
		

	private static void run(String source) {
		Scanner scanner = new Scanner(source);
		List<Token> tokens = scanner.scanTokens();
		ParserExceptionHandler parserExceptionHandler = new ParserExceptionHandler();
		Parser parser = new Parser(tokens, parserExceptionHandler);
	}

	public static void main(String [] args) throws IOException {
		if (args.length < 1) {
			System.out.println("No arguments passed in, program will now exit");
			return;
		}
		else if(isValidFormat(args[0])) runFile();
	}
}
