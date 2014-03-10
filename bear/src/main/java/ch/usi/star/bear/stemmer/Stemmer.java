package ch.usi.star.bear.stemmer;

import ch.usi.star.bear.loader.LogLine;

public interface Stemmer {
	public boolean matches(LogLine logLine);
}
