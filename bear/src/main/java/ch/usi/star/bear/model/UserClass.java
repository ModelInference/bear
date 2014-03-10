package ch.usi.star.bear.model;



public class UserClass {
	
	private String name;
	private String value;
	private int occurrences;
	
	
	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	public UserClass(String name, String value, int occurrences) {
		this.name = name;
		this.value = value;
		this.occurrences = occurrences;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.name);
		sb.append("=");
		sb.append(value);
		return sb.toString();
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	public UserClass newUserClassWithOccurrences(int occurrences){
		return new UserClass(this.name, this.value, occurrences);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserClass other = (UserClass) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	public int getOccurrences() {
		return occurrences;
	}
}