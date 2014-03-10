package ch.usi.star.bear.model;


public class Label implements Comparable<Label>{
	
	private String name;
	private Long reward;

	public Label(String name, Long reward) {
		this.name = name.trim().replaceAll(" ", "");
		this.reward = reward;
	}

	public Label(String name) {
		this(name, new Long(0));
	}
	
	@Override
	public String toString(){
		return this.name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Label other = (Label) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}



	public String getName() {
		return name;
	}

	public Long getReward() {
		return reward;
	}
	
	public int compareTo(Label l) {
		return this.name.compareTo(l.name);
	}
}
