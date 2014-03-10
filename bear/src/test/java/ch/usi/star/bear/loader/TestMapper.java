package ch.usi.star.bear.loader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.usi.star.bear.annotations.BearFilter;
import ch.usi.star.bear.loader.LogLine;
import ch.usi.star.bear.loader.StateMapper;
import ch.usi.star.bear.model.Label;
import ch.usi.star.bear.model.State;

public class TestMapper {

	private StateMapper mapper;
	private String packagePrefix = "ch.usi.star.bear.loader";
	private String ip = "1.1.1.1";
	private static boolean filterCalled;
	private static boolean regexFiletrCalled;
	private static boolean regexFiletrNotCalled;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		mapper = new StateMapper(this.packagePrefix);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testFilterAnnotation() {
		TestMapper.filterCalled = false;
		LogLine logLine = new LogLine("", ip, "", new Date(), "", "");
		try {
			mapper.getRequestedState(logLine);
		} catch (Exception e) {
			e.printStackTrace();
			fail();

		}
		assertTrue(filterCalled);
	}

	@BearFilter
	public static Set<Label> filterForTestFilterAnnotation(LogLine logLine) {
		TestMapper.filterCalled = true;
		return new HashSet<Label>();
	}

	@Test
	public void testGetRequestedState() {
		State state = null;
		LogLine logLine = new LogLine("", ip, "", new Date(), "", "");
		try {
			state = mapper.getRequestedState(logLine);
		} catch (Exception e) {
			e.printStackTrace();
			fail();

		}
		assertNotNull(state);
		assertEquals(state.getLabels().size(), 3);
		assertTrue(state.getLabels().contains(new Label("first")));
		assertTrue(state.getLabels().contains(new Label("second")));
		assertTrue(state.getLabels().contains(new Label("third")));
	}

	@BearFilter
	public static Set<Label> filterFortestGetRequestedState(LogLine logLine) {
		Set<Label> result = new HashSet<Label>();
		result.add(new Label("first"));
		result.add(new Label("second"));
		return result;
	}

	@BearFilter
	public static Set<Label> secondFilterForGetRequestedState(LogLine logLine) {
		Set<Label> result = new HashSet<Label>();
		result.add(new Label("second"));
		result.add(new Label("third"));
		return result;
	}

	@Test
	public void testRegexFilterAnnotation() {
		TestMapper.regexFiletrCalled = false;
		TestMapper.regexFiletrNotCalled = true;
		LogLine logLine = new LogLine("", ip, "/this/is/a/mathicng/url/5/", new Date(), "", "");
		try {
			mapper.getRequestedState(logLine);
		} catch (Exception e) {
			e.printStackTrace();
			fail();

		}
		assertTrue(TestMapper.regexFiletrCalled);
		assertTrue(TestMapper.regexFiletrNotCalled);

	}

	@BearFilter(regex = "/this/is/a/mathicng/url/(\\w+)")
	public static Set<Label> regexFilterFortestGetRequestedState(LogLine logLine) {
		TestMapper.regexFiletrCalled = true;
		return new HashSet<Label>();
	}

	@BearFilter(regex = "/this/is/a//non/mathicng/url/(\\w+)")
	public static Set<Label> secondRegexFilterFortestGetRequestedState(LogLine logLine) {
		TestMapper.regexFiletrNotCalled = false;
		return new HashSet<Label>();
	}

}
