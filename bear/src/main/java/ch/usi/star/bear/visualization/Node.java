package ch.usi.star.bear.visualization;

public class Node {
	private String name; // good coding practice would have this as private

	public Node(String name) {
//		System.out.println(name);
		this.name = name;
	}

	public String toString() {  
		return name;
	}

}
