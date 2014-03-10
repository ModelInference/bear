package ch.usi.star.bear.apache;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

import org.apache.log4j.Logger;

import ch.usi.star.bear.loader.Loader;
import ch.usi.star.bear.loader.LogLine;

public class ApacheLoader implements Loader {

	private BufferedReader input;
	private File logFile;
	private ApacheTokenizer tokenizer;
	private Logger log = Logger.getLogger(this.getClass().getSimpleName());

	public ApacheLoader(File logFile) {
		this.logFile = logFile;
		this.tokenizer = new ApacheTokenizer();
	}

	public LogLine next() {
		String strLine = null;
		if (this.input == null)
			try {
				this.input = new BufferedReader(new FileReader(this.logFile));
			} catch (FileNotFoundException e) {
				log.error("Log file not Found.");
				System.exit(-1);
			}
		/*
		 * readLine is a bit quirky : it returns the content of a line MINUS the
		 * newline. it returns null only for the END of the stream. it returns
		 * an empty String if two newlines appear in a row.
		 */

		try {
			strLine = input.readLine();
		} catch (IOException e) {
			log.error("IO Exception reading log file.");
			System.exit(-1);
		}

		// EOF Condition
		if (strLine == null) {
			return null;
		}
		LogLine logLine = tokenizer.tokenize(strLine);
		if(logLine==null){
			logLine = new LogLine("","","", new Date(), "","");
		}
		return logLine;
	}

	public void dispose() {
		try {
			this.input.close();
		} catch (IOException e) {
			log.error("IO Exception closing log file");
			e.printStackTrace();
			System.exit(-1);
		} finally {
			this.input = null;
		}
	}
}