/**
 * 
 */
package ch.usi.star.bear.apache.generator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rax
 *
 */
public class RequestGenerator implements Generator {
	
	private final List<String> labels = new ArrayList<String>();
	private final static double CONSTANT= 0.5;
	

	public RequestGenerator(final int counter) {
		for (int i = 0; i < counter; i++) {
			String s = "s" + i;
			labels.add(s);
		}
	}
	
	public RequestGenerator(final List<String> list) {
		labels.addAll(list);
	}


	public String generate() {
		StringBuilder builder = new StringBuilder();
		builder.append("GET /");
		for(String label: labels){
			double randomNumber = Math.random();
			if(randomNumber > CONSTANT){
				builder.append(label)
				       .append("/");
			}
		}
		builder.append(" HTTP/1.1");
		return builder.toString();
	}

}
