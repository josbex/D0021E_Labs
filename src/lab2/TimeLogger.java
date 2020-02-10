package lab2;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class TimeLogger {
	
	public void logTime(String filename, double time ){
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(filename +".txt", true));
			writer.append(time + "\n" );
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
