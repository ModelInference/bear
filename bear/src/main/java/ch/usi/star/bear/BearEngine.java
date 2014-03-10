package ch.usi.star.bear;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import ch.usi.star.bear.loader.Loader;
import ch.usi.star.bear.loader.LogLine;
import ch.usi.star.bear.loader.StateMapper;
import ch.usi.star.bear.loader.Tracker;
import ch.usi.star.bear.loader.UserClassMapper;
import ch.usi.star.bear.model.Model;
import ch.usi.star.bear.model.State;
import ch.usi.star.bear.model.UserClass;
import ch.usi.star.bear.stemmer.Stemmer;

public class BearEngine {

	private Loader loader;
	private StateMapper stateMapper;
	private UserClassMapper classMapper;
	private Stemmer stemmer;

	private final String logConfFile = "res/Log4j.conf";
	private Logger log = Logger.getLogger(this.getClass().getSimpleName());
	private int lineCounter = 0;

	public BearEngine(String filtersPackagePrefix, String classifiersPackagePrefix) {
		this.stateMapper = new StateMapper(filtersPackagePrefix);
		this.classMapper = new UserClassMapper(classifiersPackagePrefix);
		PropertyConfigurator.configure(this.logConfFile);
	}
	
	
	public String analyze(String scope, String property, List<Model> models, boolean visualize) throws Exception{
		AnalysisEngine analisysEngine = new AnalysisEngine(models);
		return analisysEngine.analyze(scope, property, "Prova", visualize);
	}
	
	
	

	public void infers(Loader loader) {
		if (loader == null) {
			throw new IllegalStateException("Cannot set a null loader");
		}
		
		this.loader = loader;
		LogLine logLine = null;
		do {
			log.debug("\t---------BEGIN-----------");
			try {

				// Fetching a new LogLine from the log file.
				logLine = this.loader.next();
				// If the fetched line is not null (EOF) and if is not a line to
				// be stemmed We process it.
				if (logLine != null && logLine.getRawLine() != "" && !stem(logLine)) {
					log.debug("\tRaw line: " + logLine.getRawLine());
					log.debug("\tUser agent line: " + logLine.getAgent());
					log.debug("\tIP: " + logLine.getIp());

					// Loading the States associated to the LogLine
					State destination = stateMapper.getRequestedState(logLine);
					if (destination == null) {
						log.debug("\tUnmacthed state: " + logLine.getUrl());
						log.debug("\t---------END-------------");
						continue;
					}
					log.debug("\tMatched state: " + destination + " for url: " + logLine.getUrl());

					Set<UserClass> classes = this.classMapper.getUserClass(logLine);
					
					if (classes.size()<1) {
						log.debug("\tUnmacthed class");
						log.debug("\t---------END-------------");
						log.debug("\n");
						continue;
					}
					
					
					for(UserClass uc : classes){
						log.debug("\tMatched Class: " + uc);
						InferenceEngine inferenceEngine = InferenceEngine.getInferenceEngine(uc);
						Tracker tracker = Tracker.getTracker(uc, inferenceEngine.getInitState());
						tracker.increaseUserCount();
						String userName = tracker.generateUserName(logLine.getIp(), logLine.getDate());
						log.debug("\tUser ID: " + userName);
						State source = tracker.getCurrentState(userName);
						inferenceEngine.signalTransition(source, destination);
						tracker.updateCurrentState(userName, destination);
						log.debug("\tTransition: source=" + source + " destination=" + destination+ " for class "+uc);
					}

					this.lineCounter++;

					// log.info(this.lineCounter + " lines parsed so far");
				} else {
					log.debug("\tDiscarding line or EOF: " +logLine);
				}
			} catch (Exception e) {
				log.debug(e.getMessage());
				e.printStackTrace();
				System.exit(-1);
			}
			log.debug("\t---------END-------------\n");
		} while (logLine != null);
		loader.dispose();
		log.debug("\tProcessed: " + this.lineCounter + " lines");
	}

	public List<Model> exportModels() throws Exception {
		List<Model> models = new ArrayList<Model>();
		HashMap<UserClass, Tracker> trackers = Tracker.getTrackers();
		for (UserClass uc : trackers.keySet()) {
			InferenceEngine inferenceEngine = InferenceEngine.getInferenceEngine(uc);
			Tracker tracker = trackers.get(uc);
			Model model = inferenceEngine.exportModel(tracker.getCurrentGlobalState(), uc.newUserClassWithOccurrences(tracker.getUserCount()));
			models.add(model);
		}
		return models;
	}
	

	private boolean stem(LogLine logLine) {
		if (stemmer != null) {
			boolean stemmed = stemmer.matches(logLine);
			if (stemmed) {
				// log.debug("Line stemmed: "+logLine.getUrl());
				return true;
			}
		}
		return false;
	}

	public Stemmer getStemmer() {
		return stemmer;
	}

	public void setStemmer(Stemmer stemmer) {
		this.stemmer = stemmer;
	}
}
