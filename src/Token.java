import java.util.Arrays;


public class Token {
	
	private String word;
	private String type,value;
	
	
	
	public static String TOKEN_IDENITIFER_REGEX = "^([-a-zA-Z0-9_])+|~$";
	public static String TOKEN_INTEGER_REGEX = "^\\d+$";
	public static String TOKEN_FLOAT_REGEX ="^([+-]?\\d*\\.?\\d*)$";
	public static String TOKEN_HEX_REGEX ="^[0-9A-F]+$";
	public static String[] keywords = {
			"auto",
			"break",
			"case",
			"char",
			"const",
			"continue",
			"default",
			"do",
			"double",
			"else",
			"enum",
			"extern",
			"float",
			"for",
			"goto",
			"if",
			"int",
			"long",
			"register",
			"return",
			"short",
			"signed",
			"sizeof",
			"static",
			"struct",
			"switch",
			"typedef",
			"union",
			"unsigned",
			"void",
			"volatile",
			"while",
			};
	public static String[] operators = {
			"=",
			"+",
			"-",
			"*",
	};
	public static String[] constants = {
			"0",
			"1",
			"2",
			"3",
			"4",
			"5",
			"6",
			"7",
			"8",
			"9"
	};
	public static String[] delimiters = {
			",",".","(",")","[","]","{","}",";"," "
	};
	public static Token errorToken(String word, int row, int column) {
		return new Token("[!]Error",word+" (row "+row+", column "+column+")");
	}
	
	public static Token  getToken(String word, char tokenType){
		Token token;
		if(Arrays.asList(keywords).contains(word)) {
			token = new Token("keyword",word);
			//System.out.println(token);
			return token;
		}
		String message = "";
		String type ="";
		switch(tokenType){
		case '1':
			type = "identifier";
			break;
		case '2':
			type = "constant";
			break;
		case '3':
			type = "comment";
			break;
		case '4':
			type = "operator";
			break;
		case '5':
			type = "operator";
			break;
		case '6':
			type = "delimiter";
			break;
		case '7':
			type = "constant";
			break;
			
		}
		String value = word.replace('\n', ' ');
		token = new Token(type, value);
		return token;
	}
	private static boolean isConstant(String word) {
		return 
				word.matches(TOKEN_INTEGER_REGEX) || 
				word.matches(TOKEN_FLOAT_REGEX)  ||
				word.matches(TOKEN_HEX_REGEX) 
				;
	}
	
	Token(String type, String value){
		this.type = type;
		this.value = value;
	}	
	@Override
	public String toString() {
		String token="";
		int len = type.length() + value.length();
		String separator = "+";
		for(int i = 0 ; i <= len + 2; ++i)
			separator += "=";
		separator += '+';
		token += type + " " + value;
		
		return separator + "\n|" + type + " : " + value + "|\n" + separator;
		//return type + " \t " + value;
	}
	@Override
	public boolean equals(Object token) {
		Token t = (Token) token;
		return this.value.equals(t.value);
	}

	
}
