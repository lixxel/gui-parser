    import java.io.*;  
    public class Lexer {  
    	
    private BufferedReader in;
    private int i = 0;
    private char ch = ' ';   
    private int intValue = 0;  
    public Token token,matchToken;  
    private String lexeme = "";
    private String line = "";
    private int lineNo = 0;
      
    public Lexer (BufferedReader in) {  
        this.in = in;  
        token = Token.SEMICOLON;  
        matchToken = Token.SEMICOLON; 
    } // Lexer  
      
    public void getNextToken ( ) { 
        lexeme = "";
        while (ch !=0 && Character.isWhitespace(ch))
        	ch = getChar( );
        if (ch == 0) {
        	token = Token.EOF;
        }
        while (Character.isLetter(ch)) {  
        lexeme += ch;  
        ch = getChar( ); 
        }
        if (lexeme.equals("Window")) token = Token.WINDOW;
        else if (lexeme.equals("End")) token = Token.END;
        else if (lexeme.equals("Layout")) token = Token.LAYOUT;
        else if (lexeme.equals("Flow")) token = Token.FLOW;
        else if (lexeme.equals("Grid")) token = Token.GRID;
        else if (lexeme.equals("Button")) token = Token.BUTTON;
        else if (lexeme.equals("Group")) token = Token.GROUP;
        else if (lexeme.equals("Label")) token = Token.LABEL;
        else if (lexeme.equals("Panel")) token = Token.PANEL;
        else if (lexeme.equals("Textfield")) token = Token.TEXTFIELD;
        else if (lexeme.equals("Radio")) token = Token.RADIO;
         
        else if (Character.isDigit(ch)) {  
        intValue = number( );  
        token = Token.NUMBER;  
        }  
        else if (ch == '"') {
        	ch = getChar( );
        	token = Token.STRING;
        	while (ch != '"'){
        		lexeme += ch;
        		ch = getChar( );
        	}
        	ch = getChar();
        }
        else {  
        switch (ch) {  
            case ',' : ch = getChar( );  
            token = Token.COMMA;  
            break;  
      
            case ':' : ch = getChar( );  
            token = Token.COLON;  
            break;  
      
            case ';' : ch = getChar( );  
            token = Token.SEMICOLON;  
            break;  
      
            case '.' : ch = getChar( );  
            token = Token.PERIOD;  
            break;  
      
            case '(' : ch = getChar( );  
            token = Token.LEFT_PAREN;  
            break;  
      
            case ')' : ch = getChar( );  
            token = Token.RIGHT_PAREN;  
            break;  
            
      
            //default : error ("Illegal character " + ch );  
        } // switch  
        } // if   
    } // getNextToken  
      
      
    public int getNumber ( ) {  
        return intValue;  
    }   
    public String getLexeme () {
    	return lexeme;
    }
    public int getLineNo() {
    	return lineNo;
    }
      
      
    private int number ( ) {  
        int rslt = 0;  
        do {  
        rslt = rslt * 10 + Character.digit(ch, 10);  
        ch = getChar( );  
        } while (Character.isDigit(ch));  
        return rslt;  
    } // number  
    
    public boolean match (Token t) {
    	matchToken = t;
    	return token==t;
    }
    public String getMatchTokenString() {
    	return matchToken.toString();
    }
    
    public char getChar ( ) {  
    	try
        {
            if (line == null)
                return 0;
            if (i == line.length())
            {
            	lineNo++;
            	line = in.readLine();
                i = 0;
                return '\n';
            }
            return line.charAt(i++);
        }
        catch (IOException exception)
        {
            return 0;
        }
    } // getChar
      
    } // Lexer  
       