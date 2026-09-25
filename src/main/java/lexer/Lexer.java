package lexer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class Lexer {

	private Collection<String> tokensList;
	private static final Set<String> KEYWORDS = Set.of("let", "for", "while", "do");
	
	public Lexer(String tokens) {

		this.tokensList = Arrays.stream(tokens.split(" "))
           .filter(a -> !KEYWORDS.contains(a))
           .collect(Collectors.toCollection(ArrayList::new));		
	}
	
	
	public Collection<String> getTokensList() {
		return Collections.unmodifiableCollection(this.tokensList);
	}
	
	
}
