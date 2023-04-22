package edu.upenn.cit594.logging;

import java.io.FileWriter;
import java.io.IOException;

public class Logger {

	/**
	 * uses the SINGLETON implementation design pattern
	 */

	private FileWriter out;
	private boolean fileFound;
	private Logger() {} 
	private static Logger instance = new Logger();

	/**
	 * singleton accessor method
	 * @return instance of logger
	 */
	public static Logger getInstance() {
		return instance;
	}

	/**
	 * method that logs events to file
	 * and flushes afterward
	 * @param event 
	 */

	public void log(String event) {
		try {
			if (fileFound == true) {
				out.write(System.currentTimeMillis() + " " + event);
				out.flush();
			} else {
				System.err.println(System.currentTimeMillis() + " " + event);
			}
		} catch (IOException e) {
			System.out.println(event + " was unable to be logged");
			e.printStackTrace();
		}

	}

	/**
	 * method that sets or changes output destination
	 * @param filename
	 */

	public void setDestination(String filename) throws IOException{
		// check if out is null and close the existing FileWriter accordingly
		if (out != null) {
			out.close();
		}

		fileFound = findFile(filename);

		// check if filename is null / not given
		if (fileFound == true) {
			out = new FileWriter(filename, true);
		}

	}

	private boolean findFile(String filename) {
		if (filename != null) {
			return true;
		}

		return false;
	}

}

