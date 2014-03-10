package ch.usi.star.bear.loader;

import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;

import ch.usi.star.bear.model.State;
import ch.usi.star.bear.model.UserClass;
import ch.usi.star.bear.properties.BearProperties;

public class Tracker {
	
	
	
	private static HashMap<UserClass, Tracker> trackerMap = new HashMap<UserClass, Tracker>();  
	
	private HashMap<String, State> notifications;
	private HashMap<String, Date> timer;
	private HashMap<String, String> ipToUsername = new HashMap<String, String>();
	private State initialState;
	private int userwindow;
	private long usernameCount = 0;
	private int occurrences = 0;
	private int userCount=0;
	private Logger log = Logger.getLogger(this.getClass().getSimpleName());
	
	
	public void increaseUserCount(){
		this.userCount++;
	}
	
	public int getUserCount(){
		return this.userCount;
	}
	
	public static Tracker getTracker(UserClass userClass, State initialState) {
		Tracker tracker;
		if (trackerMap.containsKey(userClass)) {
			tracker = trackerMap.get(userClass);
		} else {
			tracker = new Tracker(initialState);
			trackerMap.put(userClass, tracker);
		}
		return tracker;

	}

	@SuppressWarnings("unchecked")
	public static HashMap<UserClass, Tracker> getTrackers() {
		return (HashMap<UserClass, Tracker>) trackerMap.clone();
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, State> getCurrentGlobalState() {
		return (HashMap<String, State>) this.notifications.clone();
	}

	public Tracker(State initialState) {
		this.initialState = initialState;
		this.notifications = new HashMap<String, State>();
		this.timer = new HashMap<String, Date>();
		this.userwindow = Integer.parseInt(BearProperties.getInstance().getProperty(BearProperties.USERWINDOW));

	}

	public State getCurrentState(String userName) throws Exception {
		State state = notifications.get(userName);
		if (state == null) {
			throw new Exception("Unknown Username.");
		}
		return state;
	}

	public void updateCurrentState(String userName, State state) {
		log.debug("\tsaving state: " + state);
		this.notifications.put(userName, state);
		this.occurrences++;
	}


	public String generateUserName(String ip, Date date) {
		String userName;

		if (ipToUsername.containsKey(ip)) {
			// Already recorded IP
			userName = this.ipToUsername.get(ip);
			Date lastTime = timer.get(userName);

			long diff = date.getTime() - lastTime.getTime();
			long diffMinutes = diff / (60 * 1000);

			if (diffMinutes > this.userwindow) {
				// New user
				this.usernameCount++;
				userName = String.valueOf(this.usernameCount);
				this.ipToUsername.put(ip, userName);
				this.notifications.put(userName, this.initialState);
			}

		} else {
			// First time IP
			this.usernameCount++;
			userName = String.valueOf(this.usernameCount);
			this.ipToUsername.put(ip, userName);
			this.notifications.put(userName, this.initialState);
		}
		this.timer.put(userName, date);
		return userName;
	}

	public synchronized int getOccurrences() {
		return occurrences;
	}
}
