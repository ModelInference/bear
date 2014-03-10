package ch.usi.star.bear.example;


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
		return false;
	}

}
