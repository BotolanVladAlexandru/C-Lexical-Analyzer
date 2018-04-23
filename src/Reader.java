import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Reader {
	

	
	Scanner scanner;
	File file;
	String fileContent;
	
	public Reader(String path) {
		try {
			file = new File(path);
			scanner = new Scanner(file);
			readFromFile();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void readFromFile() {
		fileContent = "";
		while(scanner.hasNext()) {
			fileContent += scanner.nextLine();
		}
		
	}
	
	public String getFileContent() {
		return fileContent;
	}
	
}
