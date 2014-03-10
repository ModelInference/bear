package ch.usi.star.bear.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.usi.star.bear.InferenceEngine;

public class TestInferenceEngine {

	private InferenceEngine inferenceEngine;

	@Before
	public void setUp() {
		inferenceEngine = new InferenceEngine();
	}

	@Test
	public void testInitialModel() {
		assertEquals(this.inferenceEngine.numberOfStates(), 2);
		assertEquals(this.inferenceEngine.numberOfTransitions(), 0);
		assertTrue(this.inferenceEngine.containsState(this.inferenceEngine.getInitState()));
		assertTrue(this.inferenceEngine.containsState(this.inferenceEngine.getGoalState()));
	}



	@Test
	public void testSignalTransition() {
		State uno;
		try {
			uno = new State(new Label("Uno"));
			State due = new State(new Label("Due"));
			State tre = new State(new Label("Tre"));
			this.inferenceEngine.signalTransition(uno, due);
			this.inferenceEngine.signalTransition(due, tre);
			assertTrue(inferenceEngine.containsTransition(new Transition(uno, due)));
			assertTrue(inferenceEngine.containsTransition(new Transition(due, tre)));
			assertTrue(inferenceEngine.numberOfTransitions() == 2);
		} catch (Exception e1) {
			fail();
			e1.printStackTrace();
		}

	}

	@Test
	public void testSignalTransitionWithOccurrences() {
		State uno;
		try {
			uno = new State(new Label("Uno"));
			State due = new State(new Label("Due"));
			assertTrue(inferenceEngine.getTransitionOccurrences(new Transition(uno, due)) == 0);
			this.inferenceEngine.signalTransition(uno, due);
			assertTrue(inferenceEngine.getTransitionOccurrences(new Transition(uno, due)) == 1);
			this.inferenceEngine.signalTransition(uno, due);
			assertTrue(inferenceEngine.containsTransition(new Transition(uno, due)));
			assertTrue(inferenceEngine.numberOfTransitions() == 1);
			assertEquals(inferenceEngine.getTransitionOccurrences(new Transition(uno, due)), 2);
		} catch (Exception e1) {
			fail();
			e1.printStackTrace();
		}

	}

	@Test
	public void TestFinalizedModel() {

		try {
			State uno = new State(new Label("Uno"));
			State due = new State(new Label("Due"));
			State tre = new State(new Label("Tre"));

			HashMap<String, State> usersState = new HashMap<String, State>();

			
			this.inferenceEngine.signalTransition(this.inferenceEngine.getInitState(), uno);
			this.inferenceEngine.signalTransition(uno, due);
			this.inferenceEngine.signalTransition(due, tre);
			
			UserClass uc = new UserClass("", "", 0);
			this.inferenceEngine.exportModel(usersState, uc);
			
			assertTrue(this.inferenceEngine.numberOfTransitions() == 5);
			assertTrue(this.inferenceEngine.containsTransition(new Transition(this.inferenceEngine.getInitState(), uno)));
			assertTrue(this.inferenceEngine.containsTransition(new Transition(tre, this.inferenceEngine.getGoalState())));
			assertTrue(this.inferenceEngine.containsTransition(new Transition(this.inferenceEngine.getGoalState(), this.inferenceEngine
					.getGoalState())));
		} catch (Exception e1) {
			fail();
			e1.printStackTrace();
		}

	}

	@Test
	public void testEqualsTransitions() {
		try {
			State uno = new State(new Label("Uno"));
			State due = new State(new Label("Due"));
			Transition t = new Transition(new State(new Label("Uno")), new State(new Label("Due")));
			Transition t2 = new Transition(uno, due);
			Transition t3 = new Transition(uno, new State(new Label("Tre")));
			assertFalse(t.equals(t3));
			assertTrue(t.equals(t2));
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}

	}

	@After
	public void tearDown() {
		inferenceEngine = new InferenceEngine();
	}

}
