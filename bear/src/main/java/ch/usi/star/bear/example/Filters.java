package ch.usi.star.bear.example;
import java.util.HashSet;
import java.util.Set;

import ch.usi.star.bear.annotations.BearFilter;
import ch.usi.star.bear.loader.LogLine;
import ch.usi.star.bear.model.Label;

public class Filters {

	private static Label uno = new Label("Uno", 1L);
	private static Label due = new Label("Due", 2L);
	private static Label tre = new Label("Tre", 3L);
	private static Label quattro = new Label("Quattro", 4L);
	private static Label cinque = new Label("Cinque", 5L);

	@BearFilter(regex = "/uno/")
	public static Set<Label> FilterUno(LogLine logline) {
		Set<Label> result = new HashSet<Label>();
		result.add(uno);
		return result;
	}

	@BearFilter(regex = "/due/")
	public static Set<Label> FilterDue(LogLine logline) {
		Set<Label> result = new HashSet<Label>();
		result.add(due);
		return result;
	}

	@BearFilter(regex = "/tre/")
	public static Set<Label> FilterTre(LogLine logline) {
		Set<Label> result = new HashSet<Label>();
		result.add(tre);
		return result;
	}

	@BearFilter(regex = "/quattro/")
	public static Set<Label> FilterQuattro(LogLine logline) {
		Set<Label> result = new HashSet<Label>();
		result.add(quattro);
		return result;
	}

	@BearFilter(regex = "/cinque/")
	public static Set<Label> FilterCinque(LogLine logline) {
		Set<Label> result = new HashSet<Label>();
		result.add(cinque);
		return result;
	}

}
