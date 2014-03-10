package ch.usi.star.bear.loader;

import java.util.Date;

public class LogLine {

	private String rawLine;

	private String ip;

	private String url;

	private Date date;
	
	private String agent;

	private String referrer;

	public LogLine(String rawLine, String ip, String url, Date date, String referrer, String agent) {
		super();
		if (rawLine == null || ip == null || url == null || date == null)
			throw new NullPointerException();
		this.rawLine = rawLine;
		this.ip = ip;
		this.url = url;
		this.date = date;
		this.agent = agent;
		this.referrer = referrer;
	}
	
	@Override
	public String toString(){
		return this.rawLine;
	}
	

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getReferrer() {
		return referrer;
	}

	public void setReferrer(String referrer) {
		this.referrer = referrer;
	}

	public String getRawLine() {
		return rawLine;
	}

	public void setRawLine(String rawLine) {
		this.rawLine = rawLine;
	}
}
