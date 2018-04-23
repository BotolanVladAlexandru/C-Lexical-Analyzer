import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DFA {
	private List <Integer> states;
	private List <Integer> finalStates;
	private List <Transition> transitions;
	private List< Token> tokens;
	
	private char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
	private char[] digits = "123456789".toCharArray();
	private char[] delimiters =",.;(){}[]\\: \n?".toCharArray();
	private char[] simpleOperators = "-+<>&|*/%&^~!=".toCharArray();
	private char[] doubleOperators = "-+<>&|".toCharArray();
	private char[] hexLetters = "abcdef".toCharArray();
	private String outPath;
	
	private int row,column;
	
	private PrintWriter writer;
	

	private int currentState;
	
	public DFA() {
		outPath = "C:\\Users\\Dev01\\eclipse-workspace\\LexicalAnalyzer\\src\\output.txt";
		row=1;
		column = 0;
		states = new ArrayList <Integer>();
		finalStates = new ArrayList <Integer>();
		transitions = new ArrayList <Transition> ();
		tokens = new ArrayList<Token>();
		
		try {
			writer = new PrintWriter(outPath,"UTF-8");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		states.add(0); // Start
		states.add(1); // Identifier
		states.add(2); // Constant
		states.add(3); // Comments
		states.add(31);
		states.add(32);
		states.add(22); // Number that starts with 0 (hex)
		states.add(23); // 0x
		states.add(25); // 0x123..
		states.add(24); // Floats
		states.add(26); // 0.2f
		states.add(27);
		states.add(28);
		states.add(4); // Operators
		states.add(4000); // += , -=, &=
		states.add(6); // Delimiters
		states.add(7); // Strings
		states.add(70);
		
		generateLiteralTransition(0, 1); // From 0 to Identifier
		generateTransition(0,'_',1);
		generateLiteralTransition(1, 1); // Identifier with Literal
		generateTransition(1,'_',1);
		generateDigitTransition(1, 1); // Identifier with Digit

		
		/*generateTransition(0, (char)13, 6);
		generateTransition(0, (char)10, 6);*/
		
		
		//generateDelimiterTransition(6, 6);
		
		generateSpaceTransition(0,5); // Space
		
		generateSimpleOperatorsTransition();
		generateDoubleOperatorsTransition();
		
		
		generateDelimiterTransition(0,6); // Delimiters

		
		generateDigitTransition(0,2); // From 0 to Integer
		generateDigitTransition(2,2); // Integer
		generateTransition(2,'0',2); //  Integer
		
		generateTransition(0,'0',22); // 0 
		generateTransition(22,'x',23); //0x
		generateHexTransition(23,23);
		generateDigitTransition(23, 25); //0x1
		generateTransition(23, '0', 25);
		generateHexTransition(25,25);
		generateDigitTransition(25, 25); //0x12
		generateTransition(25, '0',25);  //0x10
		
		

		// Floats
		generateTransition(2,'.',24); //1.
		generateDigitTransition(24, 24); // 1.241241
		generateTransition(24, '0', 24); // 1.0
		generateTransition(22,'.',24); // 0.23121
		
		generateTransition(22, 'f', 26); //0f
		generateTransition(2,'f',26);//1f
		generateTransition(24,'f',26);//1.2f
		
		generateTransition(22, 'e', 26); //0f
		generateTransition(2,'e',26);//1f
		generateTransition(24,'e',26);//1.2f
		
		generateTransition(22, 'g', 26); //0f
		generateTransition(2,'g',26);//1f
		generateTransition(24,'g',26);//1.2f
		
		generateTransition(22, 'd', 26); //0f
		generateTransition(2,'d',26);//1f
		generateTransition(24,'d',26);//1.2f
		generateTransition(26,'-',2);
		generateDigitTransition(26, 28);
		

		//1e5
		generateHexTransition(23,27);//0xe
		generateHexTransition(23,25); //0xe
		generateHexTransition(25,25); //0x1ee
		generateHexTransition(25,27); //0x11241e
		generateHexTransition(27,27); //0x1eeee
		generateDigitTransition(27, 27);
		generateHexTransition(2,27); //1.12f
		generateTransition(27,'+',28);
		generateTransition(27,'-',28);
		
		//escape chars
		
		generateTransition(0,(char)39,2);
		generateTransition(2,'\\',200);
		generateTransition(2,'\\',201);
		generateTransition(200, 'a', 201);
		generateTransition(200, 'b', 201);
		generateTransition(200, 'n', 201);
		generateTransition(200, 'r', 201);
		generateTransition(200, 't', 201);
		generateTransition(200, 'v', 201);
		generateTransition(201, (char)39, 202);
		
		
		//Comments
		
		// /* ... */
		//generateTransition(48 , '/', 31);
		generateTransition(48,'*',32);
		generateDigitTransition(32, 32);
		generateLiteralTransition(32, 32);
		generateDelimiterTransition(32, 32);
		addCharsTransition(32, 32);
		removeTransition(32,'\\', 32);
		//removeTransition(32,'/', 32); //?
		removeTransition(32,'*',32);
		generateTransition(32, '"', 32);
		generateTransition(32,'*',333);
		generateLiteralTransition(333,333);
		generateDigitTransition(333,333);
		generateDelimiterTransition(333, 333);
		generateTransition(333,'/',34);
		generateTransition(333,'*',333);
		generateLiteralTransition(33,32);
		generateDelimiterTransition(33, 32);
		generateDigitTransition(33, 32);
		generateTransition(33,'*',32);
		generateTransition(33,'/',34);
		
		// single line comments
		generateTransition(48,'/',35);
		generateLiteralTransition(35,35);
		generateDigitTransition(35,35);
		generateDelimiterTransition(35,35);
		addCharsTransition(35, 35);
		generateTransition(35,'"',35);
		generateTransition(35,'\n',36);
		removeTransition(35,'\n',35);
		
		generateDigitTransition(28, 28);
		
		
		//Strings
		
		generateTransition(0, '"', 7);
		generateLiteralTransition(7, 7);
		generateDigitTransition(7, 7);
		generateTransition(7, '0', 7);
		addCharsTransition(7, 7);
		generateDelimiterTransition(7, 7);
		removeTransition(7, '\n', 7);
		generateTransition(7, '"', 70);
		
		//chars
		removeTransition(0, (char)39, 2);
		generateTransition(0,(char)39,77); // ' 
		generateLiteralTransition(77, 78); // 'a
		generateDigitTransition(77, 78); //'1
		generateTransition(77, '"', 78);
		addCharsTransition(77, 78); // '#
		generateTransition(78,(char)39,70); // 'a'
		
		
		generateTransition(403,'=',4000); // <<=
		generateTransition(404,'=',4000); // >>=
		
		finalStates.add(1);
		finalStates.add(2);
		finalStates.add(4);
		finalStates.add(6);
		finalStates.add(22);
		finalStates.add(24);
		finalStates.add(25);
		finalStates.add(26);
		finalStates.add(27);
		finalStates.add(28);
		finalStates.add(34);
		finalStates.add(36);
		finalStates.add(70);
		finalStates.add(4000);
		finalStates.add(202);
		
		
		currentState = 0;
	}
	
	private void generateHexTransition(int state, int nextState) {
		for(char letter : hexLetters)
		transitions.add(new Transition(state, letter, nextState));
		
	}
	
	private void removeTransition(int state, char symbol, int nextState) {
		transitions.remove(new Transition(state, symbol, nextState));
	}

	private void generateDoubleOperatorsTransition() {
		int count = 0;
		for(char operator : doubleOperators){
			int newState = 400 + (++count);
			int currentState = 40 + (count);
			states.add(newState);
			finalStates.add(newState);
			transitions.add(new Transition(currentState,operator,newState));
		}
		
	}

	private void generateTransition(int state, char symbol, int nextState){
		transitions.add(new Transition(state, symbol, nextState));
	}
		
	private void generateSimpleOperatorsTransition() {
		int count = 0;
		for(char operator : simpleOperators){
			int newState = 40 + (++count);
			states.add(newState);
			
			finalStates.add(newState);
			transitions.add(new Transition(0,operator,newState));
			if(operator == '-'){
				generateDigitTransition(newState, 2);
				generateDigitTransition(newState, 22);
				transitions.add(new Transition(newState,'0',22));
				transitions.add(new Transition(newState,'0',2));
			}
			transitions.add(new Transition(newState,'=',4000));
		}
	}
	
	private void generateOperatorTransitions(int state, int nextState){
		for(char operator : simpleOperators)
			transitions.add(new Transition(state,operator,nextState));
	}
		
	
	private void generateDelimiterTransition(int state, int nextState) {
		for(char delimiter : delimiters)
			transitions.add(new Transition(state,delimiter,nextState));
	}

	private void generateSpaceTransition(int state, int nextState) {
		transitions.add(new Transition(state, ' ', nextState));
		
	}

	@Override
	public String toString() {
		return "DFA [transitions=" + transitions + "]";
	}
	
	public void generateLiteralTransition(int state, int nextState){
		for(int i = 0 ; i < alphabet.length ; ++i) {
			char symbol = alphabet[i];
			transitions.add(new Transition(state, symbol, nextState));
			transitions.add(new Transition(state ,Character.toUpperCase(symbol),nextState));
		}
	}
	
	public void generateDigitTransition(int state, int nextState){
		for(int i = 0 ; i < digits.length ; ++i) {
			char symbol = digits[i];
			transitions.add(new Transition(state, symbol, nextState));
		}
	}

	public void generateTransition(char symbol, int nextState) {
		Transition transition = new Transition(currentState,symbol,nextState);
		if(!transitions.contains(transition))
			transitions.add(transition);
	}
	
	public void addCharsTransition(int state, int nextState){
		for(int i = 33 ; i < 96 ; ++i){
			if(i != 34){
			char symbol = (char) i;
		transitions.add(new Transition(state, symbol, nextState));
			}
		}
	}
	
	public void analyze(String content){
		String token = "";
		for(int i = 0 ; i < content.length() ; ++i){
			++column;
			char symbol = content.charAt(i);
			if(symbol == '\n') {
				row++;
				column = 0;
			}
			boolean ok = false;
			System.out.println("Current state: "+currentState+" Symbol: '"+symbol+"'"+" "+(int)symbol);
			for(Transition transition : transitions)
				if(transition.accepts(currentState, symbol)){
					ok = true;
					token += Character.toString(symbol);
					System.out.println("\tOK "+currentState +" -> "+transition.getNexState());
					currentState = transition.getNexState();
					break;
				}
			if(!ok){
				if(column > 0)
					--column;
						if(!isDelimiter(token))
						if(isFinal(currentState)){
							char tokenType = (currentState+"").charAt(0);
							int index;
							Token t = Token.getToken(token,tokenType);
							if( (index = tokens.indexOf(t)) == -1 ) {
								tokens.add(t);
								writer.println(t + " (" + tokens.size() +")\n");
							}
							else writer.println(t + " (" + (index+1) +")\n");

							//System.out.println(token+"\t"+currentState);
						}
						else {

							writer.println(Token.errorToken(token, row, column));
						}
							//writer.println("[!]Error at:\t"+token.replace('\n',' ')+content.charAt(i));
						--i;
						currentState = 0;
						token = "";
						if(symbol == '\n')
							--row;
				}
			 /*try
		        {
		            System.in.read();
		        }  
		        catch(Exception e)
		        {}  */
			}
		if(!isDelimiter(token)){
			if(isFinal(currentState)){
				char tokenType = (currentState+"").charAt(0);
				int index;
				Token t = Token.getToken(token,tokenType);
				if( (index = tokens.indexOf(t)) == -1 ) {
					tokens.add(t);
					writer.println(t + " (" + tokens.size() +")\n");
				}
				else writer.println(t + " (" + (index+1) +")\n");

				//System.out.println(token+"\t"+currentState);
			}
			else {

				writer.println(Token.errorToken(token, row, column));
			}
		}
		writer.close();
		}
	
	
	private boolean isDelimiter(String token) {
		if(token.length() == 0)
			return true;
		return Arrays.asList(Token.delimiters).contains(token) || token.charAt(0) == (char)10 || token.charAt(0) == (char)13;
	}

	private boolean isFinal(int state){
		return finalStates.contains(state);
	}


}
