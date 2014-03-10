package ch.usi.star.bear.findyourhouse;


import ch.usi.star.bear.loader.LogLine;

public class Stemmer implements ch.usi.star.bear.stemmer.Stemmer{

	public boolean matches(LogLine logLine) {
		if(logLine.getUrl().contains("/media/"))
			return true;
		if(logLine.getUrl().contains("/favicon.ico"))
			return true;
		if(logLine.getUrl().contains("/captcha/image"))
			return true;
		if(logLine.getUrl().contains("/fancybox/"))
			return true;
		if(logLine.getUrl().contains("ajax"))
			return true;
		if(logLine.getUrl().contains(".asp"))
			return true;
		if(logLine.getUrl().contains("/?ag=&r=&pr=&tv=&p=&l"))
			return true;
		if(logLine.getUrl().contains("robots.txt"))
			return true;
		if(logLine.getRawLine().contains("msnbot"))
			return true;
		if(logLine.getRawLine().contains("Googlebot"))
			return true;
		if(logLine.getRawLine().contains("Ezooms"))
			return true;
		if(logLine.getRawLine().contains("AhrefsBot"))
			return true;
		if(logLine.getRawLine().contains("TurnitinBot"))
			return true;
		if(logLine.getRawLine().endsWith("\"-\" \"-\""))
			return true;
		if(logLine.getRawLine().contains("Java"))
			return true;
		if(logLine.getRawLine().contains("crawler"))
			return true;
		return false;
	}

}
