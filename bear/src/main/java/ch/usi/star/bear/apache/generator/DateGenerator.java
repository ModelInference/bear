/**
 * 
 */
package ch.usi.star.bear.apache.generator;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author rax
 *
 */
public class DateGenerator implements Generator {

	private static SimpleDateFormat FORMAT = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z");
	
	private Calendar start;
	private final int maxDelta;
	
	public DateGenerator(final Calendar start, final int maxDelta) {
		this.start = start;
		this.maxDelta = maxDelta;
	}
	
	public String generate() {
		//long time = start.getTime() + (long) (Math.random()*maxDelta);
		start.add(Calendar.MILLISECOND, (int)Math.random()*maxDelta);
		//20/Dec/2011:15:55:02 +0100
		return FORMAT.format(start.getTime());
	}

}
