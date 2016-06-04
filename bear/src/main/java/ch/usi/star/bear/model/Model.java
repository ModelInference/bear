package ch.usi.star.bear.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import ch.usi.star.bear.properties.BearProperties;

public class Model {

	private Set<State> states;
	private Set<Transition> transitions;
	private RewardSchema rewardSchema;
	private UserClass userClass;

	public UserClass getUserClass() {
		return userClass;
	}

	public RewardSchema getRewardSchema() {
		return this.rewardSchema;
	}

	public Set<State> getStates() {
		return states;
	}

	public Set<Label> getLabels() {
		HashSet<Label> labels = new HashSet<Label>();
		for (State s : states) {
			labels.addAll(s.getLabels());
		}
		return labels;
	}

	public Set<Transition> getTransitions() {
		return transitions;
	}

	public Transition getTransitionsFromStateToState(State fromState, State toState) {
		for (Transition t : this.transitions) {
			if (t.getSource().equals(fromState) && t.getDestination().equals(toState)) {
				return t;
			}
		}
		return null;
	}

	public Set<Transition> getTransitionsFromState(State fromState) {
		Set<Transition> result = new HashSet<Transition>();
		for (Transition t : transitions) {
			if (t.getSource().equals(fromState)) {
				result.add(t);
			}
		}
		return result;
	}

	public Model(Set<State> states, Set<Transition> transitions, UserClass uc) {
		this.states = states;
		this.userClass = uc;
		this.transitions = transitions;
	}

	public Model(Set<State> states, Set<Transition> transitions, UserClass uc, RewardSchema schema) {
		this(states, transitions, uc);
		this.rewardSchema = schema;
	}

	public int numberOfStates() {
		return states.size();
	}

	public int numberOfTransitions() {
		return transitions.size();
	}

	public String generatePrismModel(String moduleName) {

		Set<State> states = this.getStates();
		Set<Label> labels = this.getLabels();

		String header = createHeader(moduleName, labels);
		String body = "\n";

		for (State s : states) {
			body += "[] " + "( " + getStateRepresentation(s, false) + " ) -> ";
			Set<Transition> outgoing = this.getTransitionsFromState(s);
			Iterator<Transition> iterator = outgoing.iterator();

			while (iterator.hasNext()) {
				Transition t = iterator.next();
				body += t.getProbability() + " : " + getStateRepresentation(t.getDestination(), true);
				if (iterator.hasNext())
					body += " + ";
				else
					body += ";\n";
			}
		}

		String footer = "\nendmodule";

		String rewards = generateRewards();
		return header + body + footer + rewards;
	}

	private String generateRewards() {
		Set<State> states = this.getStates();

		String rewards = "";
		String stringSchema = "\n\nrewards ";
		boolean anyState = false;
		stringSchema += "\"" + this.rewardSchema.getName() + "\"\n\n";
		for (State s : states) {
			if (this.rewardSchema.contains(s)) {
				anyState = true;
				stringSchema += getStateRepresentation(s, false) + " : " + this.rewardSchema.getReward(s) + ";\n";
			}
		}
		if (anyState)
			stringSchema += "\nendrewards";
		else
			stringSchema = "";
		rewards += stringSchema;
		return rewards;
	}

	private String getStateRepresentation(State s, boolean prime) {
		String rep = "";
		Set<Label> negatedLabels = new HashSet<Label>();
		negatedLabels.addAll(this.getLabels());
		negatedLabels.removeAll(s.getLabels());

		rep += addLabels(s.getLabels(), prime, true);
		rep += " & ";
		rep += addLabels(negatedLabels, prime, false);

		return rep;
	}

	private String addLabels(Set<Label> labels, boolean prime, boolean value) {
		String rep = "";
		Iterator<Label> iterator = labels.iterator();
		while (iterator.hasNext()) {
			rep += "(" + iterator.next();
			if (prime)
				rep += "'";

			if (value) {
				rep += "=true)";
			} else
				rep += "=false)";

			if (iterator.hasNext())
				rep += " & ";
		}
		return rep;
	}

	private String createHeader(String moduleName, Set<Label> labels) {
		String header = "dtmc\n\nmodule " + moduleName + "\n\n";
		for (Label l : labels) {
			if (l.getName() == BearProperties.getInstance().getProperty(BearProperties.STARTSTATE))
				header += l + " : bool init true;\n";
			else
				header += l + " : bool init false;\n";
		}
		return header;
	}
	
	// creates a "displayable" JSONObject and adds it to the JSONArray of displayables
	// returns the object's ID
	@SuppressWarnings("unchecked")
	private long createDisplayable(String value, JSONArray otherDisplayables) {
		long displayableID = AtomicIdCounter.nextId();
		JSONObject displayable = new JSONObject();
		displayable.put("id", displayableID);
		displayable.put("displayableValue", value);
		otherDisplayables.add(displayable);
		return displayableID;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject generateJSONModel() {
		JSONObject model = new JSONObject();
		JSONArray otherDisplayables = new JSONArray();
		
		Set<State> states = this.getStates();
		JSONArray nodes = new JSONArray();
		// Transitions that share a State are not guaranteed to be pointing to the same State object,
		// however all State objects with the same toString can be considered the same
		// HashMap uses the toString method to find the State ID that we are saving to JSON
		HashMap<State, Long> nodeIdHash = new HashMap<State, Long>();
		for (State s : states) {
			JSONObject node = new JSONObject();
			long nodeId = AtomicIdCounter.nextId();
			node.put("id", nodeId);
			nodeIdHash.put(s, nodeId);
			
			long displayableID = createDisplayable(s.toString(), otherDisplayables);
			JSONArray displayableIDs = new JSONArray();
			displayableIDs.add(displayableID);
			node.put("displayableIDs", displayableIDs);
			
			nodes.add(node);
		}
		model.put("nodes", nodes);


		Set<Transition> transitions = this.getTransitions();
		JSONArray edges = new JSONArray();
		for (Transition t : transitions) {
			JSONObject edge = new JSONObject();
			edge.put("id", AtomicIdCounter.nextId());
			edge.put("srcNodeID", nodeIdHash.get(t.getSource()));
			edge.put("destNodeID", nodeIdHash.get(t.getDestination()));
			
			long displayableID = createDisplayable(t.getProbability() + "", otherDisplayables);
			JSONArray displayableIDs = new JSONArray();
			displayableIDs.add(displayableID);
			edge.put("displayableIDs", displayableIDs);
			
			edges.add(edge);
		}
		model.put("edges", edges);
		
		model.put("otherDisplayables", otherDisplayables);
		return model;
	}
}
