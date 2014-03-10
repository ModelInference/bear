/**
 * 
 */
package ch.usi.star.bear.apache.generator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Calendar;

/**
 * Generate logs according to given parameters.
 * Logs are in the format:
 * 
 * domain:port ip - - [date] "request" response_status response_size "origin" "user agent"
 * 
 * www.test.it:80 1.1.1.1 - - [20/Dec/2011:15:55:02 +0100] "GET /uno/ HTTP/1.1" 200 18359 "-" "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; .NET CLR 2.0.50727; MS-RTC LM 8)"
 * www.test.it:80 1.1.1.1 - - [20/Dec/2011:15:56:02 +0100] "GET /due/ HTTP/1.1" 200 18359 "http://www.test.it/uno/" "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; .NET CLR 2.0.50727; MS-RTC LM 8)"
 * www.test.it:80 1.1.1.1 - - [20/Dec/2011:15:58:02 +0100] "GET /tre/ HTTP/1.1" 200 18359 "http://www.test.it/due/" "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; .NET CLR 2.0.50727; MS-RTC LM 8)"
 * www.test.it:80 1.1.1.2 - - [20/Dec/2011:16:05:02 +0100] "GET /uno/ HTTP/1.1" 200 18359 "-" "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; .NET CLR 2.0.50727; MS-RTC LM 8)"
 * www.test.it:80 1.1.1.2 - - [20/Dec/2011:16:07:02 +0100] "GET /due/ HTTP/1.1" 200 18359 "http://www.test.it/uno/" "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; .NET CLR 2.0.50727; MS-RTC LM 8)"
 * www.test.it:80 1.1.1.2 - - [20/Dec/2011:16:08:02 +0100] "GET /quattro/ HTTP/1.1" 200 18359 "http://www.test.it/due/" "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; .NET CLR 2.0.50727; MS-RTC LM 8)"
 * www.test.it:80 1.1.1.3 - - [20/Dec/2011:16:13:02 +0100] "GET /uno/ HTTP/1.1" 200 18359 "-" "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; .NET CLR 2.0.50727; MS-RTC LM 8)"
 * www.test.it:80 1.1.1.3 - - [20/Dec/2011:16:15:02 +0100] "GET /due/ HTTP/1.1" 200 18359 "http://www.test.it/uno/" "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; .NET CLR 2.0.50727; MS-RTC LM 8)"
 * www.test.it:80 1.1.1.3 - - [20/Dec/2011:16:22:02 +0100] "GET /cinque/ HTTP/1.1" 200 18359 "http://www.test.it/due/" "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; .NET CLR 2.0.50727; MS-RTC LM 8)"
 * 
 * @author Michele Sama
 *
 */
public class LogGenerator implements Generator {

	private final Generator address;
	private final Generator ip;
	private final Generator date;
	private final Generator request;
	private final Generator responseStatus;
	private final Generator responseSize;
	private final Generator origin;
	private final Generator userAgent;
	
	public LogGenerator(Generator address, Generator ip, Generator date,
			Generator request, Generator responseStatus,
			Generator responseSize, Generator origin, Generator userAgent) {
		this.address = address;
		this.ip = ip;
		this.date = date;
		this.request = request;
		this.responseStatus = responseStatus;
		this.responseSize = responseSize;
		this.origin = origin;
		this.userAgent = userAgent;
	}

	public String generate() {
		//www.test.it:80 1.1.1.1 - - [20/Dec/2011:15:55:02 +0100] "GET /uno/ HTTP/1.1" 200 18359 "-" "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; .NET CLR 2.0.50727; MS-RTC LM 8)"
		StringBuilder builder = new StringBuilder();
		builder.append(address.generate())
		        .append(" ")
		        .append(ip.generate())
				.append(" - - [")
				.append(date.generate())
				.append("] \"")
				.append(request.generate())
				.append("\" ")
				.append(responseStatus.generate())
				.append(" ")
				.append(responseSize.generate())
				.append(" \"")
				.append(origin.generate())
				.append("\" \"")
				.append(userAgent.generate())
				.append("\"");
				
		return builder.toString();
	}

	public void generate(Writer writer){
		PrintWriter pw = new PrintWriter(writer);
		pw.println(generate());
	}
	

	public static void main(String[] args){
		if (args.length != 3){
		   System.out.println("Usage: LogGenerator <filename> <lines> <num_labels>");
		   System.exit(-1);
		}
		File destination = new File(args[0]);
		int numLine = Integer.parseInt(args[1]);
		int numLabels = Integer.parseInt(args[2]);
		
		CompositRequestGenerator composit = new CompositRequestGenerator(numLabels, 0.5, 0.4);
		
		Generator address = new EchoGenerator("www.test.it:80");
		Generator ip = composit.getIp();
		Generator date = new DateGenerator(Calendar.getInstance(), 60 * 1000);
		Generator request = composit.getRequest();
		Generator responseStatus = new EchoGenerator("200");
		Generator responseSize = new EchoGenerator("18359");
		Generator origin = composit.getOrigin();
		Generator userAgent = new EchoGenerator("Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; .NET CLR 2.0.50727; MS-RTC LM 8)");
		
		LogGenerator logGenerator = new LogGenerator(
				address, ip, date, request, responseStatus, responseSize, origin, userAgent);
		
		Writer writer = null;
		try{
			writer = new FileWriter(destination);
			
			for(int i = 0; i < numLine; i++){
				logGenerator.generate(writer);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
