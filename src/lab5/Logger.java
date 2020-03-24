package lab5;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Logger {

	private static ArrayList<Logger> loggers = new ArrayList<>();

	public static void writeAllLoggers() {
		for (Logger logger : loggers) {
			logger.writeLog();
		}
	}

	private ArrayList<String> values;
	private String filePath;
	private boolean append; // Append if we have written before during this run.

	public Logger(String filePath) {
		this.filePath = filePath;
		this.values = new ArrayList<String>();
		this.append = false;
		Logger.loggers.add(this);
	}

	public void log(String value) {
		values.add(value);
	}

	public void writeLog() {
		try {
			FileWriter writer = new FileWriter(this.filePath, this.append);
			for (String value : values) {
				writer.write(value + System.lineSeparator());
			}
			writer.close();
			this.append = true;
			this.values = new ArrayList<String>(); // Clear log after write
		} catch (IOException ex) {
			System.out.println("[LOGGER] Failed to write to file " + this.filePath);
		}
	}

}
