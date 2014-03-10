package ch.usi.star.bear.annotations;

import ch.usi.star.bear.loader.LogLine;

public class DefaultBearClassifiers {

	@BearClassifier(name="userAgent")
	public static String UserAgentClassifier(LogLine logline) {
		return logline.getAgent();
	}
}