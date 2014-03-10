package ch.usi.star.bear.model;

import java.util.Collection;
import java.util.HashMap;

public class RewardSchema {
	
	private HashMap<State, Long> schema = new HashMap<State, Long>();
	private String name;
	
	public String getName() {
		return name;
	}

	public RewardSchema(String name, Collection<State> states){
		this.name = name;
		for (State s : states) {
			this.schema.put(s, s.getReward());
		}
	}
	
	public Long getReward(State s){
		return schema.get(s);
	}
	
	public boolean contains(State s){
		return schema.containsKey(s);
	}
}
