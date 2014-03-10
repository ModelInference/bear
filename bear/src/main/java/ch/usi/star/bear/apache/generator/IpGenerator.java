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
public class IpGenerator implements Generator {
	private final List<String> ip = new ArrayList<String>();
	private final double newVisitorProbability;
	private final double leavingVisitorPeobability;
	private final int[] lastAssignedAddress = {1,1,1,1};
	

	public IpGenerator(double newVisitorProbability,
			double leavingVisitorPeobability) {
		this.newVisitorProbability = newVisitorProbability;
		this.leavingVisitorPeobability = leavingVisitorPeobability;
	}
	
	public String createIp(){
		StringBuilder builder = new StringBuilder();
		builder.append(lastAssignedAddress[0])
		       .append(".")
		       .append(lastAssignedAddress[1])
		       .append(".")
		       .append(lastAssignedAddress[2])
		       .append(".")
		       .append(lastAssignedAddress[3]);
		String value =  builder.toString();
		ip.add(value);
		lastAssignedAddress[3] += 1;
		if(lastAssignedAddress[3] == 256){
			lastAssignedAddress[3] = 1;
			lastAssignedAddress[2] += 1;
			if(lastAssignedAddress[2] == 256){
				lastAssignedAddress[2] = 1;
				lastAssignedAddress[1] += 1;
				if(lastAssignedAddress[1] == 256){
					lastAssignedAddress[1] = 1;
					lastAssignedAddress[0] += 1;
					if(lastAssignedAddress[0] == 256){
						lastAssignedAddress[0] = 1;
					}
				}
			}
		}
		
		return value;
	}


	public String generate() {
		String value = null;
		if(ip.isEmpty()){
		    value = createIp();
		}
		else{
			if (Math.random() < newVisitorProbability){
				value = ip.get((int) (Math.random()*(ip.size()-1)));
			}
			else {
				value = createIp();
			}
		}
		if (Math.random() < leavingVisitorPeobability){
			ip.remove(value);
		}
		return value;
	}

}
