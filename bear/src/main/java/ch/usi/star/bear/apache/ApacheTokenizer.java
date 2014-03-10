package ch.usi.star.bear.apache;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.usi.star.bear.loader.LogLine;

public class ApacheTokenizer {
	

	private String ipEx = "(\\d+)\\.(\\d+)\\.(\\d+)\\.(\\d+)";
	private String dateEx = "\\[(\\d+/\\w+/\\d+:\\d+:\\d+:\\d+ \\+\\d+)";
	private String url = "(GET|POST|HEAD)\\s/?(\\S+)";
	private String referrer = "(\\d+) (\\d+) \"[^\"]+";
	private String agent = "\"(.+?)\"";
	private Pattern patternIp, patternDate, patternUrl, patternReferrer, patternAgent;

	public ApacheTokenizer() {
		this.patternIp = Pattern.compile(ipEx);
		this.patternDate = Pattern.compile(dateEx);
		this.patternUrl = Pattern.compile(url);
		this.patternReferrer = Pattern.compile(referrer);
		this.patternAgent = Pattern.compile(agent);
	}

	public String extractIp(String line) {
		Matcher matcher = patternIp.matcher(line);
		matcher.find();
		// Note: this may throw several exceptions
		return matcher.group(0);
	}
	
	public String extractAgent(String line) {
		Matcher matcher = patternAgent.matcher(line);
		matcher.find();
		matcher.find();
		matcher.find();
		// Note: this may throw several exceptions
		String agent = matcher.group(0);
		agent = agent.substring(1, agent.length()-1);
		return agent;
	}

	public Date extractDate(String line) throws ParseException {
		Matcher matcher = patternDate.matcher(line);
		matcher.find();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy:hh:mm:ss Z");
		return dateFormat.parse(matcher.group(1));
	}

	public String extractReferrer(String line) {
		Matcher matcher = patternReferrer.matcher(line);
		matcher.find();
		String referrer = matcher.group(0);
		int start = referrer.indexOf('"', 0);
		referrer = referrer.substring(start + 1);
		if (!referrer.equals("-")) {
			return referrer;
		} else {
			return null;
		}
	}

	public String extractUrl(String line) {
		Matcher matcher = patternUrl.matcher(line);
		matcher.find();

		String url = matcher.group(0);
		if (url.startsWith("GET "))
			url = url.subSequence(4, url.length()).toString();
		else
			url = url.subSequence(5, url.length()).toString();
		return url;
	}

	public LogLine tokenize(String line) {
		try {
			String ip = this.extractIp(line);
			String url = this.extractUrl(line);
			Date date = this.extractDate(line);
			String referrer = this.extractReferrer(line);
			String agent = this.extractAgent(line);
			LogLine logLine = new LogLine(line, ip, url, date, referrer, agent);
			return logLine;
		} catch (IllegalStateException ex) {
			return null;
		} catch (ParseException ex) {
			return null;
		}
	}
}
