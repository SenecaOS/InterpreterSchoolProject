package lexer;


//DONT RUN AS IT WILL RESET THE TokenTest CLASS

import java.io.FileNotFoundException;
import java.io.PrintWriter;


public class TokenTestBoiler {

    private static final String[] testInputs = {
            "loop",
            "if",
            "variable",
            "123",
            "+",
            "-",
            "(",
            ")",
            "[",
            "]",
            "{",
            "}",
            "*",
            ";",
            "/",
            "",
            "\"hello\"",
            "!",
            "!=",
            "|",
            "&",
            "<",
            ">",
            ">=",
            "<=",
            "smth",
            "true",
            "=",
            "==",
            "else",
            "return",
            "noth",
            "false",
            ",",
            "print",
            "function",
            "interrupt"
    }; //Here I used generative AI specifically to create this array so I wont have to do it by hand to make generating the boiler much easier

    public static void main(String[] args) throws FileNotFoundException {
        int index = 0;
        PrintWriter writer = new PrintWriter("src/test/java/lexer/TokenTest.java");

        writer.println("package lexer;");
        writer.println("import static org.junit.jupiter.api.Assertions.assertEquals;");
        writer.println("import static org.junit.jupiter.api.Assertions.assertThrows;");
        writer.println("import org.junit.jupiter.api.Test;\n\n");
        writer.println("public class TokenTest{;\n\n");
        writer.println(" @Test");
        writer.println("void testReturnsCorrectTokenType() {");
        for(TokenType t : TokenType.values()){
            writer.println("scanner = new Scanner(\"" + testInputs[index] + "\");");
            writer.println("scanner.scanTokens();");
            writer.println( "type = TokenType." + t + ";");
            writer.println("assertEquals(type, scanner.getTokens().get(0).getTokenType());");
            writer.println("assertEquals(\"" + testInputs[index++] +"\", scanner.getTokens().get(0).getVal());\n\n");
        }
        writer.println("\t}");
        writer.println("}");
        writer.close();
    }
}
