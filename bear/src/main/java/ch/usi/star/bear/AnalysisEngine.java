package ch.usi.star.bear;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.log4j.Logger;

import ch.usi.star.bear.model.Model;
import ch.usi.star.bear.model.RewardSchema;
import ch.usi.star.bear.model.State;
import ch.usi.star.bear.model.Transition;
import ch.usi.star.bear.properties.BearProperties;
import ch.usi.star.bear.visualization.BearVisualizer;

public class AnalysisEngine {

	private Logger log = Logger.getLogger(this.getClass().getSimpleName());

	private List<Model> models;

	public List<Model> getModels() {
		return models;
	}

	public void setModels(List<Model> models) {
		this.models = models;
	}

	public AnalysisEngine(List<Model> models) {
		this.models = models;
	}

	public String analyze(String scope, String property, String moduleName, boolean visualize) throws Exception {
		Model model = this.synthesize(models, scope);
		this.saveModel(model, moduleName);
		this.saveProperty(property);
		String result;
		try{
			result = this.executePrism();
		}catch(ExecuteException e){
			throw new Exception("Impossible to execute PRISM. Check the correcteness of model and formulae in temp directory");
		}
		if(visualize){
			BearVisualizer visualizer = new BearVisualizer(model);
			visualizer.display();
		}
		return result;
	}

	private String executePrism() throws ExecuteException, IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		PumpStreamHandler handler = new PumpStreamHandler(bos, System.out);

		CommandLine command = new CommandLine(BearProperties.getInstance().getProperty(BearProperties.PRISMPATH) + "/prism");
		command.addArgument("temp/model.pm"); // Adding a String argument
		command.addArgument("temp/property.pctl");
		DefaultExecutor executor = new DefaultExecutor();
		executor.setStreamHandler(handler); // Sets the stream handler
		executor.execute(command);// Executes the command
		bos.close();
		return bos.toString();
	}

	private String[] getScopeElements(String scope) {
		scope = scope.substring(1, scope.length() - 1);
		return scope.split("=");
	}

	private Model synthesize(List<Model> models, String scope) throws Exception {
		log.debug("\tTotal number of Models: " + models.size());

		if (models.size() < 1)
			throw new Exception("At least one model is required");

		Set<State> states = new HashSet<State>();
		Set<Transition> transitions = new HashSet<Transition>();

		String[] elements = this.getScopeElements(scope);
		List<Model> validModels = new ArrayList<Model>();

		if (elements.length == 1 && elements[0].equals("")) {
			for (Model m : models) {
				states.addAll(m.getStates());
				validModels.add(m);
			}
		} else {

			for (Model m : models) {
				if (elements[0].equals(m.getUserClass().getName()) && this.matches(elements[1], m.getUserClass().getValue())) {
					states.addAll(m.getStates());
					validModels.add(m);
				}
			}
		}
		log.debug("\tModels to be synthesized: " + validModels.size());
		HashMap<State, Integer> occurrences = new HashMap<State, Integer>();
		
		if(validModels.size()<1){
			throw new Exception("You need to select at least one model. Check the correcteness of your scope");
		}
		
		for (Model m : validModels) {
			for (Transition t : m.getTransitions()) {
				int value = 0;
				if (occurrences.containsKey(t.getSource())) {
					value = occurrences.get(t.getSource());
				}
				value += t.getOccurrences();
				occurrences.put(t.getSource(), value);
			}
		}

		for (State s1 : states) {
			for (State s2 : states) {
				int localOccurrences = 0;
				for (Model m : validModels) {
					Transition tr = m.getTransitionsFromStateToState(s1, s2);
					if (tr != null) {
						localOccurrences += tr.getOccurrences();
					}
				}

				if (localOccurrences > 0) {
					float probability = ((float) localOccurrences) / (float) occurrences.get(s1);
					Transition transition = new Transition(s1, s2, localOccurrences, probability);
					transitions.add(transition);
				}
			}
		}

		String labelRewardName = BearProperties.getInstance().getProperty(BearProperties.LABELREWARDS);
		RewardSchema rewardSchema = new RewardSchema(labelRewardName, states);
		Model model = new Model(states, transitions, null, rewardSchema);
		return model;
	}

	private boolean matches(String regex, String url) {
		Pattern pattern = Pattern.compile(regex);
		return pattern.matcher(url).find();
	}

	private void saveModel(Model model, String moduleName) throws FileNotFoundException, UnsupportedEncodingException {
		File file = new File("temp/model.pm");
		PrintWriter writer = new PrintWriter(file);
		writer.println(model.generatePrismModel(moduleName));
		writer.close();
	}

	private void saveProperty(String property) throws FileNotFoundException, UnsupportedEncodingException {
		File file = new File("temp/property.pctl");
		PrintWriter writer = new PrintWriter(file);
		writer.println(property);
		writer.close();
	}

}
