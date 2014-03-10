package ch.usi.star.bear.loader;


public interface Loader {
	
	public LogLine next();
	
	public void dispose();

}
