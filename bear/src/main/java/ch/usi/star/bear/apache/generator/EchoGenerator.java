/**
 * 
 */
package ch.usi.star.bear.apache.generator;

/**
 * creates a generator that repeats the given message string
 * 
 * @author rax
 *
 */
public class EchoGenerator implements Generator {
	
    private final String message;
   
    public EchoGenerator(final String message) {
		this.message = message;
	}
    
	public String generate() {
		return message;
	}
	
}
