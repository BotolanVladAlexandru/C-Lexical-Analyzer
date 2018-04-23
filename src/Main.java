import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
public class Main {
	private static String content="";
	private static DFA automat;
	private static String path = "C:\\\\Users\\\\Dev01\\\\eclipse-workspace\\\\LexicalAnalyzer\\\\src\\\\sursa";
	public static void main(String [] args) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		for(int i = 0 ; i < encoded.length ; ++i){
			if(encoded[i] != 10 && encoded[i] != 13)
				content += (char)encoded[i];
			else if(encoded[i] == 13) 
				content += '\n';
		}
		automat = new DFA();
		System.out.println(automat);
		automat.analyze(content);
	}
}
/*


 **/


