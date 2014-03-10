package ch.usi.star.bear.visualization;

import java.awt.Dimension;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import ch.usi.star.bear.model.Model;
import ch.usi.star.bear.model.State;
import ch.usi.star.bear.model.Transition;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;

public class BearVisualizer {

	private Graph<Node, Edge> graph;
	private HashMap<State, Node> nodes = new HashMap<State, Node>();

	public BearVisualizer(Model model) {
		graph = new DirectedSparseMultigraph<Node, Edge>();
		// Create some MyNode objects to use as vertices

		for (State s : model.getStates()) {
			Node n = new Node(s.toString());
			nodes.put(s, n);
			graph.addVertex(n);
		}

		for (Transition t : model.getTransitions()) {
			graph.addEdge(new Edge(t.getProbability()), nodes.get(t.getSource()), nodes.get(t.getDestination()), EdgeType.DIRECTED);
		}

	}

	public void display() {
		// The Layout<V, E> is parameterized by the vertex and edge types
		Layout<Node, Edge> layout = new CircleLayout<Node, Edge>(graph);
		layout.setSize(new Dimension(1000, 1000)); 
		
		// sets the initial size of the
		// space
		// The BasicVisualizationServer<V,E> is parameterized by the edge types
		BasicVisualizationServer<Node, Edge> vv = new BasicVisualizationServer<Node, Edge>(layout);
		vv.setPreferredSize(new Dimension(1000, 1000)); // Sets the viewing area size
		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<Node>());
        vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller<Edge>());
        vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR); 
		JFrame frame = new JFrame("Bear Visualization Tool");
		//Create a JPanel  
		JPanel panel=new JPanel();  
		JScrollPane scrollBar=new JScrollPane(panel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);  
		//Add JScrollPane into JFrame  
		panel.add(vv);
		frame.add(scrollBar);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}

}
