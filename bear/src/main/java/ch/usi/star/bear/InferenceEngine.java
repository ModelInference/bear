package ch.usi.star.bear;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.usi.star.bear.loader.LogLine;
import ch.usi.star.bear.model.Label;
import ch.usi.star.bear.model.Model;
import ch.usi.star.bear.model.RewardSchema;
import ch.usi.star.bear.model.State;
import ch.usi.star.bear.model.Transition;
import ch.usi.star.bear.model.UserClass;
import ch.usi.star.bear.properties.BearProperties;

public class InferenceEngine {
	
	private static HashMap<UserClass, InferenceEngine> inferenceMap = new HashMap<UserClass, InferenceEngine>();

	private HashSet<State> states;
	private HashSet<Transition> transitions;
	private HashMap<State, Long> stateOccurrences;
	private State initState;
	private State goalState;

	private String startstate = BearProperties.getInstance().getProperty(BearProperties.STARTSTATE);
	private String endstate = BearProperties.getInstance().getProperty(BearProperties.ENDSTATE);
	
	
	protected static InferenceEngine getInferenceEngine(UserClass userClass) {
		InferenceEngine inferenceEngine;
		if (inferenceMap.containsKey(userClass)) {
			inferenceEngine = inferenceMap.get(userClass);
		} else {
			inferenceEngine = new InferenceEngine();
			inferenceMap.put(userClass, inferenceEngine);
		}
		return inferenceEngine;

	}

	public State getGoalState() {
		return goalState;
	}

	public State getInitState() {
		return initState;
	}

	public InferenceEngine() {
		this.states = new HashSet<State>();
		this.transitions = new HashSet<Transition>();
		this.stateOccurrences = new HashMap<State, Long>();
		try {
			this.initState = new State(new Label(this.startstate));
			this.goalState = new State(new Label(this.endstate));
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.states.add(initState);
		this.states.add(goalState);
	}

	public int getTransitionOccurrences(Transition transition) {
		for (Transition t : transitions) {
			if (t.equals(transition)) {
				return t.getOccurrences();
			}
		}
		return 0;
	}

	private int signal(State source, State destination) throws Exception {
		if (destination.equals(this.goalState) || source.equals(this.goalState) || destination.equals(this.initState))
			throw new Exception("cannot add transitions to/from the GOAL/START states");
		// insert destination state (it may be new)
		states.add(destination);
		Transition t = this.createOrUpdateTransition(source, destination);
		return t.getOccurrences();
	}

	public int signalTransition(State source, State destination) throws Exception {
		int res = this.signal(source, destination);
		Long occurrences = this.stateOccurrences.get(source);

		if (occurrences == null) {
			occurrences = new Long(1);
		} else
			occurrences++;

		this.stateOccurrences.put(source, occurrences);
		return res;
	}

	private Transition createOrUpdateTransition(State source, State destination) {
		Transition transition = new Transition(source, destination);
		Transition oldT = null;
		Transition newT = null;
		
		for (Transition t : transitions) {
			if (t.equals(transition)) {
				newT = new Transition(t.getSource(), t.getDestination(), t.getOccurrences()+1, t.getProbability());
				oldT = t;
			}
		}
		
		if(newT!=null){
			transitions.remove(oldT);
		}else{
			newT = transition;
		}
		transitions.add(newT);
		return newT;
	}

	@SuppressWarnings("unchecked")
	public Model exportModel(HashMap<String, State> usersState, UserClass uc, List<LogLine> logLines) throws Exception {
		this.finalizeModel(usersState);
		

		String labelRewardLabel = BearProperties.getInstance().getProperty(BearProperties.LABELREWARDS);
		RewardSchema rewardSchema = new RewardSchema(labelRewardLabel, states);
		Model model = new Model((Set<State>) this.states.clone(), (Set<Transition>) this.transitions.clone(), uc, rewardSchema, logLines);
		return model;
	}

	private void finalizeModel(HashMap<String, State> usersState) throws Exception {
		this.finalizeTransitions(usersState);
		this.computeProbabilities();
	}


	private void computeProbabilities() throws Exception {
		// saving all outgoing transition for every state
		HashSet<Transition> newTransitions = new HashSet<Transition>();
		for (State s : states) {
			float totalOccurencies = 0;
			Set<Transition> outgoing = new HashSet<Transition>();
			for (Transition t : transitions) {
				if (t.getSource().equals(s)) {
					outgoing.add(t);
					if (t.getOccurrences() <= 0)
						throw new Exception("Fatal error: transition with negative or zero occurrencies");
					totalOccurencies += t.getOccurrences();
				}
			}

			for (Transition t : outgoing) {
				Float probability = ((float) t.getOccurrences()) / totalOccurencies;
				Transition transition = new Transition(t.getSource(), t.getDestination(), t.getOccurrences(), probability);
				newTransitions.add(transition);
			}
		}
		this.transitions = newTransitions;
	}

	private void finalizeTransitions(HashMap<String, State> usersState) {
		this.createOrUpdateTransition(goalState, goalState);
		Set<String> users = usersState.keySet();

		for (String user : users) {
			State lastState = usersState.get(user);
			this.createOrUpdateTransition(lastState, goalState);
		}

		for (State source : states) {
			boolean childLess = true;
			for (State destination : states) {
				// Goal and Initial States are not childless
				if (destination.equals(this.goalState) | destination.equals(this.initState)) {
					continue;
				}
				Transition t = new Transition(source, destination);
				if (transitions.contains(t)) {
					childLess = false;
				}
			}
			// If current state is childless we add a transition to the final
			// state
			if (childLess) {
				createOrUpdateTransition(source, this.goalState);
//				log.debug("State " + source + " is childless, fixing it.");
			}
		}
	}

	public int numberOfStates() {
		return this.states.size();
	}

	public int numberOfTransitions() {
		return transitions.size();
	}

	public boolean containsState(State state) {
		return states.contains(state);
	}

	public boolean containsTransition(Transition transition) {
		return transitions.contains(transition);
	}

}
