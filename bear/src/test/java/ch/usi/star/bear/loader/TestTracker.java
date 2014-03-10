package ch.usi.star.bear.loader;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.usi.star.bear.model.Label;
import ch.usi.star.bear.model.State;
import ch.usi.star.bear.properties.BearProperties;

public class TestTracker {

	private Tracker tracker;
	private String ip = "1.1.1.1";
	private State initialState;
	private State middleState;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		this.initialState = new State(new Label(BearProperties.getInstance().getProperty(BearProperties.STARTSTATE)));
		this.middleState = new State(new Label("middle"));
		tracker = new Tracker(this.initialState);
	}
	
	@Test
	public void testGenerateUserName(){
		//Generate a date for Jan. 9, 2013, 10:11:12 AM
		Calendar cal = Calendar.getInstance();
		
		cal.set(2013, Calendar.JANUARY, 9, 10, 11, 12); //Year, month, day of month, hours, minutes and seconds
		Date date = cal.getTime();
		
		String userName = tracker.generateUserName(ip, date);
		tracker.updateCurrentState(userName, this.middleState);
				
		String userName2 = tracker.generateUserName(ip, date);
		
		assertEquals(userName, userName2);
	}
	
	
	@Test
	public void testGenerateUserNameAfterTimeout(){
		//Generate a date for Jan. 9, 2013, 10:11:12 AM
		Calendar cal = Calendar.getInstance();
		
		cal.set(2013, Calendar.JANUARY, 9, 10, 11, 12); //Year, month, day of month, hours, minutes and seconds
		Date date = cal.getTime();
		
		String userName = tracker.generateUserName(ip, date);
		tracker.updateCurrentState(userName, this.middleState);
		
		cal.set(2013, Calendar.JANUARY, 10, 10, 11, 12); //Year, month, day of month, hours, minutes and seconds
		date = cal.getTime();
		String userName2 = tracker.generateUserName(ip, date);
		
		assertNotEquals(userName, userName2);
	}
	
	@Test
	public void testGetCurrentStateWithInitalState(){
		//Generate a date for Jan. 9, 2013, 10:11:12 AM
		Calendar cal = Calendar.getInstance();
		
		cal.set(2013, Calendar.JANUARY, 9, 10, 11, 12); //Year, month, day of month, hours, minutes and seconds
		Date date = cal.getTime();
		
		String userName = tracker.generateUserName(ip, date);
		try {
			assertEquals(this.initialState, tracker.getCurrentState(userName));
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetCurrentState(){
		//Generate a date for Jan. 9, 2013, 10:11:12 AM
		Calendar cal = Calendar.getInstance();
		
		cal.set(2013, Calendar.JANUARY, 9, 10, 11, 12); //Year, month, day of month, hours, minutes and seconds
		Date date = cal.getTime();
		
		String userName = tracker.generateUserName(ip, date);
		tracker.updateCurrentState(userName, this.middleState);
		
		try {
			assertEquals(this.middleState, tracker.getCurrentState(userName));
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
		
	}
	
	


	@After
	public void tearDown() throws Exception {
	}

	
}
