/**
 * 
 */
package ch.usi.star.bear.apache.generator;

import java.util.HashMap;
import java.util.Map;

/**
 * @author rax
 *
 */
public class CompositRequestGenerator {
	
	private final Generator ip ;
	private final Generator request;
	private final Generator origin = new HistoryOriginGenerator();
	
	private final Map<String, String> history = new HashMap<String, String>();
	private String lastIp;
	private String lastUrl;
	
	public CompositRequestGenerator(final int counter, double newVisitorProbability,
			double leavingVisitorPeobability){
		ip = new HistoryIpGenerator(newVisitorProbability, leavingVisitorPeobability);
		request = new HistoryRequestGenerator(counter);
	}
	
	private class HistoryIpGenerator extends IpGenerator {
		public HistoryIpGenerator(double newVisitorProbability,
				double leavingVisitorProbability) {
			super(newVisitorProbability, leavingVisitorProbability);
		}

		public String generate() {
			lastIp = super.generate();
			return lastIp;
		}
	}
	
	private class HistoryOriginGenerator implements Generator {
		public String generate() {
			String value = history.get(lastIp);
			if(value == null){
				value = "-";
			}
			history.put(lastIp, lastUrl);
			return value;
		}
	}
	
	private class HistoryRequestGenerator extends RequestGenerator {
		public HistoryRequestGenerator(int counter) {
			super(counter);
		}

		public String generate() {
			String request = super.generate();
			lastUrl = request.split(" ")[1];
			return request;
		}
	}

	public Generator getIp() {
		return ip;
	}

	public Generator getRequest() {
		return request;
	}

	public Generator getOrigin() {
		return origin;
	}

}
