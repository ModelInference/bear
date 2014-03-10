package ch.usi.star.bear.loader;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;

import ch.usi.star.bear.annotations.BearFilter;
import ch.usi.star.bear.model.Label;
import ch.usi.star.bear.model.State;

public class StateMapper {

	private String filtersPackagePrefix;

	public StateMapper(String filtersPackagePrefix) {
		this.filtersPackagePrefix = filtersPackagePrefix;
	}

	@SuppressWarnings("unchecked")
	public State getRequestedState(LogLine logLine) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException,
			IntrospectionException {
		Reflections reflections = new Reflections(filtersPackagePrefix, new MethodAnnotationsScanner());
		Set<Method> methods = reflections.getMethodsAnnotatedWith(BearFilter.class);
		Set<Label> labels = new HashSet<Label>();
		for (Method method : methods) {
			if (Modifier.isStatic(method.getModifiers())) {

				BearFilter bearAnnotation = method.getAnnotation(BearFilter.class);
				String regex = bearAnnotation.regex();
				if (regex != "" && this.matches(regex, logLine.getUrl())) {
					labels.addAll((Set<Label>) method.invoke(null, logLine));
				}

				if (regex == "") {
					labels.addAll((Set<Label>) method.invoke(null, logLine));
				}
			} else
				throw new IntrospectionException("Filter methods needs to be static");
			
		}
		State state;
		try {
			state = new State(labels.toArray(new Label[0]));
		} catch (Exception e) {
			return null;
		}
		String prova = state.toString();
		if(prova.contains("&")){
			System.out.println(logLine);
			System.exit(-1);
		}
		return state;
	}
	

	private boolean matches(String regex, String url) {
		Pattern pattern = Pattern.compile(regex);
		return pattern.matcher(url).find();
	}

}
