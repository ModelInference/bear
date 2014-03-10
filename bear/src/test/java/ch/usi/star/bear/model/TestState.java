package ch.usi.star.bear.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestState {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testStateEquals() {
		State state1;
		try {
			state1 = new State(new Label("label1"), new Label("label2"));
			State state2 = new State(new Label("label2"), new Label("label1"));
			State state3 = new State(new Label("label2"), new Label("label3"));
			assertTrue(state1.equals(state2));
			assertFalse(state2.equals(state3));
			assertTrue(state2.equals(state2));
		} catch (Exception e) {
			fail("Unable to create states");
			e.printStackTrace();
		}
		
		
	}
}
