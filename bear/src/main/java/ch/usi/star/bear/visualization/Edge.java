package ch.usi.star.bear.visualization;

public class Edge {
	private Float weigth; // good coding practice would have this as private

	public Edge(float weigth) {
		this.weigth = weigth;
	}

	public String toString() {  
		return weigth.toString();
	}
}
