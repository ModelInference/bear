package ch.usi.star.bear.properties;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class BearProperties {
	
	private final String propertyfile = "res/config.properties";

	public static String USERWINDOW = "userwindow";
	public static String LABELREWARDS = "labelrewards";
	public static String TIMINGREWARDS = "timingrewards";
	public static String STARTSTATE = "startstate";
	public static String ENDSTATE = "endstate";
	public static String PRISMPATH = "prismpath";

	private static BearProperties instance = null;
	private Properties prop;

	public static BearProperties getInstance() {
		if (BearProperties.instance == null) {
			BearProperties.instance = new BearProperties();
		}
		return BearProperties.instance;
	}
	
	public String getProperty(String property){
		return this.prop.getProperty(property);
	}
	
	

	public BearProperties() {
		prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream(propertyfile);
			// load a properties file
			prop.load(input);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

}
