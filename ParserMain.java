import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;  
  
public class ParserMain {  
  
public static void main(String args[]) {  
    Parser parser;
	try {
		parser = new Parser(new Lexer(  		
		new BufferedReader(new FileReader("test.txt"))));
		parser.run();
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	}
} // main  
      
} // class Test 