package ch.usi.star.bear.model;

public class Transition {

	private State source;
	private State destination;
	private int occurrences;
	private float probability;

	public float getProbability() {
		return probability;
	}

	public Transition(State source, State destination) {
		this.source = source;
		this.destination = destination;
		this.occurrences = 1;
		this.probability=0f;
	}
	
	public Transition(State source, State destination, int occurrecnes) {
		this(source, destination);
		this.occurrences = occurrecnes;
	}
	
	public Transition(State source, State destination, int occurrences, float probability) {
		this(source, destination, occurrences);
		this.probability = probability;
	}

	public int getOccurrences() {
		return occurrences;
	}

	public State getSource() {
		return source;
	}

	public State getDestination() {
		return destination;
	}

	public boolean equals(Transition transition) {
		return (this.source.equals(transition.source) && this.destination.equals(transition.destination));
	}

	@Override
	public String toString() {
		return source + " -> " + destination;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((destination == null) ? 0 : destination.hashCode());
		result = prime * result + ((source == null) ? 0 : source.hashCode());
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
		Transition other = (Transition) obj;
		if (destination == null) {
			if (other.destination != null)
				return false;
		} else if (!destination.equals(other.destination))
			return false;
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		return true;
	}
}
