package ch.usi.star.bear.loader;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;

import ch.usi.star.bear.annotations.BearClassifier;
import ch.usi.star.bear.model.UserClass;

public class UserClassMapper {

	private String filtersPackagePrefix;

	public UserClassMapper(String filtersPackagePrefix) {
		this.filtersPackagePrefix = filtersPackagePrefix;
	}

	public Set<UserClass> getUserClass(LogLine logLine) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException,
			IntrospectionException {
		Reflections reflections = new Reflections(filtersPackagePrefix, new MethodAnnotationsScanner());
		Set<Method> methods = reflections.getMethodsAnnotatedWith(BearClassifier.class);
		Set<UserClass> classes = new HashSet<UserClass>();
				
		for (Method method : methods) {
			if (Modifier.isStatic(method.getModifiers())) {
				BearClassifier bearAnnotation = method.getAnnotation(BearClassifier.class);
				String name = bearAnnotation.name();
				if (name != "") {
					 UserClass userClass = new UserClass(name, (String) method.invoke(null, logLine),0);
					 classes.add(userClass);
					 continue;
				}
			}
			throw new IntrospectionException("Filter methods needs to be static");
		}
		return classes;
	}
}
