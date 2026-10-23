package lexer;

import exceptions.lexer.LexerExceptionHandler;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LexerErrorHandlerTest {
    @Test
    void test_GathersInvalidNums(){
        //Från EC Sifferinmatning som slutar på ‘.’
        LexerExceptionHandler exceptionHandler = new LexerExceptionHandler();

        Scanner scanner = new Scanner("25.", exceptionHandler);
           try {
               scanner.scanTokens();
           }
           catch(RuntimeException e){
            assertEquals("Error at line 1 with token type: VALUE\n", exceptionHandler.printExceptions());
        }

        //Från EC Värde med ogiltig symbol
        exceptionHandler = new LexerExceptionHandler();

        scanner = new Scanner("25A", exceptionHandler);

        try {
            scanner.scanTokens();
        }
        catch(RuntimeException e){
            assertEquals("Error at line 1 with token type: VALUE\n", exceptionHandler.printExceptions());
        }


    }

}
