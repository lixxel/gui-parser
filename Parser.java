    import javax.swing.*;
    import java.awt.FlowLayout;
    import java.awt.GridLayout;
      
    /* 
         
    */  
      
    public class Parser {  
      
        private Lexer scanner;
        private int level;
        private JPanel[] panels = new JPanel[10];
        private String guiText;
        private JFrame guiFrame;
        private JPanel guiPanel;
        private ButtonGroup guiRadioGroup;
        private JRadioButton guiRadioButton;
        
      
      
    public Parser(Lexer scanner) {  
        this.scanner = scanner;  
        panels[0] = new JPanel();
    } // Parser  
      
      
    public void run ( ) {  
        next();  
        boolean complete = guiNonTerm( ); 
        if (complete) System.out.println("good");
        else System.out.println("Expected token " + scanner.getMatchTokenString( ) + ", got " + scanner.token.toString() + " at line " + scanner.getLineNo());
    } // run  
      
    private void next () {
    	scanner.getNextToken ();
    }
    
    private boolean guiNonTerm () {
    	//gui ::= Window STRING '(' NUMBER ',' NUMBER ')' layout widgets End '.'
    	int width;
    	if (scanner.match(Token.WINDOW)) {
    		level = 0;
    		guiFrame = new JFrame();
    		next();
    		if (scanner.match(Token.STRING)) {
    			guiFrame.setTitle(scanner.getLexeme());
    			next();
    			if (scanner.match(Token.LEFT_PAREN)) {
    				next();
    				if (scanner.match(Token.NUMBER)) {
    					width = scanner.getNumber();
    					next();
    					if (scanner.match(Token.COMMA)) {
    						next();
    						if (scanner.match(Token.NUMBER)) {
    							guiFrame.setSize(width, scanner.getNumber());;
    							next();
    							if (scanner.match(Token.RIGHT_PAREN)) {
    								next();
    								if (this.layoutNonTerm()) {
    									if (this.widgetsNonTerm()) {
    										if (scanner.match(Token.END)) {
    											next();
    											if (scanner.match(Token.PERIOD)) {
    												guiFrame.setVisible(true);
    												return true;
    											}
    										}
    									}
    								}
    							}
    						}
    					}
    				}
    			}
    		}
    	}
    	return false;
    }
    
    private boolean layoutNonTerm() {
    	//layout ::= Layout layout_type ':'
    	if (scanner.match(Token.LAYOUT)) {
    		next();
    		if (this.layoutTypeNonTerm()) {
    			if (scanner.match(Token.COLON)) {
    				next();
    				return true;
    			}
    		}
    	}
    	return false;
    }
    
    private boolean layoutTypeNonTerm() {
    	//layout_type ::=
    	//Flow |
    	//Grid '(' NUMBER ',' NUMBER [',' NUMBER ',' NUMBER] ')'
    	int rows, columns, horiGap, vertGap;
    	if (scanner.match(Token.FLOW)) {
    		if (level == 0)
    			guiFrame.setLayout(new FlowLayout());
    		else
    			panels[level].setLayout(new FlowLayout());
    		next();
    		return true;
    	}
    	else if (scanner.match(Token.GRID)) {
    		next();
    		if (scanner.match(Token.LEFT_PAREN)) {
    			next();
    			if (scanner.match(Token.NUMBER)) {
    				rows = scanner.getNumber();
    				next();
    				if (scanner.match(Token.COMMA)) {
    					next();
    					if (scanner.match(Token.NUMBER)) {
    						columns = scanner.getNumber();
    						next();
    						if (scanner.match(Token.RIGHT_PAREN)) {
    							if (level == 0)
    								guiFrame.setLayout(new GridLayout(rows,columns));
    							else
    								panels[level].setLayout(new GridLayout(rows,columns));
    							next();
    							return true;
    						}
    						else if (scanner.match(Token.COMMA)) {
    							next();
    							if (scanner.match(Token.NUMBER)) {
    								horiGap = scanner.getNumber();
    								next();
    								if (scanner.match(Token.COMMA)) {
    									next();
    									if (scanner.match(Token.NUMBER)) {
    										vertGap = scanner.getNumber();
    										next();
    										if (scanner.match(Token.RIGHT_PAREN)) {
    											if (level == 0)
    												guiFrame.setLayout(new GridLayout(rows, columns, horiGap, vertGap));
    											else
    												panels[level].setLayout(new GridLayout(rows, columns, horiGap, vertGap));
    											next();
    											return true;
    										}
    									}
    								}
    							}
    						}
    					}
    				}
    			}
    		}
    	}
    	return false;
    }
    
    private boolean widgetsNonTerm() {
    	//widgets ::=
    	//widget widgets |
    	//widget
    	if (this.widgetNonTerm()) {
    		if (this.widgetsNonTerm()) {
    			return true;
    		}
    		return true;
    	}
    	return false;
    }
    
    private boolean widgetNonTerm() {
    	//widget ::=
    	//Button STRING ';' |
    	//Group radio_buttons End ';' |
    	//Label STRING ';' |
    	//Panel layout widgets End ';' |
    	//Textfield NUMBER ';'
    	if (scanner.match(Token.BUTTON)) {
    		next();
    		if (scanner.match(Token.STRING)) {
    			guiText = scanner.getLexeme();
    			next();
    			if (scanner.match(Token.SEMICOLON)) {
    				if (level == 0)
    					guiFrame.add(new JButton(guiText));
    				else
    					panels[level].add(new JButton(guiText));
    				next();
    				return true;
    			}
    		}
    	}
    	else if (scanner.match(Token.GROUP)) {
    		guiRadioGroup = new ButtonGroup();
    		next();
    		if (radioButtonsNonTerm()) {
    			if (scanner.match(Token.END)) {
    				next();
    				if (scanner.match(Token.SEMICOLON)) {
    					next();
    					return true;
    				}
    			}
    		}
    	}
    	else if (scanner.match(Token.LABEL)) {
    		next();
    		if (scanner.match(Token.STRING)) {
    			guiText = scanner.getLexeme();
    			next();
    			if (scanner.match(Token.SEMICOLON)) {
    				
    				if (level == 0)
    					guiFrame.add(new JLabel(guiText));
    				else
    					panels[level].add(new JLabel(guiText));
    				next();
    				return true;
    			}
    		}
    	}
    	else if (scanner.match(Token.PANEL)) {
    		level++;
    		guiPanel=new JPanel();
    		panels[level]=guiPanel;
    		next();
    		if (layoutNonTerm()) {
    			if (widgetsNonTerm()) {
    				if (scanner.match(Token.END)) {
    					next();
    					if (scanner.match(Token.SEMICOLON)) {
    						if (level==1) {
    							guiFrame.add(panels[level]);
    						}
    						else {
    							panels[level-1].add(panels[level]);
    						}
    						panels[level]=null;
    						level--; 
    						next();
    						return true;
    					}
    				}
    			}
    		}
    	}
    	else if (scanner.match(Token.TEXTFIELD)) {
    		next();
    		if (scanner.match(Token.NUMBER)) {
    			int length = scanner.getNumber();
    			next();
    			if (scanner.match(Token.SEMICOLON)) {
    				if (level == 0)
    					guiFrame.add(new JTextField(length));
    				else
    					panels[level].add(new JTextField(length));
    				next(); 
    				return true;
    			}
    		}
    	}
    	return false;
    }
    
    private boolean radioButtonsNonTerm() {
    	//radio_buttons ::=
    	//radio_button radio_buttons |
    	//radio_button
    	if (radioButtonNonTerm()) {
    		if (radioButtonsNonTerm()) {
    			return true;
    		}
    		return true;
    	}
    	return false;
    }
    
    private boolean radioButtonNonTerm() {
    	//radio_button ::=
    	//Radio STRING ';'
    	if (scanner.match(Token.RADIO)) {
    		next();
    		if (scanner.match(Token.STRING)) {
    			guiText = scanner.getLexeme();
    			next();
    			if (scanner.match(Token.SEMICOLON)) {
    				guiRadioButton = new JRadioButton(guiText);
    				guiRadioGroup.add(guiRadioButton);
    				if (level == 0)
    					guiFrame.add(guiRadioButton);
    				else {
    					panels[level].add(guiRadioButton);
    				}
    				next();
    				return true;
    			}
    		}
    	}
    	return false;
    }
    
    } // class Parser  