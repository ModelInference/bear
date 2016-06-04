package ch.usi.star.bear.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class State {

	private long id;
	private Label[] labels;
	private String hashCode;
	private long reward = 0;
	
	public State(Label... labels) throws Exception {
		this.id = AtomicIdCounter.nextId();
		
		if (labels.length == 0)
			throw new Exception("State needs a least one label!");
		this.labels = labels;
		Arrays.sort(labels);

		StringBuilder sb = new StringBuilder();
		for (Label s : labels) {
			sb.append(s.toString());
			this.reward += s.getReward();
		}
		hashCode = sb.toString();
	}
	
	public long getId() {
		return id;
	}
	
	public Set<Label> getLabels() {
		return new HashSet<Label>(Arrays.asList(labels));
	}
	
	public long getReward() {
		return reward;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((hashCode == null) ? 0 : hashCode.hashCode());
		result = prime * result + Arrays.hashCode(labels);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		State other = (State) obj;
		if (hashCode == null) {
			if (other.hashCode != null)
				return false;
		} else if (!Arrays.equals(this.labels, other.labels))
			return false;
		if (!Arrays.equals(labels, other.labels))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Label s : labels) {
			sb.append(s.toString());
			sb.append(" ");
		}
		return sb.toString();
	}

}
